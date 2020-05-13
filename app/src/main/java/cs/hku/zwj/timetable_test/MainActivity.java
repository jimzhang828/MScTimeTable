package cs.hku.zwj.timetable_test;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.islandparadise14.mintable.MinTimeTableView;
import com.islandparadise14.mintable.ScheduleDay;
import com.islandparadise14.mintable.ScheduleEntity;
import com.islandparadise14.mintable.OnScheduleClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private MyApplication application;
    private String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private ArrayList<ScheduleEntity> scheduleList = new ArrayList<>();
    private AlarmManager alarmManager;
    private AlertDialog alertDialog3;
    private DatabaseHelper dbHelper;
    private Button add;
    private Button prev;
    private Button next;
    private Button today;
    private TextView weekNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        today = findViewById(R.id.today);
        weekNum = findViewById(R.id.weekNum);
        dbHelper = new DatabaseHelper(this, "mydb", null, 1);
        application = (MyApplication)this.getApplication();

    }

    public void setCalendarTitle(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.WEEK_OF_YEAR, week + 3);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        String[] months = {
                "Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        String month = months[calendar.get(Calendar.MONTH)];
        TextView monthView = findViewById(R.id.month);
        monthView.setText(month);
        TextView[] textViews = new TextView[7];
        int[] days = {R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5, R.id.day6, R.id.day7};

        for (int i = 0; i < 7; i++) {
            textViews[i] = findViewById(days[i]);
            textViews[i].setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void searchDatabase(int week) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from sem2 where weekNum="+week, null);
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

                if (!application.getCourseList().contains(courseId)) continue;

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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        MinTimeTableView table = findViewById(R.id.timetable);
        table.initTable(day);

        // get the week number of today
        Date todayDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        int thisWeek = cal.get(Calendar.WEEK_OF_YEAR) - 3;
        System.out.println("##### " + thisWeek);

        // set the week number
        Intent myIntent = getIntent();
        int week = myIntent.getIntExtra("weekNum",thisWeek);
        weekNum.setText("Week "+week);

        // set calendar title
        setCalendarTitle(week);

        // get data from database according to week number
        searchDatabase(week);

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
        // 加一个临时展示通知用的
        final ScheduleEntity tmp_schedule = new ScheduleEntity(
                99999,
                "For Notification Demo",
                "None",
                ScheduleDay.SUNDAY,
                "10:00",
                "13:00",
                "yellow",
                "#000000"
        );
        tmp_schedule.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent intentMorning = new Intent(MainActivity.this, AlarmBroadcastReceiver.class);
                        intentMorning.setAction("CLOCK_IN");

                        intentMorning.putExtra("CourseName", tmp_schedule.getScheduleName());
                        intentMorning.putExtra("Time", "class begin at: " + tmp_schedule.getScheduleDay() + " " + tmp_schedule.getStartTime());
                        PendingIntent piMorning = PendingIntent.getBroadcast(MainActivity.this, 0, intentMorning, PendingIntent.FLAG_UPDATE_CURRENT);

                        long time = System.currentTimeMillis();
                        Date d = new Date(time);
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        c.add(Calendar.SECOND, 5);

                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), piMorning);
                    }
                }
        );
        scheduleList.add(tmp_schedule);

        table.updateSchedules(scheduleList);

        next.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("weekNum", week+1);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        prev.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("weekNum", week-1);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        today.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("weekNum", thisWeek);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

    }

    public void showMutilAlertDialog(View view){
        final String[] items = {
                "COMP7103B",
                "COMP7305B",
                "COMP7309",
                "COMP7404D",
                "COMP7405",
                "COMP7407",
                "COMP7408",
                "COMP7506A",
                "COMP7506B",
                "COMP7606A",
                "COMP7606B",
                "COMP7606C",
                "COMP7801",
                "COMP7901",
                "COMP7904"
        };
        boolean[] checked = {
                false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false
        };
        ArrayList<String> courseList = application.getCourseList();
        for (int i=0; i<items.length; ++i) {
            if (courseList.contains(items[i])) checked[i] = true;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Choose Your Enrolled Course: ");
        /**
         *第一个参数:弹出框的消息集合，一般为字符串集合
         * 第二个参数：默认被选中的，布尔类数组
         * 第三个参数：勾选事件监听
         */
        alertBuilder.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked){
                    System.out.println("#### check: " + items[i]);
                    checked[i] = true;
                }else {
                    System.out.println("#### uncheck: " + items[i]);
                    checked[i] = false;
                }
            }
        });
        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                courseList.clear();
                for (int j=0; j<checked.length; ++j) {
                    if (checked[j]) courseList.add(items[j]);
                }
                application.setCourseList(courseList);
                setNotification();
                Intent myIntent = getIntent();
                startActivity(myIntent);
                overridePendingTransition(0, 0);
                alertDialog3.dismiss();
                checkCourselist();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog3.dismiss();
            }
        });


        alertDialog3 = alertBuilder.create();
        alertDialog3.show();

    }

    public void checkCourselist() {
        System.out.println("checkcourselist");
        MyApplication application = (MyApplication) this.getApplication();
        ArrayList<String> courseList = application.getCourseList();
        for (int week=7; week<=16;week++) {
            DatabaseHelper dbHelper = new DatabaseHelper(this, "mydb", null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from sem2 where weekNum=" + week, null);
            Cursor cursor2 = db.rawQuery("select * from sem2 where weekNum=" + week, null);
            if (cursor.moveToFirst()) {
                do {
                    String courseId = cursor.getString(cursor.getColumnIndex("courseId"));
                    String scheduleDate = cursor.getString(cursor.getColumnIndex("scheduleDate"));
                    String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                    String endTime = cursor.getString(cursor.getColumnIndex("endTime"));

                    if (!courseList.contains(courseId)) continue;

                    if (cursor2.moveToFirst()) {
                        do {
                            String courseId2 = cursor.getString(cursor.getColumnIndex("courseId"));
                            String scheduleDate2 = cursor.getString(cursor.getColumnIndex("scheduleDate"));
                            String startTime2 = cursor.getString(cursor.getColumnIndex("startTime"));
                            String endTime2 = cursor.getString(cursor.getColumnIndex("endTime"));

                            if (!courseList.contains(courseId)) continue;

                            DateFormat df = new SimpleDateFormat("HH:mm"); //创建时间转换对象：时 分 秒
                            try {
                                Date date11 = df.parse(startTime); //转换为 date 类型 Debug：Thu Jan 01 11:11:11 CST 1970
                                Date date21 = df.parse(startTime2); // 		 Debug：Thu Jan 01 12:12:12 CST 1970
                                Date date12 = df.parse(endTime);
                                Date date22 = df.parse(endTime2);
                                assert date11 != null;
                                assert date22 != null;
                                boolean flag = date11.getTime() >= date22.getTime();
                                assert date21 != null;
                                assert date12 != null;
                                boolean flag2 = date21.getTime() >= date12.getTime();
                                if ((!scheduleDate.equals(scheduleDate2)) || flag || flag2)
                                {
                                    //没有重合
                                    //System.out.println("Class schedule time non-overlapping");
                                    //Toast.makeText(MainActivity.this, "Classes schedule time non-overlapping:", Toast.LENGTH_LONG).show();

                                }else if (!courseId.equals(courseId2))
                                {
                                    //两门课程时间重合
                                    System.out.println("class schedule time overlap= " + courseId + courseId2);
                                    Toast.makeText(MainActivity.this, "Classes schedule time overlapping:\n" + courseId + " & " +courseId2 +" at " + scheduleDate, Toast.LENGTH_LONG).show();
                                }

                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } while (cursor.moveToNext());
                    }

                } while (cursor.moveToNext());
            }

        }
    }

    public void setNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from sem2", null);
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

                if (!application.getCourseList().contains(courseId)) continue;

                SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
                Date date = null;
                try {
                    date = sdf.parse(scheduleDate + " " + startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                Intent intentMorning = new Intent(MainActivity.this, AlarmBroadcastReceiver.class);
                intentMorning.setAction("CLOCK_IN");

                intentMorning.putExtra("CourseName", courseName);
                intentMorning.putExtra("Time", "class begin at: " + scheduleDate + " " + startTime);
                PendingIntent piMorning = PendingIntent.getBroadcast(MainActivity.this, 0, intentMorning, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), piMorning);

            } while (cursor.moveToNext());
        }
    }

}
