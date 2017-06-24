package eu.kudan.rahasianusantara.component;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import eu.kudan.rahasianusantara.R;

/**
 * Created by fauza on 6/17/2017.
 */

public class DialogueComponent extends CardView {

    ImageView dialogueCardOption;
    TextView dialogueCardOrder, dialogueCardText;

    public DialogueComponent(Context context){
        super(context);
        init(context);
    }

    public DialogueComponent(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public DialogueComponent(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.dialogue, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        dialogueCardOption = (ImageView) findViewById(R.id.dialogue_card_option);
        dialogueCardOrder = (TextView) findViewById(R.id.dialogue_card_order);
        dialogueCardText = (TextView) findViewById(R.id.dialogue_card_text);
    }

    public void setDialogueCardOrder(String order){
        dialogueCardOrder.setText(order);
    }

    public void setDialogueCardText(String text){
        dialogueCardText.setText(text);
    }

}
