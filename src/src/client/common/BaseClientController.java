/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.common;

import src.client.data.ClientConnection;

public class BaseClientController {
    private ClientConnection clientConnection = null;
    
    public BaseClientController(){
        clientConnection = getClientConnectionInstance();
    }
    private ClientConnection getClientConnectionInstance(){
        if(clientConnection == null){
            return new ClientConnection();
        }
        else return clientConnection;
    }
//    void doRequest(String url, String data, Response response){
//        clientConnection
//    }
}
