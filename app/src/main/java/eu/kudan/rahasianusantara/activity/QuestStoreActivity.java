package eu.kudan.rahasianusantara.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.ProfileQuestStoreComponent;
import eu.kudan.rahasianusantara.model.Quest;

public class QuestStoreActivity extends AppCompatActivity implements View.OnClickListener{

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Quest> questManager;

    FloatingActionButton addQuestButton;
    LinearLayout questContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_store);
        // firebase initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        questManager = new ArrayList<Quest>();

        if (firebaseAuth.getCurrentUser()==null){
            Toast.makeText(QuestStoreActivity.this, "You need to sign in first", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return;
        }

        getDatabaseQuest();

        // Initialize View
        initView();

        // Set Listener
        setListener();
    }

    private void initView(){
        questContainer = (LinearLayout) findViewById(R.id.quest_container);
    }

    private void setListener(){
        addQuestButton = (FloatingActionButton) findViewById(R.id.quest_add_button);

        addQuestButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view == addQuestButton){
            startActivity(new Intent(getApplicationContext(), QuestEditActivity.class));
        }
    }

    private void getDatabaseQuest(){
        DatabaseReference questReference = databaseReference.child("Quests");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Waiting quest to load...");
        progressDialog.show();

        questReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> quests = dataSnapshot.getChildren();
                for (DataSnapshot quest : quests){
                    Quest input = quest.getValue(Quest.class);
                    collectQuest(input);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(QuestStoreActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void collectQuest(Quest input){
        questManager.add(input);
        ProfileQuestStoreComponent questComponent = new ProfileQuestStoreComponent(getApplicationContext());
        questComponent.setHeaderQuest(input.getHeader());
        questComponent.setNameQuest(input.getTitle());
        questComponent.setAuthorQuest(input.getAuthor());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int margin = (int) getResources().getDimension(R.dimen.quest_component_margin);
        layoutParams.setMargins(0,0,margin,0);

        questComponent.setLayoutParams(layoutParams);

        questContainer.addView(questComponent);
    }
}
