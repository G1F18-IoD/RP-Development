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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
     * Method to test the JSON decoder. To test reading output from a subprocess To test sort MessageExecutor objects after FlightPlan priority
     *
     * @param args
     * @throws java.io.IOException Execution of python script in Runtime environment
     */
    public static void main(String[] args) throws IOException {
        String json = "{\n"
                + "    \"auth_token\": \"Randomly generated token, created by RPi initially and used by BE to handshake requests sent to RPi.\",\n"
                + "    \"author_id\": 1,\n"
                + "	\"created_at\" : 0,\n"
                + "    \"priority\": 0,\n"
                + "	\"cmd_delay\": 1,\n"
                + "    \"commands\": [\n"
                + "        {\n"
                + "            \"cmd_id\": 0,\n"
                + "            \"parameters\": [1]\n"
                + "        },\n"
                + "		{\n"
                + "            \"cmd_id\": 1,\n"
                + "            \"parameters\": []\n"
                + "        }\n"
                + "    ]\n"
                + "}";

        FlightPlan fp = Json.decode(json, FlightPlan.class);
        MessageManager.getInstance().handleFlightPlan(fp);
        MessageManager.getInstance().executeFlightPlan();
//        System.out.println(fp.getAuthToken());
//        // Get command ID
//        System.out.println(fp.getCommands().get(0).getCmdId());
//        // Get PARAMS list for each command
//        for (DroneCommand cmd : fp.getCommands()) {
//            System.out.println(cmd.getParams());
//        }
//        // Initialize subprocess
//        String line;
//        Process getStatusPros = Runtime.getRuntime().exec(("python C:\\Users\\chris\\Documents\\G1F18-IoD\\RP-development\\PyhtonScrips\\test.py"));
//
//        // Get subprocess inputstream and create a reader based on that
//        Reader inStreamReader = new InputStreamReader(getStatusPros.getInputStream());
//        BufferedReader in = new BufferedReader(inStreamReader);
//
//        // read stuff
//        System.out.println("Stream started");
//        while ((line = in.readLine()) != null) {
//            System.out.println(line);
//        }
//        in.close();
//        System.out.println("Stream Closed");
//
//        System.out.println(Json.encode(DRONE_CMD.getAvailableCommands()));
//        // Priority sort test
//        MessageManager inst = MessageManager.getInstance();
//
//        FlightPlan fp1 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 0, "1", 0, 0, 0, 0);
//        FlightPlan fp2 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 0, "2", 0, 0, 0, 0);
//        FlightPlan fp3 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 1, "3", 0, 0, 0, 0);
//        FlightPlan fp4 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 1, "4", 0, 0, 0, 0);
//        FlightPlan fp5 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 2, "5", 0, 0, 0, 0);
//        FlightPlan fp6 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 2, "6", 0, 0, 0, 0);
//
//        inst.runnableFlightPlans.add(new MessageExecutor(fp1, new DroneCommHandler(), 2500));
//        inst.runnableFlightPlans.add(new MessageExecutor(fp2, new DroneCommHandler(), 2500));
//        inst.runnableFlightPlans.add(new MessageExecutor(fp3, new DroneCommHandler(), 2500));
//        inst.runnableFlightPlans.add(new MessageExecutor(fp4, new DroneCommHandler(), 2500));
//        inst.runnableFlightPlans.add(new MessageExecutor(fp5, new DroneCommHandler(), 2500));
//        inst.runnableFlightPlans.add(new MessageExecutor(fp6, new DroneCommHandler(), 2500));
//
//        FlightPlan fp7 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 0, "7", 0, 0, 0, 0);
//        inst.handleFlightPlan(fp7);
//        System.out.println("print 77777777777777777777777777777777777777777777");
//        for (MessageExecutor me : inst.runnableFlightPlans) {
//            System.out.print(me.getFlightplan().getAuthToken());
//        }
//        System.out.println("");
//        System.out.println("");
//
//        FlightPlan fp8 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 1, "8", 0, 0, 0, 0);
//        inst.handleFlightPlan(fp8);
//        System.out.println("print 888888888888888888888888888888888888888");
//        for (MessageExecutor me : inst.runnableFlightPlans) {
//            System.out.print(me.getFlightplan().getAuthToken());
//        }
//        System.out.println("");
//        System.out.println("");
//
//        FlightPlan fp9 = new FlightPlan(new LinkedList<DroneCommand>(), 0, 2, "9", 0, 0, 0, 0);
//        inst.handleFlightPlan(fp9);
//        System.out.println("print 999999999999999999999999999999999999999999");
//        for (MessageExecutor me : inst.runnableFlightPlans) {
//            System.out.print(me.getFlightplan().getAuthToken());
//        }
        // Expected output is: 569348127
        // New elements always placed at the back of their priority level queue.
    }

    /**
     * Invoking this method will perform a call to the databasehandler, requesting all FlightPlan objects which has not yet been executed by the drone
     */
    void getNonexecutedFlightplans() {
        for (FlightPlan fp : this.databaseHandler.getNonexecutedFlightPlans()) {
            this.handleFlightPlan(fp);
        }
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

    /**
     * Public method to be used by the CommandController. This method handles incoming flightplans. This involves storing them in the database and adding them to the list of available flightplans.
     *
     * @param fp FlightPlan object to store in this.flightPlans and in the Database
     * @return True on successful storage in database, false otherwise. Note: This might also return false (and fail) if the JSON is not decoded correctly
     */
    public boolean handleFlightPlan(FlightPlan fp) {
        this.runnableFlightPlans.add(new MessageExecutor(fp, new DroneCommHandler(), fp.getCmdDelay()));
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
        // Store FlightPlan in database
        return /*this.databaseHandler.storeFlightPlan(fp)*/ true;
    }

    /**
     * Public method to invoke flightplan execution. This method will take the first element in this.runnableFlightPlans and create a Thread backed by that Runnable. That newly created Thread will
     * then execute the DroneCommand objects found in the MessageExecutor flightplan object
     *
     * @return True on succesful Thread instantiation, false if there are no MessageExecutor objects available
     */
    public boolean executeFlightPlan() {
        // If this.runnableFlightPlans.get(0) == null or the list is simply empty, we have no Runnable FlightPlan objects to execute, we then return from this method. 
        if (this.runnableFlightPlans.get(0) == null || this.runnableFlightPlans.isEmpty()) { // Check if we even have a flightplan object to execute
            return false;
        }

        // If currentExecutionThread == null, there is no current Thread executing commands on drone
        if (this.currentExecutionThread == null) {
            return this.beginFlightplanExecution();
        }

        // If the MessageExecutor Runnable found in this.runnableFlightPlans.get(0) has higher priority than the current executing MessageExecutor,
        if (this.runnableFlightPlans.get(0).getPriority() > this.currentExecutionRunnable.getPriority()) {
            // We then wish to terminate that MessageExecutor process
            this.currentExecutionRunnable.terminate();
            // Store whatever is remaining of commands as a new MessageExecutor based on what's left in the FlightPlan object
            this.handleFlightPlan(this.currentExecutionRunnable.getFlightplan());
            try {
                // Join the Thread with our current one, releasing the resources
                this.currentExecutionThread.join();
            } catch (InterruptedException ex) {
            }
            this.currentExecutionThread = null;
            this.beginFlightplanExecution();
        }

        return this.currentExecutionThread != null;
    }

    private boolean beginFlightplanExecution() {
        this.currentExecutionThread = new Thread(this.runnableFlightPlans.remove(0));
        this.currentExecutionThread.start();
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
     * Waiting for implementation of flightplan execution interrupt.!!!! EDIT: Now only waiting for implementation.
     *
     * @return DroneStatus object containing fields with the drones current parameters.
     */
    public DroneStatus getStatus() {
        return null;
    }
}
