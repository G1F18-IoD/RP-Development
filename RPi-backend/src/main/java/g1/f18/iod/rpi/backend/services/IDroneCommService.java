/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.services;

import g1.f18.iod.rpi.backend.datastructure.DroneStatus;
import java.util.List;

/**
 * Interface for serial communication methods
 * @author chris
 */
public interface IDroneCommService {
    
    /**
     * Method to arm the drone
     * @param parameters
     *              Parameters required by Pixhawk
     */
    public abstract void arm(List parameters);
    
    /**
     * Method to disarm the drone
     * @param parameters
     *              Parameters required by Pixhawk
     */
    public abstract void disarm(List parameters);
    
    /**
     * Method to get the drones current status, is wrapped in a DroneStatus object
     * @return 
     *              DroneStatus object with the drones current parameters/status
     */
    public abstract DroneStatus getStatus();
    
    /**
     * Method to perform Yaw roration counter clockwise
     * @param parameters 
     *              Parameters required by Pixhawk
     */
    public abstract void yawCounterCw(List parameters);
    
    /**
     * Method to perform Yaw roration clockwise
     * @param parameters 
     *              Parameters required by Pixhawk
     */
    public abstract void yawCw(List parameters);
}
