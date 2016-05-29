package app.droidekas.fittest.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;

import app.droidekas.fittest.Global;

import static app.droidekas.fittest.log.Logger.logger;

/**
 * Created by Satyarth on 26/05/16.
 */
public class BatteryReceiver extends BroadcastReceiver {

    private static final String TAG = "BatteryReceiver";
    public static final String BATTERY_STATE = "batteryState";

    public BatteryReceiver() {
        super();
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        logger(TAG, "Battery status is %d and timestamp is %s", status, (System.currentTimeMillis() / 1000) + "");
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        logger(TAG, "battery charging is %s", isCharging + "");
        if (isCharging) {
            Bundle b = new Bundle();
            b.putString(BATTERY_STATE, usbCharge ? "usb" : (acCharge ? "plugged" : "unknown"));
            Global.fireLocationIntent(context, b);
        }
    }


}
