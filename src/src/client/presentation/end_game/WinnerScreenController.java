package src.client.presentation.end_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class WinnerScreenController {

    @FXML
    private Label winnerName;

    @FXML
    private Button continueButton;

    @FXML
    private Button logoutButton;

    // Event handler for the "Continue" button
    @FXML
    public void continueGame(ActionEvent event) throws IOException {
        try {
            // Load the HomeScreen FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/home_screen/HomeScreen.fxml"));
            Scene scene = new Scene(loader.load());

            // Set the new scene on the current stage
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Event handler for the "Log Out" button
    @FXML
    public void logout(ActionEvent event) throws IOException {
        try {
            // Load the LoginScreen FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/login/LoginScreen.fxml"));
            Scene scene = new Scene(loader.load());

            // Set the new scene on the current stage
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
