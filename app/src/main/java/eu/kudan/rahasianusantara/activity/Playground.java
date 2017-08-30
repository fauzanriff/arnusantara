package eu.kudan.rahasianusantara.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARGyroPlaceManager;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARTextureMaterial;
import eu.kudan.rahasianusantara.FirebaseController;
import eu.kudan.rahasianusantara.FirebaseInterface;
import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.model.Mission;
import eu.kudan.rahasianusantara.model.Quest;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class Playground extends eu.kudan.kudan.ARActivity implements FirebaseInterface{

    private ARImageTrackable trackable;         // for image trackable marker
    private ARImageTracker trackmanager;
    private ARArbiTrack ararbitrack;
    private ARGyroPlaceManager gyroplacemanager;
    private ARModelNode modelnode;

    private FirebaseController firebaseController;

    private Quest quest;
    private Mission mission;
    private String markerName, modelName;

    private ARBITRACK_STATE arbitrack_state;

    @Override
    public void onSignedUser() {

    }

    @Override
    public void onUnsignedUser() {

    }

    @Override
    public void onReceiveFromDatabase(DataSnapshot dataSnapshot, int id) {

    }

    @Override
    public void onErrorDatabase(DatabaseError databaseError, int id) {

    }

    //Tracking enum
    enum ARBITRACK_STATE {
        ARBI_PLACEMENT,
        ARBI_TRACKING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera);
        arbitrack_state  = ARBITRACK_STATE.ARBI_PLACEMENT;

        // Initialization firebase
        firebaseController = new FirebaseController(this, getApplicationContext());

        // Get Quest and Mission from storage
        getQuestMission();


        // Get marker and model
        getMarkerModel();

    }

    public void setup(){





//        setModel();

//        setTrackableArb();
//        setModelArb();
//        setModel();

//        ararbitrack = ARArbiTrack.getInstance();
//        ararbitrack.initialise();
//
//        ararbitrack.addListener(R.layout.activity_arcamera);
    }

    protected void setARListener(){
        trackable.addListener(new ARImageTrackableListener() {
            @Override
            public void didDetect(ARImageTrackable arImageTrackable) {
                LinearLayout dialogContainer = (LinearLayout) findViewById(R.id.dialogue_container);
                TextView dialogueText = (TextView) findViewById(R.id.dialogue_text);

                dialogueText.setText(mission.getDialogue());
                dialogContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void didTrack(ARImageTrackable arImageTrackable) {

            }

            @Override
            public void didLose(ARImageTrackable arImageTrackable) {
                LinearLayout dialogContainer = (LinearLayout) findViewById(R.id.dialogue_container);
                dialogContainer.setVisibility(View.GONE);
            }
        });
    }

    protected void getMarkerModel(){
        StorageReference markerRef;
        final File[] localFile;
        localFile = new File[2];
        if(mission.getArBased() == Mission.BASED_MARKER){
            // Get Marker
            String fbMarkerPath = "Quests/"+quest.getId()+"/missions/marker_"+mission.getId();
            markerRef = firebaseController.getReference(fbMarkerPath);
            try {
                localFile[0] = File.createTempFile(markerName, ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Waiting...");
            progressDialog.show();

            markerRef.getFile(localFile[0]).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local file created
                    progressDialog.dismiss();
                    Log.d("filepath", localFile[0].getPath());
                    addImageTrackable(localFile[0].getPath());
                    setARListener();

                    String fbModelPath = "Quests/"+quest.getId()+"/missions/model_"+mission.getId();

                    final StorageReference modelRef = firebaseController.getReference(fbModelPath);
                    try {
                        localFile[1] = File.createTempFile(modelName, ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    modelRef.getFile(localFile[1]).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local file created
                            if(mission.getModelType() == Mission.MODEL_IMAGE){
                                addImageNode(localFile[1].getPath());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error
                    progressDialog.dismiss();
                }
            });
        }
    }

    protected void getQuestMission(){
        if(Quest.activeAvailable(getApplicationContext())){
            quest = Quest.loadActiveQuest(getApplicationContext());
            if(Mission.activeAvailable(getApplicationContext())){
                mission = Mission.loadActiveMission(getApplicationContext());
                markerName = "/marker_"+mission.getId();
                modelName = "/model_"+mission.getId();
            }else{
                Toast.makeText(getApplicationContext(), "This quest have no available mission", Toast.LENGTH_LONG);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }else{
            Toast.makeText(getApplicationContext(), "Choose your quest", Toast.LENGTH_LONG);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    protected void createTarget(){
        ARNode targetnode = new ARNode();
        targetnode.setName("targetNode");

        trackable.getWorld().addChild(targetnode);

        ararbitrack.setTargetNode(targetnode);
    }

    protected void setTrackableArb(){
        ararbitrack = ARArbiTrack.getInstance();
        ararbitrack.initialise();

        gyroplacemanager = ARGyroPlaceManager.getInstance();
        gyroplacemanager.initialise();
    }

    protected void setModelArb(){
        ARImageNode targetimagenode = new ARImageNode("spaceMarker.jpg");
        gyroplacemanager.getWorld().addChild(targetimagenode);

        targetimagenode.scaleByUniform(0.3f);
        targetimagenode.rotateByDegrees(90,1,0,0);

        ararbitrack.setTargetNode(targetimagenode);
    }

    protected void startArb(View view){
        ararbitrack.start();
        modelnode.setVisible(true);
        ararbitrack.getTargetNode().setVisible(false);
    }

    protected void stopArb(View view){
        ararbitrack.stop();
        modelnode.setVisible(false);
        ararbitrack.getTargetNode().setVisible(true);
    }

    protected void addImageTrackable(String path){
        //Choose image track
        trackable = new ARImageTrackable("marker");
        trackable.loadFromPath(path, true);
        //Create Manager
        trackmanager = ARImageTracker.getInstance();
        //Setting image track to manager
        trackmanager.addTrackable(trackable);
    }

    protected void addImageNode(String path) {

        // Initialise image node
        ARImageNode imageNode = new ARImageNode("eyebrow.png");
        ARTexture2D texture = new ARTexture2D();
        texture.loadFromPath(path);
        imageNode.setTexture(texture);

        // Add image node to image trackable
        trackable.getWorld().addChild(imageNode);

        // Image scale
        ARTextureMaterial textureMaterial = (ARTextureMaterial)imageNode.getMaterial();
        float scale = trackable.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);

        // Hide image node
        imageNode.setVisible(true);
    }

    protected void setModel(){
        //Import
        ARModelImporter modelimporter = new ARModelImporter();
        modelimporter.loadFromAsset("ben.jet");
        modelnode = (ARModelNode)modelimporter.getNode();

        //Load texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset("bigBenTexture.png");

        //Apply texture
        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);

        //Apply mesh
        for(ARMeshNode meshnode : modelimporter.getMeshNodes()){
            meshnode.setMaterial(material);
        }

        ararbitrack.getWorld().addChild(modelnode);
        modelnode.setVisible(false);
    }

}
