package eu.kudan.rahasianusantara.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eu.kudan.rahasianusantara.FirebaseController;
import eu.kudan.rahasianusantara.FirebaseInterface;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.ProfileQuestStoreComponent;
import eu.kudan.rahasianusantara.model.Quest;
import eu.kudan.rahasianusantara.model.User;

public class QuestActivity extends AppCompatActivity implements FirebaseInterface, View.OnClickListener{

    private static final int REQ_QUEST = 0;
    private static final int REQ_USER = 1;

    // Firebase
    private FirebaseController firebaseController;

    private HashMap<Integer, ProfileQuestStoreComponent> questComponentManager;
    private HashMap<Integer, Quest> questManager;

    private FloatingActionButton addQuestButton;
    private Button ownMoreButton;
    private LinearLayout questContainer;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        // Initialize View
        initView();

        // firebase initialize
        firebaseController = new FirebaseController(this, getApplicationContext());
        questManager = new HashMap();
        questComponentManager = new HashMap();

        firebaseController.reqDatabase("Users/"+firebaseController.getUser().getUid(), REQ_USER);
        firebaseController.reqDatabase("Quests", REQ_QUEST);

        // Set Listener
        setListener();
    }

    private void initView(){
        questContainer = (LinearLayout) findViewById(R.id.quest_container);
    }

    private void setListener(){
        addQuestButton = (FloatingActionButton) findViewById(R.id.quest_add_button);
        ownMoreButton = (Button) findViewById(R.id.own_get_more);

        addQuestButton.setOnClickListener(this);
        ownMoreButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view == addQuestButton){
            startActivity(new Intent(getApplicationContext(), QuestEditActivity.class));
        }else if(view == ownMoreButton){
            startActivity(new Intent(getApplicationContext(), QuestStoreActivity.class));
        }else{
            for (Map.Entry<Integer, ProfileQuestStoreComponent> entry : questComponentManager.entrySet()){
                if (view.getId() == entry.getKey()){
                    Intent intent = new Intent(getApplicationContext(), QuestProfileActivity.class);
                    intent.putExtra("quest", questManager.get(entry.getKey()));
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    private void collectQuest(Quest input){

        if(user != null && user.getQuests() != null){
            int j = 0;

            LinearLayout ownedLayout = (LinearLayout) findViewById(R.id.owned_quest);
            TextView emptyMessage = (TextView) findViewById(R.id.empty_owned_quest);
            if(emptyMessage != null){
                ownedLayout.removeView(emptyMessage);
            }

            Iterator it = user.getQuests().entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                String questid = (String) pair.getKey();
                if(questid.equals(input.getId())){

                    ProfileQuestStoreComponent questComponent = new ProfileQuestStoreComponent(getApplicationContext(), input);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int margin = (int) getResources().getDimension(R.dimen.quest_component_margin);
                    layoutParams.setMargins(0,0,margin,0);
                    questComponent.setLayoutParams(layoutParams);

                    int uniqId = View.generateViewId();
                    questComponent.getChildAt(0).setId(uniqId);
                    questComponent.getChildAt(0).setOnClickListener(this);

                    ownedLayout.addView(questComponent);

                    questManager.put(uniqId, input);
                    questComponentManager.put(uniqId, questComponent);
                    j++;
                }
            }
        }

        // If user is the owner of the quest
        if (input.getAuthor().equals(firebaseController.getUser().getEmail())){

            TextView emptyMessage = (TextView) findViewById(R.id.empty_creation_quest);
            if(emptyMessage != null){
                questContainer.removeView(emptyMessage);
            }

            ProfileQuestStoreComponent questComponent = new ProfileQuestStoreComponent(getApplicationContext(), input);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = (int) getResources().getDimension(R.dimen.quest_component_margin);
            layoutParams.setMargins(0,0,margin,0);
            questComponent.setLayoutParams(layoutParams);

            int uniqId = View.generateViewId();
            questComponent.getChildAt(0).setId(uniqId);
            questComponent.getChildAt(0).setOnClickListener(this);

            questContainer.addView(questComponent);

            questManager.put(uniqId, input);
            questComponentManager.put(uniqId, questComponent);
        }
    }

    @Override
    public void onSignedUser() {
        // Authenticated User - nothing to do
    }

    @Override
    public void onUnsignedUser() {
        Toast.makeText(QuestActivity.this, "You need to Sign In first", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onReceiveFromDatabase(DataSnapshot dataSnapshot, int id) {
        if(id == REQ_QUEST){
            Iterable<DataSnapshot> quests = dataSnapshot.getChildren();
            for (DataSnapshot quest : quests){
                Quest input = quest.getValue(Quest.class);
                collectQuest(input);
            }
            
        }else if(id == REQ_USER){
            user = (User) dataSnapshot.getValue(User.class);
            if(user != null){

            }
        }
    }

    @Override
    public void onErrorDatabase(DatabaseError databaseError, int id) {

    }
}
