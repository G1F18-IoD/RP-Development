/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import g1.f18.iod.rpi.backend.datastructure.DRONE_CMD;
import g1.f18.iod.rpi.backend.datastructure.DroneCommand;
import g1.f18.iod.rpi.backend.datastructure.FlightPlan;
import g1.f18.iod.rpi.backend.services.IDroneCommService;

/**
 * Runnable Object which will be run in a Thread of its own.
 * This class handles the execution of a FlightPlan object and the MAVLinkMessage objects contained within it.
 * @author chris
 */
public class MessageExecutor implements Runnable {
    private final FlightPlan flightPlan;
    private final IDroneCommService droneComm;
    private final int sleepBetweenCmds;
    
    public MessageExecutor(FlightPlan flightPlan, IDroneCommService droneComm, int sleepBetweenCmds){
        this.flightPlan = flightPlan;
        this.droneComm = droneComm;
        this.sleepBetweenCmds = sleepBetweenCmds;
    }
    
    public int getPriority(){
        return this.flightPlan.getPriority();
    }
    
    @Override
    public void run() {
        for(DroneCommand cmd : this.flightPlan.getCommands()){
            switch(cmd.getCmdId()){
                // ARM
                case DRONE_CMD.ARM:
                    droneComm.arm(cmd.getParams());
                    break;
                    
                // DISARM
                case DRONE_CMD.DISARM:
                    break;
                    
                // GET_STATUS
                case DRONE_CMD.THROTTLE:
                    break;
                    
                // YAW_COUNTER_CW (Counter-Clockwise)
                case DRONE_CMD.YAW_COUNTER_CW:
                    break;
                    
                // YAW_CW (Clockwise)
                case DRONE_CMD.YAW_CW:
                    break;
                    
                // Default
                default:
                    break;
            }
            try {
                Thread.sleep(sleepBetweenCmds);
            } catch (InterruptedException ex) {
            }
        }
    }
}
