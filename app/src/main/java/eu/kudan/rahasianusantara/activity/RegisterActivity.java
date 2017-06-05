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

public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private EditText reenterPasswordView;
    private View loginForm;
    private ProgressDialog progressDialog;

    // Auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        reenterPasswordView = (EditText) findViewById(R.id.reenter_password);

        progressDialog = new ProgressDialog(this);

        Button signup_button = (Button) findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        loginForm = findViewById(R.id.signup_form);
    }

    protected void registerUser(){
        // Initial value email and password
        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        String reenter_password = reenterPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(email) && isEmailValid(email)){
            Toast.makeText(this, "Please enter your email correctly", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 6){
            Toast.makeText(this, "Password must be at least 6 character", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isPasswordValid(password, reenter_password)){
            Toast.makeText(this, "Password you enter didn't match", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering you in, please wait...");
        progressDialog.show();

        // Create new user with firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // success action
                    Toast.makeText(RegisterActivity.this, "Your account registered successfully.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    // fail action
                    Toast.makeText(RegisterActivity.this, "There some error to register your account please try again.", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String reenter_password) {
        //TODO: Replace this with your own logic
        return password.equals(reenter_password);
    }
}
