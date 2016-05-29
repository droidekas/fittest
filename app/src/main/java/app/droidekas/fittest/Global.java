package app.droidekas.fittest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.orm.SugarContext;

import java.util.Locale;

import app.droidekas.fittest.services.LocationService;

/**
 * Created by Satyarth on 29/05/16.
 */
public class Global extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        Stetho.initializeWithDefaults(this);
    }


    public static void fireLocationIntent(Context ctx, Bundle extras) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, ctx, LocationService.class);
        intent.putExtras(extras);
        ctx.startService(intent);
    }

    public static void tst(Context ctx, String s, String... args) {
        Toast.makeText(ctx, String.format(Locale.ENGLISH, s, args), Toast.LENGTH_SHORT).show();
    }

    public static void snackBar(String string, View tvTest) {
        Snackbar.make(tvTest, string, Snackbar.LENGTH_SHORT).show();
    }

}
