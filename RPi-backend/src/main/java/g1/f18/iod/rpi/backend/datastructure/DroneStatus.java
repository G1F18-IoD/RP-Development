/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.datastructure;

/**
 * DroneStatus class
 * @author chris
 */
public class DroneStatus {
    private String gps;
    private float battery;
    private float grndSpeed;
    private float heading;
    private boolean armed;
    private boolean armable;
    private int lastHeartbeat;
    private String systemState;
    private String mode;
    
    
    public DroneStatus(String gps, float battery, float grndSpeed, float heading, boolean armed, int lastHeartbeat, boolean armable, String systemState, String mode){
        this.gps = gps;
        this.battery = battery;
        this.grndSpeed = grndSpeed;
        this.heading = heading;
        this.armed = armed;
        this.armable = armable;
        this.lastHeartbeat = lastHeartbeat;
        this.systemState = systemState;
        this.mode = mode;
    }

    public boolean isArmable() {
        return armable;
    }

    public int getLastHeartbeat() {
        return lastHeartbeat;
    }

    public String getSystemState() {
        return systemState;
    }

    public String getMode() {
        return mode;
    }
    
    public String getGps(){
        return this.gps;
    }

    public float getBattery() {
        return battery;
    }

    public float getGrndSpeed() {
        return grndSpeed;
    }

    public float getHeading() {
        return heading;
    }

    public boolean isArmed() {
        return armed;
    }
}
