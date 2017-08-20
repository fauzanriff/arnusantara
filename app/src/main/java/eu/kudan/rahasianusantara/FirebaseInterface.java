package eu.kudan.rahasianusantara;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by fauzanrifqy on 8/7/17.
 */

public interface FirebaseInterface {
    public void onSignedUser();
    public void onUnsignedUser();
    public void onReceiveFromDatabase(DataSnapshot dataSnapshot, int id);
    public void onErrorDatabase(DatabaseError databaseError, int id);
}
