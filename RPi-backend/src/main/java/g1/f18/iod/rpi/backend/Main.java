/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.MAVLink.*;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.enums.MAV_CMD;

/**
 *
 * @author chris
 */
@SpringBootApplication
public class Main {

    /**
     * Old main method
     * @param args
     * @deprecated as of 07-05/2018, with introduction of new Main method in Application.class
     */
    @Deprecated
    public void main(String[] args) {
        /**
         * Create messages by creating a new instance of msg_command_long. Other messages exists, but unsure if they are relevant
         * Set the command attribute to the desired command to be sent to the drone. (See com/MAVLink/enums/MAV_CMD.java for list of command enums)
         * Generate a MAVLinkPacket by using the pack() method on the msg_ object.
         * Send the packet to the drone through REST API. 
         */
        SpringApplication.run(Main.class, args);
        
        msg_command_long msg = new msg_command_long();
        msg.command = MAV_CMD.MAV_CMD_COMPONENT_ARM_DISARM;
        msg.param1 = 1;
        MAVLinkPacket mav = msg.pack();
        System.out.println(msg);
        System.out.println(mav);
    }
}
