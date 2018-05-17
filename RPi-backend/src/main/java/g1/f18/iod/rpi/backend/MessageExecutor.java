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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runnable Object which will be run in a Thread of its own. This class handles the execution of a FlightPlan object and the DroneCommand objects contained within it.
 *
 * @author chris
 */
public class MessageExecutor implements Runnable {

    /**
     * FlightPlan object this MessageExecutor will be executing once it is invoked by a Thread.start().
     */
    private final FlightPlan flightPlan;
    
    /**
     * IDroneCommService which is used by this MessageExecutor to execute the DroneCommand objects. 
     */
    private final IDroneCommService droneComm;
    
    /**
     * Time (in ms) for this thread to sleep between each DroneCommand execution
     */
    private final int sleepBetweenCmds;
    
    /**
     * Flag for the run() method to check whether it should keep running. 0 = terminate run, 1 = continue running
     */
    private AtomicInteger keepRunning;

    /**
     * Public constructor to create a new MessageExecutor Runnable
     * @param flightPlan FlightPlan object for this Runnable to execute through
     * @param droneComm DroneCommHandler object for this MessageExecutor to use
     * @param sleepBetweenCmds Time (in ms) to delay between each DroneCommand execution
     */
    public MessageExecutor(FlightPlan flightPlan, IDroneCommService droneComm, int sleepBetweenCmds) {
        this.flightPlan = flightPlan;
        this.droneComm = droneComm;
        this.sleepBetweenCmds = sleepBetweenCmds;
        this.keepRunning = new AtomicInteger();
        this.keepRunning.addAndGet(1);
    }

    /**
     * Invoking this method will cause this MessageExecutor to terminate its run() method and release it's resources once it dies.
     */
    public void terminate() {
        this.keepRunning.decrementAndGet();
    }

    /**
     * Invoking this method returns this MessageExecutors priority.
     * @return Priority of the FlightPlan in this MessageExecutor. (0 = low, 1 = middle, 2 = high)
     */
    public int getPriority() {
        return this.flightPlan.getPriority();
    }

    /**
     * Gets this MessageExecutors FlightPlan object.
     * Synchronized because this is accessible from MessageManager.
     * @return FlightPlan object 
     */
    public synchronized FlightPlan getFlightplan() {
        return this.flightPlan;
    }

    /**
     * Run method.
     * This is being called when the Thread.start() method is invoked.
     * The way this is implemented is by using a while(boolean) pattern.
     * This allows the MessageManager to terminate this MessageExecutor, by setting the AtomicInteger to 0 (zero)
     * 
     */
    @Override
    public void run() {
        int index = 0;
        while (this.keepRunning.get() == 1 && !this.flightPlan.getCommands().isEmpty()) {
            // Remove index DroneCommand object and switch on its cmdId.
            DroneCommand cmd = this.flightPlan.getCommands().remove(index);
            switch (cmd.getCmdId()) {
                // ARM
                case DRONE_CMD.ARM:
                    this.droneComm.arm(cmd.getParams());
                    break;

                // DISARM
                case DRONE_CMD.DISARM:
                    this.droneComm.disarm(cmd.getParams());
                    break;

                // GET_STATUS
                case DRONE_CMD.THROTTLE:
                    this.droneComm.throttle(cmd.getParams());
                    break;

                // YAW_COUNTER_CW (Counter-Clockwise)
                case DRONE_CMD.YAW_COUNTER_CW:
                    this.droneComm.yawCounterCw(cmd.getParams());
                    break;

                // YAW_CW (Clockwise)
                case DRONE_CMD.YAW_CW:
                    this.droneComm.yawCw(cmd.getParams());
                    break;

                // Default
                default:
                    break;
            }
            index++;
            try {
                Thread.sleep(sleepBetweenCmds);
            } catch (InterruptedException ex) {
            }

        }
    }
}
