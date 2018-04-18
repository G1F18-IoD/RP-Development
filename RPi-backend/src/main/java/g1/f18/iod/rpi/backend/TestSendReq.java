/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend;

import com.squareup.okhttp.*;
import java.io.IOException;
import java.net.InetAddress;
import org.json.*;
/**
 *
 * @author chris
 */
public class TestSendReq {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
//    public static void main(String[] args) throws IOException{
//        System.out.println(doPostRequest());
//    }
    
    public static <T> String doPostRequest() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String jsonString = new JSONObject()
                .put("class", "iod_ipconfig")
                .put("method", "setIp")
                .put("ip", ip)
                .toString();
        RequestBody body = RequestBody.create(JSON, jsonString);
        Request request = new Request.Builder()
                .url("http://skjoldtoft.dk/daniel/hab/index.php")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if(response.body() == null) {
            throw new IOException("Error[" + response.code() + "]: NullPointerException on response.body()");
        }
        if(response.code() == 200) {
            return response.code() + ": " + response.body().string();
        }
        throw new IOException("Error[" + response.code() + "]: " + response.body().string());
}
}
