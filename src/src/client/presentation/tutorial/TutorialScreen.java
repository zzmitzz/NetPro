package src.client.presentation.tutorial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import src.client.data.dto.User;
//import src.client.presentation.home_screen.HomeScreen;

public class TutorialScreen {
    private JFrame frame;
    private static final String TITLE = "Hướng dẫn";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 16);

    public TutorialScreen() {
        initializeFrame();
        JPanel mainPanel = createMainPanel();
        addComponents(mainPanel);
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.add(mainPanel);
        return mainPanel;
    }

    private void addComponents(JPanel mainPanel) {
        // Add title
        JLabel titleLabel = new JLabel(TITLE, SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Add tutorial content
        JTextArea tutorialText = createTutorialTextArea();
        JScrollPane scrollPane = new JScrollPane(tutorialText);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton closeButton = createStyledButton("Close");
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextArea createTutorialTextArea() {
        JTextArea tutorialText = new JTextArea();
        tutorialText.setText("Hướng dẫn chơi game\n\n" +
                             "Luật chơi ở đây");

        tutorialText.setEditable(false);
        tutorialText.setLineWrap(true);
        tutorialText.setWrapStyleWord(true);
        tutorialText.setFont(CONTENT_FONT);
        tutorialText.setMargin(new Insets(10, 10, 10, 10));
        tutorialText.setBackground(Color.WHITE);
        return tutorialText;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(e -> frame.dispose());
        return button;
    }
}