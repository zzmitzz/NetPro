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
        void onCountDownTime(int remainTime);
        void onStartGame();
        void onReceiveQuestion(int id, String question);
        void onAnswerResult(double point, boolean status);
        void onEndGame(double stateResult) throws IOException;
        void onGameEnd();
    }

    public void postAnswer(int idQuestion, String answerInput, String questionType){
        JsonObject body = new JsonObject();
        body.addProperty("id", idQuestion+"" );
        body.addProperty("answer", answerInput);
        body.addProperty("timeStamp","" + System.currentTimeMillis());
        body.addProperty("type", questionType);
        doJsonRequest(body, "/postAnswer");
    }
    public void postScore(double score){
        JsonObject body = new JsonObject();
        body.addProperty("score",score);
        doJsonRequest(body, "/postScore");
    }
    public CrosswordGameScreenController(onActionResponse action) throws IOException{
        this.listener = action;
        callbackAction = new onAction(){
            @Override
            public void onSuccess(String data) throws IOException {

                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if(route.equals("/postQuestion")){
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
                    listener.onReceiveQuestion(parseData.get("id").getAsInt(), parseData.get("question").getAsString());
                }
                if(route.equals("/onGameFinish")){
                    listener.onEndGame(gson.fromJson(rp.data, JsonObject.class).get("status").getAsDouble());
                }
                if(route.equals("/onAnswerReceive")){
                    JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
                    boolean status = jsonObject.get("status").getAsBoolean();
                    double point  = jsonObject.get("point").getAsDouble();
                    listener.onAnswerResult(point, status);
                }
                if(route.equals("/onCountdown")){
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
                    listener.onCountDownTime(parseData.get("timeLeft").getAsInt());
                }
                if(route.equals("/endGame")){
                    listener.onGameEnd();
                }
            }
            @Override
            public void onFail() {

            }

            @Override
            public void onError(String e) {

            }
        };
        onStartLiveUpdate(getClass().toString());

    }
}
