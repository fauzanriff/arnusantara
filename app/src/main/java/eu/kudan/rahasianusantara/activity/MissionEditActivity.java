package eu.kudan.rahasianusantara.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import eu.kudan.rahasianusantara.R;
import eu.kudan.rahasianusantara.model.Mission;

public class MissionEditActivity extends AppCompatActivity implements View.OnClickListener {

    Button addDialogueButton, doneMissionButton;

    Mission mission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_edit);

        mission = new Mission();

        setListener();
    }

    private void setListener(){

        addDialogueButton = (Button) findViewById(R.id.add_dialogue_button);
        doneMissionButton = (Button) findViewById(R.id.done_mission_button);

        addDialogueButton.setOnClickListener(this);
        doneMissionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == doneMissionButton){
            submitMission();
        }
    }

    private void submitMission(){

        TextView titleMission = (TextView) findViewById(R.id.title_mission);
        TextView orderMission = (TextView) findViewById(R.id.order_mission);-

        mission.setTitle(titleMission.getText().toString());
        mission.setOrder(Integer.valueOf(orderMission.getText().toString()));
    }
}
