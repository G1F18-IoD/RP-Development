/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.services;

import g1.f18.iod.rpi.backend.datastructure.FlightPlan;
import java.util.List;

/**
 * Database service interface.
 * @author chris
 */
public interface IDatabaseService {
    /**
     * Method to store whole flight plans in Database
     * @param flightPlan 
     *              FlightPlan object to store in DB, including MAVLink Message objects
     * @return 
     *              ID of the newly stored flightplan object, -1 on failure
     */
    public abstract int storeFlightPlan(FlightPlan flightPlan);
    
    /**
     * Method to get a list of all stored flightplans
     * @return List of FlightPlan objects stored in database
     */
    public abstract List<FlightPlan> getFlightPlans();
    
    /**
     * Method to get a list of non executed FlightPlans from database.
     * @return List of FlightPlan objects, which has not yet been executed, from database.
     */
    public abstract List<FlightPlan> getNonexecutedFlightPlans();
    
    /**
     * Method to get all flightlogs concerning a FlightPlan ID
     * @param id ID of the flightplan which flightlogs should be returned
     * @return List of flightlogs based on id
     */
    public abstract List<String> getFlightLogs(int id);
    
    /**
     * Method to get all flightlogs stored in database
     * @return List of all flightlogs
     */
    public abstract List<String> getFlightLogs();
    
    /**
     * Method to store a unix time in database for when flightplan with flightplanId has begun being executed
     * @param flightplanId Id of the flightplan which has begun execution
     * @return True on succes, false otherwise
     */
    public abstract boolean beginExecution(int flightplanId);
}
