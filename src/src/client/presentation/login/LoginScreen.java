
package src.client.presentation.login;
// CHAT GPT
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import src.client.presentation.home_screen.HomeScreen;

public class LoginScreen {
    private static String username = "";
    private static String password = "";
    
    private static String getTextFromDocumentEvent(DocumentEvent e) {
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
    
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Trò chơi đoán ô chữ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Create a panel for the form
        JPanel panel = new JPanel();
        panel.setLayout(null);  // Using null layout for manual positioning
        frame.add(panel);
        
        // Create and set title label
        JLabel titleLabel = new JLabel("Trò chơi đoán ô chữ", SwingConstants.CENTER);
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
               username = getTextFromDocumentEvent(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                username = getTextFromDocumentEvent(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                username = getTextFromDocumentEvent(e);
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
                password = getTextFromDocumentEvent(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                password = getTextFromDocumentEvent(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                password = getTextFromDocumentEvent(e);
            }
        });
        
        // Register and Login buttons
        JButton registerButton = new JButton("Đăng ký");
        registerButton.setBounds(100, 200, 80, 25);
        panel.add(registerButton);
        
        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(200, 200, 100, 25);
        panel.add(loginButton);
        
        // Add listener for Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                HomeScreen a = new HomeScreen();
            }
        });
        
        // Add listener for Login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login button click
            }
        });
        
        // Set the frame visibility
        frame.setVisible(true);
    }
}
