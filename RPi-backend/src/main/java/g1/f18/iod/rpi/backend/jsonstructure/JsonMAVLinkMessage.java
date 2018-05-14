/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.jsonstructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Json data structure class for MAVLink messages
 * @author chris
 */
public class JsonMAVLinkMessage {

    @JsonProperty("cmd_id")
    private int cmdId;

    @JsonProperty("params")
    private List params;

    public int getCmdId() {
        return cmdId;
    }
    
    public List getParams(){
        return this.params;
    }
}
