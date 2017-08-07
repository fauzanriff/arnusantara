package eu.kudan.rahasianusantara.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.component.MapFragment;
import eu.kudan.rahasianusantara.component.MissionSingleComponent;
import eu.kudan.rahasianusantara.model.Mission;
import eu.kudan.rahasianusantara.model.Quest;

public class MissionEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_MODEL = 1;
    private static final int PICK_MARKER = 2;

    private Button addMarkerButton, addModelButton, doneMissionButton;
    private MapFragment mapFragment;
    private CheckBox markerBased;
    private Mission mission;
    private EditText missionDialogue;
    private Spinner modelType;

    private Uri filePathMarker, filePathModel;
    private String markerLink, missionKey, modelLink;
//    private HashMap<String, Mission> missions;
    private Quest quest;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_edit);

        // Firebase Initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        // Quest
        quest = (Quest) getIntent().getSerializableExtra("quest");

        // Map Initialize
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Map create
        mapFragment = new MapFragment(getApplicationContext());
        fragmentTransaction.add(R.id.mission_map_container, mapFragment);
        fragmentTransaction.commit();

        // Get Mission Database
        mission = new Mission();

        renderView();
        setListener();
    }

    private void renderView(){
        modelType = (Spinner) findViewById(R.id.spinner_model_type);
        markerBased = (CheckBox) findViewById(R.id.checkbox_marker_based);
        missionDialogue = (EditText) findViewById(R.id.mission_dialogue);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.list_model_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelType.setAdapter(adapter);
    }

    private void setListener(){
        doneMissionButton = (Button) findViewById(R.id.done_mission_button);
        addMarkerButton = (Button) findViewById(R.id.choose_marker);
        addModelButton = (Button) findViewById(R.id.choose_model_type);

        doneMissionButton.setOnClickListener(this);
        addMarkerButton.setOnClickListener(this);
        addModelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == doneMissionButton){
            submitMission();
        }else if(v == addMarkerButton){
            openImageLibrary(PICK_MARKER);
        }else if(v == addModelButton){
            openImageLibrary(PICK_MODEL);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MARKER && resultCode == Activity.RESULT_OK){
            if (data==null){
                // Error message
                return;
            }
            filePathMarker = data.getData();

            TextView labelPathMarker = (TextView) findViewById(R.id.label_path_marker);
            labelPathMarker.setText(filePathMarker.getPath());
        }else if(requestCode == PICK_MODEL && resultCode == Activity.RESULT_OK){
            if (data==null){
                // Error message
                return;
            }
            filePathModel = data.getData();

            TextView labelPathModel = (TextView) findViewById(R.id.label_path_model);
            labelPathModel.setText(filePathModel.getPath());
        }
    }

    private void openImageLibrary(int parent){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Marker"), parent);
    }

    private void uploadModel(){

        String id = firebaseAuth.getCurrentUser().getUid().toString();
        String qid = quest.getId();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading your model...");
        progressDialog.show();

        // Uploading to storage
        StorageReference profileReference = storageReference.child("Quests/"+qid+"/missions/model_"+missionKey);
        profileReference.putFile(filePathModel).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MissionEditActivity.this, "Upload successfully.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                modelLink = taskSnapshot.getDownloadUrl().toString();
                // Push model link
                mission.setModelLink(modelLink);

                pushDatabase();

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MissionEditActivity.this, "Failure to upload", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setTitle("Uploading model...");
            }
        });
    }

    private void uploadMarker(){

        String id = firebaseAuth.getCurrentUser().getUid().toString();
        String qid = quest.getId();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading your marker...");
        progressDialog.show();

        // Uploading to storage
        StorageReference profileReference = storageReference.child("Quests/"+qid+"/missions/marker_"+missionKey);
        profileReference.putFile(filePathMarker).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MissionEditActivity.this, "Upload successfully.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                markerLink = taskSnapshot.getDownloadUrl().toString();
                // Push marker link
                mission.setMarkerLink(markerLink);

                uploadModel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MissionEditActivity.this, "Failure to upload", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setTitle("Uploading marker...");
            }
        });
    }

    private void pushDatabase(){
        databaseReference.child("Quests/"+quest.getId()+"/missions/"+mission.getId()).setValue(mission);
    }

    private void submitMission(){

        DatabaseReference missionsReference = databaseReference.child("Quests/"+quest.getId()+"/missions");
        missionKey = missionsReference.push().getKey();

        TextView titleMission = (TextView) findViewById(R.id.title_mission);
        TextView orderMission = (TextView) findViewById(R.id.order_mission);

        mission.setId(missionKey.toString());
        mission.setTitle(titleMission.getText().toString());
        if (isParsable(orderMission.getText().toString())){
            mission.setOrder(Integer.parseInt(orderMission.getText().toString()));
        }else{
            mission.setOrder(0);
        }
        mission.setLatLng(mapFragment.getCurrentLocation());
        mission.setModelType(modelType.getSelectedItemPosition());
        mission.setArBased(markerBased.isEnabled() ? mission.BASED_MARKER : mission.BASED_MARKERLESS);
        mission.setDialogue(missionDialogue.getText().toString());
        // upload marker and model
        if (mission.getArBased() == mission.BASED_MARKER){
            uploadMarker();
        }else{
            uploadModel();
        }

    }

    public static boolean isParsable(String input){
        boolean parsable = true;
        try{
            Integer.parseInt(input);
        }catch(NumberFormatException e){
            parsable = false;
        }
        return parsable;
    }
}
