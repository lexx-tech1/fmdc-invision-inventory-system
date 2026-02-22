import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Page_ProjectsViewLots {

    private static final TableView<LotModel> table = new TableView<>();
    private static final ObservableList<LotModel> data = FXCollections.observableArrayList();
    private static int currentBlockId;

    public static BorderPane createPage(MainWindow mainWindow, int blockId) {
        currentBlockId = blockId;

        BorderPane root = new BorderPane();
        root.setLeft(Panel_Sidebar.createSidebar(mainWindow, "Projects"));

        VBox topPanel = new VBox(
                Panel_Header.createHeader(mainWindow),
                Panel_Title.createTitle("PROJECT LOTS")
        );
        BorderPane rightSide = new BorderPane();
        rightSide.setTop(topPanel);

        loadData(blockId); // Load lots for this block

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        TableView<LotModel> lotsTable = createLotsTable();
        VBox.setVgrow(lotsTable, Priority.ALWAYS);
        mainContent.getChildren().setAll(lotsTable);

        ScrollPane scrollPane = Panel_Scrollbar.createScrollPane(mainContent);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        rightSide.setCenter(scrollPane);
        root.setCenter(rightSide);

        return root;
    }

    private static TableView<LotModel> createLotsTable() {
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(70);
        table.setStyle("-fx-selection-bar: transparent; -fx-background-color: transparent; -fx-table-cell-border-color: transparent;");

        TableColumn<LotModel, String> lotCol = new TableColumn<>("Lot");
        lotCol.setCellValueFactory(c -> c.getValue().nameProperty());
        lotCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label nameLabel = new Label(item);
                    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                    nameLabel.setPadding(new Insets(0, 0, 0, 20));
                    setGraphic(nameLabel);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

        table.getColumns().setAll(lotCol);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(LotModel item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    String color = (getIndex() % 2 == 0) ? "#F9F9F9" : "#FFFFFF";
                    setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; -fx-background-insets: 5;");
                } else {
                    setStyle("-fx-background-color: transparent;");
                }
            }
        });

        return table;
    }

    private static void loadData(int blockId) {
        data.clear();

        String sql = "SELECT lot_id, lot_name FROM lots WHERE block_id = ?";

        try (Connection conn = (Connection) DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, blockId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                data.add(new LotModel(
                        rs.getInt("lot_id"),
                        rs.getString("lot_name")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// LotModel class
class LotModel {
    private final javafx.beans.property.SimpleIntegerProperty lotId;
    private final javafx.beans.property.SimpleStringProperty name;

    LotModel(int lotId, String name) {
        this.lotId = new javafx.beans.property.SimpleIntegerProperty(lotId);
        this.name = new javafx.beans.property.SimpleStringProperty(name);
    }

    public int getLotId() { return lotId.get(); }
    public String getName() { return name.get(); }
    public javafx.beans.property.StringProperty nameProperty() { return name; }
}
