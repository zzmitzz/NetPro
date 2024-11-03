/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client;

import src.client.data.ClientConnection;
import src.client.presentation.login.LoginScreen;


public class ClientApplication {
    public static void main(String[] args) {
        ServiceLocator.initAPI();
        new LoginScreen();
    }
}
