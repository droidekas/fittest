package app.droidekas.fittest.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import app.droidekas.fittest.Global;
import app.droidekas.fittest.R;
import app.droidekas.fittest.services.AppService;
import butterknife.Bind;
import butterknife.ButterKnife;

import static app.droidekas.fittest.log.Logger.logger;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private final int LOCATION_PERMISSION_REQUEST = 20;
    public static GoogleApiClient mClient = null;
    Intent mServiceIntent;


    @Bind(R.id.tv_base)
    TextView tvTest;
    @Bind(R.id.bt_next_act)
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        buildGoogleClient();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestLocationPermission();
            }
        }
        next.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RecordsActivity.class)));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        try {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Global.snackBar("App needs to access you location", tvTest);
                new Handler().postDelayed(() ->
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST)
                        , Toast.LENGTH_LONG + 500);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
        } catch (Exception e) {
            logger(TAG, e);
        }
    }

    private void buildGoogleClient() {
        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                logger(TAG, "Connected!!!");
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    logger(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    logger(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .enableAutoManage(this, 0, result -> {
                    logger(TAG, "Google Play services connection failed. Cause: " +
                            result.toString());

                    Global.snackBar("Exception while connecting to Google Play services: " +
                            result.getErrorMessage(), tvTest);
                })
                .build();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            try {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Got permission
                } else {
                    // Permission denied
                    Global.snackBar("App needs to access you location", tvTest);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        }
    }

    public void checkAndStartService() {
        AppService service = new AppService();
        mServiceIntent = new Intent(getApplicationContext(), service.getClass());
        if (!isServiceRunning(service.getClass())) {
            startService(mServiceIntent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkAndStartService();
    }


    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mServiceIntent);
    }
}
