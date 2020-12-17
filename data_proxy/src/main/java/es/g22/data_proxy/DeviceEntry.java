package es.g22.data_proxy;

import java.sql.Timestamp;

/**
 * DeviceEntry
 */
public class DeviceEntry {

    private String id;
    private Timestamp time;


    public DeviceEntry() {
    }

    public DeviceEntry(String id, Timestamp time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return this.time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public DeviceEntry id(String id) {
        this.id = id;
        return this;
    }

    public DeviceEntry time(Timestamp time) {
        this.time = time;
        return this;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }

    
}