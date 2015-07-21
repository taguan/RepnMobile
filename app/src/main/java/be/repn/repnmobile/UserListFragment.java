package be.repn.repnmobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class UserListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private SimpleCursorAdapter mAdapter;

    public UserListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.user_info, null,
                new String[]{RepnContract.User.COLUMN_NAME_LAST_NAME, RepnContract.User.COLUMN_NAME_FIRST_NAME},
                new int[]{R.id.lastNameView, R.id.firstNameView}, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        AbsListView mListView = (AbsListView) view.findViewById(android.R.id.list);
        if(mListView instanceof ListView){
            ((ListView) mListView).setAdapter(mAdapter);
        } else if(mListView instanceof GridView){
            ((GridView) mListView).setAdapter(mAdapter);
        } else throw new RuntimeException("Unsupported AbsListView " + mListView.getClass().toString());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), UserDetailsActivity.class);
                Uri userUri = Uri.parse(RepnUserContentProvider.CONTENT_URI + "/" + id);
                i.putExtra(RepnUserContentProvider.CONTENT_ITEM_TYPE, userUri);

                startActivity(i);
            }
        });
        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), RepnUserContentProvider.CONTENT_URI, new String[]{
                RepnContract.User._ID, RepnContract.User.COLUMN_NAME_FIRST_NAME, RepnContract.User.COLUMN_NAME_LAST_NAME
        }, null, null, RepnContract.User.COLUMN_NAME_LAST_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        mAdapter.swapCursor(null);
    }


}
