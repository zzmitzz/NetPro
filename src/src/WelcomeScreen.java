/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen {
    static interface ScreenCallBack{
        void onServerClick();
        void onClientClick();
    }
    public void initScreen(ScreenCallBack a) {
        // Create the frame
        JFrame frame = new JFrame("Device Type Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 120);

        // Create a panel with vertical BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        // Create and add the label
        JLabel label = new JLabel("Which type of device?");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the label
        panel.add(label);

        // Create a sub-panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));  // Center the buttons, with spacing
        
        // Create the "Server" button
        JButton serverButton = new JButton("Server");
        buttonPanel.add(serverButton);

        // Add listener for the "Server" button
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle server selection
                System.out.println("Server selected");
                a.onServerClick();
            }
        });

        // Create the "Client" button
        JButton clientButton = new JButton("Client");
        buttonPanel.add(clientButton);

        // Add listener for the "Client" button
        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle client selection
                System.out.println("Client selected");
                a.onClientClick();
            }
        });

        // Add the button panel to the main panel
        panel.add(buttonPanel);

        // Set the frame visibility
        frame.setVisible(true);
    }
}
