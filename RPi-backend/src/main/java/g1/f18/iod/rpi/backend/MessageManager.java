/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import g1.f18.iod.rpi.backend.datastructure.DRONE_CMD;
import g1.f18.iod.rpi.backend.datastructure.DroneStatus;
import g1.f18.iod.rpi.backend.datastructure.FlightPlan;
import g1.f18.iod.rpi.backend.datastructure.Json;
import g1.f18.iod.rpi.backend.persistence.dronecomm.DroneCommHandler;
import g1.f18.iod.rpi.backend.services.IDatabaseService;
import g1.f18.iod.rpi.backend.services.IDroneCommService;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MessageManager has responsibility for creating a flightplan, in MAVLink messages based on the JSON string object coming from API. Also handles the execution of these messages through the
 * DroneCommHandler to the drone.
 *
 * @author chris
 */
@Service
public class MessageManager {

    /**
     * Database service
     */
    @Autowired
    private IDatabaseService databaseHandler;

    /**
     * Drone Communication service
     */
    @Autowired
    private IDroneCommService droneCommHandler;

    /**
     * Thread safe synchronized list containing Runnable objects of type MessageExecutor to be executed in their own Thread.
     */
    private List<MessageExecutor> runnableFlightPlans = Collections.synchronizedList(new LinkedList<>());

    /**
     * Thread object currently executing Drone commands, if this value is null, no current execution thread exists.
     */
    private Thread currentExecutionThread = null;

    /**
     * MessageExecutor Runnable object currently executing commands through the currentExecutionThread.
     */
    private MessageExecutor currentExecutionRunnable = null;

    /**
     * Method to create a test disarm flightplan, this will create a python script to disarm the drone.
     * @throws IOException 
     */
    public void testDisarm() throws IOException {
        System.out.println("Executing testDisarm()");
        String json = "{\n"
                + "	\"created_at\" : 0,\n"
                + "    \"priority\": 1,\n"
                + "	\"cmd_delay\": 60000,\n"
                + "    \"commands\": [\n"
                + "		{\n"
                + "            \"cmd_id\": 1,\n"
                + "            \"parameters\": []\n"
                + "        }"
                + "    ]\n"
                + "}";

        FlightPlan fp = Json.decode(json, FlightPlan.class);
        this.handleFlightPlan(fp);
        this.executeFlightPlan();
    }

    /**
     * Method to test the python script generator
     */
    public void testPython() {
        this.currentExecutionRunnable = null;
        this.currentExecutionThread = null;
        System.out.println("Executing testPython()");
        String json = "{\n"
                + "	\"created_at\" : 0,\n"
                + "    \"priority\": 0,\n"
                + "	\"cmd_delay\": 1,\n"
                + "    \"commands\": [\n"
                + "        {\n"
                + "            \"cmd_id\": -99,\n"
                + "            \"parameters\": []\n"
                + "        }"
                + "    ]\n"
                + "}";

        FlightPlan fp = Json.decode(json, FlightPlan.class);
        this.handleFlightPlan(fp);
        this.executeFlightPlan();
    }

    /**
     * Method to test the JSON decoder. To test reading output from a subprocess To test sort MessageExecutor objects after FlightPlan priority
     *
     * @throws java.io.IOException Execution of python script in Runtime environment
     */
    public void testArm() throws IOException {
        System.out.println("Executing testArm()");
        String json = "{\n"
                + "	\"created_at\" : 0,\n"
                + "    \"priority\": 0,\n"
                + "	\"cmd_delay\": 60000,\n"
                + "    \"commands\": [\n"
                + "        {\n"
                + "            \"cmd_id\": 0,\n"
                + "            \"parameters\": [1]\n"
                + "        }"
                + "    ]\n"
                + "}";

        FlightPlan fp = Json.decode(json, FlightPlan.class);
        this.handleFlightPlan(fp);
        this.executeFlightPlan();
    }

