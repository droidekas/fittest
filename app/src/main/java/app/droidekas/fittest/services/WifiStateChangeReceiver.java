package app.droidekas.fittest.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import app.droidekas.fittest.Global;

import static app.droidekas.fittest.log.Logger.logger;

/**
 * Created by Satyarth on 28/05/16.
 */
public class WifiStateChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "WifiStateChangeReceiver";
    public static final String WIFI_NAME = "wifiName";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if (SupplicantState.isValidState(state)
                    && state == SupplicantState.COMPLETED) {
                checkConnectedToDesiredWifi(context);
            }
        }
    }

    /**
     * Detect you are connected to a specific network.
     */
    private void checkConnectedToDesiredWifi(Context ctx) {

        WifiManager wifiManager =
                (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi = wifiManager.getConnectionInfo();
        if (wifi != null) {
            logger(TAG, "Connected to wifi %s", wifi.getSSID());
            Global.tst(ctx, "Connected to wifi %s", wifi.getSSID());
            Bundle extras = new Bundle();
            extras.putString(WIFI_NAME, wifi.getSSID());
            Global.fireLocationIntent(ctx, extras);
        }
    }
}
