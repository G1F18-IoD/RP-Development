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
    /**
     * CMD_ID to perform ARM command. This command requires 1 field in its parameter array.
     */
    public static final int ARM = 0;
    
    /**
     * CMD_ID to perform DISARM command. This command requires 0 fields in its parameter array.
     */
    public static final int DISARM = 1;
    
    /**
     * CMD_ID to perform YAW_COUNTER_CW command. This command is unused at the moment since the drone doesn't fully support this operation.
     */
    public static final int YAW_COUNTER_CW = 2;
    
    /**
     * CMD_ID to perform YAW_COUNTER_CW command. This command is unused at the moment since the drone doesn't fully support this operation.
     */
    public static final int YAW_CW = 3;
    
    /**
     * Public method to get a map of all available commands
     * @return Map of commands. Integer = Command ID, String = Command Name
     */
    public static Map<Integer, String> getAvailableCommands(){
        HashMap<Integer, String> cmds = new HashMap<>();
        cmds.put(ARM, "ARM");
        cmds.put(DISARM, "DISARM");
        cmds.put(YAW_COUNTER_CW, "YAW COUNTER CLOCKWISE");
        cmds.put(YAW_CW, "YAW CLOCKWISE");
        return cmds;
    }
}
