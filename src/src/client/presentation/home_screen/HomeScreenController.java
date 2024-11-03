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

public class HomeScreenController extends BaseClientController{
    
    private final onActionResponseHomeScreen listener;
    public interface onActionResponseHomeScreen {
        void onListUserRes(List<User> listUser);
        void onPlayGameState(boolean status);
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
                if(route.equals("/getListUser")){
                    List<User> list = gson.fromJson(rp.data, new TypeToken<List<User>>(){}.getType());
                    listener.onListUserRes(list);
                }
                else if(route.equals("/playGameUser")){
                    try {
                        JsonObject result = gson.fromJson(rp.data, JsonObject.class);
                        boolean status = result.get("status").getAsBoolean();
                        listener.onPlayGameState(status);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                else if(route.equals("/test")){
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
        System.out.println("CAllback init" + Thread.currentThread().getName());
        onStartLiveUpdate(this.getClass().getName());

        // Verify callback was set
        if (!hasValidCallback()) {
            System.err.println("[ERROR] HomeScreen - Callback not properly set!");
        } else {
            System.out.println("[DEBUG] HomeScreen - Callback successfully set");
        }
    }
    public void getUserList(){
        doJsonRequest(null, "/getListUser");
    }
    
    public void invitePlay(String username, String currUser){
        username = "2";
        JsonObject body = new JsonObject();
        body.addProperty("opponent", username);
        body.addProperty("currUser", currUser);
        doJsonRequest(body, "/playGameUser");
    }
}
