package src.client.presentation.home_screen;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.client.data.dto.User;

public class WaitingDialog {
    public Stage dialogStage;
    private Label timerLabel;
    private Timeline timeline;
    private final DialogAction controller;
    private final User user;
    private final Stage parentStage;
    public interface DialogAction{
        void onCancelFindGame(String username);
    }
    public WaitingDialog(Stage parentStage, DialogAction controller, User user) {
        this.parentStage = parentStage;
        this.controller = controller;
        this.user = user;
    }

    public void show() {
        // Create this on JavaFX Application Thread
        Platform.runLater(() -> {
            createAndShowDialog();
            startTimer();
        });
    }

    private void createAndShowDialog() {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Đang tìm trận");
        dialogStage.setMinWidth(300);
        dialogStage.setMinHeight(150);

        // Main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setAlignment(Pos.CENTER);

        // Timer label
        timerLabel = new Label("0");
        timerLabel.setStyle("-fx-font-size: 14px;");

        // Message label
        Label messageLabel = new Label("Đang tìm trận");
        messageLabel.setStyle("-fx-font-size: 14px;");

        // Cancel button
        Button cancelButton = new Button("Hủy tìm trận");
        cancelButton.setOnAction(e -> {
            controller.onCancelFindGame(user.getUsername());
            cleanup();
        });

        // Add all components to layout
        mainLayout.getChildren().addAll(timerLabel, messageLabel, cancelButton);

        // Create scene and show
        Scene scene = new Scene(mainLayout);
        dialogStage.setScene(scene);

        // Center on parent
        dialogStage.setOnShown(e -> {
            dialogStage.setX(parentStage.getX() + (parentStage.getWidth() - dialogStage.getWidth()) / 2);
            dialogStage.setY(parentStage.getY() + (parentStage.getHeight() - dialogStage.getHeight()) / 2);
        });

        // Handle window close
        dialogStage.setOnCloseRequest(e -> cleanup());

        dialogStage.show();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int currentValue = Integer.parseInt(timerLabel.getText());
            timerLabel.setText(String.valueOf(currentValue + 1));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void cleanup() {
        if (timeline != null) {
            timeline.stop();
        }
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}