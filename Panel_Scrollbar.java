import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Panel_Scrollbar {

    public static ScrollPane createScrollPane(Node content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 0; -fx-border-color: transparent;");
        scrollPane.setId("custom-scroll-pane");

        String customScrollBarCss =
                "#custom-scroll-pane .viewport {" +
                        "-fx-background-color: #FFFFFF;" +
                        "}" +

                        "#custom-scroll-pane .corner {" +
                        "-fx-background-color: #FFFFFF;" +
                        "}" +

                        // Target the ScrollBar (vertical)
                        "#custom-scroll-pane .scroll-bar:vertical {" +
                        "-fx-pref-width: 20px;" +
                        "-fx-padding: 0 5 0 5;" + // REMOVED 20px bottom padding
                        "-fx-background-color: transparent;" +
                        "}" +

                        // Target the Track
                        "#custom-scroll-pane .scroll-bar:vertical .track {" +
                        "-fx-background-color: #E0E0E0;" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: transparent;" +
                        "}" +

                        // Target the Thumb
                        "#custom-scroll-pane .scroll-bar:vertical .thumb {" +
                        "-fx-background-color: #666666;" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-radius: 10;" +
                        "}" +

                        "#custom-scroll-pane .scroll-bar > .increment-button," +
                        "#custom-scroll-pane .scroll-bar > .decrement-button {" +
                        "-fx-background-color: transparent;" +
                        "-fx-opacity: 0;" +
                        "-fx-pref-height: 0;" +
                        "}" +

                        "#custom-scroll-pane {" +
                        "-fx-background-color: #FFFFFF;" +
                        "-fx-border-width: 0;" +
                        "-fx-border-color: transparent;" +
                        "}";

        String dataUri = "data:text/css;charset=utf-8," + customScrollBarCss.replaceAll("[\n\r]", "");
        scrollPane.getStylesheets().add(dataUri);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return scrollPane;
    }
}