package cs1302.api;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import cs1302.api.ApiApp.WeatherResults;

/**
 * Class that creates a box that shows all
 * relevant weather data for one day.
 */
public class DayForecast extends HBox {
    WeatherResults weatherData;
    int day;

    /* LEFT SIDE */
    VBox leftVBox;
    Text dateText;
    ImageView weatherIconIV;

    /* RIGHT SIDE */
    VBox rightVBox;
    HBox weatherHBox;
    Text weatherText;
    Text weatherModelText;
    // temperature
    VBox tempVBox;
    Text temperatureText;
    HBox highLowHBox;
    VBox HLIconsVBox;
    ImageView highIconIV;
    ImageView lowIconIV;
    VBox HLTextsVBox;
    Text highText;
    Text lowText;
    VBox HLTempVBox;
    Text tempHighText;
    Text tempLowText;

    /* WEATHER ICONS */
    static Map<String, Image> weatherIcons = new HashMap<String,Image>() {{
        put("sun", new Image("file:resources/sun.png"));
        put("cloudy", new Image("file:resources/cloudy.png"));
        put("rain", new Image("file:resources/rainy.png"));
        put("snow", new Image("file:resources/snow.png"));
        put("storm", new Image("file:resources/storm.png"));

    }};

    /**
     * Creates the DayForecast object, initializes variables.
     *
     * @param weatherData data from the weather API.
     * @param day the current day to show data for.
     */
    public DayForecast(WeatherResults weatherData, int day) {
        super();

        /* INITIALIZE VARIABLES */
        this.weatherData = weatherData;
        this.day = day;

        // left side
        this.leftVBox = new VBox(5);
        this.dateText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.weatherIconIV = new ImageView();
        // right side
        this.rightVBox = new VBox(2);
        this.weatherHBox = new HBox(8);
        this.weatherText = new Text("Weather:") {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.weatherModelText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};
        // temperature
        this.tempVBox = new VBox(4);
        this.temperatureText = new Text("Temperature:") {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.highLowHBox = new HBox(6);
        this.HLIconsVBox = new VBox(4);
        this.highIconIV = new ImageView(new Image("file:resources/up-arrow.png"));
        this.lowIconIV = new ImageView(new Image("file:resources/down-arrow.png"));
        this.HLTextsVBox = new VBox(5);
        this.highText = new Text("High:") {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.lowText = new Text("Low:") {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.HLTempVBox = new VBox(6);
        this.tempHighText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.tempLowText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};

        /* UPDATE ALL DATA */
        //this.setData();
        this.setData();

        /* ADD NODES TO PARENTS */
        this.getChildren().addAll(leftVBox, rightVBox);
        this.leftVBox.getChildren().addAll(dateText, weatherIconIV);
        this.rightVBox.getChildren().addAll(weatherHBox, tempVBox);
        this.weatherHBox.getChildren().addAll(weatherText, weatherModelText);
        this.tempVBox.getChildren().addAll(temperatureText, highLowHBox);
        this.highLowHBox.getChildren().addAll(HLIconsVBox, HLTextsVBox, HLTempVBox);
        this.HLIconsVBox.getChildren().addAll(highIconIV, lowIconIV);
        this.HLTextsVBox.getChildren().addAll(highText, lowText);
        this.HLTempVBox.getChildren().addAll(tempHighText, tempLowText);

        /* FORMAT */
        this.setSpacing(8);
        this.setPadding(new Insets(8));
        // alignment
        this.leftVBox.setAlignment(Pos.TOP_CENTER);
        this.HLIconsVBox.setAlignment(Pos.CENTER_LEFT);
        this.HLTextsVBox.setAlignment(Pos.CENTER_LEFT);
        this.HLTempVBox.setAlignment(Pos.CENTER_LEFT);
        // text
        this.dateText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 24));
        this.dateText.setBoundsType(TextBoundsType.VISUAL);
        this.weatherText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 15));
        this.weatherModelText.setFont(Font.font("Comic Sans", 17));
        this.temperatureText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 16));
        this.highText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 16));
        this.lowText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 16));
        this.tempHighText.setFont(Font.font("Comic Sans", 18));
        this.tempLowText.setFont(Font.font("Comic Sans", 18));
        // images
        this.weatherIconIV.setFitWidth(60);
        this.weatherIconIV.setFitHeight(60);
        this.highIconIV.setFitWidth(18);
        this.highIconIV.setFitHeight(18);
        this.lowIconIV.setFitWidth(18);
        this.lowIconIV.setFitHeight(18);

        // coloring
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));

    } // DayForecast

    /**
     * Uses the weatherData variable to update all
     * data in this class.
     */
    public void setData() {
        // date Text
        this.dateText.setText(String.format("%S/%S",
                                            weatherData.daily.time[day].split("-")[1],
                                            weatherData.daily.time[day].split("-")[2]));

        // weather icon and weather model Text
        int code = weatherData.daily.weather_code[day];
        if (code >= 1 && code <= 48) {
            this.weatherIconIV.setImage(weatherIcons.get("cloudy"));
            this.weatherModelText.setText("Cloudy");
        } else if ((code >= 51 && code <= 67) || (code >= 80 && code <= 82)) {
            this.weatherIconIV.setImage(weatherIcons.get("rain"));
            this.weatherModelText.setText("Rainy");
        } else if ((code >= 71 && code <= 77) || (code >= 85 && code <= 86)) {
            this.weatherIconIV.setImage(weatherIcons.get("snow"));
            this.weatherModelText.setText("Snowy");
        } else if (code >= 95 && code <= 99) {
            this.weatherIconIV.setImage(weatherIcons.get("storm"));
            this.weatherModelText.setText("Stormy");
        } else {
            this.weatherIconIV.setImage(weatherIcons.get("sun"));
            this.weatherModelText.setText("Sunny");
        } // if

        // temperatures
        this.tempHighText.setText(weatherData.daily.temperature_2m_max[day] + "°F");
        this.tempLowText.setText(weatherData.daily.temperature_2m_min[day] + "°F");

    } // setData

} // DayForecast
