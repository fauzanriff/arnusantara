package eu.kudan.rahasianusantara.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.ProfileQuestSingleComponent;
import eu.kudan.rahasianusantara.model.Quest;

public class QuestProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

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
}
