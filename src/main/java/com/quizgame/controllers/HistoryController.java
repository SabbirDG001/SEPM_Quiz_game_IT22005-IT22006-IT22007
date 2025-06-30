package com.quizgame.controllers;

import com.quizgame.models.Database;
import com.quizgame.models.QuizResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HistoryController {
    @FXML private ListView<String> historyListView;

    @FXML
    public void initialize() {
        loadQuizHistory();
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quizgame/views/quiz.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) historyListView.getScene().getWindow();
            // Store current window size and position
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            double currentX = stage.getX();
            double currentY = stage.getY();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Restore window size and position
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
            stage.setX(currentX);
            stage.setY(currentY);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to quiz view");
        }
    }

    private void loadQuizHistory() {
        try {
            List<QuizResult> history = Database.getQuizHistory();
            ObservableList<String> items = FXCollections.observableArrayList();

            for (QuizResult result : history) {
                items.add(String.format("%s - %d points (%s)",
                        result.getPlayerName(),
                        result.getScore(),
                        result.getDate().toString()));
            }

            historyListView.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load history: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}