/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.presentation.signup;

import java.io.IOException;
import java.util.Objects;

import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.data.dto.User;

import javax.swing.*;

/**
 * @author 1
 */
public class RegisterScreenController extends BaseClientController {
    //    private final onActionResponse listener;
//
//    public interface onActionResponse {
//        void registerCallback(String status);
//    }
    @FXML
    private TextField usr, pwd, confirmed_pwd, fullName;
    @FXML
    private Hyperlink loginLink;

    public RegisterScreenController() throws IOException {
        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) {
                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;

                if (route.equals("/doRegister")) {
                    if(Objects.equals(rp.status, "true")){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                showMessage("Đăng ký thành công");
                                try{
                                    launchLogin();
                                }catch (IOException e){

                                }
                            }
                        });

                    }else{
                        showMessage(rp.data);
                    }
                }
            }

            @Override
            public void onFail() {
            }

            @Override
            public void onError(String e) {
            }
        };

        onStartLiveUpdate(getClass().getName());
    }

    public void onRegister(String fullname, String username, String password) {
        doJsonRequest(
                new User(
                        fullname,
                        username,
                        password,
                        0.0
                ),
                "/doRegister");
    }

    public void initialize() {}

    public void register(ActionEvent e) throws IOException {
        String username = usr.getText();
        String password = pwd.getText();
        String fullname = fullName.getText();
        String confirmedPassword = confirmed_pwd.getText();

        if (validateInputs(username, password, confirmedPassword)) {
            onRegister(fullname,username, password );
        }
    }

    private boolean validateInputs(String username, String password, String confirmedPassword) {
        if (username.isEmpty()) {
            showMessage("Name cannot be Empty..");
            return false;
        } else if (username.equals("SERVER") || username.equals("Round")) {
            showMessage("Name \"" + username + "\" is not allowed, please change..");
            usr.clear();
            return false;
        }
        if (password.isEmpty()) {
            showMessage("Password cannot be Empty..");
            return false;
        } else if (!password.equals(confirmedPassword)) {
            showMessage("Passwords do not match !");
            return false;
        }
        return true;
    }



    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void showLoginForm(ActionEvent ae) {
        try {
            launchLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchLogin() throws IOException{
        onCloseLiveUpdate(getClass().getName());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/LoginScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) loginLink.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
