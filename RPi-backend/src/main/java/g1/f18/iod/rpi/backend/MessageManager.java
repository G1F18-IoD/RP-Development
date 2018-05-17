/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import g1.f18.iod.rpi.backend.datastructure.DRONE_CMD;
import g1.f18.iod.rpi.backend.datastructure.DroneCommand;
import g1.f18.iod.rpi.backend.datastructure.DroneStatus;
import g1.f18.iod.rpi.backend.datastructure.FlightPlan;
import g1.f18.iod.rpi.backend.datastructure.Json;
import g1.f18.iod.rpi.backend.persistence.database.DatabaseHandler;
import g1.f18.iod.rpi.backend.persistence.dronecomm.DroneCommHandler;
import g1.f18.iod.rpi.backend.services.IDatabaseService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * MessageManager has responsibility for creating a flightplan, in MAVLink messages based on the JSON string object coming from API. Also handles the execution of these messages through the
 * DroneCommHandler to the drone.
 *
 * Made Singleton, so we do not have multiple instances of MessageManager including a single point of entry to the drone communications.
 *
 * @author chris
 */
public class MessageManager {

    private static MessageManager instance;

    /**
     * Singleton design pattern
     *
     * @return The one and only instance of this
     */
    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager(new DatabaseHandler());
        }
        return instance;
    }

    /**
     * private constructor, used to initialize database handler
     */
    private MessageManager(DatabaseHandler dbHandler) {
        this.databaseHandler = dbHandler;
    }

    /**
     * Database service
     */
    private final IDatabaseService databaseHandler;

    /**
     * Queue of FlightPlan received through the API. If the length of this queue is 0, no FlightPlan objects has been received yet.
     */
    private LinkedList<FlightPlan> flightPlans = new LinkedList<>();

    /**
     * Thread object currently executing Drone commands, if this value is null, no current execution thread exists.
     */
    private Thread currentExecutionThread = null;

    /**
     * Method to test the JSON decoder. To test reading output from a subprocess
     *
     * @param args
     * @throws java.io.IOException Execution of python script in Runtime environment
     */
    public static void main(String[] args) throws IOException {
        String json = "{\n"
                + "    \"auth_token\": \"Randomly generated token, created by RPi initially and used by BE to handshake requests sent to RPi.\",\n"
                + "    \"author_id\": 1,\n"
                + "    \"created_at\" : 0,\n"
                + "    \"priority\": 0,\n"
                + "    \"commands\": [\n"
                + "        {\n"
                + "            \"cmd_id\": 0,\n"
                + "            \"params\": [0,0,0,0,0,0,0]\n"
                + "        },\n"
                + "		{\n"
                + "            \"cmd_id\": 1,\n"
                + "            \"params\": [1,2,0,0,1,0,2]\n"
                + "        }\n"
                + "    ]\n"
                + "}";

        FlightPlan fp = Json.decode(json, FlightPlan.class);
        System.out.println(fp.getAuthToken());
        // Get command ID
        System.out.println(fp.getCommands().get(0).getCmdId());
        // Get PARAMS list for each command
        for (DroneCommand cmd : fp.getCommands()) {
            System.out.println(cmd.getParams());
        }
        // Initialize subprocess
        String line;
        Process getStatusPros = Runtime.getRuntime().exec(("python C:\\Users\\chris\\Documents\\G1F18-IoD\\RP-development\\PyhtonScrips\\test.py"));

        // Get subprocess inputstream and create a reader based on that
        Reader inStreamReader = new InputStreamReader(getStatusPros.getInputStream());
        BufferedReader in = new BufferedReader(inStreamReader);

        // read stuff
        System.out.println("Stream started");
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
        System.out.println("Stream Closed");
        
        System.out.println(Json.encode(DRONE_CMD.getAvailableCommands()));
    }

    
    /**
     * Public method to get a map of all available drone commands.
     * Key Value pair is: Integer = ID of CMD, String = Name of CMD.
     * @return Map containing commands available for the drone to execute. Integer = ID, String = Name of command
     */
    public Map<Integer, String> getAvailableCommands(){
        return DRONE_CMD.getAvailableCommands();
    }
    
    /**
     * Public method to get flightplans stored in this.flightplans
     * @return this.flightPlans
     */
    public List<FlightPlan> getFlightplans(){
        return this.flightPlans;
    }
    
    /**
     * Public method to get flightplans stored in this.flightplans and the database
     * @return Flightplan objects stored in this.flightPlans and those stored in the database.
     */
    public List<FlightPlan> getAllFlightplans(){
        List<FlightPlan> toReturn = new LinkedList<>();
        toReturn.addAll(this.flightPlans);
        toReturn.addAll(this.databaseHandler.getFlightPlans());
        return toReturn;
    }
    
    /**
     * Method to get all flightlogs concerning a FlightPlan ID
     * @param id ID of the flightplan which flightlogs should be returned
     * @return List of flightlogs based on id
     */
    public List<String> getFlightLogs(int id){
        return this.databaseHandler.getFlightLogs(id);
    }
    
    /**
     * Method to get all flightlogs stored in database
     * @return List of all flightlogs
     */
    public List<String> getFlightLogs(){
        return this.databaseHandler.getFlightLogs();
    }
    
    /**
     * Public method to be used by the CommandController. This method handles incoming flightplans. This involves storing them in the database and adding them to the list of available flightplans.
     *
     * @param json JSON String of the flightplan
     * @return True on successful storage in database, false otherwise. Note: This might also return false (and fail) if the JSON is not decoded correctly
     */
    public boolean handleFlightPlan(String json) {
        FlightPlan fp = Json.decode(json, FlightPlan.class);
        this.flightPlans.addLast(fp);
        return this.databaseHandler.storeFlightPlan(fp);
    }

    /**
     * Public metho to invoke flightplan execution.
     * This method will take the first element in this.flightPlans, pass it over to a new MessageExecutor, which will execute drone commands in its own thread.
     * @return True on succesful Thread instantiation. 
     */
    public boolean executeFlightPlan() {
        if (this.flightPlans.getFirst() != null) { // Check if we even have a flightplan object to execute
            this.currentExecutionThread = new Thread(new MessageExecutor(this.flightPlans.removeFirst(), new DroneCommHandler(), 2500));
        }
        return this.currentExecutionThread != null;
    }
    
    /**
     * Public method to remove a FlightPlan from the list of available flightplans
     * @param id ID of the flightplan to remove
     * @return Returns true if the FlightPlan with id is removed from the list. Returns false if either the flightplan with id doesnt exists in list or it cannot be removed for some reason.
     */
    public boolean removeFlightPlan(int id){
        FlightPlan toRemove = null;
        for(FlightPlan fp : this.flightPlans){
            if(fp.getId() == id){
                toRemove = fp;
                break;
            }
        }
        if(toRemove == null){
            return false;
        }
        return this.flightPlans.remove(toRemove);
    }
    
    /**
     * Public method to get drone status. 
     * 
     * Waiting for implementation of flightplan execution interrupt.!!!!
     * @return DroneStatus object containing fields with the drones current parameters.
     */
    public DroneStatus getStatus(){
        return null;
    }
}
