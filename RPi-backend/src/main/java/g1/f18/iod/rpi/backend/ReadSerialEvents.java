/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author chris
 */
public class ReadSerialEvents implements SerialPortEventListener {
    private InputStream inputStream;
    
    @Override
    public void serialEvent(SerialPortEvent event) {
        System.out.println(".............." + event.getEventType());
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
                System.out.println("BI");
            case SerialPortEvent.OE:
                System.out.println("OE");
            case SerialPortEvent.FE:
                System.out.println("FE");
            case SerialPortEvent.PE:
                System.out.println("PE");
            case SerialPortEvent.CD:
                System.out.println("CD");
            case SerialPortEvent.CTS:
                System.out.println("CTS");
            case SerialPortEvent.DSR:
                System.out.println("DSR");
            case SerialPortEvent.RI:
                System.out.println("RI");
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                System.out.println("OUTPUT_BUFFER_EMPTY");
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                // we get here if data has been received  
                byte[] readBuffer = new byte[35];
                try {
                    // read data  
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                    }
                    // print data  
                    //   String result  = new String(readBuffer);  
                    //   System.out.println("Read: "+result.toString());  
                    String receivedData = DatatypeConverter.printHexBinary(readBuffer);
                    System.out.println("Read: " + receivedData);

                } catch (IOException e) {
                }

                break;
        }
    }
    
    public void setInputStream(InputStream serialIn){
        this.inputStream = serialIn;
    }

}
