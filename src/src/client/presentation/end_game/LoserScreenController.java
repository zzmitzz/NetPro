package src.client.presentation.end_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;

public class LoserScreenController {

    @FXML
    private Label loserName;

    @FXML
    public Button continueButton;

    @FXML
    public Button logoutButton;

    // Event handler for the "Try Again" button
    @FXML
    public void continueGame(ActionEvent event) throws IOException {
        loadScreen("/presentation/home_screen/HomeScreen.fxml");
    }

    // Event handler for the "Log Out" button
    @FXML
    public void logout(ActionEvent event) throws IOException {
        loadScreen("/presentation/login/LoginScreen.fxml");
    }

    // Helper method to load a new screen
    public void loadScreen(String fxmlFilePath) {
        try {
            // Load the FXML file specified by the path
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Scene scene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
