package eu.kudan.rahasianusantara.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import eu.kudan.rahasianusantara.R;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // If no current users
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(this, "Welcome back, "+firebaseAuth.getCurrentUser().getEmail().toString(), Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        // UI Initialization
        attachUI();
        // Setup listener
        setListener();
    }

    protected void attachUI(){
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
    }

    protected void setListener(){

        Button signin_button = (Button) findViewById(R.id.signin_button);
        Button signup_button = (Button) findViewById(R.id.signup_button);

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUp();
            }
        });
    }

    protected void signInUser(){
        // Initial value email and password
        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email correctly", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 6){
            Toast.makeText(this, "Password must be at least 6 character", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Signing you in, please wait...");
        progressDialog.show();

        // Sign in user to firebase
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // success action
                    Toast.makeText(LoginActivity.this, "Your account signed in successfully.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    // fail action
                    Toast.makeText(LoginActivity.this, "There some error signing in to your account please try again.", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    protected void goToSignUp(){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        finish();
        startActivity(intent);
    }

}
