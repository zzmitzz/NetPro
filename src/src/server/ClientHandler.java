/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import src.RequestWrapper;
import src.ResponseWrapper;
import src.client.data.ClientConnection;
import src.client.data.dto.User;
import src.model.Answer;
import src.server.usecase.GamePlayHandler;
import src.server.usecase.onActionServer;

/**
 *
 * @author 1
 */
public class ClientHandler extends Throwable implements Runnable {

    public final onActionServer svUC;
    public final BufferedReader reader;
    public final PrintWriter out;
    public Socket clientSocketXXX;
    public Gson gson = new Gson();
    public User user = null;
    public GamePlayHandler gameController;

    public ClientHandler(Socket clientSocket, onActionServer svUC) throws IOException {
        this.svUC = svUC;
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientSocketXXX = clientSocket;
    }
    public void setGameController(GamePlayHandler gameCtr){
        gameController = gameCtr;
    }
    public void sendMessage(String responseReturn, String route){
        ResponseWrapper response = new ResponseWrapper("", responseReturn, route);
        System.out.println(clientSocketXXX.isConnected()+"" + clientSocketXXX.getRemoteSocketAddress() + "<-- 200: OK : Send Message {" + responseReturn + "} from " + Thread.currentThread().getName());
        out.println(gson.toJson(response));
        out.flush();
    }
    @Override
    public void run() {
        String json;
        while (true) {
            try {
                try {
                    json = reader.readLine();
                    if(json == null) continue;
                    System.out.println("--> 200: Receive " + json + " from " + Thread.currentThread().getName());
                } catch (IOException ex) {
                    json = "";
                }
                RequestWrapper request = gson.fromJson(json, RequestWrapper.class);
                if (request == null) {
                    continue;
                }
                String responseReturn = "";
                String a = request.getRoute();
                if (a.equals("/doLogin")) {
                    try {
                        Map<String, Object> data = (Map<String, Object>) request.getData();
                        User user = new User(
                                "",
                                data.get("username").toString(),
                                data.get("password").toString(),
                                0.0
                        );
                        if (!Server.listClientConnection.containsKey(user.getUsername())) {
                            User returnUser = svUC.onLogin(user.getUsername(), user.getPassword());
                            Server.listClientConnection.put(returnUser.getUsername(), this);
                            this.user = returnUser;
                            responseReturn = gson.toJson(returnUser);
                        }
                        else{
                            responseReturn  = "null";
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Exception caused" + e.toString());
                    }
                } else if (a.equals("/getListUser")) {
                    try {
                        List<User> listUser = svUC.getListUser();
                        responseReturn = gson.toJson(listUser);
                    } catch (Exception e) {
                    }
                } else if (a.equals("/playGameUser")) {
                    try {
                        Map<String, String> data = (Map<String, String>) request.getData();
                        String userNamePlayWith = data.get("opponent");
                        if (Server.listClientConnection.containsKey(userNamePlayWith) && !userNamePlayWith.equals(data.get("currUser"))) {
                            Executors.newFixedThreadPool(4).submit(new GamePlayHandler(this,Server.listClientConnection.get(userNamePlayWith) ));
                            continue;
                        } else {
                            JsonObject returnJson = new JsonObject();
                            returnJson.addProperty("status", false);
                            returnJson.addProperty("message", "Player now is offline");
                            responseReturn = returnJson.toString();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else if(a.equals("/postAnswer")){
                    Map<String, String> data = (Map<String, String>) request.getData();
                    String idQuestion = data.get("id");
                    String userAns = data.get("answer");
                    Long timeStamp = Long.parseLong(data.get("timeStamp"));
                    gameController.receiveAnswer(this, new Answer(idQuestion,userAns,timeStamp));
                }
                if(responseReturn.isEmpty()) continue;
                ResponseWrapper response = new ResponseWrapper("", responseReturn, a);
                System.out.println(clientSocketXXX.isConnected()+"" + clientSocketXXX.getRemoteSocketAddress() + "<-- 200: OK : Send Message {" + responseReturn + "} from " + Thread.currentThread().getName());
                out.println(gson.toJson(response));
                out.flush();
            } catch (JsonSyntaxException e) {
                System.out.println("Fail" + e);
            }
        }
    }

}