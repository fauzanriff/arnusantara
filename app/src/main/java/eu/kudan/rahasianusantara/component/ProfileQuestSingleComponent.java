package eu.kudan.rahasianusantara.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import eu.kudan.rahasianusantara.ImageRequest;
import eu.kudan.rahasianusantara.R;

/**
 * Created by fauza on 6/11/2017.
 */

public class ProfileQuestSingleComponent extends LinearLayout {

    private ImageView questHeader;
    private TextView questTitle, questAuthor, questUpvote, questDownloader, questAchieved, questVersion, questDescription;

    public ProfileQuestSingleComponent(Context context){
        super(context);
        init(context);
    }

    public ProfileQuestSingleComponent(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public ProfileQuestSingleComponent(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.quest_single_profile, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        questHeader = (ImageView) findViewById(R.id.quest_header);
        questTitle = (TextView) findViewById(R.id.quest_title);
        questAuthor = (TextView) findViewById(R.id.quest_author);
        questUpvote = (TextView) findViewById(R.id.upvote_amount);
        questDownloader = (TextView) findViewById(R.id.downloader_amount);
        questAchieved = (TextView) findViewById(R.id.achieved_amount);
        questVersion = (TextView) findViewById(R.id.version_amount);
        questDescription = (TextView) findViewById(R.id.quest_description);
    }

    public void setQuestHeader(String link){
        new ImageRequest(questHeader).execute(link);
    }

    public void setQuestTitle(String title){
        questTitle.setText(title);
    }

    public void setQuestAuthor(String author){
        questAuthor.setText(author);
    }

    public void setQuestUpvote(String upvote) {
        questUpvote.setText(upvote);
    }

    public void setQuestDownloader(String downloader) {
        questDownloader.setText(downloader);
    }

    public void setQuestAchieved(String achieved) {
        questAchieved.setText(achieved);
    }

    public void setQuestVersion(String version) {
        questVersion.setText(version);
    }

    public void setQuestDescription(String description) {
        questDescription.setText(description);
    }
}
