/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.services;

import com.MAVLink.Messages.MAVLinkMessage;
import java.util.ArrayList;

/**
 * Interface for Database storage & fetching methods
 * @author chris
 */
public interface IDatabaseService {
    /**
     * Method to store whole flight plans in Database
     * @param msgs 
     *              List of MAVLink Messages to store in DB
     * @return 
     *              True on succesful storage, false otherwise
     */
    public boolean storeFlightPlan(ArrayList<MAVLinkMessage> msgs);
}
