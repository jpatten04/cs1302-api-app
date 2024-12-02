package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ApiApp extends Application {
    Scene scene;

    /* FIRST SCREEN */
    StackPane firstScreen;
    VBox firstVBox;
    Label titleLabel;
    HBox options;
    VBox optionLabels, optionFields;
    Label option1Label, option2Label;
    TextField option1Field, option2Field;
    Button searchButton;

    /* SECOND SCREEN */


    public ApiApp() {
        /* FIRST SCREEN */
        this.firstScreen = new StackPane();
        this.firstVBox = new VBox();
        this.titleLabel = new Label("Travel Companion");
        this.titleLabel.setFont(Font.font("Comic Sans", FontWeight.BOLD, 34));
        this.options = new HBox(10);
        this.optionLabels = new VBox(8);
        this.option1Label = new Label("City (Ex. Athens, GA):");
        this.option1Label.setFont(Font.font("Comic Sans", 18));
        this.option2Label = new Label("Start Date (Ex. MM/DD):");
        this.option2Label.setFont(Font.font("Comic Sans", 18));
        this.optionFields = new VBox(8);
        this.option1Field = new TextField();
        this.option2Field = new TextField();
        this.searchButton = new Button("Search");
        this.searchButton.setFont(Font.font(18));

        /* SECOND SCREEN */


    } // App

    @Override
    public void start(Stage stage) {
        /* ADD NODES TO PARENTS */
        // first screen
        this.firstScreen.getChildren().add(firstVBox);
        this.firstVBox.getChildren().addAll(titleLabel, options, searchButton);
        this.options.getChildren().addAll(optionLabels, optionFields);
        this.optionLabels.getChildren().addAll(option1Label, option2Label);
        this.optionFields.getChildren().addAll(option1Field, option2Field);
        // second screen

        /* FORMAT */
        // first screen
        this.firstScreen.setMinSize(500, 400);
        this.firstVBox.setAlignment(Pos.BASELINE_CENTER);
        this.firstVBox.setPadding(new Insets(40));
        this.options.setPadding(new Insets(50, 0, 30, 0));
        this.optionLabels.setMinWidth(firstScreen.getMinWidth() / 2);
        this.optionFields.setMinWidth(firstScreen.getMinWidth() / 2);
        this.optionLabels.setAlignment(Pos.CENTER_RIGHT);
        this.optionFields.setAlignment(Pos.CENTER_LEFT);

        // temp coloring


        /* SETUP SCENE & STAGE */
        this.scene = new Scene(firstScreen);
        stage.setScene(scene);
        stage.setTitle("ApiApp!");
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    } // start

} // ApiApp
