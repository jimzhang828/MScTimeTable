package cs.hku.zwj.timetable_test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static androidx.core.app.NotificationCompat.DEFAULT_ALL;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("#### Received!");
        if (intent.getAction().equals("CLOCK_IN")) {
            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder= new NotificationCompat.Builder(context);
            NotificationChannel b = new NotificationChannel("111","notification", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(b);
            builder.setChannelId("111");
            //对builder进行配置
            String coursename = intent.getStringExtra("CourseName");
            String time = intent.getStringExtra("Time");
            intent.getCharSequenceArrayExtra("Extra");
            builder.setContentTitle(coursename) //设置通知栏标题
                    .setContentText(time) //设置通知栏显示内容
//                    .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
                    .setDefaults(DEFAULT_ALL)
                    .setOnlyAlertOnce(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true); //设置这个标志当用户单击面板就可以将通知取消
            Intent mIntent=new Intent(context, MainActivity.class);  //绑定intent，点击图标能够进入某activity
            PendingIntent mPendingIntent=PendingIntent.getActivity(context, 0, mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            manager.notify(0, builder.build());  //绑定Notification，发送通知请求
        }
    }

}
