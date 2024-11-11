package src.client.presentation.play_game;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrosswordGameScreenController {

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
    private int verticalAnswerIndex = 6;
    @FXML
    public void initialize() {
        if (direction != null && direction.getItems().isEmpty()) {
            direction.getItems().addAll("Cột ngang", "Cột dọc");
        }

        confirmButton.setOnAction(this::handleConfirm);

        // Load questions
        loadQuestions();

        // Initialize and start the countdown timer
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int currentTime = Integer.parseInt(timerLabel.getText());
            if (currentTime > 0) {
                timerLabel.setText(String.valueOf(currentTime - 1));
            } else {
                handleTimeout(); // Auto-confirm when time runs out
            }
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);

        highlightColumn();
        displayQuestion();
//        resetTimer();
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

    private void displayQuestion() {
        // Lấy câu hỏi hiện tại và hiển thị
        QuestionData currentQuestion = questions.get(currentRound);
        question.setText(currentQuestion.getQuestion());
        startSquare = currentQuestion.getStartSquare();
        correctAnswer = currentQuestion.getAnswer();

        // Reset lại inputWord
        inputWord.clear();
//        System.out.println("startSquare " + startSquare);
//        System.out.println("correctAnswer " + correctAnswer.length());
//        System.out.println("currentRound " + currentRound);
        // Ẩn các ô trước đó và hiển thị ô cho câu trả lời hiện tại
        hideSquares(startSquare, correctAnswer.length());
        resetTimer();
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

            displayAnswerInSquares(); // Display correct answer in squares

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
//            System.out.println(startSquare + i + gameGrid.getColumnCount()*1 );
            label.setText(String.valueOf(correctAnswer.charAt(i)));
//            label.setStyle("-fx-background-color: #4CAF50; -fx-border-color: black; -fx-pref-width: 40; -fx-pref-height: 40;");

        }
    }


    private void resetTimer() {
        if (countdown != null) {
            countdown.stop();
        }
        timerLabel.setText("20");
        countdown.playFromStart();
    }

    private void endGame(boolean isWin) {
        if (countdown != null) {
            countdown.stop();
        }
//        if (isWin || currentScore == 100) {
//            System.out.println("Chuyển sang màn hình chiến thắng!");
//            // Chuyển sang màn hình WinnerScreen.fxml
//
//        } else {
//            System.out.println("Chuyển sang màn hình thua cuộc.");
//            // Chuyển sang màn hình LoserScreen.fxml
//        }
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

//    private void showSquare(int square) {
//        Label label = (Label) gameGrid.getChildren().get(square);
//        label.setStyle("-fx-background-color: #4CAF50; -fx-border-color: black; -fx-pref-width: 40; -fx-pref-height: 40;");
//    }

    private void highlightColumn() {
        for (int i = 0; i < gameGrid.getRowCount(); i++) {
            Label label = (Label) gameGrid.getChildren().get(i * gameGrid.getColumnCount() + verticalAnswerIndex);
//            label.setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-pref-width: 40; -fx-pref-height: 40;");
            label.setStyle("-fx-background-color: #64B5F6; -fx-border-color: black; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white; -fx-pref-width: 40; -fx-pref-height: 40;");
        }
    }

    private void handleTimeout() {
        displayAnswerInSquares();
        // Chuyển câu hỏi khi hết giờ
        if (currentRound < questions.size() - 1) {
            currentRound++;
            round.setText(String.valueOf(currentRound + 1));
            displayQuestion();
        } else {
            endGame(false); // Kết thúc trò chơi nếu hết câu hỏi
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
