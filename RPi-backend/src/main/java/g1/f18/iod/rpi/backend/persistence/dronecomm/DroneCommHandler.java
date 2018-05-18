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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for handling serial communication
 * @author chris
 */
public class DroneCommHandler implements IDroneCommService {
    
    /**
     * Subprocess running the pythonScript
     */
    private Process commandProcess;
    
    /**
     * Python script to be executed on the drone.
     */
    private String pythonScript = "";
    
    private String filePrefix = "/home/pi/RPi-backend";
    
    /**
     * 
     */
    public DroneCommHandler(){
    }

    @Override
    public void arm(List parameters) {
        // Python base script file
        this.initScript();
        // Append arm_and_takeoff.py
        String readLine, scriptPath = filePrefix + "/PyhtonScrips/arm_and_takeoff.py";
        try (BufferedReader input = new BufferedReader(new FileReader(new File(scriptPath)))){
            while((readLine = input.readLine()) != null){
                if(readLine.contains("altitude")){
                    readLine = readLine.replace("altitude", parameters.get(0).toString());
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

    @Override
    public void disarm(List parameters) {
        // Python base script file
        this.initScript();
        // Append land_and_off.py
        String readLine, scriptPath = filePrefix + "/PyhtonScrips/land_and_off.py";
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

    @Override
    public DroneStatus getStatus(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void yawCounterCw(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void yawCw(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void throttle(List parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void initScript(){
        this.pythonScript = "";
        String readLine, scriptPath = filePrefix + "/PyhtonScrips/basescript.py";
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
    
    private void writeScriptToFile(String caller){
        String scriptName = System.currentTimeMillis() + "-" + caller + ".py";
        File scriptFile = new File(filePrefix + "/PyhtonScrips/auto-generated/", scriptName);
        try (FileWriter writer = new FileWriter(scriptFile)) {
            writer.write(this.pythonScript);
        } catch (IOException ex) {
            System.out.println("Error creating file: " + scriptFile);
            System.out.println(ex.getMessage());
        }
        this.executeCommand(scriptName);
        try {
            System.out.println(this.readConsole());
        } catch (IOException ex) {
            Logger.getLogger(DroneCommHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void executeCommand(String scriptName){
        try {
            this.commandProcess = Runtime.getRuntime().exec("python " + scriptName);
        } catch (IOException ex) {
            System.out.println("Internal error upon executing python script on RPi.");
        }
    }
    
    private String readConsole() throws IOException{
        String output, toReturn = "";
        // Get subprocess inputstream and create a reader based on that
        Reader inStreamReader = new InputStreamReader(this.commandProcess.getInputStream());
        BufferedReader in = new BufferedReader(inStreamReader);

        // read stuff
        while ((output = in.readLine()) != null) {
            toReturn += output;
        }
        in.close();
        return toReturn;
    }
}
