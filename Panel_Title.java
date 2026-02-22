import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
// Removed unused Screen import

public class Panel_Title {

    public static VBox createTitle(String titleText) {
        VBox title = new VBox();

        // Background set to WHITE
        title.setStyle("-fx-background-color: " + UI_Utilities.toHexString(UI_Utilities.WHITE) + ";");
        title.setAlignment(Pos.CENTER_LEFT);
        title.setPadding(new Insets(10, 30, 10, 30));

        // --- FIX: Remove manual screen width calculation ---
        // Allow title panel to stretch to fill the width
        title.setMaxWidth(Double.MAX_VALUE);
        title.setPrefHeight(60);

        // Create title content
        Label titleLabel = new Label(titleText);
        titleLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, FontWeight.BOLD, 32));
        titleLabel.setTextFill(UI_Utilities.DARK_GREEN);

        title.getChildren().add(titleLabel);

        return title;
    }
}