package app.droidekas.fittest.models;

import com.orm.SugarRecord;

/**
 * Created by Satyarth on 26/05/16.
 */
public class InactivityHistory extends SugarRecord {

    long duration;
    long startTime, endTime;
    String reason;
    Double locationLat;
    Double locationLong;


    public InactivityHistory() {
    }


    public InactivityHistory(LocationHistory firstLh, LocationHistory lastLh) {
        super();
        startTime = firstLh.getTimestamp();
        endTime = lastLh.getTimestamp();
        locationLat = firstLh.getLocationLat();
        locationLong = firstLh.getLocationLong();
        duration = endTime - startTime;
    }


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(Double locationLong) {
        this.locationLong = locationLong;
    }
}
