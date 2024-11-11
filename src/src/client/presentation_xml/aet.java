//package src.client.presentation.play_game;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.IOException;
//import java.util.List;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.scene.text.Text;
//import src.ResponseWrapper;
//import src.client.common.BaseClientController;
//import src.client.common.onAction;
//import src.client.data.dto.User;
//import src.client.presentation.home_screen.HomeScreen;
//
//import javax.swing.*;
//
//public class CrosswordGameScreenControllerfd extends BaseClientController {
//
//    @FXML
//    public TextField playerName;
//    @FXML
//    public Label score;
//    @FXML
//    public Label round;
//    @FXML
//    public Label timeLeft;
//    @FXML
//    public TextField question;
//    @FXML
//    public TextField inputWord;
//    @FXML
//    public Button submitBtn;
//    @FXML
//    public ComboBox<String> direction;
//    @FXML
//    public GridPane gameGrid;
//
//    private User player;
//
//    private double points = 0.0;
//    private int currLevel = 1;
//    private int currQuestionID;
//    private int timeleft = 0;
//    private onActionResponse listener;
//    public interface onActionResponse {
//        void onCountDownTime(int remainTime);
//
//        void onStartGame();
//
//        void onReceiveQuestion(int id, String question);
//
//        void onAnswerResult(double point, boolean status);
//
//        void onEndGame(double stateResult) throws IOException;
//
//        void onGameEnd();
//    }
//
//    public void setUser(User user) {
//        this.player = user;
//    }
//
//    public void submitAnswer(ActionEvent e) {
//
//    }
//
//    public void postAnswer(int idQuestion, String answerInput, String questionType) {
//        JsonObject body = new JsonObject();
//        body.addProperty("id", idQuestion + "");
//        body.addProperty("answer", answerInput);
//        body.addProperty("timeStamp", "" + System.currentTimeMillis());
//        body.addProperty("type", questionType);
//        doJsonRequest(body, "/postAnswer");
//    }
//
//    public void postScore(double score) {
//        JsonObject body = new JsonObject();
//        body.addProperty("score", score);
//        doJsonRequest(body, "/postScore");
//    }
//
//    public void initView() {
//
//    }
//    public CrosswordGameScreenControllerfd(onActionResponse listener) throws IOException {
//        this.listener = listener;
//        callbackAction = new onAction() {
//            @Override
//            public void onSuccess(String data) throws IOException {
//
//                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
//                String route = rp.route;
//                if (route.equals("/postQuestion")) {
//                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
//                    onReceiveQuestion(parseData.get("id").getAsInt(), parseData.get("question").getAsString());
//                }
//                if (route.equals("/onGameFinish")) {
//                    onEndGame(gson.fromJson(rp.data, JsonObject.class).get("status").getAsDouble());
//                }
//                if (route.equals("/onAnswerReceive")) {
//                    JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
//                    boolean status = jsonObject.get("status").getAsBoolean();
//                    double point = jsonObject.get("point").getAsDouble();
//                    onAnswerResult(point, status);
//                }
//                if (route.equals("/onCountdown")) {
//                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
//                    onCountDownTime(parseData.get("timeLeft").getAsInt());
//                }
//                if (route.equals("/endGame")) {
//                    onGameEnd();
//                }
//            }
//
//
//            @Override
//            public void onFail() {
//
//            }
//
//            @Override
//            public void onError(String e) {
//
//            }
//        };
//        onStartLiveUpdate(getClass().getName());
//    }
//    public CrosswordGameScreenController() throws IOException {
//        initView();
//        callbackAction = new onAction() {
//            @Override
//            public void onSuccess(String data) throws IOException {
//
//                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
//                String route = rp.route;
//                if (route.equals("/postQuestion")) {
//                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
//                    onReceiveQuestion(parseData.get("id").getAsInt(), parseData.get("question").getAsString());
//                }
//                if (route.equals("/onGameFinish")) {
//                    onEndGame(gson.fromJson(rp.data, JsonObject.class).get("status").getAsDouble());
//                }
//                if (route.equals("/onAnswerReceive")) {
//                    JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
//                    boolean status = jsonObject.get("status").getAsBoolean();
//                    double point = jsonObject.get("point").getAsDouble();
//                    onAnswerResult(point, status);
//                }
//                if (route.equals("/onCountdown")) {
//                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
//                    onCountDownTime(parseData.get("timeLeft").getAsInt());
//                }
//                if (route.equals("/endGame")) {
//                    onGameEnd();
//                }
//            }
//
//
//            @Override
//            public void onFail() {
//
//            }
//
//            @Override
//            public void onError(String e) {
//
//            }
//        };
//        onStartLiveUpdate(getClass().getName());
//    }
//
//    private void onGameEnd() {
//        postScore(points);
//        JOptionPane.showMessageDialog(null, "Game kết thúc! Nhấn ok để xem kết quả");
//    }
//
//    private void onAnswerResult(double point, boolean status) {
//        if(status){
//            points += point;
//            this.score.setText("Điểm số: " + points);
//        }else{
//            points += point;
//            points = Math.min(0,points);
//            score.setText("Điểm số: " + points);
//            JOptionPane.showMessageDialog(null, "Bạn trả lời sai rồi!");
//        }
//    }
//
//    private void onCountDownTime(int timeLeft) {
//        this.timeLeft.setText("Thời gian: " + timeLeft + "s");
//    }
//
//    private void onEndGame(double status) throws IOException {
//        if (status == 0) {
//            JOptionPane.showMessageDialog(null, "Hoà rùi!");
//        } else if (status > 0.0) {
//            JOptionPane.showMessageDialog(null, "Winner");
//        } else if (status < 0) {
//            JOptionPane.showMessageDialog(null, "Looser");
//        }
//
//        new HomeScreen(player);
//        onCloseLiveUpdate(getClass().getName());
//    }
//
//    void closeController() {
//        onCloseLiveUpdate(getClass().getName());
//        callbackAction = null;
//    }
//
//    public void onReceiveQuestion(int id, String question) {
//        currQuestionID = id;
//        this.question.setText(question);
//        this.round.setText("Vòng " + currLevel);
//        currLevel++;
//    }
//
//}
