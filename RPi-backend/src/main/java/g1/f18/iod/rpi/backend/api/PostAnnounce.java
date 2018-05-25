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
import g1.f18.iod.rpi.backend.services.IAuthenticationService;
import java.io.IOException;
import org.json.JSONObject;

/**
 * Class to perform POST request for the announcement that this RPi is online.
 *
 * @author chris
 */
public class PostAnnounce {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String REQUEST_PREFIX = "http://";
    // Server Backend settings
//    private final String BACKEND_IP = "192.168.0.19";
//    private final String BACKEND_URL = "/api/rpiconnection/offer";
//    private final int BACKEND_PORT = 5021;
    // SkjoldToft backend settings
    private final String BACKEND_IP = "skjoldtoft.dk";
    private final String BACKEND_URL = "/daniel/g1e17/config_switch.php";
    private final int BACKEND_PORT = 5021;

    private IAuthenticationService auth;

    public PostAnnounce(IAuthenticationService auth) {
        this.auth = auth;
    }

    /**
     * Public method to announce the presence of this RPi. This will perform POST request to BACKEND_IP at BACKEND_URL, with a RequestBody containing the AuthToken generated for this session, and the
     * IP for this RPi.
     *
     * @return the ResponseBody string response from BACKEND_IP + BACKEND_URL
     * @throws IOException If ResponseBody is NULL, or the request receives timeout/other errors.
     */
    public String announcePresence() throws IOException {
        OkHttpClient client = new OkHttpClient();
        // Get AuthToken from AuthenticationService
        String authToken = this.auth.getAuthToken();

        // Get local IP address
        String correctIp = "192.168.0.18";
        // Generate Json String based on AuthToken and ip
        String json = this.generateJson(authToken, correctIp);

        // Generate RequestBody for HTTP Request
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(REQUEST_PREFIX + BACKEND_IP/* + ":" + BACKEND_PORT*/ + BACKEND_URL)
                .post(body)
                .build();

        // Execute HTTP Request
        Response response = client.newCall(request).execute();

        // Null check response
        if (response.body() == null) {
            throw new IOException("Error[" + response.code() + "]: NullPointerException on response.body()");
        }
        // OK Response, we gucci
        if (response.code() == 200) {
            return response.code() + ": " + response.body().string();
        }
        // Timeout and other errors
        throw new IOException("Error[" + response.code() + "]: " + response.body().string());
    }

    /**
     * Private method to generate Json String object.
     *
     * @param authToken Auth Token from AuthenticationService
     * @param ip IP of this local RPi
     * @return Json Stringified object
     */
    private String generateJson(String authToken, String ip) {
        return new JSONObject()
                .put("ip", ip)
                .put("port", 8080)
                .put("password", authToken)
                .toString();
    }
}
