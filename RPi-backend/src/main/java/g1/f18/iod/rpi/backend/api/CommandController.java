/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.api;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.enums.MAV_CMD;
import g1.f18.iod.rpi.backend.MessageExecutor;
import g1.f18.iod.rpi.backend.jsonstructure.Json;
import gnu.io.RXTXCommDriver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.MAVLINK javadoc
 * https://download.roboticsapi.org/2.0/javadoc/overview-summary.html
 * http://api.ning.com/files/i*tFWQTF2R*7Mmw7hksAU-u9IABKNDO9apguOiSOCfvi2znk1tXhur0Bt00jTOldFvob-Sczg3*lDcgChG26QaHZpzEcISM5/MAVLINK_FOR_DUMMIESPart1_v.1.1.pdf
 * 
 * @author chris
 */
@RestController
public class CommandController {
    
    @RequestMapping("/command/arm")
    public String arm(){
        msg_command_long armMsg = new msg_command_long();
        armMsg.command = MAV_CMD.MAV_CMD_COMPONENT_ARM_DISARM;
        armMsg.param1 = 1;
        
        MAVLinkPacket mavP = armMsg.pack();
        RXTXCommDriver comm = new RXTXCommDriver();
        
        /**
         * Send message to pixhawk through RPi TX pin.
         * Receive response from pixhawk through RPi RX pin. 
         * How?
         * We have to communicate with the pixhawk through serial ports. Using the RXTX library.
         * 
         */
        return "";
    }
    
    /**
     * Method to receive an entire flightplan formatted in json
     * @param json
     *                  JSON formatted flightplan including authentication tokens, 
     *                  MAVLink messages etc.
     * @return 
     *                  Status code
     */
    @RequestMapping("/command/flightplan")
    public String flightplan(@RequestParam(value="json", defaultValue="") String json){
        if(json.equals("")){ // Empty json string
            return "400";
        }
        // Decode the json string
        MessageExecutor.getInstance().convertFlightPlan(Json.decode(json));
        return "200";
    }
}
