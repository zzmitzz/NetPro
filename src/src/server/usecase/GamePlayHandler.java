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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import src.model.Answer;
import src.model.Question;
import src.server.ClientHandler;
import src.server.Server;

import javax.swing.*;

/**
 * @author 1
 */
public class GamePlayHandler implements Runnable {
    private final ClientHandler player1;
    private final ClientHandler player2;
    private AtomicInteger questionNumber = new AtomicInteger(0);
    private final int totalQuestions = 10;
    private Timer timer;
    private double score1 = -1;
    private double score2 = -1;
    private onActionServer svUC;
    private Map<String, List<Question>> questions = new HashMap<>();

    public GamePlayHandler(ClientHandler p1, ClientHandler p2, onActionServer svUC) {
        this.player1 = p1;
        this.player2 = p2;
        this.svUC = svUC;
        player1.setGameController(this);
        player2.setGameController(this);

    }

    private void initQuestion() {
        getListUserAsync()
                .thenAccept(results -> {
                    questions.put("honghainhi", results);
                    startGame();
                })
                .exceptionally(error -> {
                    System.err.println("Error: " + error);
                    return null;
                });
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
        returnJson.addProperty("answer", question.answer);
        returnJson.addProperty("startChar", question.firstIndex);


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
        System.out.println(response.id + "&&" + questionNumber.get());
        if (response.id.equals((questionNumber.get()) + "")) {
            if (response.type.equalsIgnoreCase("Cột ngang")) {
                if (response.answer.equalsIgnoreCase(questions.get("honghainhi").get((questionNumber.get() - 1)).answer)) {
                    JsonObject player1Result = new JsonObject();
                    player1Result.addProperty("status", true);
                    player1Result.addProperty("point", 100);
                    player1Result.addProperty("answer",response.answer );
                    player1Result.addProperty("type", response.type);

                    a.sendMessage(player1Result.toString(), "/onAnswerReceive");

                    player1Result.addProperty("point", 0);
                    // Not
                    if(a.equals(player1)){
                        player2.sendMessage(player1Result.toString(), "/onAnswerReceive");
                    }else{
                        player1.sendMessage(player1Result.toString(), "/onAnswerReceive");
                    }
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
                    player1Result.addProperty("answer",response.answer );
                    player1Result.addProperty("type", response.type);
                    a.sendMessage(player1Result.toString(), "/onAnswerReceive");

                    player1Result.addProperty("point", 0);
                    // Not
                    if(a.equals(player1)){
                        player2.sendMessage(player1Result.toString(), "/onAnswerReceive");
                    }else{
                        player1.sendMessage(player1Result.toString(), "/onAnswerReceive");
                    }
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
    public CompletableFuture<List<Question>> getListUserAsync() {
        return CompletableFuture.supplyAsync(() -> {
            List<Question> questions = new ArrayList<>();
            try{
                questions = svUC.getQuestionByPack(1);
            } catch (Exception e) {
                throw new RuntimeException("Database query failed", e);
            }
            return questions;
        });
    }
    @Override
    public void run() {
        initQuestion();
    }
}
