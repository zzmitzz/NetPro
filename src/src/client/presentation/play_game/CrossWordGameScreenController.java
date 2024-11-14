package src.client.presentation.play_game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;
import src.client.data.dto.User;
import src.client.presentation.end_game.LoserScreenController;
import src.client.presentation.end_game.WinnerScreenController;
import src.client.presentation.home_screen.HomeScreenControllerFx;
import src.model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrossWordGameScreenController extends BaseClientController {

    @FXML
    private Label playerName;
    @FXML
    private Label score;
    @FXML
    private Label round;
    @FXML
    private TextField question;
    @FXML
    private TextField inputWord;
    @FXML
    private ComboBox<String> direction;
    @FXML
    private Button confirmButton;
    @FXML
    private Label timerLabel;
    private int currentQuestionID;
    @FXML
    private GridPane gameGrid;

    private int currentScore = 0;
    private int currentRound = 0;
    private int startSquare;
    private String correctAnswer;
    private Timeline countdown;
    private User user;
    private List<QuestionData> questions = new ArrayList<>();
    private double points = 0;
    @FXML
    public void initialize() throws IOException {
        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) throws IOException {

                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if (route.equals("/postQuestion")) {
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);

                    String ques = parseData.get("question").getAsString();
                    String ans = parseData.get("answer").getAsString();
                    int firstIndex = parseData.get("startChar").getAsInt();
                    Question newQuestion = new Question(ques,ans,firstIndex);
                    onReceiveQuestion(newQuestion);
                }
                if (route.equals("/onGameFinish")) {
                    onEndGame(gson.fromJson(rp.data, JsonObject.class).get("status").getAsDouble());
                }
                if (route.equals("/onAnswerReceive")) {
                    JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
                    boolean status = jsonObject.get("status").getAsBoolean();
                    double point = jsonObject.get("point").getAsDouble();
                    String answer;
                    try {
                        answer = jsonObject.get("answer").getAsString();
                    }catch (Exception e){
                        answer = "";
                    }
                    String type = jsonObject.get("type").getAsString();
                    onAnswerResult(point, status, answer, type);
                }
                if (route.equals("/onCountdown")) {
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
                    onCountDownTime(parseData.get("timeLeft").getAsInt());
                }
                if (route.equals("/endGame")) {
                    onGameEnd();
                }
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onError(String error) {

            }
        };
        onStartLiveUpdate(getClass().getName());
        initView();
    }

    private void onGameEnd() {
        showMessageDialog("Thông báo", "Game kết thúc! Nhấn ok để xem kết quả");
        postScore(points);
    }

    private void postScore(double points) {
        JsonObject body = new JsonObject();
        body.addProperty("score", points);
        doJsonRequest(body, "/postScore");
    }

    private void onAnswerResult(double point, boolean status, String answer, String type) {
        if(status){
            points += point;
            Platform.runLater(() -> {
                this.score.setText(""+points);
            });
            if(type.equalsIgnoreCase("Cột ngang")){
                displayAnswerInSquares(answer);
            }else{
                displayAnswerVertical(answer);
            }
        }else{
            points += point;
            points = Math.min(0,points);
            Platform.runLater(() -> {
                this.score.setText(""+points);
            });
            showMessageDialog("Hê hê", "Bạn trả lời sai rồi!");
        }
    }



    private void showMessageDialog(String title, String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    private void onCountDownTime(int timeLeft) {
        Platform.runLater(()->{
            timerLabel.setText(String.format("00:%02d",timeLeft));
        });
    }
    private void onReceiveQuestion(Question questionReceive) {
        displayQuestion(questionReceive);
        Platform.runLater(()-> {
            round.setText(String.valueOf(currentRound));
        });
    }

    public void setUserData(User user){
        this.user = user;
        playerName.setText(user.getFullName());
    }
    private void initView(){
        if (direction != null && direction.getItems().isEmpty()) {
            direction.getItems().addAll("Cột ngang", "Cột dọc");
        }
        highlightColumn();
    }

    private void displayQuestion(Question currentQuestion) {
        question.setText(currentQuestion.ques);
        startSquare = currentQuestion.firstIndex;
        correctAnswer = currentQuestion.answer;
        inputWord.clear();
        hideSquares(startSquare, correctAnswer.trim().length());
        currentRound++;
    }

    private void hideSquares(int startSquare, int length) {

        System.out.println(startSquare + " HIHI " + length);

        for (int i = 0; i < startSquare; i++) {
            hideSquare(i);
        }

        for (int i = startSquare + length; i < 10; i++) {
            hideSquare(i);
        }
    }



    public void handleConfirm(ActionEvent event) {
        String input = inputWord.getText();
        if(input.isEmpty()){
            showMessageDialog("Oh no!", "Bạn chưa điền câu trả lời kìaaa");
        }
        else if(direction.getValue() == null || direction.getValue().isEmpty()){
            showMessageDialog("Oh no!", "Bạn chưa điền loại câu hỏi kìaaa");
        }
        else{
            JsonObject body = new JsonObject();
            body.addProperty("id", currentRound + "");
            body.addProperty("answer", input);
            body.addProperty("timeStamp", "" + System.currentTimeMillis());
            body.addProperty("type", direction.getValue());
            doJsonRequest(body, "/postAnswer");
        }
    }

    private void displayAnswerInSquares(String answer) {
        // Display each character of the correct answer in its corresponding square
        for (int i = 0; i < answer.length() ; i++) {
            Label label = (Label) gameGrid.getChildren().get(startSquare + i + gameGrid.getColumnCount()*(currentRound-1));
            int index = i;
            Platform.runLater(()-> {
                label.setText(String.valueOf(answer.charAt(index)));
            });

        }
    }
    private void displayAnswerVertical(String answer) {
        for (int i = 0; i < gameGrid.getRowCount(); i++) {
            int verticalAnswerIndex = 6;
            Label label = (Label) gameGrid.getChildren().get(i * gameGrid.getColumnCount() + verticalAnswerIndex);
            int index = i;
            Platform.runLater(()-> {
                label.setText(String.valueOf(answer.charAt(index)));
            });
        }
    }
    private void onEndGame(double status) {
        if (status == 0) {
            showMessageDialog("Kết quả nè", "Hoà rùi!");
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../home_screen/HomeScreen.fxml"));
                    Parent root = loader.load();
                    HomeScreenControllerFx controller = loader.getController();
                    controller.setUserData(user);
                    Stage stage = (Stage) gameGrid.getScene().getWindow();
                    // Set the new scene
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else if (status > 0.0) {
            String fxmlFile = "../end_game/WinnerScreen.fxml";
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent root = loader.load();
                    WinnerScreenController controller = loader.getController();
                    controller.setUserData(user);
                    controller.lastPoint = points;
                    Stage stage = (Stage) gameGrid.getScene().getWindow();
                    // Set the new scene
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else if (status < 0) {
            String fxmlFile = "../end_game/LoserScreen.fxml";
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent root = loader.load();
                    LoserScreenController controller = loader.getController();
                    controller.setUserData(user);
                    controller.lastPoint = points;
                    Stage stage = (Stage) gameGrid.getScene().getWindow();
                    // Set the new scene
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


        onCloseLiveUpdate(this.getClass().getName());
    }

    private void hideSquare(int square) {
        System.out.println("Delete" + (square + gameGrid.getColumnCount()*currentRound));

        Label label = (Label) gameGrid.getChildren().get(square + gameGrid.getColumnCount()*currentRound);
        label.setVisible(false);
    }

    private void highlightColumn() {
        for (int i = 0; i < gameGrid.getRowCount(); i++) {
            int verticalAnswerIndex = 6;
            Label label = (Label) gameGrid.getChildren().get(i * gameGrid.getColumnCount() + verticalAnswerIndex);
            label.setStyle("-fx-background-color: #64B5F6; -fx-border-color: black; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-pref-width: 40; -fx-pref-height: 40;");
        }
    }

}

class QuestionData {
    private final String question;
    private final String answer;
    private final int startSquare;

    public QuestionData(String question, String answer, int startSquare) {
        this.question = question;
        this.answer = answer;
        this.startSquare = startSquare;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getStartSquare() {
        return startSquare;
    }
}
