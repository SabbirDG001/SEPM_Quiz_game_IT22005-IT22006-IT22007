package com.quizgame.utils;

import com.quizgame.models.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        // Check if questions already exist
        if (questionsExist()) {
            return;
        }

        // Sample questions
        String[][] questions = {
                {"What is the capital of France?", "London", "Paris", "Berlin", "Madrid", "2"},
                {"Which planet is known as the Red Planet?", "Venus", "Mars", "Jupiter", "Saturn", "2"},
                {"What is the largest mammal?", "Elephant", "Blue Whale", "Giraffe", "Hippopotamus", "2"},
                {"In which year did World War II end?", "1943", "1945", "1947", "1950", "2"},
                {"Who painted the Mona Lisa?", "Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Michelangelo", "3"},
                {"What is the chemical symbol for gold?", "Go", "Gd", "Au", "Ag", "3"},
                {"Which country is home to the kangaroo?", "South Africa", "Brazil", "Australia", "New Zealand", "3"},
                {"What is the largest ocean on Earth?", "Atlantic", "Indian", "Pacific", "Arctic", "3"},
                {"Who wrote 'Romeo and Juliet'?", "Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain", "2"},
                {"What is the square root of 64?", "4", "6", "8", "10", "3"},
                {"Which element has the atomic number 1?", "Helium", "Hydrogen", "Lithium", "Oxygen", "2"},
                {"In computing, what does CPU stand for?", "Central Processing Unit", "Computer Processing Unit", "Central Process Unit", "Computer Process Unit", "1"},
                {"What is the main component of the Sun?", "Liquid Lava", "Hydrogen Gas", "Molten Iron", "Rock", "2"},
                {"Which country is the largest by area?", "China", "United States", "Canada", "Russia", "4"},
                {"What is the currency of Japan?", "Won", "Yen", "Yuan", "Dollar", "2"},
                {"How many continents are there?", "5", "6", "7", "8", "3"},
                {"Which language is spoken in Brazil?", "Spanish", "Portuguese", "French", "English", "2"},
                {"What is the hardest natural substance on Earth?", "Gold", "Iron", "Diamond", "Platinum", "3"},
                {"Who discovered gravity?", "Albert Einstein", "Isaac Newton", "Galileo Galilei", "Nikola Tesla", "2"},
                {"What is the largest desert in the world?", "Sahara", "Arabian", "Gobi", "Antarctic", "4"}
        };

        // Insert questions into database
        String query = "INSERT INTO questions (question_text, option1, option2, option3, option4, correct_answer) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (String[] question : questions) {
                stmt.setString(1, question[0]);
                stmt.setString(2, question[1]);
                stmt.setString(3, question[2]);
                stmt.setString(4, question[3]);
                stmt.setString(5, question[4]);
                stmt.setInt(6, Integer.parseInt(question[5]));
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean questionsExist() {
        String query = "SELECT COUNT(*) FROM questions";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}