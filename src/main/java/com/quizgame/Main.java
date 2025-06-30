package com.quizgame;

import com.quizgame.utils.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database with sample questions
        DatabaseInitializer.initializeDatabase();

        // Load the main quiz view
        Parent root = FXMLLoader.load(getClass().getResource("/com/quizgame/views/quiz.fxml"));
        primaryStage.setTitle("Quiz Game");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}