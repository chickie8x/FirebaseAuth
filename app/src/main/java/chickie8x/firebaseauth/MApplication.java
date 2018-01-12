package chickie8x.firebaseauth;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 12/18/2017.
 */

public class MApplication extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
