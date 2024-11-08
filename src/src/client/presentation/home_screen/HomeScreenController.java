package src.client.presentation.home_screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.data.dto.User;

public class HomeScreenController extends BaseClientController {
    
    private final onActionResponseHomeScreen listener;
    public interface onActionResponseHomeScreen {
        void onListUserRes(List<User> listUser);
        void onBeingInvited(String opponentUsername);
        void onBeingDeclined();
        void onBeingOffline();
        void onWaitingFindGame();
        void onFoundGame();
        void onCancelRandomInivation();
        void onPlayGameState(boolean status);
        void onLogout(String status);
    }
    public HomeScreenController(onActionResponseHomeScreen listener) throws IOException {
        this.listener = listener;
        initializeCallbacks();
    }

    private void initializeCallbacks() throws IOException {

        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) {
                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if (route.equals("/getListUser")) {
                    List<User> list = gson.fromJson(rp.data, new TypeToken<List<User>>(){}.getType());
                    listener.onListUserRes(list);

                } else if (route.equals("/respondToInvitation")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    boolean status = result.get("status").getAsBoolean();
                    listener.onBeingDeclined();

                } else if (route.equals("/playGameUser")) {
                    try {
                        JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                        boolean status = result.get("status").getAsBoolean();
                        listener.onPlayGameState(status);

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                } else if (route.equals("/invitePlay")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    boolean status = result.get("status").getAsBoolean();
                    listener.onBeingOffline();

                } else if (route.equals("/beInvited")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String opponentUsername = result.get("invitedBy").getAsString();
                    listener.onBeingInvited(opponentUsername);

                } else if (route.equals("/findGame")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String message = result.get("message").getAsString();
                    listener.onWaitingFindGame();

                } else if (route.equals("/beFoundGame")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String message = result.get("message").getAsString();
                    listener.onFoundGame();

                } else if (route.equals("/respondOnFoundGame")) {
                    JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                    String message = result.get("message").getAsString();
                    listener.onCancelRandomInivation();

                } else if (route.equals("/doLogout")) {
                    String status = rp.data;
                    listener.onLogout(status);

                } else if(route.equals("/test")){
                    System.out.println(data);
                }
            }

            @Override
            public void onError(String error) {
            }
            @Override
            public void onFail() {
            }
        };
        System.out.println("Callback init" + Thread.currentThread().getName());
        onStartLiveUpdate(getClass().getName());

        // Verify callback was set
        if (!hasValidCallback()) {
            System.err.println("[ERROR] HomeScreen - Callback not properly set!");
        } else {
            System.out.println("[DEBUG] HomeScreen - Callback successfully set");
        }
    }

    public void getUserList() {
        doJsonRequest(null, "/getListUser");
    }
    
    public void respondToInvitation(boolean status, String oppoUser, String currUser){
        JsonObject body = new JsonObject();
        body.addProperty("status", String.valueOf(status));
        body.addProperty("opponent", oppoUser);
        body.addProperty("currUser", currUser);
        doJsonRequest(body, "/respondToInvitation");
    }

    public void invitePlay(String username, String currUser) {
        JsonObject body = new JsonObject();
        body.addProperty("opponent", username);
        body.addProperty("currUser", currUser);
        doJsonRequest(body, "/invitePlay");
    }

    public void onFindGame(String username) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        doJsonRequest(body, "/findGame");
    }

    public void onCancelFindGame(String username) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("message", "cancel find game");
        doJsonRequest(body, "/cancelFindGame");
    }

    public void respondToFoundGame(String username, boolean accept) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("accept", String.valueOf(accept));
        doJsonRequest(body, "/respondOnFoundGame");
    }

    public void onLogout(User user) {
        doJsonRequest(user, "/doLogout");
    }

    @Override
    public void onCloseLiveUpdate(String controllerName) {
        super.onCloseLiveUpdate(controllerName);

    }
}
