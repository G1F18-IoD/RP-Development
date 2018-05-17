/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Class intended to invoke the MessageManager.getNonexecutedFlightplans() and thereby populating the MessageManager with FlightPlan objects
 * which has not yet been executed by the drone.
 * This class will be constructed and init() called after Spring is done configuring it's variables and environment
 * @author chris
 */
@Component
public class Populator {
    
    /**
     * Method will be called AFTER Spring has finished configuring it's beans etc.
     */
    @PostConstruct
    public void init(){
        MessageManager.getInstance().getNonexecutedFlightplans();
    }
}
