package src.client.presentation.signup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import src.client.data.dto.User;
import src.client.presentation.home_screen.HomeScreen;
import src.client.presentation.login.LoginScreen;
import src.client.presentation.login.LoginScreenController;
import src.client.presentation.signup.RegisterScreenController;
import src.client.common.LoadingDialog;

public class RegisterScreen implements RegisterScreenController.onActionResponse {
    private String fullname = "";
    private String username = "";
    private String password = "";
    private JFrame frame;
    private RegisterScreenController controller;
    LoadingDialog loadingDialog;

    private RegisterScreenController getController() throws IOException {
        if (controller == null) {
            controller = new RegisterScreenController(this);
            return controller;
        }
        return controller;
    }

    private String getTextFromDocumentEvent(DocumentEvent e) {
        Document doc = e.getDocument();
        int offset = e.getOffset();
        int length = e.getLength();
        try {
            // Get the text from the document at the specified offset and length
            return doc.getText(offset, length);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            return ""; // Return empty string if something goes wrong
        }
    }

    public RegisterScreen() {
        
        // Create the frame
        frame = new JFrame("Đăng ký");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        loadingDialog = new LoadingDialog(frame);
        
        // Create a panel for the form
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create and set title label
        JLabel titleLabel = new JLabel("Đăng ký", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Instruction label
        JLabel instructionLabel = new JLabel("Vui lòng nhập tên đầy đủ, tên đăng nhập và mật khẩu:");
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(instructionLabel, gbc);
        
        // Fullname label and text field
        JLabel fullnameLabel = new JLabel("Tên đầy đủ:");
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(fullnameLabel, gbc);

        JTextField fullnameText = new JTextField(20);
        gbc.gridx = 1;
        panel.add(fullnameText, gbc);

        // Add listener for fullname text field
        fullnameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fullname = fullnameText.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fullname = fullnameText.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fullname = fullnameText.getText();
            }
        });

        // Username label and text field
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(usernameLabel, gbc);
        
        JTextField usernameText = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameText, gbc);
        
        // Add listener for username text field
        usernameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            username = usernameText.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            username = usernameText.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            username = usernameText.getText();
            }
        });

        // Password label and text field
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(passwordLabel, gbc);
        
        JPasswordField passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordText, gbc);
        
        // Add listener for password text field
        passwordText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                password = new String(passwordText.getPassword());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                password = new String(passwordText.getPassword());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                password = new String(passwordText.getPassword());
            }
        });
        
        // Register button
        JButton registerButton = new JButton("Đăng ký");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);
        
        // Add listener for Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    getController().onRegister(fullname, username, password);
                } catch (IOException ex) {
                    Logger.getLogger(RegisterScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // Set the frame visibility
        frame.setVisible(true);
    }

    @Override
    public void registerCallback(String status) {
        if (status.equals("successfully create account")) {
            JOptionPane.showMessageDialog(frame, "Tạo tài khoản thành công", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            try {
                new LoginScreen();
                getController().onCloseLiveUpdate(getController().getClass().getName());
                getController().callbackAction = null;
                controller = null;
            } catch (IOException ex) {
                Logger.getLogger(RegisterScreen.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Username đã tồn tại, vui lòng thử lại.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
