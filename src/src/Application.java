/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import src.server.ServerConnection;

import javax.swing.*;

/**
 *
 * @author 1
 */
public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeScreen::new);
    }
}
