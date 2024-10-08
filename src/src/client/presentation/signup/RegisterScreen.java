/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.presentation.signup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RegisterScreen {

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Đăng ký");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Create a panel for the form
        JPanel panel = new JPanel();
        panel.setLayout(null);  // Using null layout for manual positioning
        frame.add(panel);
        
        // Create and set title label
        JLabel titleLabel = new JLabel("Đăng ký", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(50, 20, 300, 30);
        panel.add(titleLabel);
        
        // Instruction label
        JLabel instructionLabel = new JLabel("Vui lòng nhập tên đăng nhập và mật khẩu:");
        instructionLabel.setBounds(50, 60, 300, 25);
        panel.add(instructionLabel);
        
        // Username label and text field
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setBounds(50, 100, 100, 25);
        panel.add(usernameLabel);
        
        JTextField usernameText = new JTextField(20);
        usernameText.setBounds(160, 100, 150, 25);
        panel.add(usernameText);
        
        // Add listener for username text field
        usernameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Handle when text is inserted
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Handle when text is removed
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Handle when text is changed
            }
        });

        // Password label and text field
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setBounds(50, 140, 100, 25);
        panel.add(passwordLabel);
        
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(160, 140, 150, 25);
        panel.add(passwordText);
        
        // Add listener for password text field
        passwordText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Handle when password is inserted
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Handle when password is removed
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Handle when password is changed
            }
        });
        
        // Register button
        JButton registerButton = new JButton("Đăng ký");
        registerButton.setBounds(150, 200, 100, 25);
        panel.add(registerButton);
        
        // Add listener for Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle register button click
            }
        });
        
        // Set the frame visibility
        frame.setVisible(true);
    }
}
