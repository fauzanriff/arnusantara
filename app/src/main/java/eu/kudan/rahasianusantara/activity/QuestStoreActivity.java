package eu.kudan.rahasianusantara.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

import eu.kudan.rahasianusantara.FirebaseController;
import eu.kudan.rahasianusantara.FirebaseInterface;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.ProfileQuestStoreComponent;
import eu.kudan.rahasianusantara.model.Quest;

public class QuestStoreActivity extends AppCompatActivity implements FirebaseInterface, View.OnClickListener {

    private static final int REQ_QUEST = 0;

    private FirebaseController firebaseController;

    private LinearLayout questContainer;
    private HashMap<Integer, ProfileQuestStoreComponent> questComponentManager = new HashMap<>();
    private HashMap<Integer, Quest> questManager = new HashMap<>();

    private boolean left = true;
    private int containerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_store);

        getViewComponent();

        firebaseController = new FirebaseController(this, getApplicationContext());
        firebaseController.reqDatabase("Quests", REQ_QUEST);
    }

    @Override
    public void onClick(View v) {
        Log.d("onclick", "clicked");
        for (Map.Entry<Integer, ProfileQuestStoreComponent> entry : questComponentManager.entrySet()){
            if (v.getId() == entry.getKey()){
                Intent intent = new Intent(getApplicationContext(), QuestProfileActivity.class);
                intent.putExtra("quest", questManager.get(entry.getKey()));
                startActivity(intent);
                break;
            }
        }
    }


    @Override
    public void onSignedUser() {
        // Authenticated User - nothing to do
    }

    @Override
    public void onUnsignedUser() {
        Toast.makeText(QuestStoreActivity.this, "You need to Sign In first", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onReceiveFromDatabase(DataSnapshot dataSnapshot, int id) {
        if(id == REQ_QUEST){
            Iterable<DataSnapshot> quests = dataSnapshot.getChildren();
            for (DataSnapshot quest : quests){
                Quest input = quest.getValue(Quest.class);
                renderQuest(input);
            }
        }
    }

    @Override
    public void onErrorDatabase(DatabaseError databaseError, int id) {

    }

    private void getViewComponent(){
        questContainer = (LinearLayout) findViewById(R.id.quest_store_container);
    }

    private LinearLayout createFirstLayout(){
        int uniqId = View.generateViewId();
        containerId = uniqId;
        LinearLayout newLayout = new LinearLayout(getApplicationContext());
        newLayout.setId(uniqId);
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setPadding(0,10,0,10);
        LinearLayout.LayoutParams layoutStyle =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newLayout.setLayoutParams(layoutStyle);
        return newLayout;
    }

    private LinearLayout createSecondLayout(){
        LinearLayout newLayout = new LinearLayout(getApplicationContext());
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        newLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutStyle =new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutStyle.weight = 1;
        newLayout.setLayoutParams(layoutStyle);
        return newLayout;
    }

    private void renderQuest(Quest input){

        // Create View for quest
        int uniqId = View.generateViewId();
        ProfileQuestStoreComponent questComponent = new ProfileQuestStoreComponent(getApplicationContext(), new Quest(input));
        questComponent.getChildAt(0).setId(uniqId);
        questComponent.getChildAt(0).setOnClickListener(this);

        LinearLayout inLayout = createSecondLayout();
        LinearLayout outLayout;

        inLayout.addView(questComponent);

        // Check render position for quest
        if(left){
            outLayout = createFirstLayout();
            outLayout.addView(inLayout);
            questContainer.addView(outLayout);
        }else{
            outLayout = (LinearLayout) findViewById(containerId);
            outLayout.addView(inLayout);
        }

        left = !left;                   // Change render position

        // Push quest component for listener
        questManager.put(uniqId, input);
        questComponentManager.put(uniqId, questComponent);
    }
}
