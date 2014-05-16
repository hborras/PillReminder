package com.plaglabs.pillreminder.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.plaglabs.pillreminder.app.PillReminder.PillSelectFragment;
import com.plaglabs.pillreminder.app.PillReminder.PillsReminderFragment;
import com.plaglabs.pillreminder.app.Pills.PillsFragment;
import com.plaglabs.pillreminder.app.R;

import java.util.Calendar;
import java.util.List;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.PillReminder;
import SQLite.Model.Pill_PillReminder;

import static android.media.RingtoneManager.*;

/**
 * Created by plagueis on 15/05/14.
 */
public class AlarmActivity extends Activity{

    Button btnAccept, btnRepeat;
    Vibrator vibrator;
    ImageView imgPill;
    TextView tvDescription,tvPill;
    int reminderId;
    PillReminderDBHelper db;
    Uri notification;
    Pill_PillReminder pillpillreminder;
    Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window wind;
        wind = this.getWindow();
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.alarm_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern= {0,2000,1000};
        notification = getDefaultUri(TYPE_RINGTONE);
        r = getRingtone(getApplicationContext(), notification);
        r.play();
        vibrator.vibrate(pattern,0);
        reminderId = getIntent().getExtras().getInt("reminderId");
        db = new PillReminderDBHelper(this);
        pillpillreminder = db.getPillReminderWithPill(reminderId);

        //Toast.makeText(this,,Toast.LENGTH_SHORT).show();
        imgPill = (ImageView) findViewById(R.id.imgPill);

        imgPill.setImageResource(pillpillreminder.getPill().getmImage());

        imgPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopVibrator();
                r.stop();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                finish();
                startActivity(i);
            }
        });

        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(pillpillreminder.getPillReminder().getmDescription());

        tvPill = (TextView) findViewById(R.id.tvPill);
        tvPill.setText(pillpillreminder.getPill().getmName());

        killAlarms();
    }

    private void killAlarms() {
        PillReminder pillReminder = pillpillreminder.getPillReminder();;
        if(checkTimeOut(pillReminder)){
            Intent intentAlarm = new Intent(this, AlarmReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,pillReminder.getmReminderId(), intentAlarm, 0);
            db.updatePillReminderState(pillReminder.getmReminderId(),PillReminder.STATE_ARCHIVE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "This was the last alarm, because the date finish has passed", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
    }

    private boolean checkTimeOut(PillReminder pillReminder) {
        boolean timeOut = false;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());

        calendar1.add(Calendar.HOUR,pillReminder.getmEveryHours());

        int year;
        int month;
        int day;
        int hour;
        int minute;

        String date = pillReminder.getmDateFinish();

        year = Integer.parseInt(date.substring(6,10));
        month = Integer.parseInt(date.substring(3,5)) - 1;
        day = Integer.parseInt(date.substring(0,2));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.YEAR,year);
        calendar2.set(Calendar.MONTH,month);
        calendar2.set(Calendar.DAY_OF_MONTH,day);
        calendar2.set(Calendar.HOUR_OF_DAY, 00);
        calendar2.set(Calendar.MINUTE, 00);

        long c1,c2;

        c1 = calendar1.getTimeInMillis();
        c2 = calendar2.getTimeInMillis();

        if(c1 > c2){
            timeOut = true;
        }
        return timeOut;
    }

    private void stopVibrator(){
        vibrator.cancel();
    }


}
