package be.repn.repnmobile;

import android.app.Application;

import java.io.IOException;

public class RepnApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            if(SecurityHelpers.getCurrentSecureCode(getApplicationContext()).isPresent()){
                RestService.startActionSynchroUsers(getApplicationContext()); //todo should be better to use a sync adapter
                //we could also use a last synchro date to avoid synchronizing to often
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
