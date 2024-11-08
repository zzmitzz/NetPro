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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Iterator;
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
        sendFakeRequest();
        ResponseWrapper response = new ResponseWrapper("", responseReturn, route);
        System.out.println(clientSocketXXX.isConnected() + "" + clientSocketXXX.getRemoteSocketAddress() + " <-- 200: OK : Send Message {" + responseReturn + "} from " + Thread.currentThread().getName());
        out.println(gson.toJson(response));
        out.flush();
    }

    /**
     * Fake the first request when creating new thread while the old thread still listening one.
     */
    private void sendFakeRequest(){
        ResponseWrapper response = new ResponseWrapper("", "", "");
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
                String status = "";
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
                            status = "true";
                            responseReturn = gson.toJson(returnUser);

                        }
                        else{
                            status = "false";
                            responseReturn  = "Your account has been logged in. ";
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Exception caused" + e);
                    }

                } else if (a.equals("/doLogout")) {
                    Map<String, Object> data = (Map<String, Object>) request.getData();

                    User user = new User(
                                data.get("fullName").toString(),
                                data.get("username").toString(),
                                data.get("password").toString(),
                                0.0
                        );

                    if (Server.listClientConnection.containsKey(user.getUsername())) {
                        Server.listClientConnection.remove(user.getUsername());
                        responseReturn = "successfully logout";
                    } else {
                        responseReturn = "error on logout";
                    }

                } else if (a.equals("/doRegister")) {
                    try {
                        Map<String, Object> data = (Map<String, Object>) request.getData();
                        User user = new User(
                                data.get("fullName").toString(),
                                data.get("username").toString(),
                                data.get("password").toString(),
                                0.0
                        );
                        
                        boolean isUserExist = false;
                        List<User> listUser = svUC.getListUser();
                        for (User eachUser : listUser) {
                            if (eachUser.getUsername().equals(user.getUsername())) {
                                isUserExist = true;
                                break;
                            }
                        }

                        if (!isUserExist) {
                            boolean isSuccessCreateAccount = svUC.onSigningUp(user.getFullName(), user.getUsername(), user.getPassword());
                            if (isSuccessCreateAccount) {
                                status = "true";
                                responseReturn = "Successfully Create Account";
                            } else {
                                status = "false";
                                responseReturn = "Server error";
                            }
                        } else {
                            responseReturn  = "username already exist";
                        }
                    }  catch (NumberFormatException e) {
                        System.out.println("Exception caused" + e.toString());
                    }

                } else if (a.equals("/getListUser")) {
                    try {
                        List<User> listUser = svUC.getListUser();
                        responseReturn = gson.toJson(listUser);
                    } catch (Exception e) {}

                } else if (a.equals("/invitePlay")) {
                    try {
                        Map<String, String> data = (Map<String, String>) request.getData();
                        String userNamePlayWith = data.get("opponent");
                        if (Server.listClientConnection.containsKey(userNamePlayWith) && !userNamePlayWith.equals(data.get("currUser"))) {
                            ClientHandler opponentClientHandler = Server.listClientConnection.get(userNamePlayWith);

                            JsonObject returnJson = new JsonObject();
                            returnJson.addProperty("invitedBy", data.get("currUser"));
                            returnJson.addProperty("message", "your are invited to join a game");
                            responseReturn = returnJson.toString();
                            opponentClientHandler.sendMessage(responseReturn, "/beInvited");
                            continue;
                        } else {
                            JsonObject returnJson = new JsonObject();
                            returnJson.addProperty("status", false);
                            returnJson.addProperty("message", "player is offline now");
                            responseReturn = returnJson.toString();

                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                } else if (a.equals("/respondToInvitation")) {
                    try {
                        Map<String, String> data = (Map<String, String>) request.getData();
                        boolean statusBoolean = Boolean.parseBoolean(data.get("status"));
                        String userNamePlayWith = data.get("opponent");

                        if (statusBoolean && Server.listClientConnection.containsKey(userNamePlayWith) && !userNamePlayWith.equals(data.get("currUser"))) {
                            Executors.newFixedThreadPool(4).submit(new GamePlayHandler(this, Server.listClientConnection.get(userNamePlayWith)));
                            continue;

                        } else if (!statusBoolean) {
                            ClientHandler opponentClientHandler = Server.listClientConnection.get(data.get("opponent"));
                            JsonObject returnJson = new JsonObject();
                            returnJson.addProperty("status", String.valueOf(false));
                            returnJson.addProperty("message", "your invitation has been declined");
                            responseReturn = returnJson.toString();
                            opponentClientHandler.sendMessage(responseReturn, "/respondToInvitation");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                } else if (a.equals("/findGame")) {
                    Map<String, String> data = (Map<String, String>) request.getData();
                    String username = data.get("username");

                    if (Server.listFindMatchUser.size() > 0) {
                        List<String> keys = new ArrayList<>(Server.listFindMatchUser.keySet());
                        Random random = new Random();
                        String randomKey = keys.get(random.nextInt(keys.size()));
                        ClientHandler curr = Server.listClientConnection.get(username);
                        ClientHandler opponent = Server.listFindMatchUser.remove(randomKey);

                        JsonObject returnJson = new JsonObject();
                        returnJson.addProperty("message", "game found");
                        responseReturn = returnJson.toString();

                        curr.sendMessage(responseReturn, "/beFoundGame");
                        opponent.sendMessage(responseReturn, "/beFoundGame");
                        continue;

                    } else {
                        Server.listFindMatchUser.put(username, Server.listClientConnection.get(username));
                        JsonObject returnJson = new JsonObject();
                        returnJson.addProperty("message", "currently find match");
                        responseReturn = returnJson.toString();
                    }

                } else if (a.equals("/cancelFindGame")) {
                    Map<String, String> data = (Map<String, String>) request.getData();
                    String username = data.get("username");
                    String message = data.get("message");
                    Server.listFindMatchUser.remove(username);

                    JsonObject returnJson = new JsonObject();
                    returnJson.addProperty("message", "successfully cancel find match");
                    responseReturn = returnJson.toString();

                } else if (a.equals("/respondOnFoundGame")) {
                    Map<String, String> data = (Map<String, String>) request.getData();
                    String username = data.get("username");
                    boolean accept = Boolean.parseBoolean(data.get("accept"));

                    Server.pairOfUsers.put(username, accept);

                    synchronized (Server.pairOfUsers) {
                        if (Server.pairOfUsers.size() == 2) {
                            boolean allAccepted = true;
                            for (boolean accepted : Server.pairOfUsers.values()) {
                                if (!accepted) {
                                    allAccepted = false;
                                    break;
                                }
                            }

                            if (allAccepted) {
                                Iterator<String> iterator = Server.pairOfUsers.keySet().iterator();
                                String player1Username = iterator.next();
                                String player2Username = iterator.next();
                                
                                ClientHandler player1 = Server.listClientConnection.get(player1Username);
                                ClientHandler player2 = Server.listClientConnection.get(player2Username);
                                
                                Server.pairOfUsers.clear();

                                Executors.newFixedThreadPool(4).submit(new GamePlayHandler(player1, player2));
                                continue;

                            } else {
                                JsonObject returnJson = new JsonObject();
                                returnJson.addProperty("message", "game has been cancelled");
                                responseReturn = returnJson.toString();
                                for (String playerUsername : Server.pairOfUsers.keySet()) {
                                    Server.listClientConnection.get(playerUsername).sendMessage(responseReturn, "/respondOnFoundGame");
                                }
                                Server.pairOfUsers.clear();
                                continue;
                            }
                        }
                    }
                } else if(a.equals("/postAnswer")){
                    Map<String, String> data = (Map<String, String>) request.getData();
                    String idQuestion = data.get("id");
                    String userAns = data.get("answer");
                    long timeStamp = Long.parseLong(data.get("timeStamp"));
                    String type = data.get("type");
                    gameController.receiveAnswer(this, new Answer(idQuestion,userAns,timeStamp,type));
                } else if(a.equals("/postScore")){
                    Map<String, Double> data = (Map<String, Double>) request.getData();
                    double point = data.get("score");
                    gameController.receiveScore(this,point);
                }
                sendFakeRequest();
                if(responseReturn.isEmpty()) continue;
                ResponseWrapper response = new ResponseWrapper(status, responseReturn, a);
                System.out.println(clientSocketXXX.isConnected() + "" + clientSocketXXX.getRemoteSocketAddress() + " <-- 200: OK | Send Message: {" + responseReturn + "} from " + Thread.currentThread().getName());
                out.println(gson.toJson(response));
                out.flush();
            } catch (JsonSyntaxException e) {
                System.out.println("Fail" + e);
            }
        }
    }

}
