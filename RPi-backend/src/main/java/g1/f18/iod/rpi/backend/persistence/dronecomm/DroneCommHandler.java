/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.persistence.dronecomm;

import g1.f18.iod.rpi.backend.datastructure.DroneStatus;
import g1.f18.iod.rpi.backend.services.IDroneCommService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 * Class for handling building and execution of python scripts.
 * Made as a Service such that Spring can perform Dependency injection with this.
 * @author chris
 */
@Service
public class DroneCommHandler implements IDroneCommService {
    
    /**
     * Subprocess running the pythonScript
     */
    private Process commandProcess;
    
    /**
     * Python script to be executed on the drone.
     */
    private String pythonScript = "";

    /**
     * Invoking this will create a python script which will perform drone arming.
     * @param parameters List of parameters required for arming the drone
     */
    @Override
    public void arm(List parameters) {
        // Python base script file
        this.initScript();
        // Append arm_and_takeoff.py
        String readLine;
        String scriptPath = "./PythonScrips/arm_and_takeoff.py";
        try (BufferedReader input = new BufferedReader(new FileReader(new File(scriptPath)))){
            while((readLine = input.readLine()) != null){
                if(readLine.contains("altitude")){
                    readLine = readLine.replace("altitude", parameters.get(0).toString());
                    System.out.println("Replaced altitude in file: arm_and_takeoff.py");
                }
                this.pythonScript += readLine + "\n";
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND: " + scriptPath);
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ERROR READING FILE: " + scriptPath);
            System.out.println(ex.getMessage());
        }
        this.writeScriptToFile("arm");
    }

    /**
     * Invoking this will create a python script which will perform drone disarming.
     * @param parameters List of parameters required for disarming the drone. Currently no parameters are used.
     */
    @Override
    public void disarm(List parameters) {
        // Python base script file
        this.initScript();
        // Append land_and_off.py
        String readLine, scriptPath = "./PythonScrips/land_and_off.py";
        try (BufferedReader input = new BufferedReader(new FileReader(new File(scriptPath)))){
            while((readLine = input.readLine()) != null){
                this.pythonScript += readLine + "\n";
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND: " + scriptPath);
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ERROR READING FILE: " + scriptPath);
            System.out.println(ex.getMessage());
        }
        this.writeScriptToFile("disarm");
    }

    /**
     * Invoking this method will get the drone actual status.
     * 
     * Note: The current implementation only returns dummy values, as a proof of concept for this functionality.
     * @return a DroneStatus object with all its fields filled.
     */
    @Override
    public DroneStatus getStatus() {
        Random rnd = new Random();
        return new DroneStatus("Altitide:5;", rnd.nextInt(), rnd.nextInt(100), (float)(Math.random() * (2 * Math.PI)), rnd.nextBoolean(), System.currentTimeMillis(), rnd.nextBoolean(), "IDLE", "GUIDED");
    }

    /**
     * Invoking this will create a python script which will perform yaw counter clockwise drone command.
     * @param parameters List of parameters required for performing yaw counter clockwise.
     */
    @Override
    public void yawCounterCw(List parameters) {
        // Python base script file
        this.initScript();
        // Append yaw_rotate.py
        String readLine, scriptPath = "./PythonScrips/yaw_rotate.py";
        try (BufferedReader input = new BufferedReader(new FileReader(new File(scriptPath)))){
            while((readLine = input.readLine()) != null){
                if(readLine.contains("degrees")){
                    readLine = readLine.replace("degrees", parameters.get(0).toString()); // Heading in degrees, This will be relative to the drones current heading
                }
                if(readLine.contains("clockwise")){
                    readLine = readLine.replace("clockwise", parameters.get(1).toString()); // -1 = CCW, 1 = CW
                }
                this.pythonScript += readLine + "\n";
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND: " + scriptPath);
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ERROR READING FILE: " + scriptPath);
            System.out.println(ex.getMessage());
        }
        this.writeScriptToFile("yawCounterCw");
    }

    /**
     * Invoking this will create a python script which will perform yaw clockwise drone command.
     * @param parameters List of parameters required for performing yaw clockwise
     */
    @Override
    public void yawCw(List parameters) {
        // Python base script file
        this.initScript();
        // Append yaw_rotate.py
        String readLine, scriptPath = "./PythonScrips/yaw_rotate.py";
        try (BufferedReader input = new BufferedReader(new FileReader(new File(scriptPath)))){
            while((readLine = input.readLine()) != null){
                if(readLine.contains("degrees")){
                    readLine = readLine.replace("degrees", parameters.get(0).toString()); // heading
                }
                if(readLine.contains("clockwise")){
                    readLine = readLine.replace("clockwise", parameters.get(1).toString()); // -1 = CCW, 1 = CW
                }
                this.pythonScript += readLine + "\n";
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND: " + scriptPath);
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ERROR READING FILE: " + scriptPath);
            System.out.println(ex.getMessage());
        }
        this.writeScriptToFile("yawCw");
    }
    
    /**
     * This will be invoked by all methods in the IDroneCommService interface to get the base pythonscript and append it to this.pythonScript.
     */
    private void initScript(){
        this.pythonScript = "";
        String readLine, scriptPath = "./PythonScrips/basescript.py";
        try (BufferedReader input = new BufferedReader(new FileReader(new File(scriptPath)))){
            while((readLine = input.readLine()) != null){
                this.pythonScript += readLine + "\n";
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND: " + scriptPath);
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ERROR READING FILE: " + scriptPath);
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Writes a new python script to file. Name is created based on currentTimeMillis and who invoked the method. 
     * @param caller This methods invoker
     */
    private void writeScriptToFile(String caller){
        String scriptName = System.currentTimeMillis() + "-" + caller + ".py";
        System.out.println("Writing python script to file: " + scriptName);
        File scriptFile = new File("./PythonScrips/auto-generated/", scriptName);
        try (FileWriter writer = new FileWriter(scriptFile)) {
            writer.write(this.pythonScript);
        } catch (IOException ex) {
            System.out.println("Error creating file: " + scriptFile);
            System.out.println(ex.getMessage());
        }
        this.executeCommand(scriptName);
    }
    
    /**
     * Execute the python script scriptName, and put the subprocess created into this.commandProcess. 
     * @param scriptName The absolute path, including file name of the python script to execute. 
     */
    private void executeCommand(String scriptName){
        System.out.println("Executing python script: " + scriptName);
        try {
            String[] cmd = { "python", scriptName };
            this.commandProcess = Runtime.getRuntime().exec(cmd);
            System.out.println("DroneCommHandler.java:commandProcess = " + this.commandProcess);
            this.readConsole();
        } catch (IOException ex) {
            System.out.println("Internal error upon executing python script on RPi.");
        }
    }
    
    /**
     * Reads the subprocess' console output, creates a String based on what it reads, and returns this String. 
     * @return A String object, with all the output from the subprocess, split by a \n .
     * @throws IOException If the subprocess streams are not found, or another I/O error occurs.
     */
    private String readConsole() throws IOException{
        String output, toReturn = "";
        // Get subprocess inputstream and create a reader based on that
        Reader inStreamReader = new InputStreamReader(this.commandProcess.getInputStream());
        BufferedReader in = new BufferedReader(inStreamReader);

        // read stuff
        while ((output = in.readLine()) != null) {
            System.out.println(output);
            toReturn += output + "\n";
        }
        in.close();
        return toReturn;
    }

    /**
     * Invoking this will execute a test python script.
     * This was used to check if Java can execute Python Scripts, and how it reacts to executing them.
     */
    @Override
    public void testCmd() {
        String scriptPath = "./PythonScrips/test.py";
        this.executeCommand(scriptPath);
    }
}
