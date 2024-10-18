package src.client.data;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.RequestWrapper;
import src.client.common.Response;
import src.client.data.dto.User;
import src.server.Constant;

public class ClientConnection {

    private Socket mySocket;
    private final String serverHost = Constant.privateServerNetworkIP;
    private final int serverPort = Constant.portServer;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public Socket openConnection() {
        try {
            mySocket = new Socket(serverHost, serverPort);
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            out = new PrintWriter(mySocket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println(ex + getClass().toString());
            return null;
        }
        return mySocket;
    }

    public <T, R> void sendData(T object, String route) {
        if (in == null || out == null) {
            return;
        }
        try {
            Gson gson = new Gson();
            RequestWrapper<T> requestWrapper = new RequestWrapper("", object, route);
            String jsonRequest = gson.toJson(requestWrapper);
            out.println(jsonRequest);
            out.flush();
        } catch (Exception ex) {
            return;
        }
        return;
    }

    // Send object and return json string
    public <T> void doPostJsonRequest(T object, String route, Response callback) throws IOException {
        sendData(object, route);
        long currentTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTime <= 2000) {
            String dataRes = in.readLine();
            if (dataRes != null && !dataRes.isEmpty()) {
                callback.onSuccess(dataRes);
                return;
            }
        }
        callback.onFailure();
    }

    public void closeConnection() {
        try {
            mySocket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ClientConnection cc = new ClientConnection();
        cc.openConnection();
        try {
            cc.doPostJsonRequest(
                    new User("hi", "alicej", "password789", 1.0),
                    "/doLogin",
                    new Response() {
                        @Override
                        public void onSuccess(String data) {
                            System.out.println("Success: " + data);
                        }
                        
                        @Override
                        public void onFailure() {
                            System.out.println("Failure");
                        }
                    }
            );
        } catch (IOException ex) {
            System.out.println("Damn error");
        }
    }
}
