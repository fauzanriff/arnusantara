package eu.kudan.rahasianusantara.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import eu.kudan.rahasianusantara.ImageRequest;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.model.User;

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private ImageView profilePicture;
    private Uri filePath;
    private String fileLink;
    Button userAcceptButton;
    Button userChoosePictureButton;

    private static final int PICK_PHOTO = 1;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        user = new User();

        attachUI();
        setListener();
    }

    protected void attachUI(){
        username = (EditText) findViewById(R.id.user_name);
        profilePicture = (ImageView) findViewById(R.id.profile_picture_view);

        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users/"+firebaseAuth.getCurrentUser().getUid());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tunggu sebentar...");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null){
                    username.setText(user.getUsername());
                    new ImageRequest(profilePicture).execute(user.getPicture());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //failed
                progressDialog.dismiss();
            }
        });

        username.setText(user.getUsername());
    }

    protected void setListener(){
        userAcceptButton = (Button) findViewById(R.id.user_accept);
        userChoosePictureButton = (Button) findViewById(R.id.profile_picture_button);

        userAcceptButton.setOnClickListener(this);
        userChoosePictureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == userAcceptButton){
            registerUsername();
        }else if(view == userChoosePictureButton){
            choosePhotoFromLibrary();
        }
    }

    private void registerUsername() {
        // register username
        String id = firebaseAuth.getCurrentUser().getUid().toString();

        // Save image to storage
        if (filePath == null){
            Toast.makeText(UserEditActivity.this, "Please choose photo for your profile picture", Toast.LENGTH_LONG
            ).show();
            return;
        }

        // Create progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading your profile data...");
        progressDialog.show();

        // Uploading to storage
        StorageReference profileReference = storageReference.child("Images/profiles/"+id);
        profileReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserEditActivity.this, "Upload successfully.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                fileLink = taskSnapshot.getDownloadUrl().toString();
                createUser();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UserEditActivity.this, "Failure to upload", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setTitle("Uploading...");
            }
        });
    };

    protected void createUser(){
        // Init user info
        String name = username.getText().toString();
        String id = firebaseAuth.getCurrentUser().getUid().toString();
        String email = firebaseAuth.getCurrentUser().getEmail().toString();
        // Init database
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        // Create user with profile picture
        user = new User(id, name, email, fileLink);
        databaseReference.child("Users/"+id).setValue(user);
    }


    protected void choosePhotoFromLibrary () {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK){
            if (data==null){
                // Error message
                return;
            }
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
