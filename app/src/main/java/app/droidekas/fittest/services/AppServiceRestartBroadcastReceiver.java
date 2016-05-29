package app.droidekas.fittest.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static app.droidekas.fittest.log.Logger.logger;

/**
 * Created by Satyarth on 29/05/16.
 */
public class AppServiceRestartBroadcastReceiver extends BroadcastReceiver {

    public static final String NAME = "AppServiceRestartBroadcastReceiver";

    private static final String TAG = "AppServiceRestartBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        logger(TAG, "Resarting service");
    }
}
