package cs1302.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
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
    Text titleText;
    HBox options;
    VBox optionTexts, optionFields;
    Text option1Text, option2Text;
    TextField option1Field, option2Field;
    Button searchButton;

    /* SECOND SCREEN */
    StackPane secondScreen;
    VBox secondVBox;
    Text cityText;
    HBox hbox;
    ScrollPane weatherScroll;
    VBox weatherVBox;
    Text weatherText;
    VBox daysWeatherVBox;
    VBox potentialPlaces;
    Text placesText;
    ScrollPane placesScroll;
    VBox placesVBox;
    Button backButton;

    /* CLASSES FOR API DATA */
    /** Class for Google Maps geolocation API data. */
    public class MapsResults {
        Candidate[] candidates;

        /** Class that contains each place in results. */
        public class Candidate {
            String formatted_address;
            Geometry geometry;

            /** Class that contains relevant data for each place. */
            public class Geometry {
                Location location;

                /** Class that contains location data for place. */
                public class Location {
                    double lat;
                    double lng;
                } // Location
            } // Geometry
        } // Candidates
    } // TempResults

    /** Class for weather API data. */
    public class WeatherResults {
        Daily daily;

        /** Class that contains all weather data for every day. */
        public class Daily {
            String[] time;
            int[] weather_code;
            double[] temperature_2m_max;
            double[] temperature_2m_min;
        } // Daily
    } // WeatherResults

    /** Class for Google Maps nearby lodging API data. */
    public class PlacesResults {
        Result[] results;

        /** Class that contains all data about each place. */
        public class Result {
            String business_status;
            String formatted_address;
            String name;
            double rating;
            int user_ratings_total;
        } // Result
    } // Places Results

    /* API STUFF */
    HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String mapURL = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";
    String weatherURL = "https://historical-forecast-api.open-meteo.com/v1/forecast";
    String placesURL = "https://maps.googleapis.com/maps/api/place/textsearch/json";
    // results
    MapsResults mapsResults;
    WeatherResults weatherResults;
    PlacesResults placesResults;
    double latitude, longitude;

    /**
     * Constructor for ApiApp, creates the object and initializes all variables.
     */
    public ApiApp() {
        /* FIRST SCREEN */
        this.firstScreen = new StackPane();
        this.firstVBox = new VBox(20);
        this.titleText = new Text("Travel Companion") {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.options = new HBox();
        this.optionTexts = new VBox(28);
        this.option1Text = new Text("City  (Ex. Athens, GA):") {{
            setBoundsType(TextBoundsType.VISUAL); }};
        this.option2Text = new Text("Start Date  (Ex. MM/DD):") {{
            setBoundsType(TextBoundsType.VISUAL); }};
        this.optionFields = new VBox(8);
        this.option1Field = new TextField();
        this.option2Field = new TextField();
        this.searchButton = new Button("Search Location");

        /* SECOND SCREEN */
        this.secondScreen = new StackPane();
        this.secondVBox = new VBox(20);
        this.cityText = new Text("CITY, STATE") {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.hbox = new HBox(10);
        this.weatherVBox = new VBox(5);
        this.weatherText = new Text("8-Day Forecast") {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.daysWeatherVBox = new VBox(8);
        this.weatherScroll = new ScrollPane(daysWeatherVBox);
        this.potentialPlaces = new VBox(5);
        this.placesText = new Text("Places to Stay (within 5 miles)") {{
            setBoundsType(TextBoundsType.VISUAL); }};
        this.placesVBox = new VBox(8);
        this.placesScroll = new ScrollPane(placesVBox);
        this.backButton = new Button("Back to Search");
    } // App

    /**
     * Creates a new thread and calls all the existing methods that have api calls.
     * Creates a pop-up alert if any error occurs.
     */
    public void getData() {
        // run all api calls and dependants on new thread
        Thread apiThread = new Thread(() -> {
            try {
                this.getMapsResults();
                this.getWeatherResults();
                this.getPlacesResults();

                Platform.runLater(() -> this.setData());
            } catch (Exception e) {
                // create alert if error
                Platform.runLater(() -> {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            } // try
        });
        apiThread.setDaemon(true);
        apiThread.start();
    } // getData

    /**
     * Makes the api call for the given class-type and url.
     *
     * @param resultClass the class of which to convert the api data into.
     * @param fullURL the required url that corresponds with the desired class-type.
     *
     * @returns the api data converted into the desired class-type.
     *
     * @throws Exception if there is an error when creating or sending api request.
     */
    public <T> T getResults(Class<T> resultClass, String fullURL) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullURL)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // if response has any errors, throw exception
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        } // if

        // create results object from json file
        return gson.fromJson(response.body(), resultClass);
    } // getResults

    /**
     * Gets api results for the given location.
     *
     * @throws Exception if there is an error when retrieving results.
     */
    public void getMapsResults() throws Exception {
        // build full url
        String fullURL = mapURL + String.format("?inputtype=%s&fields=%s&input=%s&key=%s",
            "textquery", "formatted_address,geometry",
            option1Field.getText().replaceAll("\\s", ""),
            "AIzaSyB2FtQLcf02m0NWrQpyn02VVYfHMNLpAI8");

        // get api data
        this.mapsResults = this.getResults(MapsResults.class, fullURL);

        // check if there is results
        if (this.mapsResults.candidates.length == 0) {
            throw new IllegalArgumentException("No Results, Try Again.");
        } // if

        // set coordinates
        this.latitude = mapsResults.candidates[0].geometry.location.lat;
        this.longitude = mapsResults.candidates[0].geometry.location.lng;
    } // getMapsResults

    /**
     * Gets api results for all weather data for 8 days.
     *
     * @throws Exception if there is an error when retrieving results.
     */
    public void getWeatherResults() throws Exception {
        // get correct dates
        String[] start = option2Field.getText().split("/");
        LocalDate startDate = LocalDate.of(2023,
            Integer.parseInt(start[0]), Integer.parseInt(start[1]));
        LocalDate endDate = startDate.plusWeeks(1);

        // build full url
        String fullURL = weatherURL + String.format(
            "?daily=%s&temperature_unit=%s&timezone=%s&" +
            "latitude=%s&longitude=%s&start_date=%s&end_date=%s",
            "weather_code,temperature_2m_max,temperature_2m_min", "fahrenheit", "auto",
            latitude, longitude, startDate.toString(), endDate.toString());

        // get api data
        this.weatherResults = this.getResults(WeatherResults.class, fullURL);
    } // getWeatherResults

    /**
     * Get api results for all nearby lodging places.
     *
     * @throws Exception if there is an error when retrieving results.
     */
    public void getPlacesResults() throws Exception {
        // build full url
        String fullURL = placesURL + String.format("?query=%s&radius=%s&location=%s&key=%s",
            "lodging", "8000", String.format("%S,%S", latitude, longitude),
            "AIzaSyB2FtQLcf02m0NWrQpyn02VVYfHMNLpAI8");

        // get api data
        this.placesResults = this.getResults(PlacesResults.class, fullURL);
    } // getPlacesResults

    /**
     * Takes all the data from api results, and
     * updates all visuals.
     */
    public void setData() {
        // title
        this.cityText.setText(mapsResults.candidates[0].formatted_address);

        // weather
        for (int i = 0; i < weatherResults.daily.time.length; i++) {
            this.daysWeatherVBox.getChildren().add(new DayForecast(weatherResults, i));
        } // for

        // places
        for (int i = 0; i < placesResults.results.length; i++) {
            if (placesResults.results[i].business_status.equals("OPERATIONAL")
                && placesResults.results[i].rating > 0) {
                this.placesVBox.getChildren().add(new Place(placesResults, i));
            } // if
        } // for
    } // setData

    @Override
    public void init() {
        /* ALL EVENT HANDLERS */
        // search button
        this.searchButton.setOnAction(ae -> {
            // format window and switch screen
            this.scene.setRoot(secondScreen);
            this.stage.sizeToScene();
            this.stage.centerOnScreen();

            // run all api calls
            this.getData();
        });
        // back button
        this.backButton.setOnAction(ae -> {
            // format window and switch screen
            this.scene.setRoot(firstScreen);
            this.stage.sizeToScene();
            this.stage.centerOnScreen();

            // reset data
            this.mapsResults = null;
            this.weatherResults = null;
            this.placesResults = null;
            this.daysWeatherVBox.getChildren().clear();
            this.placesVBox.getChildren().clear();
        });
    } // init

    @Override
    public void start(Stage stage) {
        /* ADD NODES TO PARENTS */
        // first screen
        this.firstScreen.getChildren().add(firstVBox);
        this.firstVBox.getChildren().addAll(titleText, options, searchButton);
        this.options.getChildren().addAll(optionTexts, optionFields);
        this.optionTexts.getChildren().addAll(option1Text, option2Text);
        this.optionFields.getChildren().addAll(option1Field, option2Field);
        // second screen
        this.secondScreen.getChildren().addAll(secondVBox);
        this.secondVBox.getChildren().addAll(cityText, hbox, backButton);
        this.hbox.getChildren().addAll(weatherVBox, potentialPlaces);
        this.weatherVBox.getChildren().addAll(weatherText, weatherScroll);
        this.potentialPlaces.getChildren().addAll(placesText, placesScroll);

        /* FORMAT */
        // first screen
        this.firstScreen.setMinSize(500, 400);
        this.firstScreen.setPadding(new Insets(40));
        // alignment
        this.firstVBox.setAlignment(Pos.CENTER);
        this.options.setAlignment(Pos.CENTER);
        this.optionTexts.setAlignment(Pos.CENTER_RIGHT);
        this.optionFields.setAlignment(Pos.CENTER_LEFT);
        // sizing
        this.options.setMinHeight(150);
        this.options.setPadding(new Insets(10));
        this.optionTexts.setMinWidth(firstScreen.getMinWidth() / 2);
        this.optionFields.setMinWidth(firstScreen.getMinWidth() / 2);
        this.optionFields.setPadding(new Insets(0, 0, 0, 10));
        // text
        this.titleText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 42));
        this.option1Text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 22));
        this.option2Text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 22));
        this.searchButton.setFont(Font.font(18));
        this.option1Field.setFont(Font.font(18));
        this.option2Field.setFont(Font.font(18));

        // second screen
        this.secondScreen.setMinSize(800, 600);
        this.secondScreen.setPadding(new Insets(0, 20, 0, 20));
        // alignment
        this.secondVBox.setAlignment(Pos.CENTER);
        this.weatherVBox.setAlignment(Pos.CENTER);
        this.potentialPlaces.setAlignment(Pos.CENTER);
        // sizing
        this.weatherVBox.setMinWidth(secondScreen.getMinWidth() / 3);
        HBox.setHgrow(potentialPlaces, Priority.ALWAYS);
        this.weatherScroll.setPrefHeight(420);
        this.weatherScroll.setFitToWidth(true);
        VBox.setVgrow(daysWeatherVBox, Priority.ALWAYS);
        this.daysWeatherVBox.setPadding(new Insets(5));
        this.placesScroll.setPrefHeight(420);
        this.placesScroll.setMaxWidth(secondScreen.getMinWidth() / 3 * 2);
        this.placesVBox.setPadding(new Insets(5));
        // text
        this.cityText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 30));
        this.weatherText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 24));
        this.placesText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 24));
        this.backButton.setFont(Font.font(18));

        /* COLORING */
        // first screen
        this.firstScreen.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        this.options.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        // second screen
        this.secondScreen.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        this.daysWeatherVBox.setBackground(new Background(
                                               new BackgroundFill(Color.WHITE, null, null)));

        /* SETUP SCENE & STAGE */
        this.stage = stage;
        this.scene = new Scene(firstScreen);
        this.stage.setScene(scene);
        this.stage.setTitle("ApiApp!");
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.show();
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.show();
    } // start

} // ApiApp
