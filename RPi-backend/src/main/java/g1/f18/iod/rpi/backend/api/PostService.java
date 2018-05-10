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

/**
 * Class to perform post requests.
 * @author chris
 */
public class PostService {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String REQUEST_PREFIX = "http://";
    private static final String BACKEND_IP = "192.168.0.19";
    private static final int BACKEND_PORT = 5070;

    public static <T> String doPostRequest(String json, String BACKEND_URL) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(REQUEST_PREFIX + BACKEND_IP + ":" +BACKEND_PORT + BACKEND_URL)
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
