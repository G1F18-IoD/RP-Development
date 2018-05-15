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
     * ID of this flightplan
     */
    @JsonProperty("id")
    private int id;
    
    /**
     * Auth token for this flightplans HTTP request.
     */
    @JsonProperty("auth_token")
    private String authToken;

    /**
     * ID of the User who created this flightplan.
     */
    @JsonProperty("author_id")
    private int authorId;

    /**
     * Time at which this flightplan was created, in UNIX.
     */
    @JsonProperty("created_at")
    private int createdAt;

    /**
     * Priority of this flightplan (0 = low, 1 = middle, 2 = high).
     */
    @JsonProperty("priority")
    private int priority;

    /**
     * List of MAVLink messages for this flight.
     */
    @JsonProperty("commands")
    private List<DroneCommand> commands;
    
    /**
     * Time at which this flightplan was executed, in UNIX.
     */
    private int executedAt;

    /**
     * Public constructor if we need to create new Flightplans internally in the RPi.
     * Might be used to return executed previously executed flightplans to the Backend.
     * @param cmds List of DroneCommand objects this flightplan contains
     * @param id ID of this FlightPlan
     * @param priority Priority for this flightplan (0 = low, 1 = middle, 2 = high)
     * @param authToken HTTP Auth token used to receive this flightplan
     * @param authorId User ID of this flightplans author
     * @param createdAt UNIX time of which this flightplan was created
     * @param executedAt UNIX time of which this flightplan was executed
     */
    public FlightPlan(List<DroneCommand> cmds, int id, int priority, String authToken, int authorId, int createdAt, int executedAt) {
        this.id = id;
        this.commands = cmds;
        this.authToken = authToken;
        this.priority = priority;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.executedAt = executedAt;
    }
    
    /**
     * Default Constructor for JSON
     */
    public FlightPlan(){
    }

    /**
     * Get the HTTP auth token which was given for this flightplan
     * @return 
     *              String representation of this flightplan's HTTP auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Get the user id for this flightplan's author
     * @return 
     *              User ID
     */
    public int getAuthorId() {
        return authorId;
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
    public int getCreatedAt() {
        return this.createdAt;
    }
    
    /**
     * Get UNIX time of which this flightplan was executed
     * @return 
     *              UNIX time of execution
     */
    public int getExecutedAt(){
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

}
