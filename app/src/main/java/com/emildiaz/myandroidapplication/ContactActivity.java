package com.emildiaz.myandroidapplication;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.emildiaz.myandroidapplication.models.CommentTable;

public class ContactActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONTACT_LOADER = 0;
    private static final String[] projection = {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.CONTACT_PRESENCE
    };
    private static final String[] cursorColumns = {
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.CONTACT_PRESENCE
    };
    private static final int[] viewFields = {
        R.id.name,
        R.id.phone
    };
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        adapter = new SimpleCursorAdapter(
            this,
            R.layout.activity_contact_entry,
            null,
            cursorColumns,
            viewFields,
            0
        );

        getListView().setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(
            this,
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.TIMES_CONTACTED + " COLLATE LOCALIZED DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(CommentTable.COLUMN_CONTACT_ID, id);
        startActivity(intent);
    }
}