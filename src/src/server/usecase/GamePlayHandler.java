/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.server.usecase;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import src.model.Answer;
import src.model.Question;
import src.server.ClientHandler;

/**
 * @author 1
 */
public class GamePlayHandler<T> implements Runnable {
    private final ClientHandler player1;
    private final ClientHandler player2;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final BlockingQueue<Answer> responseQueue = new LinkedBlockingQueue<>();
    private int questionNumber = 1;
    private final int totalQuestions = 10;


    private Map<String, List<Question>> questions = new HashMap<>();

    public GamePlayHandler(ClientHandler p1, ClientHandler p2) {
        this.player1 = p1;
        this.player2 = p2;
        player1.setGameController(this);
        player2.setGameController(this);
        initQuestion();
    }

    private void initQuestion() {
        Question q1 = new Question(
                "Hai số đối nhau có giá trị tuyệt đối … bằng nhau",
                "bằng nhau",
                6 - "bằng nhau".indexOf("h")
        );

        Question q2 = new Question(
                "Ở Hà Nội, phố nào nối phố Đồng Xuân và phố Hàng Ngang, nổi tiếng với ô mai?",
                "Hàng Lược",
                6 - "Hàng Lược".indexOf("o")
        );

        Question q3 = new Question(
                "Which Vietnamese dynasty lasted from 1802 to 1945?",
                "Nguyen",
                6 - "Nguyen".indexOf("n")
        );

        Question q4 = new Question(
                "Những số tự nhiên lớn hơn 1, chỉ có hai ước số là 1 và chính nó, được gọi chung là gì?",
                "Songuyento",
                6 - "Songuyento".indexOf("g")
        );

        Question q5 = new Question(
                "Ở các tế bào nhân thực, quá trình hô hấp tế bào diễn ra chủ yếu trong …",
                "tithe",
                6 - "tithe".indexOf("h")
        );

        Question q6 = new Question(
                "Loài nào sau đây sống ở môi trường rừng ngập mặn: sú, vẹt, đước, bần?",
                "Tatca",
                6 - "Tatca".indexOf("a")
        );

        Question q7 = new Question(
                "Cảng hàng không quốc tế Long Thành được xây dựng trên địa bàn tỉnh nào?",
                "Dongnai",
                6 - "Dongnai".indexOf("i")
        );

        Question q8 = new Question(
                "Tạp chất boron làm cho kim cương có màu gì?",
                "Xanhlam",
                6 - "Xanhlam".indexOf("n")
        );

        Question q9 = new Question(
                "Truyền thuyết 'Sự tích đầm Mực' nhắc đến nhân vật lịch sử nào?",
                "Chuvanan",
                6 - "Chuvanan".indexOf("h")
        );

        Question q10 = new Question(
                "Vị vua nào đã lãnh đạo cuộc kháng chiến chống Mông - Nguyên lần thứ nhất của nhân dân Đại Việt?",
                "Tranthaitong",
                6 - "Tranthaitong".indexOf("i")
        );
        ArrayList<Question> listQuestion = new ArrayList<>();
        listQuestion.add(q1);
        listQuestion.add(q2);
        listQuestion.add(q3);
        listQuestion.add(q4);
        listQuestion.add(q5);
        listQuestion.add(q6);
        listQuestion.add(q7);
        listQuestion.add(q8);
        listQuestion.add(q9);
        listQuestion.add(q10);

        questions.put("honghainhi", listQuestion);
    }

    public void startGame() {
        JsonObject returnJson = new JsonObject();
        returnJson.addProperty("status", true);
        returnJson.addProperty("message", "Let play");
        player1.sendMessage(returnJson.toString(), "/playGameUser");
        player2.sendMessage(returnJson.toString(), "/playGameUser");
        sendNextQuestion();
    }

    private void sendNextQuestion() {
        if (questionNumber >= totalQuestions) {
            endGame();
            return;
        }
        Question question = questions.get("honghainhi").get(questionNumber);


        JsonObject returnJson = new JsonObject();
        returnJson.addProperty("question", question.ques);
        returnJson.addProperty("id", questionNumber);


        player1.sendMessage(returnJson.toString(), "/postQuestion");
        player2.sendMessage(returnJson.toString(), "/postQuestion");

        // Schedule next question in 10 seconds, if there are more questions
        scheduler.schedule(this::sendNextQuestion, 30, TimeUnit.SECONDS);
        questionNumber++;
    }

    public void receiveAnswer(ClientHandler a, Answer response){
        responseQueue.offer(response);
        try{
            if(!responseQueue.isEmpty()){
                Answer ans = responseQueue.take();
                if(a.equals(player1)){

                }
                if(a.equals(player2)){

                }
            }
        }catch (Exception e){

        }

    }

    private void endGame() {
        JsonObject player1Result = new JsonObject();
        player1Result.addProperty("status", true);
        JsonObject player2Result = new JsonObject();
        player2Result.addProperty("status", true);
        player1.sendMessage(player1Result.toString(), "/endGame");
        player2.sendMessage(player2Result.toString(), "/endGame");
        scheduler.shutdown();
    }

    @Override
    public void run() {
        startGame();
    }
}
