package com.plaglabs.pillreminder.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.plaglabs.pillreminder.app.AlarmReciever;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by plagueis on 14/05/14.
 */
public class AlarmScheduler {

    public void scheduleAlarm(Context context,int reminderId, int year, int month, int day, int hour, int minute, int every_hours){
        Intent intentAlarm = new Intent(context, AlarmReciever.class);

        // create the object
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intentAlarm.putExtra("reminderId",reminderId);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,reminderId,intentAlarm,0);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(System.currentTimeMillis());

        while (calendar.getTimeInMillis()< calendarNow.getTimeInMillis()){
            calendar.add(Calendar.MINUTE,every_hours);
        }
        if(MainActivity.DEBUG>0) {
            Log.e("Alarm", String.valueOf(year));
            Log.e("Alarm", String.valueOf(month));
            Log.e("Alarm", String.valueOf(day));
            Log.e("Alarm", String.valueOf(hour));
            Log.e("Alarm", String.valueOf(minute));
            Log.e("Alarm", String.valueOf(every_hours));
        }
        //set the alarm for particular time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                every_hours * 1000 * 60 , alarmIntent);
    }
}
