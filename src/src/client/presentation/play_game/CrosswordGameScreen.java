package src.client.presentation.play_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrosswordGameScreen extends JFrame {

    public CrosswordGameScreen() {
        // Set up the frame properties
        setTitle("Trò chơi đoán ô chữ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(false);

        // Create a main panel with a background color
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set gradient background
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 204, 204), 0, getHeight(), new Color(204, 204, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Create a panel for the crossword grid with rounded corners
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(10, 10));  // 10x10 grid
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 102), 3));
        gridPanel.setPreferredSize(new Dimension(500, 250));  // Adjust size for better layout

        // Add 100 JLabels representing the grid cells
        for (int i = 0; i < 100; i++) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setBackground(Color.GREEN);  // Set the background color to green
            label.setPreferredSize(new Dimension(40, 40));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(label);
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // Create a panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(5, 2));  // 5 rows, 2 columns
        controlPanel.setBackground(new Color(255, 255, 255, 150)); // Semi-transparent white
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Username label
        JLabel usernameLabel = new JLabel("Tên người chơi:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(usernameLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setEditable(false);  // Non-editable field
        controlPanel.add(usernameField);

        // Points label
        JLabel pointsLabel = new JLabel("Điểm số:");
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(pointsLabel);

        JLabel pointsValueLabel = new JLabel("0");  // Starting points
        pointsValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(pointsValueLabel);

        // Round label
        JLabel roundLabel = new JLabel("Vòng:");
        roundLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(roundLabel);

        JLabel roundValueLabel = new JLabel("1");  // Starting round
        roundValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(roundValueLabel);

        // Question label
        JLabel questionLabel = new JLabel("Câu hỏi:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(questionLabel);

        JTextField questionField = new JTextField(20);
        questionField.setEditable(false);  // Non-editable field
        controlPanel.add(questionField);

        // Word input label
        JLabel wordInputLabel = new JLabel("Nhập từ:");
        wordInputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(wordInputLabel);

        // Word input text field
        JTextField wordInputField = new JTextField(20);
        controlPanel.add(wordInputField);

        // Confirm button
        JButton confirmButton = new JButton("Xác nhận");
        confirmButton.setBackground(new Color(0, 51, 102));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(100, 30));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle confirm button click
                // You can add your confirmation logic here
                JOptionPane.showMessageDialog(CrosswordGameScreen.this, "Xác nhận thành công!");
            }
        });
        controlPanel.add(confirmButton);

        // Dropdown for row/column selection
        String[] directions = { "Cột ngang", "Cột dọc" };
        JComboBox<String> directionDropdown = new JComboBox<>(directions);
        controlPanel.add(directionDropdown);

        // Set frame visibility
        setVisible(true);
    }

    public static void main(String[] args) {
        // Create an instance of the CrosswordGameScreen to display it
        SwingUtilities.invokeLater(() -> {
            CrosswordGameScreen gameScreen = new CrosswordGameScreen();
            gameScreen.setVisible(true);
        });
    }
}
