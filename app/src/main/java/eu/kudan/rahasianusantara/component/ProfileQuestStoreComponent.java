package eu.kudan.rahasianusantara.component;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import eu.kudan.rahasianusantara.ImageRequest;
import eu.kudan.rahasianusantara.R;

/**
 * Created by fauza on 6/8/2017.
 */

public class ProfileQuestStoreComponent extends CardView {

    private ImageView headerQuest;
    private TextView nameQuest;
    private TextView authorQuest;

    public ProfileQuestStoreComponent(Context context){
        super(context);
        init(context);
    }

    public ProfileQuestStoreComponent(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public ProfileQuestStoreComponent(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        View.inflate(context, R.layout.quest_store_profile, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        headerQuest = (ImageView) findViewById(R.id.quest_header);
        nameQuest = (TextView) findViewById(R.id.quest_name);
        authorQuest = (TextView) findViewById(R.id.quest_author);
    }

    public void setHeaderQuest(String link){
        new ImageRequest(headerQuest).execute(link);
    }

    public void setNameQuest(String name){
        nameQuest.setText(name);
    }

    public void setAuthorQuest(String author){
        authorQuest.setText(author);
    }

}



















