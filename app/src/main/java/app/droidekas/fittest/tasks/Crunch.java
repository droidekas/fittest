package app.droidekas.fittest.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import app.droidekas.fittest.Global;
import app.droidekas.fittest.models.InactivityHistory;
import app.droidekas.fittest.models.LocationHistory;

import static app.droidekas.fittest.log.Logger.logger;

/**
 * Created by Satyarth on 29/05/16.
 */
public class Crunch extends AsyncTask<Context, Integer, Void> {
    public static final long HOURS = 60, MINUTES = 60, DAYS = 24;
    public static final long DURATION_THRESHOLD = 1 * HOURS * 1 * MINUTES;
    private static final String TAG = "Crunch";
    private static final long MAX_LONG = Long.MAX_VALUE;
    private List<InactivityHistory> mList = new ArrayList<>();
    private Context ctx;
    CrunchListener mListener;


    public Crunch(CrunchListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Context... params) {
        ctx = params[0];
        InactivityHistory.deleteAll(InactivityHistory.class);
        buildData();
        return null;
    }

    private void buildData() {
        LocationHistory startLh = getFirstLocationHistoryAfterLastInactivityEvent(getLastInactivityPeriod());
//        long startStamp = startLh != null ? startLh.getTimestamp() : MAX_LONG;
        long startStamp = 0l;
        while (MAX_LONG > startStamp)
            startStamp = buildSession(getFirstLocationHistoryAfterLastInactivityEvent(startStamp));
        InactivityHistory.saveInTx(mList);
    }

    private long buildSession(LocationHistory lh) {
        if (lh != null) {
            LocationHistory lastLh = getLastLocationHistoryForGivenLocation(lh);
            if (lastLh != null) {
                logger(TAG, "lastLh is %s and firstLh is %s", lastLh.toString(), lh.toString());
                if (lastLh.getTimestamp() - lh.getTimestamp() >= DURATION_THRESHOLD)
                    mList.add(new InactivityHistory(lh, lastLh));
                return lastLh.getTimestamp() + 1;
            }
        }
        return MAX_LONG;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Global.tst(ctx, "Done");
        mListener.gotData();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }


    private long getLastInactivityPeriod() {
        List<InactivityHistory> response = InactivityHistory.findWithQuery(InactivityHistory.class, "Select * from Inactivity_History where end_time <= ? ORDER BY end_time DESC LIMIT 1", (System.currentTimeMillis() / 1000) + "");
        if (response != null && response.size() > 0)
            return (response.get(0)).getEndTime();
        return -1;
    }

    private LocationHistory getFirstLocationHistoryAfterLastInactivityEvent(long timestamp) {
        List<LocationHistory> response = LocationHistory.findWithQuery(LocationHistory.class, "Select * from LOCATION_HISTORY where TIMESTAMP >= ? ORDER BY TIMESTAMP ASC", timestamp + "");
        if (response != null && response.size() > 0)
            return response.get(0);
        return null;
    }


    private LocationHistory getLastLocationHistoryForGivenLocation(LocationHistory lh) {
        List<LocationHistory> response = LocationHistory.findWithQuery(LocationHistory.class, "SELECT *  FROM LOCATION_HISTORY " +
                "WHERE id > ? and" +
                " id < COALESCE((SELECT min(LOCATION_HISTORY.id) " +
                "FROM LOCATION_HISTORY JOIN " +
                "(SELECT * FROM LOCATION_HISTORY WHERE id > ? ORDER BY id LIMIT 1)" +
                " AS first ON LOCATION_HISTORY.id > first.id AND" +
                " (LOCATION_HISTORY.LOCATION_LAT != first.LOCATION_LAT" +
                " OR LOCATION_HISTORY.LOCATION_LONG != first.LOCATION_LONG))," +
                " 'infinite')", lh.getId() + "", lh.getId() + "");
        if (response != null && response.size() > 0)
            return response.get(response.size() - 1);
        return null;
    }

    public interface CrunchListener {
        void gotData();
    }

}
