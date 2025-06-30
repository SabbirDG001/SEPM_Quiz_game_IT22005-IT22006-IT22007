package com.quizgame.models;

import java.time.LocalDateTime;

public class QuizResult {
    private String playerName;
    private int score;
    private LocalDateTime date;

    public QuizResult(String playerName, int score, LocalDateTime date) {
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public LocalDateTime getDate() { return date; }
}