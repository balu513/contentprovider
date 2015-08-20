package com.afbb.balakrishna.albumart.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.afbb.balakrishna.MyProvider";

    public static final String URL_STUDENTS = "content://" + PROVIDER_NAME + "/students";
    public static final Uri CONTENT_URI_STUDENTS = Uri.parse(URL_STUDENTS);

    public static final String URL_FACULTY = "content://" + PROVIDER_NAME + "/faculty";
    public static final Uri CONTENT_URI_FACULTY = Uri.parse(URL_FACULTY);


    static final UriMatcher uriMatcher;
    public static final int uriCode_student = 1;
    public static final int uriCode_faculty = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PROVIDER_NAME, "students", uriCode_student);
        uriMatcher.addURI(PROVIDER_NAME, "students/*", uriCode_student);

        uriMatcher.addURI(PROVIDER_NAME, "faculty", uriCode_faculty);
        uriMatcher.addURI(PROVIDER_NAME, "faculty/*", uriCode_faculty);
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode_student:
                return "vnd.android.cursor.dir/student";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case uriCode_student:
                qb.setTables(TABLE_NAME_STUDENT);
                c = qb.query(db, null, null, null, null,
                        null, null);
                break;
        }
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (uriMatcher.match(uri)) {
            case uriCode_student:
                long rowID = db.insert(TABLE_NAME_STUDENT, "", values);
                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI_STUDENTS, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode_student:
                count = db.delete(TABLE_NAME_STUDENT, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode_student:
                count = db.update(TABLE_NAME_STUDENT, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            return true;
        }
        return false;
    }


    private SQLiteDatabase db;

    static final String TABLE_NAME_STUDENT = "students";
    static final String TABLE_NAME_FACULTY = "faculty";

    static final String COL_STUDENT_NAME = "name";
    static final String COL_STUDENT_BRANCH = "branch";

    static final String COL_FACULTY_NAME = "name";
    static final String COL_FACULTY_SUBJECT = "subject";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "mydb";

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String CREATE_DB_TABLE_STUDENT = " CREATE TABLE " + TABLE_NAME_STUDENT
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_STUDENT_NAME + " TEXT NOT NULL, " + COL_STUDENT_BRANCH + " TEXT NOT NULL );";

            String CREATE_DB_TABLE_FACULTY = " CREATE TABLE " + TABLE_NAME_FACULTY
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_FACULTY_NAME + " TEXT NOT NULL, " + COL_FACULTY_SUBJECT + " TEXT NOT NULL);";

            db.execSQL(CREATE_DB_TABLE_STUDENT);
            db.execSQL(CREATE_DB_TABLE_FACULTY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FACULTY);
            onCreate(db);
        }
    }
}