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
    private float battery;
    private float altitude;
    private float grndSpeed;
    private float heading;
    private boolean armed;
    
    public DroneStatus(float battery, float altitude, float grndSpeed, float heading, boolean armed){
        this.battery = battery;
        this.altitude = altitude;
        this.grndSpeed = grndSpeed;
        this.heading = heading;
        this.armed = armed;
    }

    public float getBattery() {
        return battery;
    }

    public float getAltitude() {
        return altitude;
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
