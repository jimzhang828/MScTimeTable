package cs.hku.zwj.timetable_test;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.islandparadise14.mintable.ScheduleEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyApplication extends Application {
    private ArrayList<String> courseList = null;
    private SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        courseList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this, "mydb", null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select course from courseList;", null);
        if (cursor.moveToFirst()) {
            do {
                String course = cursor.getString(cursor.getColumnIndex("course"));
                courseList.add(course);
            } while (cursor.moveToNext());
        }
    }


    public ArrayList<String> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<String> newList) {
        courseList = newList;
        db.execSQL("DELETE FROM courseList;");
        for (String course: courseList) {
            db.execSQL("insert into courseList values ('" + course + "');");
        }
    }



}