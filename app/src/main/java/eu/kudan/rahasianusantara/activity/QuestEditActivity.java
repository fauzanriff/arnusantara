package eu.kudan.rahasianusantara.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.ProfileMainComponent;
import eu.kudan.rahasianusantara.model.Quest;
import eu.kudan.rahasianusantara.model.User;

public class QuestEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_PHOTO = 1;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    DatabaseReference questReference;

    Quest quest;

    Uri filePath;

    Button choosePictureButton, saveQuestButton;
    ImageView headerPicture;
    EditText titleQuest, versionQuest, descriptionQuest;
    String fileLink, databaseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_edit);

        // Firebase Initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // Check User Authentication
        if (firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return;
        }

        // Set Listener
        setListener();
        // Initialize View
        initView();
    }

    private void setListener(){
        // Get Button
        choosePictureButton = (Button) findViewById(R.id.quest_upload_button);
        saveQuestButton = (Button) findViewById(R.id.quest_save_button);
        // Set Listener
        choosePictureButton.setOnClickListener(this);
        saveQuestButton.setOnClickListener(this);
    }

    private void initView(){
        // Initialize View
        headerPicture = (ImageView) findViewById(R.id.quest_header);
        titleQuest = (EditText) findViewById(R.id.quest_name);
        versionQuest = (EditText) findViewById(R.id.quest_version);
        descriptionQuest = (EditText) findViewById(R.id.quest_description);
    }


    @Override
    public void onClick(View view){
        if (view == choosePictureButton){
            choosePhotoFromLibrary();
        }else if(view == saveQuestButton){
            saveQuest();
        }
    }

    // Library Open
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
                headerPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save To Database and Storage
    protected void saveQuest(){
        // Init storage and DB
        questReference = firebaseDatabase.getReference().child("Quests").push();
        databaseKey = questReference.getKey();
        StorageReference headerReference = storageReference.child("Images/questsHeader/"+databaseKey);

        // Check file path
        if (filePath == null){
            Toast.makeText(QuestEditActivity.this, "Silahkan pilih gambar terkait sejarah yang akan anda buat.", Toast.LENGTH_LONG).show();
            return;
        }

        // Create progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading your quest...");
        progressDialog.show();

        // Upload header
        headerReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(QuestEditActivity.this, "Your quest uploaded successfully", Toast.LENGTH_LONG).show();
                // Create Quest
                fileLink = taskSnapshot.getDownloadUrl().toString();
                saveQuestToDatabase();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(QuestEditActivity.this, "Your quest failure to upload, please try again", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // On Progress
            }
        });
    }

    protected void saveQuestToDatabase(){
        // Initialize data input
        final String title = titleQuest.getText().toString();
        final String version = versionQuest.getText().toString();
        final String description = descriptionQuest.getText().toString();
        final String author = firebaseAuth.getCurrentUser().getDisplayName();
        // Upload quest to database
        quest = new Quest(databaseKey, title, version, description, fileLink, author);
        questReference.setValue(quest);
    }
}