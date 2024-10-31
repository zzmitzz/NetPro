package src.client.presentation.play_game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.data.dto.User;

public class CrosswordGameScreenController extends BaseClientController {
    
    private final onActionResponse listener;
    public interface onActionResponse {
        void onStartGame();
        void onReceiveQuestion(int level, String question);
        void onAnswerResult(double point, boolean status);
        void onEndGame();
    }
    
    public CrosswordGameScreenController(onActionResponse action) throws IOException{
        this.listener = action;
        callbackAction = new onAction(){
            @Override
            public void onSuccess(String data) {
                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                System.out.println("CrosswordGameScreenController receive data: " + rp.data);
                if(rp.data.equals("/onStartGame")){
                    listener.onStartGame();
                }
                if(rp.data.equals("/onReceiveQuestion")){
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
                    int level = parseData.get("level").getAsInt();
                    String question = parseData.get("question").getAsString();
                    listener.onReceiveQuestion(level, question);
                }
                if(rp.data.equals("/onAnswerResult")){
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
                    double point = parseData.get("point").getAsDouble();
                    boolean status = parseData.get("status").getAsBoolean();
                    listener.onAnswerResult(point, status);
                }
                if(rp.data.equals("/onEndGame")){
                    listener.onEndGame();
                }
            }
            @Override
            public void onFail() {

            }

            @Override
            public void onError(String e) {

            }
        };
    }
}
