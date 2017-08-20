package eu.kudan.rahasianusantara.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.kudan.rahasianusantara.FirebaseController;
import eu.kudan.rahasianusantara.FirebaseInterface;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.MissionSingleComponent;
import eu.kudan.rahasianusantara.component.ProfileQuestSingleComponent;
import eu.kudan.rahasianusantara.model.Mission;
import eu.kudan.rahasianusantara.model.Quest;
import eu.kudan.rahasianusantara.model.User;

public class QuestProfileActivity extends AppCompatActivity implements FirebaseInterface, View.OnClickListener {

    private static final int REQ_MISSION = 0;
    private static final int REQ_USER = 1;

    private FirebaseController firebaseController;

    private LinearLayout questEditButton;
    private CardView addMissionButton;
    private Button saveQuestButton, activateQuestButton;
    private Quest quest;
    private ArrayList<Mission> missionHandler;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_profile);

        // get quest id
        quest = (Quest) getIntent().getSerializableExtra("quest");
        missionHandler = new ArrayList<Mission>();

        // Initialize View
        getViewComponent();
        renderView();

        // Firebase initialize
        firebaseController = new FirebaseController(this, getApplicationContext());
        firebaseController.reqDatabase("Users/"+firebaseController.getUser().getUid(), REQ_USER);

        if (firebaseController.isUserEmail(quest.getAuthor())){
            firebaseController.reqDatabase("Quests/"+quest.getId()+"/missions", REQ_MISSION);
        }else{
            firebaseController.reqDatabase("Quests/"+quest.getId()+"/missions", REQ_MISSION);
            LinearLayout editLayout = (LinearLayout) findViewById(R.id.edit_mode_quest);
            editLayout.setVisibility(View.GONE);
        }

        // Set Listener
        setListener();
    }

    private void renderMission(Mission mission){
        MissionSingleComponent missionProfile = new MissionSingleComponent(getApplicationContext());

        missionProfile.setOrderMission(mission.getOrder());
        missionProfile.setTitleMission(mission.getTitle());
        // Model type icon and label
        String iconType;
        switch (mission.getModelType()){
            case Mission.MODEL_3D:
                iconType = MissionSingleComponent.MODEL_3D;
                break;
            case Mission.MODEL_IMAGE:
                iconType = MissionSingleComponent.MODEL_IMAGE;
                break;
            case Mission.MODEL_VIDEO:
                iconType = MissionSingleComponent.MODEL_VIDEO;
                break;
            default:
                iconType = MissionSingleComponent.MODEL_IMAGE;
        }
        missionProfile.setModelTypeIcon(iconType);
        missionProfile.setModelTypeLabel(iconType);
        // Marker or markerless label
        switch (mission.getArBased()){
            case Mission.BASED_MARKER:
                missionProfile.setMissionBased("Marker");
                break;
            case Mission.BASED_MARKERLESS:
                missionProfile.setMissionBased("Markerless");
                break;
        }

        LinearLayout missionContainer = (LinearLayout) findViewById(R.id.mission_container);
        missionContainer.addView(missionProfile, 1);
    }

    private void getViewComponent(){
        questEditButton = (LinearLayout) findViewById(R.id.quest_edit_button);
        addMissionButton = (CardView) findViewById(R.id.add_mission_card);
        saveQuestButton = (Button) findViewById(R.id.add_mission_library);
        activateQuestButton = (Button) findViewById(R.id.activate_mission);
    }

    private void renderView(){

        ProfileQuestSingleComponent questComponent = new ProfileQuestSingleComponent(getApplicationContext(), quest);

        LinearLayout questContainer = (LinearLayout) findViewById(R.id.quest_profile_container);
        questContainer.addView(questComponent, 0);
    }

    private void setListener(){
        questEditButton.setOnClickListener(this);
        addMissionButton.setOnClickListener(this);
        saveQuestButton.setOnClickListener(this);
        activateQuestButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == questEditButton){
            Intent intent = new Intent(getApplicationContext(), QuestEditActivity.class);
            intent.putExtra("quest", quest);
            startActivity(intent);
        }else if(view == addMissionButton){
            Intent intent = new Intent(getApplicationContext(), MissionEditActivity.class);
            intent.putExtra("quest", quest);
            startActivity(intent);
        }else if(view == saveQuestButton){
            String path = "Users/"+firebaseController.getUser().getUid().toString()+"/quests";
            String missionid = "";
            if(missionHandler.size()!=0){
                missionid = missionHandler.get(0).getId();
            }
            user.addQuest(quest.getId(), missionid);
            firebaseController.sendDatabase(path, user.getQuests());
        }else if(view == activateQuestButton){
//            String path = "Users/"+firebaseController.getUser().getUid().toString();
//            user.setActiveQuest(quest.getId());
//            firebaseController.sendDatabase(path, user);
            Quest.saveActiveQuest(quest, getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onSignedUser() {

    }

    @Override
    public void onUnsignedUser() {
        Toast.makeText(QuestProfileActivity.this, "You need to Sign In first", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onReceiveFromDatabase(DataSnapshot dataSnapshot, int id) {
        if(id == REQ_USER){
            // Remove Save Button
            user = (User) dataSnapshot.getValue(User.class);
            if(user != null && user.getQuests() != null){
                Iterator it = user.getQuests().entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry pair = (Map.Entry) it.next();
                    if(pair.getKey().equals(quest.getId())){
                        saveQuestButton.setVisibility(View.GONE);
                        if(!Quest.activeAvailable(getApplicationContext())){
                            activateQuestButton.setVisibility(View.VISIBLE);
                        }else if(!Quest.loadActiveQuest(getApplicationContext()).getId().equals(quest.getId())){
                            activateQuestButton.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
            }
        }else if(id == REQ_MISSION){
            // Get mission from database
            Iterable<DataSnapshot> missions = dataSnapshot.getChildren();
            for (DataSnapshot mission : missions){
                Mission input = mission.getValue(Mission.class);
                missionHandler.add(input);
                renderMission(input);
            }
            TextView counterMission = (TextView) findViewById(R.id.counter_mission);
            counterMission.setText(String.valueOf(missionHandler.size()));
        }
    }

    @Override
    public void onErrorDatabase(DatabaseError databaseError, int id) {

    }
}
