//package src.client.presentation.login;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.Document;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Hyperlink;
//import src.client.common.LoadingDialog;
//import src.client.data.dto.User;
//import src.client.presentation.home_screen.HomeScreen;
//import src.client.presentation.signup.RegisterScreen;
//
//
//@Deprecated
//public class LoginScreen implements LoginScreenController.onActionResponse {
//
//
//
//    private String username = "";
//    private String password = "";
//    private LoginScreenController controller;
//    private JFrame frame;
//    LoadingDialog loadingDialog;
//
//
//
//    private LoginScreenController getController() throws IOException{
//        if(controller == null){
////            controller = new LoginScreenController(this);
//            return controller;
//        }
//        return controller;
//    }
//
//    public LoginScreen() {
//        // Create the frame
//        frame = new JFrame("Trò chơi đoán ô chữ");
//        frame.addWindowListener(new WindowListener() {
//            @Override
//            public void windowOpened(WindowEvent e) {}
//
//            @Override
//            public void windowClosing(WindowEvent e) {
//                System.out.println("Window is closing");
//                // Optional: Show confirmation dialog
//                int confirmed = JOptionPane.showConfirmDialog(null,
//                        "Are you sure you want to exit?", "Exit Confirmation",
//                        JOptionPane.YES_NO_OPTION);
//
//                if (confirmed == JOptionPane.YES_OPTION) {
//                    frame.dispose();
//                    System.exit(0);  // Exits the application
//                }
//
//            }
//
//            @Override
//            public void windowClosed(WindowEvent e) {}
//            @Override
//            public void windowIconified(WindowEvent e) {}
//            @Override
//            public void windowDeiconified(WindowEvent e) {}
//            @Override
//            public void windowActivated(WindowEvent e) {}
//            @Override
//            public void windowDeactivated(WindowEvent e) {}
//        });
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 300);
//        loadingDialog = new LoadingDialog(frame);
//        // Create a panel for the form
//        JPanel panel = new JPanel();
//        panel.setLayout(null);  // Using null layout for manual positioning
//        frame.add(panel);
//
//        // Create and set title label
//        JLabel titleLabel = new JLabel("Trò chơi đoán ô chữ", SwingConstants.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setBounds(50, 20, 300, 30);
//        panel.add(titleLabel);
//
//        // Instruction label
//        JLabel instructionLabel = new JLabel("Vui lòng nhập tên đăng nhập và mật khẩu:");
//        instructionLabel.setBounds(50, 60, 300, 25);
//        panel.add(instructionLabel);
//
//        // Username label and text field
//        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
//        usernameLabel.setBounds(50, 100, 100, 25);
//        panel.add(usernameLabel);
//
//        JTextField usernameText = new JTextField(20);
//        usernameText.setBounds(160, 100, 150, 25);
//        panel.add(usernameText);
//
//        // Add listener for username text field
//        usernameText.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                username = usernameText.getText();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                username = usernameText.getText();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                username = usernameText.getText();
//            }
//        });
//
//        // Password label and text field
//        JLabel passwordLabel = new JLabel("Mật khẩu:");
//        passwordLabel.setBounds(50, 140, 100, 25);
//        panel.add(passwordLabel);
//
//        JPasswordField passwordText = new JPasswordField(20);
//        passwordText.setBounds(160, 140, 150, 25);
//        panel.add(passwordText);
//
//        // Add listener for password text field
//        passwordText.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                password = new String(passwordText.getPassword());
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                password = new String(passwordText.getPassword());
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                password = new String(passwordText.getPassword());
//            }
//        });
//
//        // Register and Login buttons
//        JButton registerButton = new JButton("Đăng ký");
//        registerButton.setBounds(100, 200, 80, 25);
//        panel.add(registerButton);
//
//        JButton loginButton = new JButton("Đăng nhập");
//        loginButton.setBounds(200, 200, 100, 25);
//        panel.add(loginButton);
//
//        // Add listener for Register button
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose();
//                try {
//                    new RegisterScreen();
//                    if(controller != null){
//                        controller.onCloseLiveUpdate(getController().getClass().getName());
//                        controller.callbackAction = null;
//                        controller = null;
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
//                    System.out.println(ex);
//                }
//            }
//        });
//
//        // Add listener for Login button
//        loginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    getController().onLogin(username, password);
//                } catch (IOException ex) {
//                    Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        // Set the frame visibility
//        frame.setVisible(true);
//    }
//
//    // JAVA SWING
//    @Override
//    public void loginCallback(User user) {
//        frame.dispose();
//        try {
//            new HomeScreen(user);
//            getController().onCloseLiveUpdate(getController().getClass().getName());
//            getController().callbackAction = null;
//            controller = null;
//        } catch (IOException ex) {
//            Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println(ex);
//        }
//    }
//
//    @Override
//    public void loginCallback(String status) {
//        if (status.equals("account is already logged in")) {
//            JOptionPane.showMessageDialog(frame, "Tài khoản của bạn đang online trên một thiết bị khác.\nVui lòng đăng xuất.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public void registerCallback() {
//
//    }
//
//
//
//}
