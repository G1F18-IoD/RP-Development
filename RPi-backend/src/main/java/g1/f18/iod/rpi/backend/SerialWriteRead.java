/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.DatatypeConverter;

/**
 * 
 * @author chris
 */
public class SerialWriteRead implements Runnable {

    static CommPortIdentifier portId;
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    static OutputStream outputStream;
    static boolean outputBufferEmptyFlag = false;

    public static void main(String[] args) {
        boolean portFound = false;
        String defaultPort;

        // determine the name of the serial port on several operating systems  
        String osname = System.getProperty("os.name", "windows").toLowerCase();
        if (osname.startsWith("windows")) {
            // windows  
            defaultPort = "COM6";
        } else if (osname.startsWith("linux")) {
            // linux  
            defaultPort = "/dev/ttyS0";
        } else if (osname.startsWith("mac")) {
            // mac  
            defaultPort = "????";
        } else {
            System.out.println("Sorry, your operating system is not supported");
            return;
        }

        if (args.length > 0) {
            defaultPort = args[0];
        }

        System.out.println("Set default port to " + defaultPort);

        // parse ports and if the default port is found, initialized the reader  
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(defaultPort)) {
                    System.out.println("Found port: " + defaultPort);
                    portFound = true;
                    // init reader thread  
                  //  WriteReadSerialPort reader = new WriteReadSerialPort();
                }
            }

        }
        if (!portFound) {
            System.out.println("port " + defaultPort + " not found.");
        }

    }

    public void initwritetoport() {
        // initwritetoport() assumes that the port has already been opened and  
        //    initialized by "public WriteReadSerialPort()"  

        try {
            // get the outputstream  
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
        }

        try {
            // activate the OUTPUT_BUFFER_EMPTY notifier  
            serialPort.notifyOnOutputEmpty(true);
        } catch (Exception e) {
            System.out.println("Error setting event notification");
            System.out.println(e.toString());
            System.exit(-1);
        }

    }

    public void writetoport() throws InterruptedException {
//        byte[] messageStringByte;
//
//        try {
//            // write string to serial port  
//            for (int i = 1; i <= 8; i++) {
//                messageString = smartPlugin(i);
//                // messageStringByte = toByteArray(smartPlugin(i));
//                outputStream.write(toByteArray(smartPlugin(i)));
//                System.out.println("Writing \"" + messageString + "\" to " + serialPort.getName());
//            }
//            TimeUnit.SECONDS.sleep(3);
//            outputStream.write(toByteArray(messageString = smartPlugin(6)));
//            System.out.println("Writing \"" + messageString + "\" to " + serialPort.getName());
//            TimeUnit.SECONDS.sleep(3);
//            outputStream.write(toByteArray(messageString = smartPlugin(7)));
//            System.out.println("Writing \"" + messageString + "\" to " + serialPort.getName());
//
//        } catch (IOException e) {
//        }
    }

    public String smartPlugin(int i) {
        String[] sq = new String[20];
//messages to pair, bind and send ON, OFF commands to smart Plug. 
        sq[1] = "020600135DE000AE03";  //pair
        sq[2] = "020700145DE00000A903"; //pair
        sq[3] = "0206640B5DE000B603";  //bind
        sq[4] = "0206650B5DE000B603";  //bind
        sq[5] = "022E010300C028EB00035508001D00BC150000000021000004162C5508001D00BC150021060003D000002100BC15002003";
        sq[6] = "021B020300C015D600035508001D00BC15000401210600FF0403012D0103";  //ON
        sq[7] = "021B030300C015D600035508001D00BC15000401210600FF0403012E0003";  //OFF
        sq[8] = "050300C028EB00035508001D00BC150000000021000004162B5508001D00BC150021020703D000002100BC150020"; // metering command
        sq[8] = "060300C017D400035508001D00BC15000401210207FF0405002C000004";//read attribute record

        //       System.out.println(sq[i]);
        return (sq[i]);

    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public SerialWriteRead() {

        // initalize serial port  
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {
        }

        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
        }
        
        ReadSerialEvents rse = new ReadSerialEvents();
        rse.setInputStream(inputStream);

        try {
            serialPort.addEventListener(rse);
        } catch (TooManyListenersException e) {
        }

        // activate the DATA_AVAILABLE notifier  
        serialPort.notifyOnDataAvailable(true);

        try {
            // set port parameters  (Baud rate, data bits, stop bits, parity)
            serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
        }

        // start the read thread  
        readThread = new Thread(this);
        readThread.start();

    }

    public void run() {
        // first thing in the thread, we initialize the write operation  
        initwritetoport();
        try {
            writetoport();
            while (true) {
                // write string to port, the serialEvent will read it  

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        }
    }
}
