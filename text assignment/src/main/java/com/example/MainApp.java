package com.example;

import com.example.data.DataEntry;
import com.example.data.DataManager;
import com.example.regex.RegexUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {
    private final DataManager dataManager = new DataManager();

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        Tab regexTab = new Tab("Regex Operations");
        regexTab.setContent(createRegexPane());
        regexTab.setClosable(false);

        Tab dataTab = new Tab("Data Management");
        dataTab.setContent(createDataPane());
        dataTab.setClosable(false);

        tabPane.getTabs().addAll(regexTab, dataTab);

        Scene scene = new Scene(tabPane, 950, 600);

        // Purple theme for the root
        tabPane.setStyle("-fx-background-color: #f4f0ff;");

        primaryStage.setTitle("Text Processing Tool - Purple UI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node createRegexPane() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f4f0ff;");

        // Left side: Input Panel
        VBox inputPanel = new VBox(12);
        inputPanel.setPadding(new Insets(15));
        inputPanel.setAlignment(Pos.TOP_LEFT);
        inputPanel.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #b39ddb; -fx-border-radius: 12;");
        inputPanel.setEffect(new DropShadow(8, Color.rgb(126, 87, 194, 0.3)));

        Label title = new Label("🔎 Regex Search & Replace");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4a148c;");

        Label textLabel = new Label("Enter Text:");
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(6);
        styleInput(textArea);

        Label patternLabel = new Label("Regex Pattern:");
        TextField patternField = new TextField();
        patternField.setPromptText("e.g., \\d+ for digits");
        styleInput(patternField);

        Label replacementLabel = new Label("Replacement:");
        TextField replacementField = new TextField();
        replacementField.setPromptText("Replacement text");
        styleInput(replacementField);

        Button findButton = createPrimaryButton("🔍 Find Matches");
        Button replaceButton = createSecondaryButton("✏ Replace");

        HBox buttonBox = new HBox(10, findButton, replaceButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        inputPanel.getChildren().addAll(title, textLabel, textArea, patternLabel, patternField,
                replacementLabel, replacementField, buttonBox);

        // Right side: Results Panel
        VBox resultPanel = new VBox(12);
        resultPanel.setPadding(new Insets(15));
        resultPanel.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #b39ddb; -fx-border-radius: 12;");
        resultPanel.setEffect(new DropShadow(8, Color.rgb(126, 87, 194, 0.3)));

        Label matchLabel = new Label("Matches Found:");
        ListView<String> matchListView = new ListView<>();
        matchListView.setPrefHeight(200);

        Label replacedLabel = new Label("Replaced Text:");
        TextArea replacedArea = new TextArea();
        replacedArea.setWrapText(true);
        replacedArea.setEditable(false);
        replacedArea.setPrefRowCount(4);
        styleInput(replacedArea);

        resultPanel.getChildren().addAll(matchLabel, matchListView, replacedLabel, replacedArea);

        // Event Handling
        findButton.setOnAction(e -> {
            String text = textArea.getText();
            String pattern = patternField.getText();
            matchListView.getItems().clear();
            if (pattern == null || pattern.isEmpty() || text == null) return;

            try {
                for (String match : RegexUtil.findMatches(pattern, text)) {
                    matchListView.getItems().add(match);
                }
                if (matchListView.getItems().isEmpty()) {
                    matchListView.getItems().add("No matches found");
                }
            } catch (Exception ex) {
                matchListView.getItems().add("Invalid pattern: " + ex.getMessage());
            }
        });

        replaceButton.setOnAction(e -> {
            String text = textArea.getText();
            String pattern = patternField.getText();
            String replacement = replacementField.getText();
            if (pattern == null || pattern.isEmpty() || text == null) return;

            try {
                String replaced = RegexUtil.replace(pattern,
                        replacement == null ? "" : replacement,
                        text);
                replacedArea.setText(replaced);
            } catch (Exception ex) {
                replacedArea.setText("Invalid pattern: " + ex.getMessage());
            }
        });

        // Layout: Side by side
        HBox mainContent = new HBox(20, inputPanel, resultPanel);
        mainContent.setAlignment(Pos.TOP_CENTER);
        root.setCenter(mainContent);

        return root;
    }

    private Node createDataPane() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f4f0ff;");

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #b39ddb; -fx-border-radius: 12;");
        contentBox.setEffect(new DropShadow(8, Color.rgb(126, 87, 194, 0.3)));

        Label title = new Label("📋 Data Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4a148c;");

        TableView<DataEntry> table = new TableView<>();
        TableColumn<DataEntry, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        TableColumn<DataEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        table.getColumns().addAll(keyCol, valueCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(dataManager.getObservableList());
        table.setPrefHeight(300);

        TextField keyField = new TextField();
        keyField.setPromptText("Key");
        styleInput(keyField);

        TextField valueField = new TextField();
        valueField.setPromptText("Value");
        styleInput(valueField);

        Button addUpdateButton = createPrimaryButton("➕ Add / Update");
        Button deleteButton = createSecondaryButton("🗑 Delete");

        HBox inputBox = new HBox(10, keyField, valueField, addUpdateButton, deleteButton);
        inputBox.setAlignment(Pos.CENTER);

        addUpdateButton.setOnAction(e -> {
            String key = keyField.getText();
            String value = valueField.getText();
            if (key == null || key.isEmpty()) return;
            if (dataManager.addOrUpdateEntry(key, value)) {
                keyField.clear();
                valueField.clear();
                table.refresh();
            }
        });

        deleteButton.setOnAction(e -> {
            String key = keyField.getText();
            if (key == null || key.isEmpty()) return;
            if (dataManager.deleteEntry(key)) {
                keyField.clear();
                valueField.clear();
                table.refresh();
            }
        });

        contentBox.getChildren().addAll(title, table, inputBox);
        root.setCenter(contentBox);

        return root;
    }

    // Helper to style inputs
    private void styleInput(Control control) {
        control.setStyle("-fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #b39ddb;");
    }

    // Helper to create primary (purple) button
    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #7e57c2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #7e57c2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;"));
        return btn;
    }

    // Helper to create secondary (light purple) button
    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #d1c4e9; -fx-text-fill: #4a148c; -fx-font-weight: bold; -fx-background-radius: 8;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #b39ddb; -fx-text-fill: #4a148c; -fx-font-weight: bold; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #d1c4e9; -fx-text-fill: #4a148c; -fx-font-weight: bold; -fx-background-radius: 8;"));
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
