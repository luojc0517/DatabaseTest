package com.jackie.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Law on 2015/11/18.
 */
public class DatabaseProvider extends ContentProvider {
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    public static final String AUTHORITY = "com.jackie.databasetest.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper myDatabaseHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    @Override
    public boolean onCreate() {
        myDatabaseHelper = new MyDatabaseHelper(getContext(), "book.db", null, 2);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                //比如：content://"+AUTHORIY+"/book/1",
                //getPathSegments() 得到/book/1
                //get(1)得到1
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("book", projection, "id=?", new String[]{bookId}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("category", projection, "id=?", new String[]{categoryId}, null, null, sortOrder);
                break;
            default:
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.jackie.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.jackie.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.jackie.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.jackie.databasetest.provider.category";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                long rowidBook = db.insert("book", null, values);
                uriReturn = uri.parse("content://" + AUTHORITY + "/book/" + rowidBook);
                break;
            case BOOK_ITEM:
                break;
            case CATEGORY_DIR:
                long rowidCategory = db.insert("category", null, values);
                uriReturn = uri.parse("content://" + AUTHORITY + "/category/" + rowidCategory);
                break;
            case CATEGORY_ITEM:
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteRow = 0;
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deleteRow = db.delete("book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                //比如：content://"+AUTHORIY+"/book/1",
                //getPathSegments() 得到/book/1
                //get(1)得到1
                String bookId = uri.getPathSegments().get(1);
                deleteRow = db.delete("book", "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRow = db.delete("category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRow = db.delete("category", "id=?", new String[]{categoryId});
                break;
            default:
                break;

        }
        return deleteRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateRow = 0;
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updateRow = db.update("book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                //比如：content://"+AUTHORIY+"/book/1",
                //getPathSegments() 得到/book/1
                //get(1)得到1
                String bookId = uri.getPathSegments().get(1);
                updateRow = db.update("book", values, "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRow = db.update("category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRow = db.update("category", values, "id=?", new String[]{categoryId});
                break;
            default:
                break;

        }
        return updateRow;
    }
}
