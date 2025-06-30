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
                {"What is Mymensingh famous for in agriculture?",
                        "Tea production", "Jute production", "Rice production", "Wheat production",
                        "2"},

                {"Which river flows beside Mymensingh city?",
                        "Padma", "Jamuna", "Brahmaputra", "Meghna",
                        "3"},

                {"What is the traditional folk song of Mymensingh called?",
                        "Bhatiali", "Baul", "Murshidi", "Bhawaiya",
                        "4"},

                {"Which famous university is located in Mymensingh?",
                        "Dhaka University", "Bangladesh Agricultural University", "Jahangirnagar University", "Rajshahi University",
                        "2"},

                {"What is the name of the famous park in Mymensingh?",
                        "Ramna Park", "Shahid Zia Park", "Botanical Garden", "Amrtokunda Park",
                        "4"},

                {"Which traditional craft is Mymensingh known for?",
                        "Nakshi Kantha", "Shital Pati", "Pottery", "Bamboo crafts",
                        "1"},

                {"What is the approximate distance of Mymensingh from Dhaka?",
                        "50 km", "100 km", "120 km", "200 km",
                        "3"},

                {"Which famous poet was born in Mymensingh?",
                        "Kazi Nazrul Islam", "Rabindranath Tagore", "Jasimuddin", "Shamsur Rahman",
                        "3"},

                {"What is the main festival celebrated in Mymensingh?",
                        "Pohela Boishakh", "Nouka Baich", "Rash Mela", "Bhawaiya Festival",
                        "4"},

                {"Which famous museum is located in Mymensingh?",
                        "Liberation War Museum", "Zainul Abedin Museum", "Bangabandhu Museum", "Ethnological Museum",
                        "2"},

                {"What is the traditional food of Mymensingh?",
                        "Morog Polao", "Kachchi Biryani", "Monda", "Bhakor Vaja",
                        "4"},

                {"Which wildlife sanctuary is near Mymensingh?",
                        "Sundarbans", "Lawachara", "Bhawal National Park", "Madhupur National Park",
                        "4"},

                {"What is the main economic activity in Mymensingh?",
                        "Fishing", "Agriculture", "Handicrafts", "Tourism",
                        "2"},

                {"Which famous folk theater originated in Mymensingh?",
                        "Jatra", "Pala Gan", "Kabigan", "Bhawaiya Gan",
                        "3"},

                {"What is the climate of Mymensingh like?",
                        "Desert climate", "Tropical monsoon", "Temperate", "Mediterranean",
                        "2"},

                {"Which famous freedom fighter was from Mymensingh?",
                        "Sheikh Mujibur Rahman", "Tajuddin Ahmad", "Abdul Hamid Khan Bhashani", "Captain Mohiuddin Jahangir",
                        "4"},

                {"What is the traditional dance of Mymensingh called?",
                        "Manipuri", "Kathak", "Bhawaiya Nritya", "Lathi Khela",
                        "3"},

                {"Which famous agricultural research center is in Mymensingh?",
                        "BRRI", "BARI", "BLRI", "BINA",
                        "1"},

                {"What is the famous sweet of Mymensingh?",
                        "Rasgulla", "Sandesh", "Monda", "Chomchom",
                        "3"},

                {"Which historical monument is in Mymensingh?",
                        "Lalbagh Fort", "Ahsan Manzil", "Shashi Lodge", "Kantajew Temple",
                        "3"}
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