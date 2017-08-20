package eu.kudan.rahasianusantara.component;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import eu.kudan.rahasianusantara.ImageRequest;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.activity.QuestProfileActivity;
import eu.kudan.rahasianusantara.model.Quest;

/**
 * Created by fauza on 6/8/2017.
 */

public class ProfileQuestStoreComponent extends CardView{

    private ImageView headerQuest;
    private TextView nameQuest;
    private TextView authorQuest;
    private TextView idQuest;

    private Context mContext;
    private Quest quest;

    public ProfileQuestStoreComponent(Context context){
        super(context);
        mContext = context;
        init(context);
    }

    public ProfileQuestStoreComponent(Context context, Quest input){
        super(context);
        mContext = context;
        quest = new Quest(input);
        init(context);
    }

    public ProfileQuestStoreComponent(Context context, AttributeSet attrs){
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public ProfileQuestStoreComponent(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }

    public void init(Context context){
        View.inflate(context, R.layout.quest_store_profile, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        headerQuest = (ImageView) findViewById(R.id.quest_header);
        nameQuest = (TextView) findViewById(R.id.quest_name);
        authorQuest = (TextView) findViewById(R.id.quest_author);
        idQuest = (TextView) findViewById(R.id.quest_id);

        if (quest != null){
            new ImageRequest(headerQuest).execute(quest.getHeader());
            nameQuest.setText(quest.getTitle());
            authorQuest.setText(quest.getAuthor());
            idQuest.setText(quest.getId());
        }else{
            quest = new Quest();
        }
    }

    public void setHeaderQuest(String link){
        new ImageRequest(headerQuest).execute(link);
    }

    public void setNameQuest(String name){
        quest.setTitle(name);
        nameQuest.setText(name);
    }

    public void setAuthorQuest(String author){
        quest.setAuthor(author);
        authorQuest.setText(author);
    }

    public void setIdQuest (String id) {
        quest.setId(id);
        idQuest.setText(id);
    }
}



















