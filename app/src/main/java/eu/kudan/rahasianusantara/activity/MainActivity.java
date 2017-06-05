package eu.kudan.rahasianusantara.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.model.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        // Initial Profile
        user = new User(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getDisplayName(), firebaseAuth.getCurrentUser().getEmail());

        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("iuozSmIwAUshClN0i0dDgqebtzCRLr/Oo40KUkGi1n/D9tJEj+n1mL/9Cpxjt2aZ7FFNzhJInREl/b9V2Ubpsp1wleDYDeGoCF32GUgo6di4tsHgrx903McfuD8RrQckIfPEPFnnz5eUnCIXw6A95HgQUTyak8X6fRgG1pvnl6cBzgNbF5U50Jsm57ONDQsqrQD2RtJKnuB87aKViJYD3EyFGjt1ZR7Sqvv324O0govHWLRjkVceOtdutnDNtV0UO/YROY6gqt92Lc9t1gevJCGTapi4Iv+8Se9CrzK3GxzGyqjVaD7rA14YxgjRHKS6DfSAdkWxHnU9oHTOGL4/aT1Oz4H5MSYJinA/H3+ijhC1VlyCYr7m+pq/U+/eqDPl2hEUU+Si4MQefK2NOe1OHRIJLnBZaYNsxhkWVfvEGaA3zXth4//c4BxL4xlpHASW5VDugrHVfX9oYNKG7GWH9/Ehk4ybQpYlKpFuQ2Hx5lc2MuLl4zs15Hx0cflupHoVh8GheZhrHV5FRYuOGxpqZOFTEUKq9V3PQd5tJtFhF+jvbCKMppCUhFnaiSK6zB/xLv8DJtDtXGGLAcfsVoXD+iWW+hleOODz/0sJaYss4YDhiksZGfkCUDAJqpolMDFnBWzXDVuJ1QotulMw/yiw36GAco+doIlN2WWBVV4oJ3A=");
        permissionsRequest();
        attachUI();
        createListener();
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

    private void attachUI(){
        TextView nameView = (TextView) findViewById(R.id.name);
        TextView emailView = (TextView) findViewById(R.id.email);

        nameView.setText(user.getUsername());
        emailView.setText(user.getEmail());
    }

    private void createListener(){
        // Initial view
        Button ar_button = (Button) findViewById(R.id.arcamera_start);
        Button logout_button = (Button) findViewById(R.id.logout);
        // Sign the function
        ar_button.setOnClickListener(goToARCamera);
        logout_button.setOnClickListener(goToLogout);

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
}
