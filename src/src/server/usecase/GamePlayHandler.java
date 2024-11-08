/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.server.usecase;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import src.model.Answer;
import src.model.Question;
import src.server.ClientHandler;

import javax.swing.*;

/**
 * @author 1
 */
public class GamePlayHandler implements Runnable {
    private final ClientHandler player1;
    private final ClientHandler player2;
    //    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final BlockingQueue<Answer> responseQueue = new LinkedBlockingQueue<>();
    private AtomicInteger questionNumber = new AtomicInteger(0);
    private final int totalQuestions = 10;
    private Timer timer;
    private double score1 = -1;
    private double score2 = -1;

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
                "Hai số đối nhau có giá trị tuyệt đối … ",
                "Bangnhau",
                6 - "Bangnhau".indexOf("h")
        );

        Question q2 = new Question(
                "Ở Hà Nội, phố nào nối phố Đồng Xuân và phố Hàng Ngang, nổi tiếng với ô mai?",
                "Hangluoc",
                6 - "Hangluoc".indexOf("o")
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
                "Loài nào sau đây sống ở môi trường rừng ngập mặn: sú, vẹt, đước, bần, tất cả?",
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
        if (questionNumber.get() >= totalQuestions) {
            preEndGame();
            timer.stop();
            timer = null;
            return;
        }

        Question question = questions.get("honghainhi").get(questionNumber.get());
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        timer = new Timer(1000, new ActionListener() {
            int timeLeft = 30; // Starting time

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    JsonObject returnJson = new JsonObject();
                    returnJson.addProperty("timeLeft", timeLeft);
                    timeLeft--;
                    player1.sendMessage(returnJson.toString(), "/onCountdown");
                    player2.sendMessage(returnJson.toString(), "/onCountdown");
                }

                if (timeLeft == 0) {
                    GamePlayHandler.this.sendNextQuestion();
                }
            }
        });
        timer.start();
        
        JsonObject returnJson = new JsonObject();
        returnJson.addProperty("question", question.ques);
        returnJson.addProperty("id", questionNumber);


        player1.sendMessage(returnJson.toString(), "/postQuestion");
        player2.sendMessage(returnJson.toString(), "/postQuestion");

        // Schedule next question in 10 seconds, if there are more questions
        questionNumber.set(questionNumber.get() + 1);
    }

    public void receiveScore(ClientHandler a, double score){
        System.out.println(score);
        if(a.equals(player1)){
            score1 = score;
        }
        if(a.equals(player2)){
            score2 = score;
        }
        if(score1 != -1 && score2 != -1){
            onGameFinish();
        }
    }

    public void receiveAnswer(ClientHandler a, Answer response) {
        if (response.id.equals((questionNumber.get() - 1) + "")) {
            if (response.type.equalsIgnoreCase("Cột ngang")) {
                if (response.answer.equalsIgnoreCase(questions.get("honghainhi").get((questionNumber.get() - 1)).answer)) {
                    JsonObject player1Result = new JsonObject();
                    player1Result.addProperty("status", true);
                    player1Result.addProperty("point", 100);
                    a.sendMessage(player1Result.toString(), "/onAnswerReceive");
                    sendNextQuestion();
                } else {
                    JsonObject player1Result = new JsonObject();
                    player1Result.addProperty("status", false);
                    player1Result.addProperty("point", -40);
                    a.sendMessage(player1Result.toString(), "/onAnswerReceive");
                }

            } else {
                if(questions.containsKey(response.answer.toLowerCase().trim())){
                    JsonObject player1Result = new JsonObject();
                    player1Result.addProperty("status", true);
                    player1Result.addProperty("point", 500);
                    a.sendMessage(player1Result.toString(), "/onAnswerReceive");
                    questionNumber.set(11);
                    sendNextQuestion();
                }
            }
        }
    }

    private void preEndGame() {
        JsonObject player1Result = new JsonObject();
        player1Result.addProperty("ending", true);
        JsonObject player2Result = new JsonObject();
        player2Result.addProperty("ending", true);
        player1.sendMessage(player1Result.toString(), "/endGame");
        player2.sendMessage(player2Result.toString(), "/endGame");
    }

    private void onGameFinish(){
        JsonObject player1Result = new JsonObject();
        JsonObject player2Result = new JsonObject();
        if(score1 == score2){
            player1Result.addProperty("status", 0);
            player2Result.addProperty("status", 0);
        }else{
            player1Result.addProperty("status", score1-score2);
            player2Result.addProperty("status", score2-score1);
        }
        player1.sendMessage(player1Result.toString(), "/onGameFinish");
        player2.sendMessage(player2Result.toString(), "/onGameFinish");
    }

    @Override
    public void run() {
        startGame();
    }
}
