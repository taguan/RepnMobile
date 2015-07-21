package be.repn.repnmobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



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
        getMenuInflater().inflate(R.menu.menu_user_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
