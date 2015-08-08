package be.repn.repnmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;


public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actity_user_list);
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
}
