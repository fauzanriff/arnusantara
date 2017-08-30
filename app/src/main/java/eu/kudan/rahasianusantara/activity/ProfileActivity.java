package eu.kudan.rahasianusantara.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.model.Mission;
import eu.kudan.rahasianusantara.model.Quest;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;

    LinearLayout editProfileButton;
    LinearLayout signOutProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        setListener();
    }

    private void setListener(){
        editProfileButton = (LinearLayout) findViewById(R.id.profile_edit_button);
        signOutProfileButton = (LinearLayout) findViewById(R.id.profile_signout_button);

        editProfileButton.setOnClickListener(this);
        signOutProfileButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == editProfileButton){
            startActivity(new Intent(getApplicationContext(), UserEditActivity.class));
        }else if (view == signOutProfileButton){
            firebaseAuth.signOut();
            Quest.deleteActiveQuest(getApplicationContext());
            Mission.deleteActiveMission(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
