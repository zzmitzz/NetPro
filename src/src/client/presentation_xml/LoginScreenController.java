//
//package src.client.presentation_xml;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import java.io.IOException;
//
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Hyperlink;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import src.ResponseWrapper;
//import src.client.common.BaseClientController;
//import src.client.common.onAction;
//import src.client.data.dto.User;
//
//import javax.swing.*;
//
//public class LoginScreenController extends BaseClientController {
//    @FXML
//    private TextField usr, pwd;
//    @FXML
//    private Button loginBtn;
//    @FXML
//    private Hyperlink registerLink;
//
//    public interface onActionResponse {
//        void loginCallback(User user);
//        void loginCallback(String status);
//        void registerCallback();
//    }
//    public LoginScreenController() throws IOException {
//        callbackAction = new onAction() {
//            @Override
//            public void onSuccess(String data) {
//                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
//                String route = rp.route;
//                if (route.equals("/doLogin")) {
//                    try {
//                        JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
//                        loadHomeScreen(new User(
//                                jsonObject.get("fullName").getAsString(),
//                                jsonObject.get("username").getAsString(),
//                                jsonObject.get("password").getAsString(),
//                                jsonObject.get("score").getAsDouble()
//                        ));
//                    } catch (Exception e) {
//                        String status = rp.data;
//                        loadHomeScreen(status);
//                    }
//                }
//            }
//
//            @Override
//            public void onFail() {}
//
//            @Override
//            public void onError(String e) {}
//        };
//        onStartLiveUpdate(this.getClass().getName());
//    }
//    public void onLogin(String username, String password) {
//        doJsonRequest(
//                new User(
//                        "test",
//                        username,
//                        password,
//                        0.0
//                ),
//                "/doLogin");
//    }
//
//
//    // JAVA FX
//    public void initialize() {}
//
//    public void click_login(ActionEvent e) throws IOException {
//        String username = usr.getText();
//        String password = pwd.getText();
//        if (validateInputs(username, password)) {
//            onLogin(username, password);
//        }
//    }
//
//    private boolean validateInputs(String username, String password) {
//        if (username.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Tên không được để trống.");
//            return false;
//        } else if (username.equals("SERVER") || username.equals("Round")) {
//            JOptionPane.showMessageDialog(null, "Name \"" + username + "\" is not allowed, please change.");
//            usr.setText("");
//            return false;
//        }
//        return true;
//    }
//    private void loadHomeScreen(String a){
//        JOptionPane.showMessageDialog(null, "Tài khoản mật khẩu không tồn tại " + a);
//    }
//    private void loadHomeScreen(User player) {
//        try {
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/src/client/presentation/home_screen/HomeScreen.fxml"));
////            Parent root = loader.load();
////            HomeScreenControllerFx controller = loader.getController();
////            controller.setUserData(player);
////            Stage stage = (Stage) loginBtn.getScene().getWindow();
////            stage.setScene(new Scene(root));
////            stage.setTitle("Guessing Word Game");
////            stage.show();
////
//            new HomeScreen(player);
//            onCloseLiveUpdate(getClass().getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void showRegisterForm(ActionEvent ae) {
//        try {
//            onCloseLiveUpdate(getClass().getName());
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("../presentation/signup/RegisterScreen.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) registerLink.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Guessing Word Game");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onExit() {
//        Platform.exit();
//    }
//}
