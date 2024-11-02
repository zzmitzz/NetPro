package src.client.presentation.play_game;

import com.google.gson.JsonObject;
import src.client.data.dto.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CrosswordGameScreen extends JFrame implements CrosswordGameScreenController.onActionResponse {

    private CrosswordGameScreenController controller;
    private User user;
    private double points = 0.0;
    private int currLevel = 1;
    private int currQuestionID;
    JTextField usernameField;
    JLabel pointsValueLabel;
    JLabel roundValueLabel;
    JTextField wordInputField;
    JTextField questionField;
    JComboBox<String> directionDropdown;
    public CrosswordGameScreen(User user) throws IOException {
        this.user = user;

        // Set up the frame properties
        setTitle("Trò chơi đoán ô chữ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
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

        usernameField = new JTextField(20);
        usernameField.setEditable(false);  // Non-editable field
        usernameField.setText(user.getUsername());
        controlPanel.add(usernameField);

        // Points label
        JLabel pointsLabel = new JLabel("Điểm số:");
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(pointsLabel);

        pointsValueLabel= new JLabel("0");  // Starting points
        pointsValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pointsValueLabel.setText(points+"");
        controlPanel.add(pointsValueLabel);

        // Round label
        JLabel roundLabel = new JLabel("Vòng:");
        roundLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(roundLabel);

        roundValueLabel= new JLabel("1");  // Starting round
        roundValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roundValueLabel.setText(currLevel+"");
        controlPanel.add(roundValueLabel);

        // Question label
        JLabel questionLabel = new JLabel("Câu hỏi:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(questionLabel);

        questionField = new JTextField(50);
        questionField.setEditable(false);  // Non-editable field
        controlPanel.add(questionField);

        // Word input label
        JLabel wordInputLabel = new JLabel("Nhập từ:");
        wordInputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(wordInputLabel);

        // Word input text field
        wordInputField = new JTextField(20);
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
                String answer = wordInputField.getText();
                String questionType = (String) directionDropdown.getSelectedItem();
                if(answer.isEmpty()){
                    JOptionPane.showMessageDialog(CrosswordGameScreen.this, "Bạn chưa điền câu trả lời");
                }
                else{
                    controller.postAnswer(currQuestionID, answer, questionType);
                }
            }
        });
        controlPanel.add(confirmButton);

        // Dropdown for row/column selection
        String[] directions = { "Cột ngang", "Cột dọc" };
        directionDropdown = new JComboBox<>(directions);
        controlPanel.add(directionDropdown);

        controller = new CrosswordGameScreenController(this);
        // Set frame visibility
        setVisible(true);
    }

    @Override
    public void onStartGame() {

    }

    @Override
    public void onReceiveQuestion(int id, String question) {
        currQuestionID = id;
        questionField.setText(question);
        roundValueLabel.setText(currLevel+"");
        currLevel++;
        roundValueLabel.repaint();
        questionField.repaint();
    }

    @Override
    public void onAnswerResult(double point, boolean status) {

    }

    @Override
    public void onEndGame(boolean stateResult) {

    }
}
