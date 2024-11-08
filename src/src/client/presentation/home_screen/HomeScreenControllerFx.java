package src.client.presentation.home_screen;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import src.client.data.dto.User;
import java.io.IOException;
import java.util.Random;

public class HomeScreenControllerFx {

    @FXML
    private TableView<Player> playerTable;

    @FXML
    private TableColumn<Player, String> nameColumn;

    @FXML
    private TableColumn<Player, Integer> scoreColumn;

    @FXML
    private Label emptyTableMessage;

    @FXML
    private Button sendInviteButton;

    @FXML
    private Button playRandomButton;

    @FXML
    private Button instructionsButton;

    @FXML
    private Button logoutButton;

    private User userData;

    @FXML
    public void initialize() {
        // Set up the table columns with their respective property bindings
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Check if the table is empty and display a message if needed
        playerTable.setPlaceholder(new Label("No data available to display."));

        // Example data (optional, for testing purposes)
        playerTable.getItems().addAll(
                new Player("Alice", 100),
                new Player("Bob", 120),
                new Player("Charlie", 90)
        );

        // Handle button click events
        setupButtonActions();
    }

    public void setUserData(User userData) {
        this.userData = userData;
        System.out.println("UserData set: " + userData.getUsername() + ", Score: " + userData.getScore());
        // Optionally update UI with userData info if needed
    }

    private void setupButtonActions() {
        sendInviteButton.setOnAction(event -> handleSendInvite());
        playRandomButton.setOnAction(event -> handlePlayRandom());
        logoutButton.setOnAction(event -> {
            try {
                handleLogout(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        instructionsButton.setOnAction(event -> {
            handleInstructions(event);
        });
    }

    private void handleSendInvite() {
        Player selectedPlayer = playerTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            System.out.println("Invitation sent to: " + selectedPlayer.getPlayerName());
            // Implement logic to send an invitation, potentially using userData
        } else {
            System.out.println("No player selected. Please select a player to send an invitation.");
        }
    }

    private void handlePlayRandom() {
        if (!playerTable.getItems().isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(playerTable.getItems().size());
            Player randomPlayer = playerTable.getItems().get(randomIndex);
            System.out.println("Random invitation sent to: " + randomPlayer.getPlayerName());
            // Implement logic to send a random invitation, potentially using userData
        } else {
            System.out.println("No players available to select.");
        }
    }

    public void handleInstructions(ActionEvent event) {
        try {
            // Load the InstructionScreen.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/src/client/presentation/instruction/InstructionScreen.fxml"));
            Parent instructionScreen = loader.load();

            // Set the scene to the InstructionScreen
            Scene instructionScene = new Scene(instructionScreen);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(instructionScene);
            stage.setTitle("Instructions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogout(ActionEvent event) throws IOException {
        // Load the LoginScreen.fxml and set it as the current scene
        Parent loginScreen = FXMLLoader.load(getClass().getResource("src/src/client/presentation/login/LoginScreen.fxml"));
        Scene loginScene = new Scene(loginScreen);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();

    }

    // Sample Player class for demonstration
    public static class Player {
        private final String playerName;
        private final int score;

        public Player(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }
    }
}
