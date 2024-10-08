/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.presentation.home_screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomeScreen extends JFrame {

    public HomeScreen() {
        setTitle("Chọn người chơi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Create table
        String[] columnNames = {"Danh sách người chơi", "Điểm"};
        Object[][] data = {
            {"User 1", ""},
            {"User 2", ""},
            {"User 3", ""},
            {"User 4", ""},
            {"User 5", ""},
            {"User 6", ""}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton inviteButton = new JButton("Gửi lời mời");
        JButton randomButton = new JButton("Chơi random");
        JButton instructionButton = new JButton("Hướng dẫn");
        JButton logoutButton = new JButton("Đăng xuất");

        buttonPanel.add(inviteButton);
        buttonPanel.add(randomButton);
        buttonPanel.add(instructionButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.EAST);

        // Set background color
        getContentPane().setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeScreen());
    }

}