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

public class InviteDialog extends Stage {
    private Timeline timer;
    private int timeLeft = 30;
    private final Label timerLabel;
    public void closeDialog(){
        Platform.exit();
    }
    public InviteDialog(Stage owner, String opponentUsername, String currentUsername,
                        InvitationResponseHandler responseHandler) {
        // Configure the dialog
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Mời vào trận");
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
        Label messageLabel = new Label("Người chơi " + opponentUsername + " mời bạn vào trận.\nChấp nhận lời mời?");
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
                handleResponse(false, opponentUsername);
            }
        }));
        timer.setCycleCount(30);

        // Button actions
        acceptButton.setOnAction(e -> {
            timer.stop();
            handleResponse(true, opponentUsername);
        });
        declineButton.setOnAction(e -> {
            timer.stop();
            handleResponse(false,opponentUsername);
        });

        // Set up scene
        Scene scene = new Scene(mainLayout);
        setScene(scene);

        // Center on owner
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

        // Handler for responses
        this.responseHandler = responseHandler;
    }

    private final InvitationResponseHandler responseHandler;

    private void handleResponse(boolean accepted, String opponent) {
        if (timer != null) {
            timer.stop();
        }
        close();
        if (responseHandler != null) {
            responseHandler.handleResponse(accepted, opponent );
        }
    }

    private void centerOnStage(Stage owner) {
        if (owner != null) {
            setX(owner.getX() + (owner.getWidth() - getWidth()) / 2);
            setY(owner.getY() + (owner.getHeight() - getHeight()) / 2);
        }
    }

    // Interface for handling responses
    public interface InvitationResponseHandler {
        void handleResponse(boolean accepted, String opponent);
    }
}