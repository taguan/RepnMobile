package be.repn.repnmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.base.Optional;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.*;
import java.net.URI;


public class LoginActivity extends AppCompatActivity {

    public static final String FORCE_NEW_LOGIN = "be.repn.repnmobile.FORCE_NEW_LOGIN";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.getBoolean(FORCE_NEW_LOGIN))
            checkIfSecurityCodeIsPresent();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.waitForCredentialCheck));
        progressDialog.setCancelable(false);
        setContentView(R.layout.activity_login);
    }

    private void checkIfSecurityCodeIsPresent() {
        try {
            Optional<String> currentSecureCode = SecurityHelpers.getCurrentSecureCode(this);
            if(currentSecureCode.isPresent()){
                toUserListActivity();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void submitSecurityCode(View view) {
        EditText editText = (EditText) findViewById(R.id.secureCodeEditText);
        String secureCode = editText.getText().toString();
        checkCredentials(secureCode);
    }

    private void checkCredentials(final String secureCode) {
        progressDialog.show();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setBasicAuth(secureCode, "aPassword");
        httpClient.get(BuildConfig.SERVER_URL + "users", new AsyncHttpResponseHandler() { //todo find more appropriate address
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.hide();
                try {
                    SecurityHelpers.writeSecureCode(secureCode, getApplicationContext());
                    RestService.startActionSynchroUsers(getApplicationContext());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Toast toast = Toast.makeText(getApplicationContext(), "starting nex task", Toast.LENGTH_SHORT);
                toast.show();
                toUserListActivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.hide();
                if(statusCode == 401 || statusCode == 403){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.wrongSecureCode, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.serverError, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void toUserListActivity() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

}
