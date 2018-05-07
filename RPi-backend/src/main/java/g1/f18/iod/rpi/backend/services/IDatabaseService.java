/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.services;

import g1.f18.iod.rpi.backend.FlightPlan;

/**
 * Interface for Database storage & fetching methods
 * @author chris
 */
public interface IDatabaseService {
    /**
     * Method to store whole flight plans in Database
     * @param flightPlan 
     *              FlightPlan object to store in DB, including MAVLink Message objects
     * @return 
     *              True on succesful storage, false otherwise
     */
    public abstract boolean storeFlightPlan(FlightPlan flightPlan);
}
