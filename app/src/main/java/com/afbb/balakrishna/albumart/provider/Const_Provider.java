package com.afbb.balakrishna.albumart.provider;

import android.net.Uri;

/**
 * Created by balakrishna on 20/8/15.
 */
public class Const_Provider {

    public static final String PROVIDER_NAME = "com.afbb.balakrishna.MyProvider";

    public static final String URL_STUDENTS = "content://" + PROVIDER_NAME + "/students";
    public static final Uri CONTENT_URI_STUDENTS = Uri.parse(URL_STUDENTS);

    public static final String URL_FACULTY = "content://" + PROVIDER_NAME + "/faculty";
    public static final Uri CONTENT_URI_FACULTY = Uri.parse(URL_FACULTY);


    public static final String TABLE_NAME_STUDENT = "students";
    public static final String TABLE_NAME_FACULTY = "faculty";

    public static final String COL_STUDENT_NAME = "name";
    public static final String COL_STUDENT_BRANCH = "branch";

    public static final String COL_FACULTY_NAME = "name";
    public static final String COL_FACULTY_SUBJECT = "subject";
}
