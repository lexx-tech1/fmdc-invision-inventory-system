import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Comparator;

public class Page_Accounts {

    private static TableColumn<Activity, ?> activeColumn;
    private static SortState activeState = SortState.NONE;
    private enum SortState { NONE, ASC, DESC }

    public static BorderPane createPage(MainWindow mainWindow) {

        BorderPane root = new BorderPane();
        root.setLeft(Panel_Sidebar.createSidebar(mainWindow, "Accounts"));

        BorderPane rightSide = new BorderPane();
        rightSide.setStyle("-fx-background-color: #FFFFFF;");

        VBox topPanel = new VBox(
                Panel_Header.createHeader(mainWindow),
                Panel_Title.createTitle("SYSTEM ACCOUNTS")
        );
        rightSide.setTop(topPanel);

        TableView<Activity> table = new TableView<>();
        ObservableList<Activity> masterData = FXCollections.observableArrayList();

        configureTable(mainWindow, table, masterData);
        loadDatabaseData(masterData); // 🔥 DATABASE CONNECTED

        VBox tableWrapper = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        ScrollPane scrollPane = Panel_Scrollbar.createScrollPane(tableWrapper);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        VBox mainContent = new VBox(scrollPane);
        mainContent.setPadding(new Insets(10, 20, 20, 20));
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        rightSide.setCenter(mainContent);
        root.setCenter(rightSide);

        table.widthProperty().addListener((obs, o, n) -> resizeColumns(table));

        return root;
    }

    private static void configureTable(MainWindow mainWindow, TableView<Activity> table, ObservableList<Activity> masterData) {

        table.setEditable(false);
        table.setFixedCellSize(55);
        table.setSelectionModel(null);

        table.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-control-inner-background: white;" +
                "-fx-hbar-policy: never;" +
                "-fx-vbar-policy: never;");

        TableColumn<Activity, String> lastLoginCol = createFixedCol("LAST LOGIN", "date", 150);
        TableColumn<Activity, String> userCol      = createFixedCol("USER", "user", 350);
        TableColumn<Activity, String> levelCol     = createFixedCol("USER LEVEL", "action", 150);
        TableColumn<Activity, String> statusCol    = createFixedCol("STATUS", "module", 150);
        TableColumn<Activity, Void> detailsCol     = createButtonColumn(mainWindow, "DETAILS", 150);

        table.getColumns().setAll(lastLoginCol, userCol, levelCol, statusCol, detailsCol);

        SortedList<Activity> sorted = new SortedList<>(masterData);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        table.getColumns().forEach(col -> {
            col.setResizable(false);
            col.setReorderable(false);
            col.setSortable(false);
            setupHeader(table, col, masterData);
        });

        table.setRowFactory(tv -> {
            TableRow<Activity> row = new TableRow<>();
            row.itemProperty().addListener((obs, o, n) -> {
                if (n != null)
                    row.setStyle(row.getIndex() % 2 == 0
                            ? "-fx-background-color: #FFFFFF;"
                            : "-fx-background-color: #F9F9F9;");
            });
            return row;
        });

        table.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                Platform.runLater(() -> {
                    for (Node node : table.lookupAll(".column-header")) {
                        node.setStyle("-fx-background-color: #F2F2F2;");
                    }
                });
            }
        });
    }

    // ================= DATABASE LOADER =================

    private static void loadDatabaseData(ObservableList<Activity> data) {

        String sql =
                "SELECT user_id, role, last_updated, IFNULL(status,'Offline') AS status " +
                        "FROM users";

        try (Connection con = (Connection) DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new Activity(
                        rs.getDate("last_updated").toString(),
                        rs.getString("user_id"),
                        rs.getString("role"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UI HELPERS =================

    private static TableColumn<Activity, Void> createButtonColumn(MainWindow mainWindow, String title, double width) {
        TableColumn<Activity, Void> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("VIEW");
            {
                btn.setStyle("-fx-background-color: #1f5a17; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnAction(e -> mainWindow.showAccountsView());
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setAlignment(Pos.CENTER);
            }
        });
        return col;
    }

    private static TableColumn<Activity, String> createFixedCol(String title, String prop, double width) {
        TableColumn<Activity, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setPrefWidth(width);
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setFont(Font.font("Arial", 14));
                setPadding(new Insets(0, 0, 0, 20));
            }
        });
        return col;
    }

    private static void setupHeader(TableView<Activity> table, TableColumn<Activity, ?> col, ObservableList<Activity> data) {
        Label label = new Label(col.getText());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        HBox box = new HBox(label);
        box.setAlignment(Pos.CENTER);
        col.setText(null);
        col.setGraphic(box);
    }

    private static void resizeColumns(TableView<Activity> table) {
        double avail = table.getWidth() - 20;
        double[] w = {150, 350, 150, 150, 150};
        for (int i = 0; i < table.getColumns().size(); i++)
            table.getColumns().get(i).setPrefWidth(avail * w[i] / 950);
    }

    private static Image loadSortIcon(String f) {
        return new Image(Paths.get("C:/FMDCINVision/icons/" + f).toUri().toString());
    }

    // ================= MODEL =================

    public static class Activity {
        private final String date, user, action, module;
        public Activity(String d, String u, String a, String m) {
            date = d; user = u; action = a; module = m;
        }
        public String getDate() { return date; }
        public String getUser() { return user; }
        public String getAction() { return action; }
        public String getModule() { return module; }
    }
}
