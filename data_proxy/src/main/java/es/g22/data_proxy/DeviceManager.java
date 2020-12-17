package es.g22.data_proxy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * DeviceManager
 */

@Component
public class DeviceManager {
    
    private ArrayList<DeviceEntry> devices;

    public DeviceManager(){
        devices = new ArrayList<DeviceEntry>();
    }

    public String newId(){
        //Generate new id
        String id;
        do{
            id = generateString(6);
        }while(checkDevice(id));

        return id;
    }

    public boolean keepAlive(String id){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DeviceEntry device = getDevice(id);
        if ( device != null){
            device.setTime(timestamp);
        }
        else{
            devices.add(new DeviceEntry(id, timestamp));
        }
        return true;
    }

    public ArrayList<DeviceEntry> getList(){
        return devices;
    }

    private String generateString(int dim) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < dim) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public boolean checkDevice(String id){
        boolean in = false;
        for(DeviceEntry d : devices){
            if (d.getId().equals(id))
                in = true;
        }
        return in;
    }
    
    public DeviceEntry getDevice(String id){
        for(DeviceEntry d : devices){
            if (d.getId().equals(id))
                return d;
        }
        return null;
    }

    @Scheduled(fixedRate = 20000)
    public void clearOldDevices(){
        if (devices.size() < 1)
            return;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (DeviceEntry d : devices){
            if (timestamp.getTime() - d.getTime().getTime() > 20000)
                devices.remove(d);
        }
    }
}