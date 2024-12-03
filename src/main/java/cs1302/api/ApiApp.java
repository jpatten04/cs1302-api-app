package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * A Travel Companion app that gives the user an expected weather
 * forecast and potential housing options for an inputed date and location.
 */
public class ApiApp extends Application {
    Stage stage;
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
    StackPane secondScreen;
    VBox secondVBox;
    Label cityLabel;
    HBox hbox;
    VBox weatherVBox;
    Label weatherLabel;
    VBox daysWeather;
    VBox potentialPlaces;
    Label placesLabel;
    ScrollPane placesScroll;
    VBox placesVBox;
    Button backButton;

    /**
     * Constructor for ApiApp, creates the object and initializes all variables.
     */
    public ApiApp() {
        /* FIRST SCREEN */
        this.firstScreen = new StackPane();
        this.firstVBox = new VBox(20);
        this.titleLabel = new Label("Travel Companion");
        this.titleLabel.setFont(Font.font("Comic Sans", FontWeight.BOLD, 36));
        this.options = new HBox();
        this.optionLabels = new VBox(16);
        this.option1Label = new Label("City  (Ex. Athens, GA):");
        this.option1Label.setFont(Font.font("Comic Sans", FontWeight.BOLD, 17));
        this.option2Label = new Label("Start Date  (Ex. MM/DD):");
        this.option2Label.setFont(Font.font("Comic Sans", FontWeight.BOLD, 17));
        this.optionFields = new VBox(8);
        this.option1Field = new TextField();
        this.option1Field.setFont(Font.font(16));
        this.option2Field = new TextField();
        this.option2Field.setFont(Font.font(16));
        this.searchButton = new Button("Search Location");
        this.searchButton.setFont(Font.font(20));

        /* SECOND SCREEN */
        this.secondScreen = new StackPane();
        this.secondVBox = new VBox();
        this.cityLabel = new Label("CITY, STATE");
        this.cityLabel.setFont(Font.font("Comic Sans", FontWeight.BOLD, 30));
        this.hbox = new HBox(10);
        this.weatherVBox = new VBox();
        this.weatherLabel = new Label("Forecast");
        this.weatherLabel.setFont(Font.font("Comic Sans", FontWeight.BOLD, 24));
        this.daysWeather = new VBox();
        this.potentialPlaces = new VBox();
        this.placesLabel = new Label("Places to Stay");
        this.placesLabel.setFont(Font.font("Comic Sans", FontWeight.BOLD, 24));
        this.placesVBox = new VBox();
        this.placesScroll = new ScrollPane(placesVBox);
        this.backButton = new Button("Back");
        this.backButton.setFont(Font.font(20));

    } // App

    @Override
    public void init() {
        /* ALL EVENT HANDLERS */
        // search button
        this.searchButton.setOnAction(ae -> {
            this.scene.setRoot(secondScreen);
            this.stage.sizeToScene();
            this.stage.centerOnScreen();
        });
        // back button
        this.backButton.setOnAction(ae -> {
            this.scene.setRoot(firstScreen);
            this.stage.sizeToScene();
            this.stage.centerOnScreen();
        });

    } // init

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
        this.secondScreen.getChildren().addAll(secondVBox);
        this.secondVBox.getChildren().addAll(cityLabel, hbox, backButton);
        this.hbox.getChildren().addAll(weatherVBox, potentialPlaces);
        this.weatherVBox.getChildren().addAll(weatherLabel, daysWeather);
        this.potentialPlaces.getChildren().addAll(placesLabel, placesScroll);

        /* FORMAT */
        // first screen
        this.firstScreen.setMinSize(500, 400);
        this.firstScreen.setPadding(new Insets(40));
        this.firstVBox.setAlignment(Pos.BASELINE_CENTER);
        this.options.setAlignment(Pos.CENTER);
        this.options.setMinHeight(150);
        this.optionLabels.setMinWidth(firstScreen.getMinWidth() / 2);
        this.optionLabels.setAlignment(Pos.CENTER_RIGHT);
        this.optionFields.setMinWidth(firstScreen.getMinWidth() / 2);
        this.optionFields.setAlignment(Pos.CENTER_LEFT);
        this.optionFields.setPadding(new Insets(0, 10, 0, 10));
        // second screen
        this.secondScreen.setMinSize(800, 600);
        this.secondScreen.setPadding(new Insets(10, 20, 0, 20));
        this.secondVBox.setAlignment(Pos.BASELINE_CENTER);
        this.hbox.setPadding(new Insets(15, 0, 0, 0));
        this.weatherVBox.setMinWidth(secondScreen.getMinWidth() / 3);
        HBox.setHgrow(potentialPlaces, Priority.ALWAYS);
        this.weatherVBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(daysWeather, Priority.ALWAYS);
        this.potentialPlaces.setAlignment(Pos.CENTER);
        this.placesScroll.setMinHeight(420);

        /* COLORING */
        // first screen
        this.firstScreen.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        this.options.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        // second screen
        this.secondScreen.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        this.daysWeather.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        /* SETUP SCENE & STAGE */
        this.stage = stage;
        this.scene = new Scene(firstScreen);
        this.stage.setScene(scene);
        this.stage.setTitle("ApiApp!");
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.show();
    } // start

} // ApiApp
