package src.client.presentation.play_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrosswordGameScreen {

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Trò chơi đoán ô chữ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        
        // Create a panel for the form
        JPanel panel = new JPanel();
        panel.setLayout(null);  // Using null layout for manual positioning
        frame.add(panel);

        // Create the grid for the crossword puzzle
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(10, 10));  // 10x10 grid
        gridPanel.setBounds(50, 20, 500, 250);  // Position and size
        
        // Add 100 JLabels representing the grid cells
        for (int i = 0; i < 100; i++) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setBackground(Color.GREEN);  // Set the background color to green
            gridPanel.add(label);
        }
        panel.add(gridPanel);

        // Round and timer labels
        JLabel roundLabel = new JLabel("Vòng 1:");
        roundLabel.setBounds(50, 280, 60, 25);
        panel.add(roundLabel);
        
        JLabel timerLabel = new JLabel("00:30");
        timerLabel.setBounds(120, 280, 60, 25);
        panel.add(timerLabel);
        
        // Question label
        JLabel questionLabel = new JLabel("Câu hỏi:");
        questionLabel.setBounds(50, 320, 60, 25);
        panel.add(questionLabel);
        
        JTextField questionField = new JTextField(20);
        questionField.setBounds(120, 320, 300, 25);
        questionField.setEditable(false);  // Non-editable field
        panel.add(questionField);

        // Confirm button
        JButton confirmButton = new JButton("Xác nhận");
        confirmButton.setBounds(440, 320, 100, 25);
        panel.add(confirmButton);

        // Add listener for confirm button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle confirm button click
            }
        });

        // Word input label and text field
        JLabel wordInputLabel = new JLabel("Nhập từ:");
        wordInputLabel.setBounds(50, 360, 60, 25);
        panel.add(wordInputLabel);
        
        JTextField wordInputField = new JTextField(20);
        wordInputField.setBounds(120, 360, 300, 25);
        panel.add(wordInputField);

        // Dropdown for row/column selection
        String[] directions = { "Cột ngang", "Cột dọc" };
        JComboBox<String> directionDropdown = new JComboBox<>(directions);
        directionDropdown.setBounds(440, 360, 100, 25);
        panel.add(directionDropdown);

        // Add listener for word input field
        wordInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle word input changes
            }
        });

        // Add listener for dropdown selection
        directionDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle direction dropdown changes
            }
        });

        // Set frame visibility
        frame.setVisible(true);
    }
}
