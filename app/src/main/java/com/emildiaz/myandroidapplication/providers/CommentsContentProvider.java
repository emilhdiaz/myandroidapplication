package com.emildiaz.myandroidapplication.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.emildiaz.myandroidapplication.models.CommentTable;
import com.emildiaz.myandroidapplication.models.DatabaseHelper;

import java.util.Arrays;
import java.util.HashSet;

public class CommentsContentProvider extends ContentProvider {
    private DatabaseHelper helper;
    private UriMatcher uriMatcher;

    private static final int COMMENTS = 10;
    private static final int COMMENT_ID = 20;
    private static final String AUTHORITY = "com.emildiaz.myandroidapplication.providers";
    private static final String BASE_PATH = "comments";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/comment";

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, BASE_PATH, COMMENTS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COMMENT_ID);
        return true;
    }

    @Override
    synchronized public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        checkColumns(projection);

        builder.setTables(CommentTable.TABLE_NAME);

        // Check for a valid URI
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case COMMENTS:
                break;
            case COMMENT_ID:
                builder.appendWhere(CommentTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = builder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        database.close();
        return cursor;
    }

    @Override
    synchronized public Uri insert(Uri uri, ContentValues values) {
        // Check for a valid URI
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case COMMENTS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase database = helper.getWritableDatabase();
        long id = database.insert(CommentTable.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
//        database.close();
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    synchronized public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Check for a valid URI
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case COMMENTS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase database = helper.getWritableDatabase();
        int countUpdated = database.update(CommentTable.TABLE_NAME, values, CommentTable.COLUMN_ID + " = " + uri.getLastPathSegment(), null);
        getContext().getContentResolver().notifyChange(uri, null);
//        database.close();
        return countUpdated;
    }

    @Override
    synchronized public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Check for a valid URI
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case COMMENT_ID:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase database = helper.getWritableDatabase();
        int countDeleted = database.delete(CommentTable.TABLE_NAME, CommentTable.COLUMN_ID + " = " + uri.getLastPathSegment(), null);
        getContext().getContentResolver().notifyChange(uri, null);
//        database.close();
        return countDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    private void checkColumns(String[] projection) {
        if (projection == null) return;

        HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
        HashSet<String> availableColumns = new HashSet<>(Arrays.asList(CommentTable.COLUMNS));

        if (!availableColumns.containsAll(requestedColumns)) {
            throw new IllegalArgumentException("Unknown columns in projection");
        }
    }
}
