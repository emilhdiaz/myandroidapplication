package com.emildiaz.myandroidapplication;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.emildiaz.myandroidapplication.models.CommentTable;
import com.emildiaz.myandroidapplication.providers.CommentsContentProvider;

public class CommentActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COMMENT_LOADER = 0;
    private static final String[] projection = {
        CommentTable.COLUMN_ID,
        CommentTable.COLUMN_COMMENT
    };
    private static final String[] cursorColumns = {
        CommentTable.COLUMN_COMMENT
    };
    private static final int[] viewFields = {
        R.id.comment
    };

    private long contactId;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        contactId = getIntent().getExtras().getLong(CommentTable.COLUMN_CONTACT_ID);
        Bundle loaderData = new Bundle();
        loaderData.putLong(CommentTable.COLUMN_CONTACT_ID, contactId);
        getLoaderManager().initLoader(COMMENT_LOADER, loaderData, this);

        adapter = new SimpleCursorAdapter(
            this,
            R.layout.activity_comment_entry,
            null,
            cursorColumns,
            viewFields,
            0
        );

        getListView().setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] selectionArgs = {String.valueOf(bundle.getLong(CommentTable.COLUMN_CONTACT_ID))};
        return new CursorLoader(
            this,
            CommentsContentProvider.CONTENT_URI,
            projection,
            "contact_id = ?",
            selectionArgs,
            null
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

    public void addComment(View view) {
        EditText editText = (EditText) findViewById(R.id.text);
        String text = editText.getText().toString();

        // Add via content provider
        ContentValues values = new ContentValues();
        values.put(CommentTable.COLUMN_CONTACT_ID, contactId);
        values.put(CommentTable.COLUMN_COMMENT, editText.getText().toString());
        getContentResolver().insert(CommentsContentProvider.CONTENT_URI, values);
    }

    public void deleteComment(View view) {
        if (adapter.getCount() == 0) return;
        int i = getListView().getCheckedItemPosition();
        long[] ids = getListView().getCheckedItemIds();

        if (ids.length == 0) return;

        // delete via content provider
        Uri uri = Uri.parse(CommentsContentProvider.CONTENT_URI + "/" + ids[0]);
        getContentResolver().delete(uri, null, null);
    }
}