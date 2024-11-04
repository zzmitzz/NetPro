/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.client.presentation.signup;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.data.dto.User;

/**
 *
 * @author 1
 */
public class RegisterScreenController extends BaseClientController {
    private final onActionResponse listener;

    public interface onActionResponse {
        void registerCallback(User user);
    }

    public RegisterScreenController(onActionResponse action) throws IOException {
        this.listener = action;
        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) {
                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if (route.equals("/doRegistery")) {
                    JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
                    System.out.println(jsonObject.get("data").getAsString());
                    listener.registerCallback(new User(
                            jsonObject.get("fullName").getAsString(),
                            jsonObject.get("username").getAsString(),
                            jsonObject.get("password").getAsString(),
                            jsonObject.get("score").getAsDouble()
                    ));
                }
            }

            @Override
            public void onFail() {}

            @Override
            public void onError(String e) {}
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
}
