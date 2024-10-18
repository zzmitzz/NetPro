package src.server;

import java.io.IOException;
import java.net.ServerSocket;
import src.server.usecase.ServerUsecase;
import src.server.usecase.onActionServer;

public class ServerConnection {
    private ServerSocket myServer;
    private final onActionServer serverUC = new ServerUsecase();
    private final int serverPort = Constant.portServer;
    
    public ServerConnection() {
        openServer(serverPort);
        listenning();
    }

    private void openServer(int portNumber) {
        try {
            myServer = new ServerSocket(portNumber);
            System.out.println("Server: Server opened, waiting for connection  .... ");
        } catch (IOException e) {
            System.out.println(e + getClass().toString());
        }
    }
    private void listenning() {
        try {
            new Thread(new ClientHandler(myServer.accept(), serverUC)).start();
        } catch (IOException e) {
            System.out.println("Can not connect to client");
        }
    }
    
    
    public static void main(String[] args) {
        new ServerConnection();
    }
}
