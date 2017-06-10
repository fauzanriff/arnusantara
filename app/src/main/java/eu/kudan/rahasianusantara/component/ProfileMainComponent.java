package eu.kudan.rahasianusantara.component;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import eu.kudan.rahasianusantara.ImageRequest;
import eu.kudan.rahasianusantara.R;

/**
 * Created by fauza on 6/7/2017.
 */

public class ProfileMainComponent extends CardView{

    ImageView profilePicture;
    TextView profileName;
    TextView profileEmail;

    public ProfileMainComponent(Context context){
        super(context);
        init(context);
    }

    public ProfileMainComponent(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public ProfileMainComponent(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.main__profile, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        profileName = (TextView) findViewById(R.id.profile_name);
        profileEmail = (TextView) findViewById(R.id.profile_email);
    }

    public void setProfilePicture(String link){
        new ImageRequest(profilePicture).execute(link);
    }

    public void setProfileName(String name){
        profileName.setText(name);
    }

    public void setProfileEmail(String email){
        profileEmail.setText(email);
    }
}