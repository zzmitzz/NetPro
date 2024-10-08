package src.server.database_connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import src.client.data.dto.User;
import src.server.Constant;

public class ServerConnection {
    private ServerSocket myServer;
    private int serverPort = Constant.portServer;
    
    public ServerConnection(){
        openServer(serverPort);
        while(true){
            listenning();
        }
    }
    private void openServer(int portNumber){
        try {
            myServer = new ServerSocket(portNumber);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void listenning(){
        try {
            Socket clientSocket = myServer.accept();
            ObjectInputStream ois = new
            ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream oos = new
            ObjectOutputStream(clientSocket.getOutputStream());
            Object o = ois.readObject();
            if(o instanceof User){
                User user = (User)o;
                if(true){
                    oos.writeObject("ok");
                }
                else oos.writeObject("false");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }   
    }
    public static void main(String[] args) {
        ServerConnection sv = new ServerConnection();
    }
}
