package src.client.data;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import src.client.data.dto.User;
import src.server.Constant;


public class ClientConnection<T,R> {
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
    public R sendData(T object, Class<R> responseType, Class<T> requesstType){
        try {
            Gson gson = new Gson();
            long currentTime = System.currentTimeMillis();
            String jsonRequest = gson.toJson(jsonElement)
            
            
            
            while(System.currentTimeMillis() - currentTime < 5000){
                R result = gson.fromJson(object, responseType);
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }
    public void receivingData(){
        
    }
    public void closeConnection(){
        try{
            mySocket.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        ClientConnection cc = new ClientConnection();
        cc.openConnection();
        cc.sendData(new User("hi","hi","hi",1.0));
        cc.closeConnection();
    }
}
