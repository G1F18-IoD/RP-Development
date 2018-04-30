/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g1.f18.iod.rpi.backend.api;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.InetAddress;
import org.json.JSONObject;

/**
 *
 * @author chris
 */
public class PostService {

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
                .url("")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.body() == null) {
            throw new IOException("Error[" + response.code() + "]: NullPointerException on response.body()");
        }
        if (response.code() == 200) {
            return response.code() + ": " + response.body().string();
        }
        throw new IOException("Error[" + response.code() + "]: " + response.body().string());
    }
}
