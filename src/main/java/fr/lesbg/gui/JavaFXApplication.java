package fr.lesbg.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Create a VBox layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15, 20, 10, 10));

        // Create a text field
        TextField textField = new TextField();
        textField.setPromptText("Enter some data");

        // Create checkboxes
        CheckBox checkBox1 = new CheckBox("Option 1");
        CheckBox checkBox2 = new CheckBox("Option 2");
        CheckBox checkBox3 = new CheckBox("Option 3");

        // Create radio buttons
        RadioButton radioButton1 = new RadioButton("Choice 1");
        RadioButton radioButton2 = new RadioButton("Choice 2");
        RadioButton radioButton3 = new RadioButton("Choice 3");

        // Group radio buttons
        ToggleGroup radioGroup = new ToggleGroup();
        radioButton1.setToggleGroup(radioGroup);
        radioButton2.setToggleGroup(radioGroup);
        radioButton3.setToggleGroup(radioGroup);

        // Create big buttons
        Button bigButton1 = new Button("Big Button 1");
        bigButton1.setMinSize(150, 50);
        Button bigButton2 = new Button("Big Button 2");
        bigButton2.setMinSize(150, 50);

        // Add all components to the VBox
        vbox.getChildren().addAll(textField, checkBox1, checkBox2, checkBox3, radioButton1, radioButton2, radioButton3, bigButton1, bigButton2);

        // Set the stage
        Stage primaryStage = new Stage();
        primaryStage.setTitle("JavaFX GUI Example");
        primaryStage.setScene(new Scene(vbox, 300, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
