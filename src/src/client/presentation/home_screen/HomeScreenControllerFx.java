package src.client.presentation.home_screen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.data.dto.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class HomeScreenControllerFx extends BaseClientController implements FoundGameDialog.onFoundGameAction,WaitingDialog.DialogAction, InviteDialog.InvitationResponseHandler {

    @FXML
    private TableView<Player> playerTable;

    @FXML
    private TableColumn<Player, String> fullname;

    @FXML
    private TableColumn<Player, String> username;

    @FXML
    private TableColumn<Player, Integer> score;
    @FXML
    private Label userFullName;
    @FXML
    private Button sendInviteButton;

    @FXML
    private Button playRandomButton;

    @FXML
    private Button instructionsButton;

    @FXML
    private Button logoutButton;

    private User user;
    private List<User> listUser;
    private final ObservableList<Player> playerData= FXCollections.observableArrayList();
    private InviteDialog inviteDialog;
    private WaitingDialog waitingDialog;
    @FXML
    public void initialize() throws IOException {
        // Set up the table columns with their respective property bindings
        fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        playerTable.setItems(playerData);
        getUserList();
        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) {
                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if (route.equals("/getListUser")) {
                    List<User> list = gson.fromJson(rp.data, new TypeToken<List<User>>(){}.getType());
                    onListUserRes(list);

                } else if (route.equals("/respondToInvitation")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    boolean status = result.get("status").getAsBoolean();
                    onBeingDeclined();

                } else if (route.equals("/playGameUser")) {
                    try {
                        JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                        boolean status = result.get("status").getAsBoolean();
                        onPlayGameState(status);

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                } else if (route.equals("/invitePlay")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    boolean status = result.get("status").getAsBoolean();
                    onBeingOffline();

                } else if (route.equals("/beInvited")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String opponentUsername = result.get("invitedBy").getAsString();
                    onBeingInvited(opponentUsername);

                } else if (route.equals("/findGame")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String message = result.get("message").getAsString();
                    onWaitingFindGame();

                } else if (route.equals("/beFoundGame")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String message = result.get("message").getAsString();
                    onFoundGame();

                } else if (route.equals("/respondOnFoundGame")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String message = result.get("message").getAsString();
                    onCancelRandomInvitation();

                } else if (route.equals("/doLogout")) {
                    String status = rp.data;
                    onLogout(status);
                }
            }

            @Override
            public void onError(String error) {
            }
            @Override
            public void onFail() {
            }
        };
        setupButtonActions();
        onStartLiveUpdate(this.getClass().getName());
    }

    private void onPlayGameState(boolean status) throws IOException {
        if(status){
            Parent playGameScreen = FXMLLoader.load(getClass().getResource("../play_game/CrossWordGameScreen.fxml"));
            Scene playGameScene = new Scene(playGameScreen);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(playGameScene);
            stage.show();
            onCloseLiveUpdate(this.getClass().getName());
        }else{
            showMessageDialog("404", "Đã có lỗi xảy ra.");
        }
    }

    private void onLogout(String status) {

    }

    private void onCancelRandomInvitation() {

    }

    private void onFoundGame() {
        Platform.runLater(()->{
            FoundGameDialog.showDialog(
                    new Stage(),  // owner stage
                    this,
                    user.getUsername()// your existing waiting timer
            );
        });
    }

    private void onWaitingFindGame() {
        Platform.runLater(()->{
            waitingDialog = new WaitingDialog(
                    new Stage(),
                    this,
                    user
            );
            waitingDialog.show();
        });
    }

    private void onBeingOffline() {
        showMessageDialog("Chẹp Chẹp", "Người chơi hiện không online, vui lòng chọn người khác.");
    }

    private void onBeingInvited(String opponentUsername) {
        Platform.runLater(()->{
            inviteDialog = new InviteDialog(
                    new Stage(),
                    opponentUsername,
                    user.getUsername(),
                    this
            );
            inviteDialog.show();
        });
    }

    private void onBeingDeclined() {
        if (inviteDialog != null) {
            inviteDialog.closeDialog();
        }
        showMessageDialog("Lời mời vào trận của bạn đã bị từ chối", "Lời mời vào trận bị từ chối");
    }
    private void showMessageDialog(String title, String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    private void onListUserRes(List<User> list) {
        this.listUser = list;
        listUser.sort((o1, o2) -> {
            if (o1.score >= o2.score) {
                return -1;
            } else {
                return 1;
            }
        });
        for(User a : listUser){
            Platform.runLater(() -> playerData.add(new Player(a.getFullName(), a.getUsername(), (int) Math.ceil(a.getScore()))));
        }
    }

    public void setUserData(User userData) {
        this.user = userData;
        userFullName.setText("Xin chào: " + user.getFullName());
    }
    public void getUserList() {
        doJsonRequest(null, "/getListUser");
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
            if(selectedPlayer.getUsername().equalsIgnoreCase(user.getUsername())){
                showMessageDialog("Ê nè!!!", "Don't be disorder like this");
            }
            else{
                JsonObject body = new JsonObject();
                body.addProperty("opponent", selectedPlayer.getUsername());
                body.addProperty("currUser", user.getUsername());
                doJsonRequest(body, "/invitePlay");
            }
        } else {
            showMessageDialog("Ê nè!!!", "No player selected. Please select a player to send an invitation.");
        }
    }

    private void handlePlayRandom() {
        JsonObject body = new JsonObject();
        body.addProperty("username", user.getUsername());
        doJsonRequest(body, "/findGame");
    }
    public void respondToInvitation(boolean status, String oppoUser, String currUser){
        JsonObject body = new JsonObject();
        body.addProperty("status", String.valueOf(status));
        body.addProperty("opponent", oppoUser);
        body.addProperty("currUser", currUser);
        doJsonRequest(body, "/respondToInvitation");
    }
    public void handleInstructions(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../tutorial/InstructionScreen.fxml"));
            Parent instructionScreen = loader.load();
            Scene instructionScene = new Scene(instructionScreen);
            Stage stage = new Stage();
            stage.setScene(instructionScene);
            stage.setTitle("Instructions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onLogout(User user) {
        doJsonRequest(user, "/doLogout");
    }
    public void handleLogout(ActionEvent event) throws IOException {
        onLogout(user);
        Parent loginScreen = FXMLLoader.load(getClass().getResource("../login/LoginScreen.fxml"));
        Scene loginScene = new Scene(loginScreen);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
        onCloseLiveUpdate(this.getClass().getName());
    }

    @Override
    public void onCancelFindGame(String username) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("message", "cancel find game");
        doJsonRequest(body, "/cancelFindGame");
    }

    @Override
    public void handleResponse(boolean accepted, String opponent) {
        respondToInvitation(accepted, opponent, user.getUsername());
    }

    @Override
    public void respondToFoundGame(String username, boolean accepted) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("accept", String.valueOf(accepted));
        doJsonRequest(body, "/respondOnFoundGame");
    }
}

