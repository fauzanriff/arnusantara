package eu.kudan.rahasianusantara.component;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import eu.kudan.rahasianusantara.R;

/**
 * Created by fauza on 6/13/2017.
 */

public class MissionSingleComponent extends CardView {

    public final static String MODEL_3D = "3D Model";
    public final static String MODEL_VIDEO = "Video Model";
    public final static String MODEL_IMAGE = "Image Model";
    public final static String MARKER_BASED = "Marker";
    public final static String MARKERLESS_BASED = "Markerless";
    TextView orderMission, titleMission, modelTypeLabel, missionBased;
    ImageView modelTypeIcon;


    public MissionSingleComponent(Context context){
        super(context);
        init(context);
    }

    public MissionSingleComponent(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public MissionSingleComponent(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.mission_single_profile, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        orderMission = (TextView) findViewById(R.id.order_mission);
        titleMission = (TextView) findViewById(R.id.title_mission);
        modelTypeLabel = (TextView) findViewById(R.id.model_type_label);
        missionBased = (TextView) findViewById(R.id.mission_based);
        modelTypeIcon = (ImageView) findViewById(R.id.model_type_icon);
    }

    public void setOrderMission(int order){
        orderMission.setText(String.valueOf(order));
    }

    public void setTitleMission (String title){
        titleMission.setText(title);
    }

    public void setModelTypeLabel (String type){
        modelTypeLabel.setText(type);
    }

    public void setMissionBased (String based){
        missionBased.setText(based);
    }

    public void setModelTypeIcon(String icon){
        switch (icon){
            case MODEL_3D:
                modelTypeIcon.setImageResource(R.drawable.model_3d_icon);
                break;
            case MODEL_VIDEO:
                modelTypeIcon.setImageResource(R.drawable.model_video_icon);
                break;
            case MODEL_IMAGE:
                modelTypeIcon.setImageResource(R.drawable.model_image_icon);
                break;
        }
    }

}
