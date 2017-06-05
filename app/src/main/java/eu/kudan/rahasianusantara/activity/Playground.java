package eu.kudan.rahasianusantara.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARGyroPlaceManager;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARTextureMaterial;
import eu.kudan.rahasianusantara.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Playground extends eu.kudan.kudan.ARActivity {

    ARImageTrackable trackable;
    ARImageTracker trackmanager;
    ARArbiTrack ararbitrack;
    ARGyroPlaceManager gyroplacemanager;
    ARModelNode modelnode;

    private ARBITRACK_STATE arbitrack_state;

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
    }

    public void setup(){
//        setImageTrackable();
//        setModel();

        setTrackableArb();
        setModelArb();
        setModel();

//        ararbitrack = ARArbiTrack.getInstance();
//        ararbitrack.initialise();
//
//        ararbitrack.addListener(R.layout.activity_arcamera);
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

    protected void setImageTrackable(){
        //Choose image track
        trackable = new ARImageTrackable("space");
        trackable.loadFromAsset("spaceMarker.jpg");
        //Create Manager
        trackmanager = ARImageTracker.getInstance();
        //Setting image track to manager
        trackmanager.addTrackable(trackable);
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
