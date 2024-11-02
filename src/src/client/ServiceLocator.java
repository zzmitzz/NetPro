/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client;

import src.client.data.ClientConnection;
import src.client.data.dto.User;

/**
 *
 * @author 1
 */
public class ServiceLocator {
    public static ClientConnection apiClientConnection;
    public static User user;
    public static ClientConnection initAPI(){
        if(apiClientConnection == null) {
            apiClientConnection = new ClientConnection();
            return apiClientConnection;
        }
        return apiClientConnection;
    }
}
