package com.example.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private Stage window;
    private List<Button> taskButtons;
    private AtomicInteger score;
    private GridPane grid;
    private Label itemLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Task Manager");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        grid = new GridPane();
        grid.setVgap(10);
        grid.setId("grid");


        TextField taskNameTextField = new TextField();
        grid.add(taskNameTextField, 0, 0);


        Button createTaskButton = new Button("Create Task");
        createTaskButton.setOnAction(e -> createTask(taskNameTextField.getText()));
        grid.add(createTaskButton, 1, 0);

        score = new AtomicInteger(0);
        Text textScore = new Text("Score: " + score.get());
        textScore.setId("textScore");
        grid.add(textScore, 0, 3);
        BorderPane.setAlignment(textScore, Pos.CENTER);


        taskButtons = new ArrayList<>();


        Button shopButton = new Button("Shop");
        shopButton.setOnAction(e -> openShop());
        HBox shopButtonBox = new HBox(10);
        shopButtonBox.setAlignment(Pos.CENTER);
        shopButtonBox.getChildren().add(shopButton);
        grid.add(shopButtonBox, 0, 4, 2, 1);


        itemLabel = new Label("Items: ");
        grid.add(itemLabel, 0, 5, 2, 1);

        borderPane.setCenter(grid);

        Scene scene = new Scene(borderPane, 300, 250);
        window.setScene(scene);

        window.show();
    }

    private void createTask(String taskName) {


        Random random = new Random();
        int taskId = random.nextInt(1000);
        Button taskButton = new Button(taskName);
        taskButton.setOnAction(e -> openTaskOptions(taskButton,taskId));


        int row = taskButtons.size() + 1;
        taskButtons.add(taskButton);
        GridPane.setConstraints(taskButton, 0, row);
        GridPane.setColumnSpan(taskButton, 2);

        grid.getChildren().add(taskButton);

        TextField taskNameTextField = (TextField) grid.getChildren().get(0);
        taskNameTextField.clear();
    }

    private void openTaskOptions(Button taskButton, int taskId) {

        VBox optionsBox = new VBox(10);
        optionsBox.setPadding(new Insets(20));

        Button validateButton = new Button("Validate");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        validateButton.setOnAction(e -> {
            score.incrementAndGet();
            taskButtons.remove(taskButton);
            grid.getChildren().remove(taskButton);
            updateScore();
            window.setScene(window.getScene());
            window.requestFocus();

            Stage optionsWindow = (Stage) validateButton.getScene().getWindow();
            optionsWindow.close();
        });

        optionsBox.getChildren().addAll(validateButton, editButton, deleteButton);

        Scene optionsScene = new Scene(optionsBox, 200, 150);


        Stage optionsWindow = new Stage();
        optionsWindow.initOwner(window);
        optionsWindow.setTitle("Task Options");
        optionsWindow.setScene(optionsScene);
        optionsWindow.show();
    }

    private void openShop() {
        VBox shopBox = new VBox(10);
        shopBox.setPadding(new Insets(20));

        Text shopTitle = new Text("Shop");


        Button itemButton1 = new Button("Casque (Cost: 2)");
        Button itemButton2 = new Button("Epee(Cost: 4)");
        Button itemButton3 = new Button("Bouclier (Cost: 6)");


        Text scoreInsufficientText = new Text("Score Insufficient");

        scoreInsufficientText.setVisible(false);

        itemButton1.setOnAction(e -> {
            if (score.get() >= 2) {
                score.addAndGet(-2);
                scoreInsufficientText.setVisible(false);
                addItem("Casque");
            } else {
                scoreInsufficientText.setVisible(true);
            }
            updateScore();
        });

        itemButton2.setOnAction(e -> {
            if (score.get() >= 4) {
                score.addAndGet(-4);
                scoreInsufficientText.setVisible(false);
                addItem("Epee");
            } else {
                scoreInsufficientText.setVisible(true);
            }
            updateScore();
        });

        itemButton3.setOnAction(e -> {
            if (score.get() >= 6) {
                score.addAndGet(-6);
                scoreInsufficientText.setVisible(false);
                addItem("Bouclier");
            } else {
                scoreInsufficientText.setVisible(true);
            }
            updateScore();
        });

        shopBox.getChildren().addAll(shopTitle, itemButton1, itemButton2, itemButton3, scoreInsufficientText);

        Scene shopScene = new Scene(shopBox, 200, 200);


        Stage shopWindow = new Stage();
        shopWindow.initOwner(window);
        shopWindow.setTitle("Shop");
        shopWindow.setScene(shopScene);
        shopWindow.show();
    }

    private void addItem(String item) {

        String currentItems = itemLabel.getText().substring(7);


        if (currentItems.isEmpty()) {
            itemLabel.setText("Items: " + item);
        } else {
            itemLabel.setText("Items: " + currentItems + ", " + item);
        }
    }

    private void updateScore() {
        Text textScore = (Text) grid.getChildren().stream()
                .filter(node -> node.getId() != null && node.getId().equals("textScore"))
                .findFirst()
                .orElse(null);

        if (textScore != null) {
            textScore.setText("Score: " + score.get());
        }
    }
}