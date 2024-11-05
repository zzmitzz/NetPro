
package src.client.presentation.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.data.dto.User;
import src.client.presentation.login.LoginScreenController.onActionResponse;

public class LoginScreenController extends BaseClientController {
    private final onActionResponse listener;

    public interface onActionResponse {
        void loginCallback(User user);
        void loginCallback(String status);
        void registerCallback();
    }

    public LoginScreenController(onActionResponse action) throws IOException {
        this.listener = action;
        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) {
                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if(route.equals("/doLogin")) {
                    try {
                        JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
                        listener.loginCallback(new User(
                                jsonObject.get("fullName").getAsString(),
                                jsonObject.get("username").getAsString(),
                                jsonObject.get("password").getAsString(),
                                jsonObject.get("score").getAsDouble()
                        ));
                    } catch (Exception e) {
                        String status = rp.data;
                        listener.loginCallback(status);
                    }
                } else if (route.equals("/test")) {
                    System.out.println(data);
                }

                if(route.equals("/doRegistery")){
                    // Todo
                }
            }

            @Override
            public void onFail() {}

            @Override
            public void onError(String e) {}
        };
        onStartLiveUpdate(this.getClass().getName());
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

}
