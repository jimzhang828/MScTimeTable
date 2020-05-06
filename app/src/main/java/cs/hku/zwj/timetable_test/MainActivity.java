package cs.hku.zwj.timetable_test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.islandparadise14.mintable.MinTimeTableView;
import com.islandparadise14.mintable.ScheduleDay;
import com.islandparadise14.mintable.ScheduleEntity;
import com.islandparadise14.mintable.OnScheduleClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private ArrayList<ScheduleEntity> scheduleList = new ArrayList<>();
//    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        MinTimeTableView table = findViewById(R.id.timetable);
        table.initTable(day);

        dbHelper = new DatabaseHelper(this, "mydb", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] args = {"COMP7801"};
        Cursor cursor = db.rawQuery("select * from sem2 where weekNum=8", null);
        if (cursor.moveToFirst()) {
            do {

                String schedule_id = cursor.getString(cursor.getColumnIndex("schedule_id"));
                String courseId = cursor.getString(cursor.getColumnIndex("courseId"));
                String courseName = cursor.getString(cursor.getColumnIndex("courseName"));
                String scheduleDate = cursor.getString(cursor.getColumnIndex("scheduleDate"));
                String classroom = cursor.getString(cursor.getColumnIndex("classroom"));
                String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                String bgColor = cursor.getString(cursor.getColumnIndex("bgColor"));
                String weekNum = cursor.getString(cursor.getColumnIndex("weekNum"));

                SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
                Date date = null;
                try {
                    date = sdf.parse(scheduleDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int wek = c.get(Calendar.DAY_OF_WEEK);
                wek = (wek + 5) % 7;

                ScheduleEntity schedule = new ScheduleEntity(
                        Integer.parseInt(schedule_id),
                        courseId + "\n" + courseName,
                        classroom,
                        wek,
                        startTime,
                        endTime,
                        bgColor,
                        "#000000"
                );
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
        }
        table.setOnScheduleClickListener(
                new OnScheduleClickListener() {
                    @Override
                    public void scheduleClicked(@NotNull ScheduleEntity scheduleEntity) {
                        AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                .setTitle(scheduleEntity.getScheduleName())//标题
                                .setMessage("Classroom: " + scheduleEntity.getRoomInfo() +
                                        "\nStart at: " + scheduleEntity.getStartTime() +
                                        "\nEnd at: " + scheduleEntity.getEndTime())
                                .setIcon(R.mipmap.ic_launcher)//图标
                                .create();
                        alertDialog1.show();
                    }
                }
        );

//        ScheduleEntity schedule = new ScheduleEntity(
//                32,
//                "Database",
//                "IT Building 301",
//                ScheduleDay.TUESDAY,
//                "8:20",
//                "10:30",
//                "#73fcae68",
//                "#000000"
//        );
//        scheduleList.add(schedule);
        table.updateSchedules(scheduleList);
    }

}
