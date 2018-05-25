/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.datastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data structure class for Drone Commands
 * @author chris
 */
public class DroneCommand {
    
    /**
     * ID of this DroneCommand in database. This is not equal to the ID's found in DRONE_CMD.java
     */
    private int id;

    /**
     * DRONE_CMD command ID. 
     */
    @JsonProperty("cmd_id")
    private int cmdId;

    /**
     * Parameter list.
     */
    @JsonProperty("parameters")
    private List parameters;

    /**
     * Constructor 
     * @param id database ID for this dronecommand. This is unique for each DroneCommand.
     * @param cmdId The DRONE_CMD ID for this DroneCommand.
     * @param parameters The parameter list for this DroneCommand.
     */
    public DroneCommand(int id, int cmdId, List parameters){
        this.id = id;
        this.cmdId = cmdId;
        this.parameters = parameters;
    }
    
    /**
     * Default constructor for JSON.
     */
    public DroneCommand(){
    }
    
    /**
     * Get ID for this DroneCommand. This is unique for each DroneCommand. Value is assigned by database.
     * @return ID for this DroneCommand
     */
    public int getId(){
        return this.id;
    }
    
    /**
     * Get DRONE_CMD ID for this DroneCommand. This is not equal to DroneCommand.id
     * @return The DRONE_CMD ID for this DroneCommand.
     */
    public int getCmdId() {
        return cmdId;
    }
    
    /**
     * Get this DroneCommands parameter list.
     * @return List of this DroneCommands parameters.
     */
    public List getParams(){
        return this.parameters;
        
    }
}
