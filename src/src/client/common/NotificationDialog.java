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
import java.util.Timer;
import java.util.TimerTask;

public class NotificationDialog extends JFrame {
    public NotificationDialog(String message, int durationMillis) {
        // Set up JFrame properties
        setTitle("Notification");
        setSize(600, 200);
        setUndecorated(true); // Removes title bar and borders
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set background color and layout
        getContentPane().setBackground(new Color(60, 63, 65));
        setLayout(new BorderLayout());

        // Create message label
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(messageLabel, BorderLayout.CENTER);

        // Set opacity if supported
        setOpacity(0.9f);

        // Auto-close the notification after a certain duration
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dispose(); // Close the window
            }
        }, durationMillis);

        // Show the dialog
        setVisible(true);
    }

}
