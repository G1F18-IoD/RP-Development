/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.datastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Static methods and fields since they should be accessible globally.
 * @author chris
 */
public class DRONE_CMD {
    public static final int ARM = 0;
    public static final int DISARM = 1;
    public static final int GET_STATUS = 2;
    public static final int THROTTLE = 3;
    public static final int YAW_COUNTER_CW = 4;
    public static final int YAW_CW = 5;
    
    /**
     * Public method to get a map of all available commands
     * @return Map of commands. Integer = Command ID, String = Command Name
     */
    public static Map<Integer, String> getAvailableCommands(){
        HashMap<Integer, String> cmds = new HashMap<>();
        cmds.put(ARM, "ARM");
        cmds.put(DISARM, "DISARM");
        cmds.put(GET_STATUS, "GET STATUS");
        cmds.put(THROTTLE, "THROTTLE");
        cmds.put(YAW_COUNTER_CW, "YAW COUNTER CLOCKWISE");
        cmds.put(YAW_CW, "YAW CLOCKWISE");
        return cmds;
    }
}
