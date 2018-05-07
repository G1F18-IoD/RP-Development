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
    
    public FlightPlan(Queue<MAVLinkMessage> msgs){
        this.mavlinkMessages = msgs;
    }
    
    /**
     * Get queue of MAVLinkMessage objects for this flightplan
     * @return 
     *              Queue of MAVLinkMessage objects
     */
    public Queue<MAVLinkMessage> getMessageQueue(){
        return this.mavlinkMessages;
    }
    
}
