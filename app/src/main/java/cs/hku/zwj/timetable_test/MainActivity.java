package cs.hku.zwj.timetable_test;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.islandparadise14.mintable.MinTimeTableView;
import com.islandparadise14.mintable.ScheduleDay;
import com.islandparadise14.mintable.ScheduleEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private MyApplication application;
    private final String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private final ArrayList<ScheduleEntity> scheduleList = new ArrayList<>();
    private AlarmManager alarmManager;
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

    public int findWeekFromDate(String scheduleDate) {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date date = null;
        try {
            date = sdf.parse(scheduleDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        assert date != null;
        c.setTime(date);
        int wek = c.get(Calendar.DAY_OF_WEEK);
        wek = (wek + 5) % 7;
        return wek;
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

                int wek = findWeekFromDate(scheduleDate);

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
            scheduleEntity -> {
                AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(scheduleEntity.getScheduleName())//标题
                        .setMessage("Classroom: " + scheduleEntity.getRoomInfo() +
                                "\nStart at: " + scheduleEntity.getStartTime() +
                                "\nEnd at: " + scheduleEntity.getEndTime())
                        .setIcon(R.mipmap.ic_launcher)//图标
                        .create();
                alertDialog1.show();
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
            v -> {
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
        );
        scheduleList.add(tmp_schedule);

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

        add.setOnClickListener(v->{
            AlertDialog choiceDialog = new AlertDialog.Builder(this).create();
            choiceDialog.show();

            Window window = choiceDialog.getWindow();
            assert window != null;
            window.setContentView(R.layout.addchoice);
            Button custom = window.findViewById(R.id.custom);
            Button msccs = window.findViewById(R.id.msccs);

            custom.setOnClickListener(vvv->{

//                AlertDialog customDialog = new AlertDialog.Builder(this).create();
//                customDialog.show();
//                Window customWindow = customDialog.getWindow();
//                assert customWindow != null;
//                customWindow.setContentView(R.layout.custom);
//                customDialog.setTitle("Input Course Information: ");

                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.custom, null);
                AlertDialog.Builder customAlert = new AlertDialog.Builder(this);
                customAlert.setTitle("Input Course Information: ");
                customAlert.setView(promptsView);

                customAlert.setPositiveButton("OK", (dialog, which) -> {
                    String courseId = ((EditText) promptsView.findViewById(R.id.courseId)).getText().toString();
                    String courseName = ((EditText) promptsView.findViewById(R.id.courseName)).getText().toString();
                    String courseDate = ((EditText) promptsView.findViewById(R.id.courseDate)).getText().toString();
//                    String location = ((EditText) promptsView.findViewById(R.id.location)).getText().toString();
//                    String startTime = ((EditText) promptsView.findViewById(R.id.startTime)).getText().toString();
//                    String endTime = ((EditText) promptsView.findViewById(R.id.endTime)).getText().toString();
                    String location = "111";
                    String startTime = "10:00";
                    String endTime = "12:00";

                    ScheduleEntity custom_schedule = new ScheduleEntity(
                            888,
                            courseId + "\n" + courseName,
                            location,
                            findWeekFromDate(courseDate),
                            startTime,
                            endTime,
                            "yellow",
                            "#000000"
                    );
                    scheduleList.add(custom_schedule);

                });

                customAlert.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                AlertDialog customDialog = customAlert.create();
                customDialog.show();

            });

            msccs.setOnClickListener(this::showMutilAlertDialog);

        });


        // 点击课表空白处，获取相应的scheduleDay和time
//        table.setOnTimeCellClickListener((scheduleDay, time)->{
//            System.out.println("scheduleDay" + scheduleDay);
//            System.out.println("time" + time);
//        });

        table.updateSchedules(scheduleList);

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
        /*
         第一个参数:弹出框的消息集合，一般为字符串集合
          第二个参数：默认被选中的，布尔类数组
          第三个参数：勾选事件监听
         */
        alertBuilder.setMultiChoiceItems(items, checked, (dialogInterface, i, isChecked) -> {
            if (isChecked){
                System.out.println("#### check: " + items[i]);
                checked[i] = true;
            }else {
                System.out.println("#### uncheck: " + items[i]);
                checked[i] = false;
            }
        });
        alertBuilder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            courseList.clear();
            for (int j=0; j<checked.length; ++j) {
                if (checked[j]) courseList.add(items[j]);
            }
            application.setCourseList(courseList);
            setNotification();
            Intent myIntent = getIntent();
            startActivity(myIntent);
            overridePendingTransition(0, 0);
            checkCourselist();
        });

        alertBuilder.setNegativeButton("Cancel", (dialog, i) -> dialog.cancel());

        AlertDialog alertDialog3 = alertBuilder.create();
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
                            String courseId2 = cursor2.getString(cursor.getColumnIndex("courseId"));
                            String scheduleDate2 = cursor2.getString(cursor.getColumnIndex("scheduleDate"));
                            String startTime2 = cursor2.getString(cursor.getColumnIndex("startTime"));
                            String endTime2 = cursor2.getString(cursor.getColumnIndex("endTime"));

                            if (!courseList.contains(courseId2)) continue;

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
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Classes schedule time overlapping:\n" + courseId + " & " +courseId2 +" at " + scheduleDate,
                                            Toast.LENGTH_LONG).show();
                                }

                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } while (cursor2.moveToNext());
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
                assert date != null;
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
