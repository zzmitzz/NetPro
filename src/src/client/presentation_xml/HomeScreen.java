//package src.client.presentation.home_screen;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import javax.swing.table.DefaultTableModel;
//
//import src.client.common.LoadingDialog;
//import src.client.common.NotificationDialog;
//import src.client.data.dto.User;
//
//public class HomeScreen extends JFrame implements HomeScreenController.onActionResponseHomeScreen {
//
//    private LoadingDialog loadingDialog = new LoadingDialog(this);
//
//    private HomeScreenController controller;
//    private Object[][] data;
//    private DefaultTableModel model;
//    private User user;
//
//    private JTable userTable;
//    private String selectedUsername;
//
//    private List<User> listUser;
//    private boolean isRandom = false;
//
//    private JDialog inviteDialog;
//    private JDialog waitingDialog;
//    private Timer waitingTimer;
//    SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
//        private JLabel timerLabel;
//
//        @Override
//        protected Void doInBackground() {
//            SwingUtilities.invokeLater(this::createAndShowDialog);
//            return null;
//        }
//
//        private void createAndShowDialog() {
//            // Store references at class level
//            waitingDialog = new JDialog(HomeScreen.this, "Đang tìm trận", true);
//            waitingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//            waitingDialog.setLayout(new BorderLayout(10, 10));
//            waitingDialog.setSize(300, 150);
//            waitingDialog.setLocationRelativeTo(HomeScreen.this);
//
//            // Rest of dialog setup remains same
//            JPanel messagePanel = new JPanel();
//            JLabel messageLabel = new JLabel("<html>Đang tìm trận</html>");
//            messagePanel.add(messageLabel);
//            waitingDialog.add(messagePanel, BorderLayout.CENTER);
//
//            timerLabel = new JLabel("0");
//            waitingDialog.add(timerLabel, BorderLayout.NORTH);
//
//            JPanel buttonPanel = new JPanel(new FlowLayout());
//            JButton cancelButton = new JButton("Hủy tìm trận");
//            buttonPanel.add(cancelButton);
//            waitingDialog.add(buttonPanel, BorderLayout.SOUTH);
//
//            // Store timer reference at class level
//            waitingTimer = new Timer(1000, e -> {
//                int currentValue = Integer.parseInt(timerLabel.getText());
//                publish(currentValue + 1);
//            });
//
//            cancelButton.addActionListener(e -> {
//                controller.onCancelFindGame(user.getUsername());
//                cleanup();
//            });
//
//            waitingTimer.start();
//            waitingDialog.setVisible(true);
//        }
//
//        @Override
//        protected void process(List<Integer> chunks) {
//            int lastValue = chunks.get(chunks.size() - 1);
//            timerLabel.setText(String.valueOf(lastValue));
//        }
//
//        private void cleanup() {
//            if (waitingTimer != null) {
//                waitingTimer.stop();
//            }
//            if (waitingDialog != null) {
//                waitingDialog.dispose();
//            }
//        }
//    };
//    public HomeScreen(User user) throws IOException {
//        this.controller = new HomeScreenController(this);
//        setTitle("Chọn người chơi");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 600);
//        setLayout(new BorderLayout());
//        this.user = user;
//
//        // Create a panel for the username
//        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        JLabel userLabel = new JLabel("User: " + user.getFullName());
//        userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
//        topPanel.add(userLabel, BorderLayout.EAST);
//        add(topPanel, BorderLayout.NORTH);
//
//        controller.getUserList();
//
//        addWindowListener(new WindowListener() {
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
//                    dispose();
//                    System.exit(0);  // Exits the application
//                }
//                controller.onCloseLiveUpdate(this.getClass().getName());
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
//        // Create styled buttons
//        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JButton inviteButton = createFancyButton("Gửi lời mời");
//        inviteButton.addActionListener((ActionEvent e) -> {
//            isRandom = false;
//            if (selectedUsername == null || selectedUsername.isEmpty()) {
//                JOptionPane.showMessageDialog(this,
//                    "Vui lòng chọn người chơi trước khi gửi lời mời",
//                    "Cảnh báo",
//                    JOptionPane.WARNING_MESSAGE);
//            } else if (selectedUsername.equals(user.getUsername())) {
//                JOptionPane.showMessageDialog(this,
//                    "Không thể gửi lời mời cho chính mình",
//                    "Cảnh báo",
//                    JOptionPane.WARNING_MESSAGE);
//
//            } else {
//                controller.invitePlay(selectedUsername, user.getUsername());
//            }
//        });
//
//        JButton randomButton = createFancyButton("Tìm trận");
//        randomButton.addActionListener((ActionEvent e) -> {
//            controller.onFindGame(user.getUsername());
//        });
//
//        JButton instructionButton = createFancyButton("Hướng dẫn");
//        instructionButton.addActionListener((ActionEvent e) -> {
//            new TutorialScreen();
//        });
//
//        JButton logoutButton = createFancyButton("Đăng xuất");
//        logoutButton.addActionListener((ActionEvent e) -> {
//            controller.onLogout(user);
//        });
//
//        buttonPanel.add(inviteButton);
//        buttonPanel.add(randomButton);
//        buttonPanel.add(instructionButton);
//        buttonPanel.add(logoutButton);
//
//        add(buttonPanel, BorderLayout.EAST);
//
//        // Set background color
//        getContentPane().setBackground(new Color(0xf0f0f0));
//        buttonPanel.setBackground(new Color(0xf0f0f0));
//        setVisible(true);
//    }
//
//    private JButton createFancyButton(String text) {
//        JButton button = new JButton(text);
//        button.setFont(new Font("Time New Romances", Font.BOLD, 14));
//        button.setForeground(Color.WHITE);
//        button.setBackground(new Color(0x4CAF50));
//        button.setFocusPainted(false);
//        button.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(0x388E3C)),
//                BorderFactory.createEmptyBorder(10, 25, 10, 25)
//        ));
//        return button;
//    }
//
//    @Override
//    public void onListUserRes(List<User> listUser) {
//        this.listUser = listUser;
//        Collections.sort(listUser, new Comparator<User>() {
//            @Override
//            public int compare(User o1, User o2) {
//                if (o1.score >= o2.score) {
//                    return -1;
//                } else {
//                    return 1;
//                }
//            }
//        });
//
//        data = new Object[listUser.size()][3];
//        for (int i = 0; i < listUser.size(); i++) {
//            User user = listUser.get(i);
//            data[i][0] = user.getUsername();
//            data[i][1] = user.getFullName();
//            data[i][2] = user.getScore();
//        }
//
//        // Create table
//        String[] columnNames = {"Danh sách người chơi", "Tên ", "Điểm"};
//        model = new DefaultTableModel(data, columnNames) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        userTable = new JTable(model);
//        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        userTable.getSelectionModel().addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                int selectedRow = userTable.getSelectedRow();
//                if (selectedRow >= 0) {
//                    selectedUsername = (String) userTable.getValueAt(selectedRow, 0);
//                }
//            }
//        });
//
//        JScrollPane scrollPane = new JScrollPane(userTable);
//        add(scrollPane, BorderLayout.CENTER);
//        userTable.setBackground(new Color(0xe6e6e6));
//        userTable.setFillsViewportHeight(true);
//        revalidate();
//        repaint();
//    }
//
//    @Override
//    public void onBeingInvited(String opponentUsername) {
//        inviteDialog = new JDialog(this, "Mời vào trận", true);
//        inviteDialog.setLayout(new BorderLayout(10, 10));
//        inviteDialog.setSize(300, 150);
//        inviteDialog.setLocationRelativeTo(this);
//
//        // Message panel
//        JPanel messagePanel = new JPanel();
//        JLabel messageLabel = new JLabel("<html>Người chơi " + opponentUsername + " mời bạn vào trận.<br>Chấp nhận lời mời?</html>");
//        messagePanel.add(messageLabel);
//        inviteDialog.add(messagePanel, BorderLayout.CENTER);
//
//        // Timer label
//        JLabel timerLabel = new JLabel("30");
//        inviteDialog.add(timerLabel, BorderLayout.NORTH);
//
//        // Buttons panel
//        JPanel buttonPanel = new JPanel(new FlowLayout());
//        JButton acceptButton = new JButton("Có");
//        JButton declineButton = new JButton("Không");
//
//        buttonPanel.add(acceptButton);
//        buttonPanel.add(declineButton);
//        inviteDialog.add(buttonPanel, BorderLayout.SOUTH);
//
//        // Timer
//        Timer timer = new Timer(1000, new ActionListener() {
//            int timeLeft = 30;
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                timeLeft--;
//                timerLabel.setText(String.valueOf(timeLeft));
//                if (timeLeft <= 0) {
//                    ((Timer)e.getSource()).stop();
//                    inviteDialog.dispose();
//                    controller.respondToInvitation(false, opponentUsername, user.getUsername());
//                }
//            }
//        });
//
//        acceptButton.addActionListener(e -> {
//            timer.stop();
//            inviteDialog.dispose();
//            controller.respondToInvitation(true,  opponentUsername, user.getUsername());
//        });
//
//        declineButton.addActionListener(e -> {
//            timer.stop();
//            inviteDialog.dispose();
//            controller.respondToInvitation(false, opponentUsername, user.getUsername());
//        });
//
//        timer.start();
//        inviteDialog.setVisible(true);
//    }
//
//    @Override
//    public void onBeingDeclined() {
//        if (inviteDialog != null) {
//            inviteDialog.dispose();
//        }
//        JOptionPane.showMessageDialog(this, "Lời mời vào trận của bạn đã bị từ chối", "Lời mời vào trận bị từ chối", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    @Override
//    public void onBeingOffline() {
//        NotificationDialog dialog = new NotificationDialog("Người chơi hiện không online, vui lòng chọn người khác.", 3000);
//        dialog.setLocationRelativeTo(this);
//        dialog.setVisible(true);
//    }
//
//    @Override
//    public void onPlayGameState(boolean status) {
//
//        if (status) {
//            if (isRandom) {
//
//            }
//            dispose();
//            try {
//                // Call the constructor first fuck it.
////                SwingUtilities.invokeLater(new Runnable() {
////                    @Override
////                    public void run() {
////                        JFXPanel fxPanel = new JFXPanel();
////                        getContentPane().add(fxPanel, BorderLayout.CENTER);
////                        Platform.runLater(() -> {
////                            try {
////                                // Load FXML and create JavaFX scene
////                                FXMLLoader loader = new FXMLLoader(getClass().getResource("../play_game/CrosswordGameScreenController.fxml"));
////                                Parent root = loader.load();
////                                Scene scene = new Scene(root);
////                                fxPanel.setScene(scene);
////                                setTitle("Playing");
////                            } catch (Exception ex) {
////                                ex.printStackTrace();
////                            }
////                        });
////                    }
////                });
////                new CrosswordGameScreen(user);
//
//                worker.cancel(true);
//                System.out.println(controller.getClass().getName());
//                controller.onCloseLiveUpdate(controller.getClass().getName());
//                controller = null;
//            } catch (Exception e) {
//
//            }
//
//        }
//    }
//
//    @Override
//    public void onWaitingFindGame() {
//        worker.execute();
//    }
//
//    @Override
//    public void onFoundGame() {
//        SwingUtilities.invokeLater(() -> {
//            if (waitingTimer != null) {
//                waitingTimer.stop();
//            }
//            if (waitingDialog != null && waitingDialog.isVisible()) {
//                waitingDialog.setVisible(false);
//                waitingDialog.dispose();
//            }
//
//            inviteDialog = new JDialog(this, "Đã tìm thấy trận", true);
//            inviteDialog.setLayout(new BorderLayout(10, 10));
//            inviteDialog.setSize(300, 150);
//            inviteDialog.setLocationRelativeTo(this);
//
//            // Message panel
//            JPanel messagePanel = new JPanel();
//            JLabel messageLabel = new JLabel("<html>Đã tìm thấy trận<br>Chấp nhận tham gia?</html>");
//            messagePanel.add(messageLabel);
//            inviteDialog.add(messagePanel, BorderLayout.CENTER);
//
//            // Timer label
//            JLabel timerLabel = new JLabel("10");
//            inviteDialog.add(timerLabel, BorderLayout.NORTH);
//
//            // Buttons panel
//            JPanel buttonPanel = new JPanel(new FlowLayout());
//            JButton acceptButton = new JButton("Có");
//            JButton declineButton = new JButton("Không");
//
//            buttonPanel.add(acceptButton);
//            buttonPanel.add(declineButton);
//            inviteDialog.add(buttonPanel, BorderLayout.SOUTH);
//
//            // Timer
//            Timer timer = new Timer(1000, new ActionListener() {
//                int timeLeft = 10;
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    timeLeft--;
//                    timerLabel.setText(String.valueOf(timeLeft));
//                    if (timeLeft <= 0) {
//                        ((Timer)e.getSource()).stop();
//                        inviteDialog.dispose();
//                        controller.respondToFoundGame(user.getUsername(), false);
//                    }
//                }
//            });
//
//            acceptButton.addActionListener(e -> {
//                timer.stop();
//                inviteDialog.dispose();
//                controller.respondToFoundGame(user.getUsername(), true);
//            });
//
//            declineButton.addActionListener(e -> {
//                timer.stop();
//                inviteDialog.dispose();
//                controller.respondToFoundGame(user.getUsername(), false);
//            });
//
//            timer.start();
//            inviteDialog.setVisible(true);
//        });
//    }
//
//    @Override
//    public void onCancelRandomInivation() {
//        if (inviteDialog != null) {
//            inviteDialog.setVisible(false);
//            inviteDialog.dispose();
//            inviteDialog = null;
//        }
//
//        JOptionPane.showMessageDialog(this,
//                            "Trận đấu đã bị hủy do đối thủ từ chối!",
//                                    "Trận đấu bị hủy",
//                                    JOptionPane.INFORMATION_MESSAGE
//        );
//    }
//
//    @Override
//    public void onLogout(String status) {
////        controller.onCloseLiveUpdate(controller.getClass().getName());
////        controller.callbackAction = null;
////        controller = null;
////
////        SwingUtilities.invokeLater(() -> {
////            dispose();
////            try {
////                new LoginScreen();
////            } catch (Exception e) {
////                Logger.getLogger(HomeScreen.class.getName()).log(Level.SEVERE, null, e);
////            }
////        });
//    }
//}
