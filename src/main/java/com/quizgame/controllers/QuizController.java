package com.quizgame.controllers;

import com.quizgame.models.Database;
import com.quizgame.models.Question;
import com.quizgame.models.QuizResult;
import com.sun.javafx.charts.Legend;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizController {
    @FXML private TextField playerNameField;
    @FXML private Button startButton;
    @FXML private Button submitButton;
    @FXML private Button resetButton;
    @FXML private Button historyButton;
    @FXML private VBox quizContainer;
    @FXML private Label resultLabel;
    @FXML private VBox mainContainer;
    @FXML private Button playAgainButton;
    private List<Question> questions;
    private List<ToggleGroup> toggleGroups = new ArrayList<>();

    @FXML
    public void initialize() {
        // Initial state
        submitButton.setDisable(true);
        playAgainButton.setDisable(true);

    }

    @FXML
    private void handleHistoryButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quizgame/views/history.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) historyButton.getScene().getWindow();
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
            showAlert("Error", "Failed to load history view");
        }
    }
    @FXML
    private void handleStartButton() {
        if (playerNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your name");
            return;
        }

        try {
            questions = Database.getRandomQuestions(5);
            displayQuestions();

            // Update UI state
            playerNameField.setDisable(true);
            startButton.setDisable(true);
            submitButton.setDisable(false);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load questions");
        }
    }

    @FXML
    private void handleSubmitButton() {
        // Calculate and show score
        int score = calculateScore();
        String playerName = playerNameField.getText();
        resultLabel.setText(playerName + ", your score: " + score + "/5");

        try {
            Database.saveQuizResult(playerName, score);
        } catch (SQLException e) {
            showAlert("Error", "Failed to save score");
        }

        // Update UI state
        submitButton.setDisable(true);
        playAgainButton.setDisable(false);
    }

    @FXML
    private void handlePlayAgainButton() {
        // Reset quiz
        quizContainer.getChildren().clear();
        toggleGroups.clear();
        resultLabel.setText("");

        // Enable name and start button
        playerNameField.setDisable(false);
        startButton.setDisable(false);

        // Disable submit and play again
        submitButton.setDisable(true);
        playAgainButton.setDisable(true);
    }

    @FXML
    private void handleResetButton() {
        for (ToggleGroup group : toggleGroups) {
            group.selectToggle(null);
        }
        resultLabel.setText("");
        submitButton.setDisable(false);
        playAgainButton.setDisable(true); // Keep disabled until submission
    }

    private void displayQuestions() {
        quizContainer.getChildren().clear();
        toggleGroups.clear();

        for (Question question : questions) {
            Label questionLabel = new Label(question.getQuestionText());
            questionLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 5 0;");

            ToggleGroup group = new ToggleGroup();
            toggleGroups.add(group);

            RadioButton option1 = createRadioButton(question.getOption1(), group, 1);
            RadioButton option2 = createRadioButton(question.getOption2(), group, 2);
            RadioButton option3 = createRadioButton(question.getOption3(), group, 3);
            RadioButton option4 = createRadioButton(question.getOption4(), group, 4);

            VBox questionBox = new VBox(5, questionLabel, option1, option2, option3, option4);
            quizContainer.getChildren().add(questionBox);
        }
    }

    private RadioButton createRadioButton(String text, ToggleGroup group, int value) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setUserData(value);
        return radioButton;
    }

    private int calculateScore() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            ToggleGroup group = toggleGroups.get(i);
            if (group.getSelectedToggle() != null) {
                int selectedAnswer = (int) group.getSelectedToggle().getUserData();
                if (selectedAnswer == questions.get(i).getCorrectAnswer()) {
                    score++;
                }
            }
        }
        return score;
    }

    private void loadQuizHistory() {
        try {
            List<QuizResult> history = Database.getQuizHistory();
            ObservableList<String> items = FXCollections.observableArrayList();

            for (QuizResult result : history) {
                items.add(result.getPlayerName() + " - " + result.getScore() + " points (" + result.getDate() + ")");
            }

//            ListView<String> historyListView = null;
//            historyListView.setItems(items);
        } catch (SQLException e) {
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