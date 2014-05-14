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
        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
        Long time = new GregorianCalendar().getTimeInMillis()+5*1000;

        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
        Intent intentAlarm = new Intent(context, AlarmReciever.class);

        // create the object
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,reminderId,intentAlarm,0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,every_hours);

        Log.e("Alarm",String.valueOf(year));
        Log.e("Alarm",String.valueOf(month));
        Log.e("Alarm",String.valueOf(day));
        Log.e("Alarm",String.valueOf(hour));
        Log.e("Alarm",String.valueOf(minute));
        Log.e("Alarm",String.valueOf(every_hours));



        //set the alarm for particular time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                every_hours * 1000 , alarmIntent);
        Toast.makeText(context, "Actual time: " + System.currentTimeMillis() + "\n" + "Alarm Time: " + calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();

    }
}
