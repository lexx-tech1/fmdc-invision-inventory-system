import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

public class Page_History {
    public static BorderPane createPage(MainWindow mainWindow) {
        BorderPane root = new BorderPane();
        // Pass "History" to highlight the History button
        root.setLeft(Panel_Sidebar.createSidebar(mainWindow, "History"));

        BorderPane rightSide = new BorderPane();
        VBox topPanel = new VBox(
                Panel_Header.createHeader(mainWindow),
                Panel_Title.createTitle("HISTORY")
        );
        rightSide.setTop(topPanel);

        Label contentLabel = new Label("Logs & History");
        contentLabel.setFont(Font.font(UI_Utilities.FONT_FAMILY, 36));
        VBox mainContent = new VBox(contentLabel);
        mainContent.setPadding(new Insets(50));
        mainContent.setStyle("-fx-background-color: #F4F4F4;");

        rightSide.setCenter(mainContent);
        root.setCenter(rightSide);
        return root;
    }
}