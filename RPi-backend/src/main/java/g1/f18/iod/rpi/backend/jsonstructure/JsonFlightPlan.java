/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.jsonstructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Json data structure class for Flight plans
 * @author chris
 */
public class JsonFlightPlan {
    @JsonProperty("auth_token")
    private String auth_token;
    
    @JsonProperty("user_id")
    private int user_id;
    
    @JsonProperty("priority")
    private int priority;
    
    @JsonProperty("messages")
    private List<JsonMAVLinkMessage> messages;

    public String getAuth_token() {
        return auth_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getPriority() {
        return priority;
    }

    public List<JsonMAVLinkMessage> getMessages() {
        return messages;
    }
    
    
}
