/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.jsonstructure;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Json data structure class for MAVLink messages
 * @author chris
 */
public class JsonMAVLinkMessage {

    @JsonProperty("cmd_id")
    private int cmd_id;

    @JsonProperty("param_1")
    private int param_1;

    @JsonProperty("param_2")
    private int param_2;

    @JsonProperty("param_3")
    private int param_3;

    @JsonProperty("param_4")
    private int param_4;

    @JsonProperty("param_5")
    private int param_5;

    @JsonProperty("param_6")
    private int param_6;

    @JsonProperty("param_7")
    private int param_7;

    public int getCmd_id() {
        return cmd_id;
    }

    public int getParam_1() {
        return param_1;
    }

    public int getParam_2() {
        return param_2;
    }

    public int getParam_3() {
        return param_3;
    }

    public int getParam_4() {
        return param_4;
    }

    public int getParam_5() {
        return param_5;
    }

    public int getParam_6() {
        return param_6;
    }

    public int getParam_7() {
        return param_7;
    }
}
