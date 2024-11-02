package src.client.presentation.home_screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import src.client.common.LoadingDialog;
import src.client.common.NotificationDialog;
import src.client.data.dto.User;
import src.client.presentation.login.LoginScreen;
import src.client.presentation.play_game.CrosswordGameScreen;

public class HomeScreen extends JFrame implements HomeScreenController.onActionResponseHomeScreen {

    private LoadingDialog loadingDialog = new LoadingDialog(this);

    private HomeScreenController controller;
    private Object[][] data;
    private DefaultTableModel model;
    private User user;
    
    public HomeScreen(User user) throws IOException {
        this.controller = new HomeScreenController(this);
        setTitle("Chọn người chơi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        this.user = user;
        // Create a panel for the username
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel userLabel = new JLabel("User: " + user.getFullName());
        userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(userLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        controller.getUserList();

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window is closing");
                // Optional: Show confirmation dialog
                int confirmed = JOptionPane.showConfirmDialog(null, 
                        "Are you sure you want to exit?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);  // Exits the application
                }
                controller.onCloseLiveUpdate(this.getClass().getName());
            }

            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        // Create styled buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton inviteButton = createFancyButton("Gửi lời mời");
        inviteButton.addActionListener((ActionEvent e) -> {
            controller.invitePlay("usernameSelected", user.getUsername());
        });
        JButton randomButton = createFancyButton("Chơi random");
        JButton instructionButton = createFancyButton("Hướng dẫn");
        JButton logoutButton = createFancyButton("Đăng xuất");

        buttonPanel.add(inviteButton);
        buttonPanel.add(randomButton);
        buttonPanel.add(instructionButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.EAST);

        // Set background color
        getContentPane().setBackground(new Color(0xf0f0f0));
        buttonPanel.setBackground(new Color(0xf0f0f0));
        setVisible(true);
    }

    private JButton createFancyButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Time New Romances", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0x4CAF50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x388E3C)),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }

    @Override
    public void onListUserRes(java.util.List<User> listUser) {
        
        Collections.sort(listUser, new Comparator<User>() {
           
            @Override
            public int compare(User o1, User o2) {
                if(o1.score >= o2.score){
                    return -1;
                }else{
                    return 1;
                }
            }
            
        });
        data = new Object[listUser.size()][3];
        
        for (int i = 0; i < listUser.size(); i++) {
            User user = listUser.get(i);
            data[i][0] = user.getUsername(); // Assuming User has getUsername() method
            data[i][1] = user.getFullName(); // Assuming User has getPassword() method
            data[i][2] = user.getScore(); // Assuming User has getFullname() method
        }
        // Create table
        String[] columnNames = {"Danh sách người chơi", "Tên ", "Điểm"};
        model= new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        table.setBackground(new Color(0xe6e6e6));
        table.setFillsViewportHeight(true);
        revalidate();
        repaint();
    }

    @Override
    public void onPlayGameState(boolean status) {
        if(status){
            dispose();
            try{
                new CrosswordGameScreen(user);
                controller.onCloseLiveUpdate(controller.getClass().getName());
                controller.callbackAction = null;
                controller = null;
            }catch (Exception e){

            }
        }else{
            NotificationDialog dialog = new NotificationDialog("Người chơi hiện không Online, chọn người khác đi ba.", 3000);
            dialog.show();
        }
    }
}
