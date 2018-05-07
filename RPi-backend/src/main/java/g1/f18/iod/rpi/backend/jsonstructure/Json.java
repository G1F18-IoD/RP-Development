/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.jsonstructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Class dedicated to encoding json strings, possibly for decoding json strings aswell
 * @author chris
 */
public class Json {

    public static <T> T decode(String json, Class<T> type) {
        ObjectMapper decoder = new ObjectMapper();
        try {
            return decoder.readValue(json, type);
        } catch (IOException ex) {
            System.out.println("Error mapping json string to JsonFlightPlan.class");
            return null;
        }
    }
}
