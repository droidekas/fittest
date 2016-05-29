package app.droidekas.fittest.models;

import com.orm.SugarRecord;

/**
 * Created by Satyarth on 29/05/16.
 */
public class LocationHistory extends SugarRecord {
    Double locationLat;
    Double locationLong;
    boolean battery;
    boolean wifi;
    String wifiName;
    String batteryChargeType;
    long timestamp;

    public LocationHistory() {
    }


    public String getBatteryChargeType() {
        return batteryChargeType;
    }

    public void setBatteryChargeType(String batteryChargeType) {
        this.batteryChargeType = batteryChargeType;
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

    public boolean isBattery() {
        return battery;
    }

    public void setBattery(boolean battery) {
        this.battery = battery;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LocationHistory{" +
                "locationLong=" + locationLong +
                ", locationLat=" + locationLat +
                ", timestamp=" + timestamp +
                ",id=" + getId() +
                '}';
    }
}
