/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.RequestWrapper;
import src.client.data.dto.User;
import src.server.usecase.onActionServer;

/**
 *
 * @author 1
 */
public class ClientHandler extends Throwable implements Runnable {

    private final onActionServer svUC;
    private final BufferedReader reader;
    private final PrintWriter out;

    public ClientHandler(Socket clientSocket, onActionServer svUC) throws IOException {
        this.svUC = svUC;
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        String json;
        while (true) {
            try {
                try {
                    json = reader.readLine();
                    System.out.println("Receive {" + json + "} from " + Thread.currentThread().getName());
                } catch (IOException ex) {
                    json = "";
                }
                Gson gson = new Gson();
                RequestWrapper request = gson.fromJson(json, RequestWrapper.class);
                if(request == null) continue;
                String responseReturn = "";
                switch (request.getRoute()) {
                    case "/doLogin":
                        try {
                            Map<String, Object> data = (Map<String, Object>) request.getData();
                            User user = new User(
                                    "",
                                    data.get("username").toString(),
                                    data.get("password").toString(),
                                    0.0
                            );
                            User test = svUC.onLogin(user.getUsername(), user.getPassword());
                            responseReturn = test.getFullName();
                            
                        } catch (NumberFormatException e) {
                            System.out.println("Exception caused" + e.toString());
                        }
                    case "/message":
                        try {

                        } catch (Exception e) {
                        }
                try {
                        User user = (User) request.getData();
                        System.out.println(user.getFullName());
                    } catch (Exception e) {

                    }
                }
                out.println(responseReturn);
                System.out.println("Return {" + responseReturn + "} from " + Thread.currentThread().getName());
                out.flush();
            } catch (JsonSyntaxException e) {
                System.out.println("Fail" + e);
            }
        }
    }

}
