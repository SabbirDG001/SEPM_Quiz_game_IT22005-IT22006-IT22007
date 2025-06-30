package com.quizgame.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_game";
    private static final String USER = "root";
    private static final String PASSWORD = "dg001"; // Change to your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Question> getRandomQuestions(int count) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions ORDER BY RAND() LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, count);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Question question = new Question(
                        rs.getInt("id"),
                        rs.getString("question_text"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getInt("correct_answer")
                );
                questions.add(question);
            }
        }
        return questions;
    }

    public static void saveQuizResult(String playerName, int score) throws SQLException {
        String query = "INSERT INTO quiz_results (player_name, score) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        }
    }

    public static List<QuizResult> getQuizHistory() throws SQLException {
        List<QuizResult> history = new ArrayList<>();
        String query = "SELECT player_name, score, date FROM quiz_results ORDER BY date DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                QuizResult result = new QuizResult(
                        rs.getString("player_name"),
                        rs.getInt("score"),
                        rs.getTimestamp("date").toLocalDateTime()
                );
                history.add(result);
            }
        }
        return history;
    }
}