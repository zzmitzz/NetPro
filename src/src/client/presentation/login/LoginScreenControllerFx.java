package src.client.presentation.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import src.client.data.dto.User;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.presentation.home_screen.HomeScreenControllerFx;

public class LoginScreenControllerFx extends BaseClientController {

    @FXML
    private TextField usr, pwd;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink registerLink;

    private User user;

    private void loginCallback(User user) throws IOException {
        System.out.println("Success");
        this.user = user;
        loadHomeScreen();
    }

    public void loginCallback(String status) {
        if (status.equals("account is already logged in")) {
            JOptionPane.showMessageDialog(null, "Tài khoản của bạn đang online trên một thiết bị khác.\nVui lòng đăng xuất.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initialize() throws IOException {
        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) {
                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if (route.equals("/doLogin")) {
                    try {
                        JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
                        System.out.println(rp.data);
                        loginCallback(new User(
                            jsonObject.get("fullName").getAsString(),
                            jsonObject.get("username").getAsString(),
                            jsonObject.get("password").getAsString(),
                            jsonObject.get("score").getAsDouble()
                        ));
                    } catch (Exception e) {
                        String status = rp.data;
                        loginCallback(status);
                    }
                }
            }

            @Override
            public void onFail() {}

            @Override
            public void onError(String e) {}
        };
        onStartLiveUpdate(this.getClass().getName());
    }

    public void click_login(ActionEvent e) throws IOException {
        String username = usr.getText();
        String password = pwd.getText();

        if (validateInputs(username, password)) {
            onLogin(username, password);
        }
    }

    private boolean validateInputs(String username, String password) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be Empty.");
            return false;
        } else if (username.equals("SERVER") || username.equals("Round")) {
            JOptionPane.showMessageDialog(null, "Name \"" + username + "\" is not allowed, please change.");
            usr.clear();
            return false;
        }
        return true;
    }

    public void onLogin(String username, String password) {        
        doJsonRequest(
                new User(
                        "test",
                        username,
                        password,
                        0.0
                ),
                "/doLogin");
    }

    private void loadHomeScreen() throws IOException {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../home_screen/HomeScreen.fxml"));
                Parent root = loader.load();
                HomeScreenControllerFx controller = loader.getController();
                controller.setUserData(user);
                // Get current stage
                System.out.println(user.getUsername());
                Stage stage = (Stage) loginButton.getScene().getWindow();

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

    public void showRegisterForm(ActionEvent ae) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../signup/RegisterScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Guessing Word Game");
            stage.show();
            onCloseLiveUpdate(this.getClass().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExit() {
        Platform.exit();
    }
}
