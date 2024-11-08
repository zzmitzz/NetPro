package src.client.presentation.play_game;

import com.google.gson.JsonObject;
import src.client.data.dto.User;
import src.client.presentation.home_screen.HomeScreen;

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
    private int timeleft = 0;
    JTextField usernameField;
    JLabel pointsValueLabel;
    JLabel roundValueLabel;
    JTextField wordInputField;
    JTextField questionField;
    JLabel countdownLabel;
    JComboBox<String> directionDropdown;

    public CrosswordGameScreen(User user) throws IOException {
        this.user = user;

        controller = new CrosswordGameScreenController(this);
        // Set up the frame properties
        setTitle("Trò chơi đoán ô chữ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 204, 204), 0, getHeight(), new Color(204, 204, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

// Top bar panel with player information and countdown timer
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setOpaque(false); // Make it transparent to show the gradient background

        // Username label
        JLabel usernameLabel = new JLabel("Tên người chơi: " + user.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(usernameLabel);

        // Points label
        pointsValueLabel = new JLabel("Điểm số: " + points);
        pointsValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(pointsValueLabel);

        // Round label
        roundValueLabel = new JLabel("Vòng: " + currLevel);
        roundValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(roundValueLabel);

        // Countdown timer label
        countdownLabel = new JLabel("Thời gian: 30s");  // Starting time
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(countdownLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel for crossword grid
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(10, 10));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 102), 3));
        gridPanel.setPreferredSize(new Dimension(600, 600));  // Adjust size for better layout

        // Add 100 JLabels representing the grid cells
        for (int i = 0; i < 100; i++) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setBackground(Color.GREEN);  // Set the background color to green
            label.setPreferredSize(new Dimension(8, 8));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(label);
        }
        JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gridWrapper.setOpaque(false); // Transparent background
        gridWrapper.add(gridPanel);
        mainPanel.add(gridWrapper, BorderLayout.CENTER);

        // Bottom panel for question, answer input, and confirm button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));  // Centered with padding
        bottomPanel.setBackground(new Color(255, 255, 255, 150)); // Semi-transparent white
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Question label
        JLabel questionLabel = new JLabel("Câu hỏi:");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(questionLabel);

        // Question text field (non-editable)
        questionField = new JTextField(30);
        questionField.setEditable(false);
        bottomPanel.add(questionField);

        // Word input label
        JLabel wordInputLabel = new JLabel("Nhập từ:");
        wordInputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(wordInputLabel);

        // Word input text field
        wordInputField = new JTextField(20);
        bottomPanel.add(wordInputField);

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
        bottomPanel.add(confirmButton);

        // Dropdown for row/column selection
        String[] directions = { "Cột ngang", "Cột dọc" };
        directionDropdown = new JComboBox<>(directions);
        bottomPanel.add(directionDropdown);

        // Set frame visibility
        setVisible(true);
    }

    @Override
    public void onCountDownTime(int remainTime) {
        countdownLabel.setText("Thời gian: " + remainTime + "s");
        countdownLabel.repaint();
    }

    @Override
    public void onStartGame() {

    }

    @Override
    public void onReceiveQuestion(int id, String question) {
        currQuestionID = id;
        questionField.setText(question);
        roundValueLabel.setText("Vòng "  + currLevel);
        currLevel++;
        roundValueLabel.repaint();
        questionField.repaint();
    }

    @Override
    public void onAnswerResult(double point, boolean status) {
        if(status){
            points += point;
            pointsValueLabel.setText("Điểm số: " + points);
            pointsValueLabel.repaint();
        }else{
            points += point;
            points = Math.min(0,points);
            pointsValueLabel.setText("Điểm số: " + points);
            pointsValueLabel.repaint();
            JOptionPane.showMessageDialog(CrosswordGameScreen.this, "Bạn trả lời sai rồi!");
        }
    }

    @Override
    public void onEndGame(double stateResult) throws IOException {
        if(stateResult == 0){
            JOptionPane.showMessageDialog(CrosswordGameScreen.this, "Hoà rùi!");
        }
        else if (stateResult > 0.0){
            JOptionPane.showMessageDialog(CrosswordGameScreen.this, "Winner");
        }else if (stateResult < 0){
            JOptionPane.showMessageDialog(CrosswordGameScreen.this, "Looser");
        }
        dispose();
        new HomeScreen(user);
        controller.closeController();
    }
    @Override
    public void onGameEnd(){
        controller.postScore(points);
        JOptionPane.showMessageDialog(CrosswordGameScreen.this, "Game kết thúc! Nhấn ok để xem kết quả");
    }
}
