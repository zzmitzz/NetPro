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
import java.util.logging.Level;
import java.util.logging.Logger;

import src.client.data.dto.User;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.presentation.home_screen.HomeScreen;
// import presentation.UserData;
import src.client.presentation.home_screen.HomeScreenControllerFx;
import src.client.presentation.home_screen.HomeScreenController;

public class LoginScreenControllerFx extends BaseClientController {

    @FXML
    private TextField usr, pwd;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink registerLink;

    private User user;

    private void loginCallback(User user) {
        System.out.println("Success");
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

    private void loadHomeScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/src/client/presentation/home_screen/HomeScreen.fxml"));
            Parent root = loader.load();
            HomeScreenControllerFx controller = loader.getController();
            controller.setUserData(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Guessing Word Game");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRegisterForm(ActionEvent ae) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/src/client/presentation/signup/RegisterScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Guessing Word Game");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExit() {
        Platform.exit();
    }
}
