/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.persistence.dronecomm;

import com.MAVLink.Messages.MAVLinkMessage;
import g1.f18.iod.rpi.backend.services.IDroneCommService;

/**
 * Class for handling serial communication
 * @author chris
 */
public class DroneCommHandler implements IDroneCommService {
    
    public DroneCommHandler(){
    }

    @Override
    public boolean openPort() {
        
        return false;
    }

    @Override
    public boolean closePort() {
        
        return false;
    }

    @Override
    public boolean parseMessage(MAVLinkMessage msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
