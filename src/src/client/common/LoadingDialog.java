/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.common;

/**
 *
 * @author 1
 */
import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog{
    
    
    /**
        loadingDialog.setVisible(true);
        loadingDialog.dispose();
     */
    public LoadingDialog(JFrame parent) {
        // Set modal to block input to other windows while loading
        super(parent, "Loading", true);
        
        // Set the size of the dialog
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setUndecorated(true); // Remove window decorations like title bar

        // Create the panel to hold the content
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30)); // Dark background
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a loading label
        JLabel label = new JLabel("Loading, please wait...");
        label.setForeground(Color.WHITE); // Set text color to white
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        // Create a progress bar as a loading spinner
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Spinner mode
        progressBar.setBackground(new Color(30, 30, 30));
        progressBar.setForeground(new Color(70, 130, 180)); // Custom color

        // Center the progress bar horizontally
        progressBar.setAlignmentX(CENTER_ALIGNMENT);

        // Add padding and layout to the panel
        panel.add(Box.createVerticalStrut(20)); // Padding at the top
        panel.add(label);
        panel.add(Box.createVerticalStrut(15)); // Space between text and progress bar
        panel.add(progressBar);
        panel.add(Box.createVerticalStrut(20)); // Padding at the bottom

        // Add the panel to the dialog
        add(panel);

        // Make the dialog non-resizable and center it
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    //Usage
    
}
