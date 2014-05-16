package com.plaglabs.pillreminder.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by plagueis on 14/05/14.
 */
public class AlarmReciever extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent scheduledIntent = new Intent(context, AlarmActivity.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        scheduledIntent.putExtra("reminderId",intent.getExtras().getInt("reminderId"));
        context.startActivity(scheduledIntent);
    }

}
