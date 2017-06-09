package eu.kudan.rahasianusantara.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.ProfileMainComponent;
import eu.kudan.rahasianusantara.model.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private User user;

    private ProfileMainComponent profileMainComponent;

    private float density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initial firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Authentication
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            return;
        }
        density = getApplicationContext().getResources().getDisplayMetrics().density;
        createUser();

        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("iuozSmIwAUshClN0i0dDgqebtzCRLr/Oo40KUkGi1n/D9tJEj+n1mL/9Cpxjt2aZ7FFNzhJInREl/b9V2Ubpsp1wleDYDeGoCF32GUgo6di4tsHgrx903McfuD8RrQckIfPEPFnnz5eUnCIXw6A95HgQUTyak8X6fRgG1pvnl6cBzgNbF5U50Jsm57ONDQsqrQD2RtJKnuB87aKViJYD3EyFGjt1ZR7Sqvv324O0govHWLRjkVceOtdutnDNtV0UO/YROY6gqt92Lc9t1gevJCGTapi4Iv+8Se9CrzK3GxzGyqjVaD7rA14YxgjRHKS6DfSAdkWxHnU9oHTOGL4/aT1Oz4H5MSYJinA/H3+ijhC1VlyCYr7m+pq/U+/eqDPl2hEUU+Si4MQefK2NOe1OHRIJLnBZaYNsxhkWVfvEGaA3zXth4//c4BxL4xlpHASW5VDugrHVfX9oYNKG7GWH9/Ehk4ybQpYlKpFuQ2Hx5lc2MuLl4zs15Hx0cflupHoVh8GheZhrHV5FRYuOGxpqZOFTEUKq9V3PQd5tJtFhF+jvbCKMppCUhFnaiSK6zB/xLv8DJtDtXGGLAcfsVoXD+iWW+hleOODz/0sJaYss4YDhiksZGfkCUDAJqpolMDFnBWzXDVuJ1QotulMw/yiw36GAco+doIlN2WWBVV4oJ3A=");
        permissionsRequest();
        attachUI();
        createListener();
    }

    private void attachUI(){
        // UI
    }

    private void createListener(){
        // Initial view
        Button arButton = (Button) findViewById(R.id.arcamera_start);
        Button logoutButton = (Button) findViewById(R.id.logout);
        Button questStoreButton = (Button) findViewById(R.id.quest_store);
        Button editProfileButton = (Button) findViewById(R.id.edit_profile);
        // Sign the function
        arButton.setOnClickListener(goToARCamera);
        logoutButton.setOnClickListener(goToLogout);
        questStoreButton.setOnClickListener(goToStore);
        editProfileButton.setOnClickListener(goToEditProfile);
    }

    private void updateProfileUI(){
        profileMainComponent = (ProfileMainComponent) findViewById(R.id.main_profile);
        if (user!=null){
            if (profileMainComponent != null){
                findViewById(R.id.main_profile).setVisibility(View.GONE);
            }
            profileMainComponent = new ProfileMainComponent(getApplicationContext());
            profileMainComponent.setId(R.id.main_profile);
            profileMainComponent.setProfileName(user.getUsername());
            profileMainComponent.setProfileEmail(user.getEmail());
            profileMainComponent.setProfilePicture(user.getPicture());

            LinearLayout root = (LinearLayout) findViewById(R.id.main_profile_container);
            root.addView(profileMainComponent, 0);
        }
    }

    protected void createUser(){
        String id = firebaseAuth.getCurrentUser().getUid();
        String email = firebaseAuth.getCurrentUser().getEmail();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+id);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting your account, please wait...");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user!=null){
                    Toast.makeText(MainActivity.this, "Success "+user.getUsername(), Toast.LENGTH_LONG).show();
                    updateProfileUI();
                }else{
                    Intent intent = new Intent(getApplicationContext(), UserEditActivity.class);
                    startActivity(intent);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // failed
            }
        });
    }

    // Requests app permissions
    public void permissionsRequest() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 111);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {

                } else {
                    permissionsNotSelected();
                }
            }
        }
    }

    private void permissionsNotSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle("Permissions Required");
        builder.setMessage("Please enable the requested permissions in the app settings in order to use this demo app");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                System.exit(1);
            }
        });
        AlertDialog noInternet = builder.create();
        noInternet.show();
    }

    private View.OnClickListener goToARCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Playground.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener goToLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            finish();
            startActivity(intent);
        }
    };

    private View.OnClickListener goToStore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), QuestStoreActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener goToEditProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), UserEditActivity.class);
            startActivity(intent);
        }
    };
}
