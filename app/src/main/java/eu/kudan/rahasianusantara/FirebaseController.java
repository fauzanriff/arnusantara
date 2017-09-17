package eu.kudan.rahasianusantara;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import eu.kudan.rahasianusantara.activity.QuestActivity;
import eu.kudan.rahasianusantara.model.Quest;
import eu.kudan.rahasianusantara.model.User;

/**
 * Created by fauzanrifqy on 8/7/17.
 */

public class FirebaseController {

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseInterface firebaseInterface;

    private User user = null;

    Context mContext;

    public FirebaseController(FirebaseInterface input, Context context){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseInterface = input;
        mContext = context;
        if (firebaseAuth.getCurrentUser() == null) {
            firebaseInterface.onUnsignedUser();
        }else{
            firebaseInterface.onSignedUser();
        }

        reqUserData();
    }

    public void reqDatabase(String path, final int id){
        DatabaseReference databaseReference = firebaseDatabase.getReference(path);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseInterface.onReceiveFromDatabase(dataSnapshot, id);
                if(id == 999){
                    user = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseInterface.onErrorDatabase(databaseError, id);
//                Toast.makeText(QuestActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendDatabase(String path, Object object){
        DatabaseReference reference = firebaseDatabase.getReference().child(path);
        reference.setValue(object);
    }

    public FirebaseUser getUser(){
        return firebaseAuth.getCurrentUser();
    }

    public User getUserData() {
        return user;
    }

    public void reqUserData(){
        reqDatabase("/Users/"+firebaseAuth.getCurrentUser().getUid(), 999);
    }

    public boolean isUserEmail(String email){
        return firebaseAuth.getCurrentUser().getEmail().toString().equals(email);
    }

    public void addExp(int input){
        if(user != null){
            user.addExp(input);
        }
        sendDatabase("Users/"+user.getId(), user);
    }

    public StorageReference getReference(String s){
        return firebaseStorage.getReference(s);
    }

}
