package net.lisadean.c196lisadean;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private String title;
    private String message;

    public AlarmReceiver(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public void createAlarm(Context context, String date) {
        try {
            Date alarmDate = new SimpleDateFormat("MMMM dd, yyyy").parse(date);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Log.d("LOG AlarmReceiver", "time: " + alarmDate.getTime());

            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            am.set(AlarmManager.RTC_WAKEUP, alarmDate.getTime(), alarmIntent);
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
            context.registerReceiver(this, intentFilter);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("LOG AlarmReceiver", e.getMessage());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_document);
        builder.setContentTitle(title);
        builder.setContentText(message);
        Notification notification = builder.build();
        NotificationManagerCompat.from(context).notify(0, notification);
    }
}