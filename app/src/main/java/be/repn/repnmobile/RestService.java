package be.repn.repnmobile;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RestService extends IntentService {
    private static final String ACTION_SYNCHRO_USERS = "be.repn.repnmobile.action.SYNCHRO_USERS";

    private static final String SECURE_CODE_PARAM = "be.repn.repnmobile.params.SECURE_CODE";

    private Handler handler;

    public static void startActionSynchroUsers(Context context) throws IOException {
        Intent intent = new Intent(context, RestService.class);
        intent.setAction(ACTION_SYNCHRO_USERS);
        intent.putExtra(SECURE_CODE_PARAM, SecurityHelpers.getCurrentSecureCode(context).get());
        context.startService(intent);
    }

    public RestService() {
        super("RestService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNCHRO_USERS.equals(action)) {
                final String secureCode = intent.getStringExtra(SECURE_CODE_PARAM);
                synchronizeUsers(secureCode);
            }
        }
    }


    private void synchronizeUsers(final String secureCode) {
        AsyncHttpClient httpClient = new SyncHttpClient();
        httpClient.setBasicAuth(secureCode, "aPassword");
        httpClient.get(BuildConfig.SERVER_URL + "users", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                InputStream is = null;
                InputStreamReader reader = null;
                try{
                    is = new ByteArrayInputStream(responseBody);
                    reader = new InputStreamReader(is);
                    User[] users = new Gson().fromJson(reader, User[].class);
                    for (User user : users) {
                        RepnUserContentProvider.insertOrUpdateUser(user, getApplicationContext());
                    }
                    displayToastMessage(R.string.synchroFinished);
                } finally {
                    if(reader != null) try {
                        reader.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                    if(is != null) try {
                        is.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(statusCode == 401 || statusCode == 403){
                    displayToastMessage(R.string.wrongSecureCode);
                } else {
                    displayToastMessage(R.string.serverError);
                }
            }
        });
    }

    public void displayToastMessage(final int stringId){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


}