    /**
     * Invoking this method will perform a call to the databasehandler, requesting all FlightPlan objects which has not yet been executed by the drone
     */
    void getNonexecutedFlightplans() {
        for (FlightPlan fp : this.databaseHandler.getNonexecutedFlightPlans()) {
            this.runnableFlightPlans.add(new MessageExecutor(fp, new DroneCommHandler(), fp.getCmdDelay()));
        }
        this.sortRunnableFlightplans();
    }

    /**
     * Public method to get a map of all available drone commands. Key Value pair is: Integer = ID of CMD, String = Name of CMD.
     *
     * @return Map containing commands available for the drone to execute. Integer = ID, String = Name of command
     */
    public Map<Integer, String> getAvailableCommands() {
        return DRONE_CMD.getAvailableCommands();
    }

    /**
     * Public method to get flightplans stored inside the elements in this.runnableFlightPlans
     *
     * @return this.flightPlans
     */
    public List<FlightPlan> getFlightplans() {
        List<FlightPlan> toReturn = new LinkedList<>();
        for (MessageExecutor ms : this.runnableFlightPlans) {
            toReturn.add(ms.getFlightplan());
        }
        return toReturn;
    }

    /**
     * Public method to get flightplans stored in this.flightplans and the database
     *
     * @return Flightplan objects stored in this.flightPlans and those stored in the database.
     */
    public List<FlightPlan> getAllFlightplans() {
        List<FlightPlan> toReturn = new LinkedList<>();
        toReturn.addAll(this.getFlightplans());
        toReturn.addAll(this.databaseHandler.getFlightPlans());
        return toReturn;
    }

    /**
     * Method to get all flightlogs concerning a FlightPlan ID
     *
     * @param id ID of the flightplan which flightlogs should be returned
     * @return List of flightlogs based on id
     */
    public List<String> getFlightLogs(int id) {
        return this.databaseHandler.getFlightLogs(id);
    }

    /**
     * Method to get all flightlogs stored in database
     *
     * @return List of all flightlogs
     */
    public List<String> getFlightLogs() {
        return this.databaseHandler.getFlightLogs();
    }
    
    private void sortRunnableFlightplans(){
        // Sort the list of MessageExecutor objects based on their priority (Order goes from high (2) to low (0))
        Collections.sort(runnableFlightPlans, new Comparator<MessageExecutor>() {
            @Override
            public int compare(MessageExecutor t, MessageExecutor t1) {
                if (t.getPriority() == t1.getPriority()) {
                    return 0;
                }
                return t.getPriority() < t1.getPriority() ? 1 : -1;
            }
        });
        System.out.println("Created Runnable MessageExecutor and sorted the list of current MessageExecutors. Size of list now: " + this.runnableFlightPlans.size());
    }

    /**
     * Public method to be used by the CommandController. This method handles incoming flightplans. This involves storing them in the database and adding them to the list of available flightplans.
     * This method also checks if the currently running MessageExecutor object is finished executing or if its even running any MessageExecutor object
     * @param fp FlightPlan object to store in this.flightPlans and in the Database
     * @return True on succesful creation of a new MessageExecutor object
     */
    public boolean handleFlightPlan(FlightPlan fp) {
        if (this.currentExecutionRunnable != null || this.currentExecutionThread != null) {
            System.out.println("CurrentExecutionThread state is: " + this.currentExecutionThread.getState());
            System.out.println("Thread is alive? : " + this.currentExecutionThread.isAlive());
            if (this.currentExecutionThread.getState() == Thread.State.TERMINATED) {
                this.currentExecutionRunnable = null;
                this.currentExecutionThread = null;
            }
        }
        int fpid = this.databaseHandler.storeFlightPlan(fp);
        System.out.println("Stored flightplan in Database");
        fp.setId(fpid);
        boolean result = this.runnableFlightPlans.add(new MessageExecutor(fp, new DroneCommHandler(), fp.getCmdDelay()));
        this.sortRunnableFlightplans();
        return result;
    }

