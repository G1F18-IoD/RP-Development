/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.datastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data structure class for Flight plans
 *
 * @author chris
 */
public class FlightPlan {

    /**
     * ID of this flightplan. Assigned by Databasehandler
     */
    private int id;

    /**
     * Time at which this flightplan was created, in UNIX.
     */
    @JsonProperty("created_at")
    private long createdAt;

    /**
     * Priority of this flightplan (0 = low, 1 = middle, 2 = high).
     */
    @JsonProperty("priority")
    private int priority;

    /**
     * Delay to be held between each command is executed by MessageExecutor
     */
    @JsonProperty("cmd_delay")
    private int cmdDelay;
    
    /**
     * List of DroneCommand objects for this flight.
     */
    @JsonProperty("commands")
    private List<DroneCommand> commands;
    
    /**
     * Time at which this flightplan was executed, in UNIX.
     */
    private long executedAt;

    /**
     * Public constructor if we need to create new Flightplans internally in the RPi.
     * Might be used to return executed previously executed flightplans to the Backend.
     * @param cmds List of DroneCommand objects this flightplan contains
     * @param id ID of this FlightPlan
     * @param priority Priority for this flightplan (0 = low, 1 = middle, 2 = high)
     * @param createdAt UNIX time of which this flightplan was created
     * @param executedAt UNIX time of which this flightplan was executed
     */
    public FlightPlan(List<DroneCommand> cmds, int id, int priority, long createdAt, long executedAt, int cmdDelay) {
        this.id = id;
        this.commands = cmds;
        this.priority = priority;
        this.createdAt = createdAt;
        this.executedAt = executedAt;
        this.cmdDelay = cmdDelay;
    }
    
    /**
     * Default constructor for JSON
     */
    public FlightPlan(){
    }

    /**
     * Get priority of this flightplan
     * @return 
     *              int representation of the priority. (0 = low, 1 = middle, 2 = high)
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Get List of DroneCommand objects for this flightplan
     *
     * @return List of DroneCommand objects
     */
    public List<DroneCommand> getCommands() {
        return commands;
    }

    /**
     * Get UNIX time of which this flightplan was created
     * @return 
     *              UNIX time of creation
     */
    public long getCreatedAt() {
        return this.createdAt;
    }
    
    /**
     * Get UNIX time of which this flightplan was executed
     * @return 
     *              UNIX time of execution
     */
    public long getExecutedAt(){
        return this.executedAt;
    }
    
    /**
     * Get ID of this flightplan
     * @return 
     *              ID of this flightplan
     */
    public int getId(){
        return this.id;
    }
    
    /**
     * Get delay (in ms) between each command for this flightplan
     * @return 
     *              Integer delay between each command execution
     */
    public int getCmdDelay(){
        return this.cmdDelay;
    }

    /**
     * Sets this FlightPlan id. This is assigned by the Databasehandler.
     * @param fpid The unique Id to set this FlightPlan's ID to.
     */
    public void setId(int fpid) {
        this.id = fpid;
    }

}
