/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.persistence.dronecomm;

import g1.f18.iod.rpi.backend.datastructure.DroneStatus;
import g1.f18.iod.rpi.backend.services.IDroneCommService;
import java.io.IOException;
import java.util.List;

/**
 * Class for handling serial communication
 * @author chris
 */
public class DroneCommHandler implements IDroneCommService {
    
    public DroneCommHandler(){
        
    }

    @Override
    public void arm(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disarm(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DroneStatus getStatus(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void yawCounterCw(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void yawCw(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void throttle(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void executeCommand(String python){
        try {
            Process p = Runtime.getRuntime().exec(python);
        } catch (IOException ex) {
            System.out.println("Internal error upon executing python script on RPi.");
        }
    }
}
