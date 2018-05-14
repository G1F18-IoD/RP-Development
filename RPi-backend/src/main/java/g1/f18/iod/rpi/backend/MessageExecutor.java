/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import g1.f18.iod.rpi.backend.services.IDroneCommService;

/**
 * Runnable Object which will be run in a Thread of its own.
 * This class handles the execution of a FlightPlan object and the MAVLinkMessage objects contained within it.
 * @author chris
 */
public class MessageExecutor implements Runnable {
    private final FlightPlan flightPlan;
    private final IDroneCommService serialComm;
    
    public MessageExecutor(FlightPlan flightPlan, IDroneCommService serialComm){
        this.flightPlan = flightPlan;
        this.serialComm = serialComm;
    }
    
    @Override
    public void run() {
        
    }
}