    /**
     * Public method to invoke flightplan execution. This method will take the first element in this.runnableFlightPlans and create a Thread backed by that Runnable.
     * That newly created Thread will then execute the DroneCommand objects found in the MessageExecutor flightplan object
     *
     * @return True on succesful Thread instantiation, false if there are no MessageExecutor objects available
     */
    public boolean executeFlightPlan() {
        System.out.println("Beginning flightplan execution");
        // If this.runnableFlightPlans.get(0) == null or the list is simply empty, we have no Runnable FlightPlan objects to execute, we then return from this method. 
        if (this.runnableFlightPlans.isEmpty()) {
            return false;
        }
        if (this.runnableFlightPlans.get(0) == null) { // Check if we even have a flightplan object to execute
            return false;
        }

        // If currentExecutionThread == null, there is no current Thread executing commands on drone
        if (this.currentExecutionThread == null) {
            System.out.println("Executing first flightplan in " + this.runnableFlightPlans);
            return this.beginFlightplanExecution();
        }

        // If the MessageExecutor Runnable found in this.runnableFlightPlans.get(0) has higher priority than the current executing MessageExecutor,
        if (this.runnableFlightPlans.get(0).getPriority() > this.currentExecutionRunnable.getPriority()) {
            // We then wish to terminate that MessageExecutor process
            this.currentExecutionRunnable.terminate();
            if (!this.currentExecutionRunnable.getFlightplan().getCommands().isEmpty()) {
                // Store whatever is remaining of commands as a new MessageExecutor based on what's left in the FlightPlan object
                this.handleFlightPlan(this.currentExecutionRunnable.getFlightplan());
            }
            try {
                // Join the Thread with our current one, releasing the resources
                this.currentExecutionThread.join();
            } catch (InterruptedException ex) {
            }
            this.currentExecutionThread = null;
            this.currentExecutionRunnable = null;
            return this.beginFlightplanExecution();
        }

        System.out.println("There is already a flightplan executing" + this.currentExecutionThread + " : " + this.currentExecutionRunnable);

        return false;
    }

    /**
     * Internal method to begin executing a MessageExecutor object's flightplan
     * This updates the database to set the current execution time for the first MessageExecutor object's flightplan object. 
     * @return True if the currentExecutionThread is not equal to null
     */
    private boolean beginFlightplanExecution() {
        System.out.println("Begin execution of " + this.runnableFlightPlans.get(0));
        this.databaseHandler.beginExecution(this.runnableFlightPlans.get(0).getFlightplan().getId());
        this.currentExecutionRunnable = this.runnableFlightPlans.remove(0);
        this.currentExecutionThread = new Thread(this.currentExecutionRunnable);
        this.currentExecutionThread.start();
        System.out.println("CurrentExecutionThread is set: " + this.currentExecutionThread);
        System.out.println("CurrentExecutionRunnable is set: " + this.currentExecutionRunnable);
        return this.currentExecutionThread != null;
    }

    /**
     * Public method to remove a FlightPlan from the list of available flightplans
     *
     * @param id ID of the flightplan to remove
     * @return Returns true if the FlightPlan with id is removed from the list. Returns false if either the flightplan with id doesnt exists in list or it cannot be removed for some reason.
     */
    public boolean removeFlightPlan(int id) {
        MessageExecutor toRemove = null;
        for (MessageExecutor me : this.runnableFlightPlans) {
            if (me.getFlightplan().getId() == id) {
                toRemove = me;
                break;
            }
        }
        if (toRemove == null) {
            return false;
        }
        return this.runnableFlightPlans.remove(toRemove);
    }

    /**
     * Public method to get drone status.
     *
     * @return DroneStatus object containing fields with the drones current parameters.
     */
    public DroneStatus getStatus() {
        return this.droneCommHandler.getStatus();
    }
}
