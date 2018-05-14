/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.services;

import com.MAVLink.Messages.MAVLinkMessage;

/**
 * Interface for serial communication methods
 * @author chris
 */
public interface IDroneCommService {
    public abstract boolean openPort();
    
    public abstract boolean closePort();
    
    public abstract boolean parseMessage(MAVLinkMessage msg);
}
