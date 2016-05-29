package app.droidekas.fittest.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Satyarth on 29/05/16.
 */
public class AppService extends Service {
    BatteryReceiver batteryReceiver = new BatteryReceiver();
    WifiStateChangeReceiver wifiStateChangeReceiver = new WifiStateChangeReceiver();

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        register();
        return START_STICKY;
    }


    public void register() {
        registerReceiver(batteryReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(wifiStateChangeReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregister();
        Intent broadcastIntent = new Intent(AppServiceRestartBroadcastReceiver.NAME);
        sendBroadcast(broadcastIntent);
    }

    public void unregister() {
        unregisterReceiver(batteryReceiver);
        unregisterReceiver(wifiStateChangeReceiver);
    }
}
