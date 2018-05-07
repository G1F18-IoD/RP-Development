/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.persistence.database;

import g1.f18.iod.rpi.backend.services.IDatabaseService;
import com.MAVLink.Messages.MAVLinkMessage;
import java.util.ArrayList;

/**
 * Database handler, as we all know and love it
 * @author chris
 */
public class DatabaseHandler implements IDatabaseService {

    @Override
    public boolean storeFlightPlan(ArrayList<MAVLinkMessage> msgs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
