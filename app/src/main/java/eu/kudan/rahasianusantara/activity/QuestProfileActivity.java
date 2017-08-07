package eu.kudan.rahasianusantara.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.MissionSingleComponent;
import eu.kudan.rahasianusantara.component.ProfileQuestSingleComponent;
import eu.kudan.rahasianusantara.model.Mission;
import eu.kudan.rahasianusantara.model.Quest;

public class QuestProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    LinearLayout questEditButton;
    CardView addMissionButton;
    Quest quest;
    ArrayList<Mission> missionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_profile);

        // get quest id
        quest = (Quest) getIntent().getSerializableExtra("quest");

        // Firebase initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        missionHandler = new ArrayList<Mission>();

        // Initialize View
        getMissionDatabase();
        renderView();

        // Set Listener
        setListener();
    }

    private void getMissionDatabase(){
        DatabaseReference missionReference = databaseReference.child("Quests/"+quest.getId()+"/missions");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Waiting missions to load...");
        progressDialog.show();

        missionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> missions = dataSnapshot.getChildren();
                for (DataSnapshot mission : missions){
                    Mission input = mission.getValue(Mission.class);
                    missionHandler.add(input);
                    renderMission(input);
                }
                TextView counterMission = (TextView) findViewById(R.id.counter_mission);
                counterMission.setText(String.valueOf(missionHandler.size()));
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(QuestProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

    private void renderView(){

        ProfileQuestSingleComponent questComponent = new ProfileQuestSingleComponent(getApplicationContext());

        questComponent.setQuestAchieved(String.valueOf(quest.getAchieved()));
        questComponent.setQuestAuthor(quest.getAuthor());
        questComponent.setQuestDescription(quest.getDescription());
        questComponent.setQuestDownloader(String.valueOf(quest.getDownloader()));
        questComponent.setQuestHeader(quest.getHeader());
        questComponent.setQuestTitle(quest.getTitle());
        questComponent.setQuestUpvote(String.valueOf(quest.getUpvote()));
        questComponent.setQuestVersion(quest.getVersion());

        LinearLayout questContainer = (LinearLayout) findViewById(R.id.quest_profile_container);
        questContainer.addView(questComponent, 0);
    }

    private void setListener(){
        questEditButton = (LinearLayout) findViewById(R.id.quest_edit_button);
        addMissionButton = (CardView) findViewById(R.id.add_mission_card);

        questEditButton.setOnClickListener(this);
        addMissionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == questEditButton){
            Intent intent = new Intent(getApplicationContext(), QuestEditActivity.class);
            intent.putExtra("quest", quest);
            startActivity(intent);
        }else if(view == addMissionButton){
            Intent intent = new Intent(getApplicationContext(), MissionEditActivity.class);
            intent.putExtra("quest", quest);
            startActivity(intent);
        }
    }
}
