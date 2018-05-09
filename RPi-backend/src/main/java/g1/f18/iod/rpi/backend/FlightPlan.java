/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import com.MAVLink.Messages.MAVLinkMessage;
import java.util.Queue;

/**
 * Class for overall flightplans, containing list of MAVLink messages relevant for this mission
 * @author chris
 */
public class FlightPlan {
    /**
     * List of MAVLink messages for this flight
     */
    private Queue<MAVLinkMessage> mavlinkMessages;
    
    /**
     * Auth token for this flightplans HTTP request
     */
    private String authToken;
    
    /**
     * Priority of this flightplan (0 = low, 1 = middle, 2 = high)
     */
    private int priority;
    
    /**
     * ID of the user who sent this flightplan HTTP request
     */
    private int ownerUserId;
    
    public FlightPlan(Queue<MAVLinkMessage> msgs, int priority, String authToken, int ownerUserId){
        this.mavlinkMessages = msgs;
        this.authToken = authToken;
        this.priority = priority;
        this.ownerUserId = ownerUserId;
    }
    
    /**
     * Get queue of MAVLinkMessage objects for this flightplan
     * @return 
     *              Queue of MAVLinkMessage objects
     */
    public Queue<MAVLinkMessage> getMessageQueue(){
        return this.mavlinkMessages;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getPriority() {
        return priority;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }
    
    
    
}
