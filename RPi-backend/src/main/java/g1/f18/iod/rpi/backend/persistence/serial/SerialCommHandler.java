/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.persistence.serial;

import com.MAVLink.Messages.MAVLinkMessage;
import com.javatechnics.rs232.Serial;
import g1.f18.iod.rpi.backend.services.ISerialCommService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for handling serial communication
 * @author chris
 */
public class SerialCommHandler implements ISerialCommService {
    private final String PORT = "/dev/ttyama0";
    private final Serial SERIAL;
    
    public SerialCommHandler(){
        this.SERIAL = new Serial();
    }

    @Override
    public boolean openPort() {
        //return this.SERIAL.open(PORT, flags);
        return false;
    }

    @Override
    public boolean closePort() {
        try {
            this.SERIAL.close();
        } catch (IOException ex) {
            Logger.getLogger(SerialCommHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.SERIAL.isOpen();
    }

    @Override
    public boolean parseMessage(MAVLinkMessage msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
