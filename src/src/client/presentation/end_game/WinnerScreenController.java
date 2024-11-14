package src.client.presentation.end_game;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.client.common.BaseClientController;
import src.client.data.dto.User;
import src.client.presentation.home_screen.HomeScreenControllerFx;

import java.io.IOException;

public class WinnerScreenController extends BaseClientController {

    @FXML
    private Label winnerName;

    @FXML
    private Button continueButton;

    @FXML
    private Button logoutButton;
    private User user;
    public double lastPoint = 0;
    @FXML
    public Label scores;
    @FXML
    public void initialize(){
        Platform.runLater(() -> {
            scores.setText(String.valueOf(lastPoint));
            winnerName.setText(String.valueOf(user.getFullName()));
        });
    }
    // Event handler for the "Continue" button
    @FXML
    public void continueGame(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../home_screen/HomeScreen.fxml"));
                Parent root = loader.load();
                HomeScreenControllerFx controller = loader.getController();
                controller.setUserData(user);
                // Get current stage
                System.out.println(user.getUsername());
                Stage stage = (Stage) logoutButton.getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        onCloseLiveUpdate(this.getClass().getName());
    }


    public void setUserData(User user, double points) {
        this.user = user;
        this.lastPoint = points;
    }
}
