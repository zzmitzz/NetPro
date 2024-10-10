package src.server;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import src.client.data.dto.User;
import src.server.Constant;
import src.RequestWrapper;
import src.model.UserLogin;

public class ServerConnection {

    private ServerSocket myServer;
    private int serverPort = Constant.portServer;
    Socket clientSocket;
    Server svUsecase = new Server();
    
    public ServerConnection() {
        openServer(serverPort);
        while (true) {
            listenning();
        }

    }

    private void openServer(int portNumber) {
        try {
            myServer = new ServerSocket(portNumber);
            clientSocket = myServer.accept();
            System.out.println("Server: Server opened, waiting for connection  .... ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenning() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String json = reader.readLine();
            Gson gson = new Gson();
            RequestWrapper request = gson.fromJson(json, RequestWrapper.class);
            String body = request.getData();
            switch (request.getRoute()) {
                case "/doLogin":
                    try{
                        UserLogin userinfo = gson.fromJson(body, UserLogin.class);
                        User a = svUsecase.doLogin(userinfo.username, userinfo.password);
                    }catch(Exception e){
                        
                    }
                default:
            }
        } catch (Exception e) {
        }
    }
    
    
    public static void main(String[] args) {
        
    }
}
