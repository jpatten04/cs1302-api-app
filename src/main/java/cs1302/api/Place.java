package cs1302.api;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import cs1302.api.ApiApp.PlacesResults;

/**
 * Class that displays all relevant data for the place
 * it is given using the API data.
 */
public class Place extends HBox {
    PlacesResults placesResults;
    int current;

    /* LEFT SIDE */
    VBox leftVBox;
    Text ratingText;
    Text totalRatingsText;

    /* RIGHT SIDE */
    VBox rightVBox;
    Text nameText;
    Text addressText;

    /**
     * Creates the Place object and initializes all variables.
     */
    public Place(PlacesResults placesResults, int current) {
        super();

        /* INITIALIZE VARIBALES */
        this.placesResults = placesResults;
        this.current = current;

        // left side
        this.leftVBox = new VBox(6);
        this.ratingText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.totalRatingsText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};
        // right side
        this.rightVBox = new VBox(8);
        this.nameText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};
        this.addressText = new Text() {{ setBoundsType(TextBoundsType.VISUAL); }};

        /* UPDATE ALL DATA */
        this.setData();

        /* ADD NODES TO PARENTS */
        this.getChildren().addAll(leftVBox, rightVBox);
        this.leftVBox.getChildren().addAll(ratingText, totalRatingsText);
        this.rightVBox.getChildren().addAll(nameText, addressText);

        /* FORMAT */
        this.setSpacing(10);
        this.setPadding(new Insets(8));
        // sizing
        this.leftVBox.setMinWidth(80);
        // alignment
        this.leftVBox.setAlignment(Pos.CENTER);
        this.rightVBox.setAlignment(Pos.CENTER_LEFT);
        // text
        this.ratingText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 40));
        this.totalRatingsText.setFont(Font.font("Comic Sans", 12));
        this.nameText.setFont(Font.font("Comic Sans", FontWeight.BOLD, 25));
        this.addressText.setFont(Font.font("Comic Sans", 13));
        // coloring
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, null, null)));
        this.ratingText.setFill(Color.YELLOW);
        this.ratingText.setStroke(Color.BLACK);
    } // Place

    /**
     * Updates all nodes with the correct data from the API results.
     */
    private void setData() {
        // rating text
        this.ratingText.setText(placesResults.results[current].rating + "");
        this.totalRatingsText.setText(
            String.format("(%S Ratings)", placesResults.results[current].user_ratings_total));

        // name and address
        this.nameText.setText(placesResults.results[current].name);
        this.addressText.setText(placesResults.results[current].formatted_address);
    } // setData

} // Place
