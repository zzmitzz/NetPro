package src.client.presentation.home_screen;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FoundGameDialog extends Stage {
    private Timeline timer;
    private int timeLeft = 10;
    private final Label timerLabel;
    private final onFoundGameAction controller;
    private final String username;

    public FoundGameDialog(Stage owner, onFoundGameAction controller, String username) {
        this.controller = controller;
        this.username = username;

        // Configure the dialog
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Đã tìm thấy trận");
        setMinWidth(300);
        setMinHeight(150);

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Timer label at top
        timerLabel = new Label(String.valueOf(timeLeft));
        timerLabel.setStyle("-fx-font-size: 14px;");
        BorderPane.setAlignment(timerLabel, Pos.CENTER);
        mainLayout.setTop(timerLabel);

        // Message in center
        Label messageLabel = new Label("Đã tìm thấy trận\nChấp nhận tham gia?");
        messageLabel.setWrapText(true);
        VBox messageBox = new VBox(messageLabel);
        messageBox.setAlignment(Pos.CENTER);
        mainLayout.setCenter(messageBox);

        // Buttons at bottom
        Button acceptButton = new Button("Có");
        Button declineButton = new Button("Không");

        HBox buttonBox = new HBox(10, acceptButton, declineButton);
        buttonBox.setAlignment(Pos.CENTER);
        mainLayout.setBottom(buttonBox);

        // Set up timer
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft--;
            timerLabel.setText(String.valueOf(timeLeft));
            if (timeLeft <= 0) {
                handleResponse(false);
            }
        }));
        timer.setCycleCount(10); // 10 seconds countdown

        // Button actions
        acceptButton.setOnAction(e -> handleResponse(true));
        declineButton.setOnAction(e -> handleResponse(false));

        // Set up scene
        Scene scene = new Scene(mainLayout);
        setScene(scene);

        // Center on owner and start timer
        setOnShown(e -> {
            centerOnStage(owner);
            timer.play();
        });

        // Clean up on close
        setOnCloseRequest(e -> {
            if (timer != null) {
                timer.stop();
            }
        });
    }

    private void handleResponse(boolean accepted) {
        if (timer != null) {
            timer.stop();
        }
        close();
        controller.respondToFoundGame(username, accepted);
    }

    private void centerOnStage(Stage owner) {
        if (owner != null) {
            setX(owner.getX() + (owner.getWidth() - getWidth()) / 2);
            setY(owner.getY() + (owner.getHeight() - getHeight()) / 2);
        }
    }

    // Method to show dialog and handle waiting dialog cleanup
    public static void showDialog(Stage owner, onFoundGameAction controller, String username) {
        Platform.runLater(() -> {
            // Show new found game dialog
            FoundGameDialog dialog = new FoundGameDialog(owner, controller, username);
            dialog.show();
        });
    }

    public interface onFoundGameAction{
        void respondToFoundGame(String username, boolean accepted);
    }
}