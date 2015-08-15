package be.repn.repnmobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;


public class UserDetailsActivity extends AppCompatActivity {

    private Uri userUri;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        if(savedInstanceState != null){
            userUri = savedInstanceState.getParcelable(RepnUserContentProvider.CONTENT_ITEM_TYPE);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userUri = extras.getParcelable(RepnUserContentProvider.CONTENT_ITEM_TYPE);
        }
        Cursor cursor = getContentResolver().query(userUri, null, null, null, null);
        cursor.moveToNext();
        user = RepnUserContentProvider.getUserFromCursor(cursor);
        cursor.close();
        TextView firstNameView = (TextView) findViewById(R.id.firstName_detail_view);
        firstNameView.setText(user.getFirstName());
        TextView lastNameView = (TextView) findViewById(R.id.last_detail_view);
        lastNameView.setText(user.getLastName());
        TextView phoneView = (TextView) findViewById(R.id.phone_detail_view);
        phoneView.setText(user.getMobile());
        TextView emailView = (TextView) findViewById(R.id.email_detail_view);
        emailView.setText(user.getEmail());
        TextView addressView = (TextView) findViewById(R.id.address_detail_view);
        addressView.setText(user.getFormattedAddress());

        if(user.getMobile() == null){
            findViewById(R.id.callButton).setEnabled(false);
            findViewById(R.id.smsButton).setEnabled(false);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(RepnUserContentProvider.CONTENT_ITEM_TYPE, userUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_modify_secure_code:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(LoginActivity.FORCE_NEW_LOGIN, true);
                startActivity(intent);
                return true;
            case R.id.action_synchronize:
                try {
                    RestService.startActionSynchroUsers(getApplicationContext());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startDial(View view) {
        if(user.getMobile() != null){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.getMobile()));
            startActivity(intent);
        }
    }

    public void startSms(View view) {
        if(user.getMobile() != null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + user.getMobile()));
            startActivity(intent);
        }
    }

    public void addContact(View view) {
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, user.getEmail());
        if(user.getLastName() != null && user.getFirstName() != null) intent.putExtra(ContactsContract.Intents.Insert.NAME, user.getFirstName() + " " + user.getLastName());
        if(user.getMobile() != null) intent.putExtra(ContactsContract.Intents.Insert.PHONE, user.getMobile());
        if(!user.getAddress().equals("")) intent.putExtra(ContactsContract.Intents.Insert.POSTAL, user.getAddress());
        startActivity(intent);
    }
}
