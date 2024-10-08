package src.client.data;

import java.io.ObjectOutputStream;
import java.net.Socket;
import src.client.data.dto.User;
import src.server.Constant;


public class ClientConnection {
    private Socket mySocket;
    private String serverHost = Constant.privateServerNetworkIP;
    private int serverPort = Constant.portServer;
    public Socket openConnection(){
        try {
            mySocket = new Socket(serverHost, serverPort); 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return mySocket;
    }
    public boolean sendData(User user){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
            oos.writeObject(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public static void main(String[] args) {
        ClientConnection cc = new ClientConnection();
        cc.openConnection();
    }
}
