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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.MapFragment;
import eu.kudan.rahasianusantara.component.ProfileMainComponent;
import eu.kudan.rahasianusantara.model.User;

public class MainActivity extends AppCompatActivity {

    public final static long LOCATION_MIN_TIME = 3000;
    public final static float LOCATION_MIN_DISTANCE = 5.0f;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private User user;

    private ProfileMainComponent profileMainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Authentication
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return;
        }
        createUser();

        // Request Permission Check
        permissionsRequest();

        // Map Initialize
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Map create
        MapFragment mapFragment = new MapFragment(getApplicationContext());
        fragmentTransaction.add(R.id.main_map_container, mapFragment);
        fragmentTransaction.commit();

        // Init Kudan AR
        ARAPIKey keyKudan = ARAPIKey.getInstance();
        keyKudan.setAPIKey("iuozSmIwAUshClN0i0dDgqebtzCRLr/Oo40KUkGi1n/D9tJEj+n1mL/9Cpxjt2aZ7FFNzhJInREl/b9V2Ubpsp1wleDYDeGoCF32GUgo6di4tsHgrx903McfuD8RrQckIfPEPFnnz5eUnCIXw6A95HgQUTyak8X6fRgG1pvnl6cBzgNbF5U50Jsm57ONDQsqrQD2RtJKnuB87aKViJYD3EyFGjt1ZR7Sqvv324O0govHWLRjkVceOtdutnDNtV0UO/YROY6gqt92Lc9t1gevJCGTapi4Iv+8Se9CrzK3GxzGyqjVaD7rA14YxgjRHKS6DfSAdkWxHnU9oHTOGL4/aT1Oz4H5MSYJinA/H3+ijhC1VlyCYr7m+pq/U+/eqDPl2hEUU+Si4MQefK2NOe1OHRIJLnBZaYNsxhkWVfvEGaA3zXth4//c4BxL4xlpHASW5VDugrHVfX9oYNKG7GWH9/Ehk4ybQpYlKpFuQ2Hx5lc2MuLl4zs15Hx0cflupHoVh8GheZhrHV5FRYuOGxpqZOFTEUKq9V3PQd5tJtFhF+jvbCKMppCUhFnaiSK6zB/xLv8DJtDtXGGLAcfsVoXD+iWW+hleOODz/0sJaYss4YDhiksZGfkCUDAJqpolMDFnBWzXDVuJ1QotulMw/yiw36GAco+doIlN2WWBVV4oJ3A=");

        attachUI();
        createListener();
    }

    private void attachUI(){
        // UI
    }

    private void createListener(){
        // Initial view
        LinearLayout profileBar = (LinearLayout) findViewById(R.id.profile_bar);
        LinearLayout playBar = (LinearLayout) findViewById(R.id.play_bar);
        LinearLayout questBar = (LinearLayout) findViewById(R.id.quest_bar);
        // Sign the function
        profileBar.setOnClickListener(goToProfile);
        playBar.setOnClickListener(goToARCamera);
        questBar.setOnClickListener(goToStore);
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
            profileMainComponent.setLevelAmount(user.getLevel());
            profileMainComponent.setAchievementAmount(user.getAchievement());
            profileMainComponent.setExpProgress(user.getExp());

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
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 111);

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

    private View.OnClickListener goToStore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), QuestActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener goToProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        }
    };
}
