package src.client.presentation.play_game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ResponseWrapper;
import src.client.common.BaseClientController;
import src.client.common.onAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrossWordGameScreenController extends BaseClientController {

    @FXML
    private TextField playerName;
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

    @FXML
    private GridPane gameGrid;

    private int currentScore = 0;
    private int currentRound = 0;
    private int startSquare;
    private String correctAnswer;
    private Timeline countdown;

    private List<QuestionData> questions = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {
        callbackAction = new onAction() {
            @Override
            public void onSuccess(String data) throws IOException {

                ResponseWrapper rp = gson.fromJson(JsonParser.parseString(data), ResponseWrapper.class);
                String route = rp.route;
                if (route.equals("/postQuestion")) {
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
//                    onReceiveQuestion(parseData.get("id").getAsInt(), parseData.get("question").getAsString());
                }
                if (route.equals("/onGameFinish")) {
//                    onEndGame(gson.fromJson(rp.data, JsonObject.class).get("status").getAsDouble());
                }
                if (route.equals("/onAnswerReceive")) {
                    JsonObject jsonObject = JsonParser.parseString(rp.data).getAsJsonObject();
                    boolean status = jsonObject.get("status").getAsBoolean();
                    double point = jsonObject.get("point").getAsDouble();
//                    onAnswerResult(point, status);
                }
                if (route.equals("/onCountdown")) {
                    JsonObject parseData = gson.fromJson(rp.data, JsonObject.class);
//                    onCountDownTime(parseData.get("timeLeft").getAsInt());
                }
                if (route.equals("/endGame")) {
//                    onGameEnd();
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

    private void initView(){
        if (direction != null && direction.getItems().isEmpty()) {
            direction.getItems().addAll("Cột ngang", "Cột dọc");
        }
        confirmButton.setOnAction(this::handleConfirm);

        highlightColumn();
        displayQuestion();
    }
    public void loadQuestions() {
        questions.add(new QuestionData("Hai số đối nhau có giá trị tuyệt đối … ?", "bangnhau", 1));
        questions.add(new QuestionData("Ở Hà Nội, phố nào nối phố Đồng Xuân và phố Hàng Ngang, nổi tiếng với ô mai?", "hangluoc", 0));
        questions.add(new QuestionData("Triều đại nào ở Việt Nam từ năm 1802 đến năm 1945?", "nguyen", 1));
        questions.add(new QuestionData("Vật dụng nào thường được treo ở cửa để báo hiệu có người đến?", "chuong", 1));
        questions.add(new QuestionData("Ở các tế bào nhân thực, quá trình hô hấp tế bào diễn ra chủ yếu trong …", "tithe", 3));
        questions.add(new QuestionData("Loài nào sau đây sống ở môi trường rừng ngập mặn: sú, vẹt, đước, bần?", "tatca", 2));
        questions.add(new QuestionData("Cảng hàng không quốc tế Long Thành được xây dựng trên địa bàn tỉnh nào?", "dongnai", 0));
        questions.add(new QuestionData("Màu gì thường được liên tưởng đến cỏ cây, lá, trời quang mây tạnh?", "xanh", 4));
        questions.add(new QuestionData("Loài chim nào nổi tiếng với màu lông hồng đặc trưng, được tạo ra từ thức ăn của chúng?", "honghac", 2));
        questions.add(new QuestionData("Vật dụng nhỏ, nhọn, thường làm bằng sắt, dùng để đóng vào gỗ?", "dinh", 5));

    }

    /**
     * Init View for each question
     */
    private void displayQuestion() {
        // Lấy câu hỏi hiện tại và hiển thị
        QuestionData currentQuestion = questions.get(currentRound);
        question.setText(currentQuestion.getQuestion());
        startSquare = currentQuestion.getStartSquare();
        correctAnswer = currentQuestion.getAnswer();
        inputWord.clear();
        hideSquares(startSquare, correctAnswer.length());
    }

    private void hideSquares(int startSquare, int length) {
        // Ẩn các ô trước ô bắt đầu
        for (int i = 0; i < startSquare; i++) {
            hideSquare(i);
        }

        // Ẩn các ô sau ô cuối cùng của đáp án
        for (int i = startSquare + length; i < 10; i++) {
            hideSquare(i);
        }
    }



    public void handleConfirm(ActionEvent event) {
        String word = inputWord.getText();
        String selectedDirection = direction.getSelectionModel().getSelectedItem();

        if ("Cột ngang".equals(selectedDirection) && word.equalsIgnoreCase(correctAnswer)) {
            currentScore += 10;
            score.setText(String.valueOf(currentScore));

            displayAnswerInSquares();

            if (currentRound < questions.size() - 1) {
                currentRound++;
                round.setText(String.valueOf(currentRound + 1));
                displayQuestion();
            } else {
                endGame(false);
            }
        } else if ("Cột dọc".equals(selectedDirection) && word.equalsIgnoreCase("honghainhi")) {
            currentScore = 100;
            score.setText(String.valueOf(currentScore));
            displayAnswerInSquares(); // Display correct answer in squares for vertical direction
            endGame(true);
        }
    }

    private void displayAnswerInSquares() {
        // Display each character of the correct answer in its corresponding square
        for (int i = 0; i < correctAnswer.length() ; i++) {

            Label label = (Label) gameGrid.getChildren().get(startSquare + i + gameGrid.getColumnCount()*currentRound);
            label.setText(String.valueOf(correctAnswer.charAt(i)));
        }
    }

    private void endGame(boolean isWin) {
        if (countdown != null) {
            countdown.stop();
        }
        String fxmlFile = isWin || currentScore == 100 ? "/presentation/end_game/WinnerScreen.fxml" : "/presentation/end_game/LoserScreen.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Tạo một Stage mới và đặt Scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(isWin ? "Congratulations!" : "Game Over");

            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) timerLabel.getScene().getWindow();
            currentStage.close();

            // Hiển thị màn hình mới
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi tải file FXML: " + fxmlFile);
        }
    }

    private void hideSquare(int square) {
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
