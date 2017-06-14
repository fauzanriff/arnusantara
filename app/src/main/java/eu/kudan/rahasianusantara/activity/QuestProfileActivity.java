package eu.kudan.rahasianusantara.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.ProfileQuestSingleComponent;
import eu.kudan.rahasianusantara.model.Quest;

public class QuestProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;

    LinearLayout questEditButton;
    Quest quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_profile);

        // get quest id
        quest = (Quest) getIntent().getSerializableExtra("quest");

        // Firebase initialize
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize View
        initView();

        // Set Listener
        setListener();
    }

    private void initView(){

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

        questEditButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == questEditButton){
            Intent intent = new Intent(getApplicationContext(), QuestEditActivity.class);
            intent.putExtra("quest", quest);
            startActivity(intent);
        }
    }
}
