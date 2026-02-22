import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Page_ProjectsViewBlocks {

    private static final TableView<BlockModel> table = new TableView<>();
    private static final ObservableList<BlockModel> data = FXCollections.observableArrayList();
    private static int currentSubdivisionId;

    public static BorderPane createPage(MainWindow mainWindow, int subdivisionId) {
        currentSubdivisionId = subdivisionId;

        BorderPane root = new BorderPane();
        root.setLeft(Panel_Sidebar.createSidebar(mainWindow, "Projects"));

        VBox topPanel = new VBox(
                Panel_Header.createHeader(mainWindow),
                Panel_Title.createTitle("PROJECT BLOCKS")
        );
        BorderPane rightSide = new BorderPane();
        rightSide.setTop(topPanel);

        loadData(subdivisionId); // Load blocks dynamically

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        TableView<BlockModel> blocksTable = createBlocksTable(mainWindow);
        VBox.setVgrow(blocksTable, Priority.ALWAYS);
        mainContent.getChildren().setAll(blocksTable);

        ScrollPane scrollPane = Panel_Scrollbar.createScrollPane(mainContent);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        rightSide.setCenter(scrollPane);
        root.setCenter(rightSide);

        return root;
    }

    private static TableView<BlockModel> createBlocksTable(MainWindow mainWindow) {
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(90);
        table.setStyle("-fx-selection-bar: transparent; -fx-background-color: transparent; -fx-table-cell-border-color: transparent;");

        // Block Name Column
        TableColumn<BlockModel, String> blockCol = new TableColumn<>("Block");
        blockCol.setCellValueFactory(c -> c.getValue().nameProperty());
        blockCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    BlockModel block = getTableView().getItems().get(getIndex());
                    Label nameLabel = new Label(item);
                    nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                    Label infoLabel = new Label("LOTS AVAILABLE: " + block.getLotCount());
                    infoLabel.setFont(Font.font("Arial", 14));
                    infoLabel.setTextFill(Color.GRAY);

                    VBox container = new VBox(2);
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.setPadding(new Insets(0, 0, 0, 20));
                    container.getChildren().setAll(nameLabel, infoLabel);
                    setGraphic(container);
                }
            }
        });

        // View Lots Button Column
        TableColumn<BlockModel, Void> viewCol = new TableColumn<>("");
        viewCol.setMaxWidth(120);
        viewCol.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("VIEW");

            {
                viewBtn.setStyle("-fx-background-color: #1f5a17; -fx-text-fill: white; -fx-background-radius: 5; -fx-min-width: 80; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;");
                viewBtn.setOnAction(e -> {
                    BlockModel block = getTableRow().getItem();
                    if (block != null) {
                        mainWindow.showProjectLots(block.getBlockId()); // Pass the blockId
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewBtn);
                setAlignment(Pos.CENTER);
            }
        });

        table.getColumns().setAll(blockCol, viewCol);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(BlockModel item, boolean empty) {
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

    // Load blocks from database with correct SQL
    private static void loadData(int subdivisionId) {
        data.clear();

        String sql = """
            SELECT 
                blocks.block_id, 
                blocks.block_name, 
                COUNT(lots.lot_id) AS lot_count
            FROM blocks
            LEFT JOIN lots ON lots.block_id = blocks.block_id
            WHERE blocks.subdivision_id = ?
            GROUP BY blocks.block_id, blocks.block_name
        """;

        try (Connection conn = (Connection) DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, subdivisionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                data.add(new BlockModel(
                        rs.getInt("block_id"),
                        rs.getString("block_name"),
                        rs.getInt("lot_count")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// BlockModel class
class BlockModel {
    private final javafx.beans.property.SimpleIntegerProperty blockId;
    private final javafx.beans.property.SimpleStringProperty name;
    private final javafx.beans.property.SimpleIntegerProperty lotCount;

    BlockModel(int blockId, String name, int lotCount) {
        this.blockId = new javafx.beans.property.SimpleIntegerProperty(blockId);
        this.name = new javafx.beans.property.SimpleStringProperty(name);
        this.lotCount = new javafx.beans.property.SimpleIntegerProperty(lotCount);
    }

    public int getBlockId() { return blockId.get(); }
    public String getName() { return name.get(); }
    public javafx.beans.property.StringProperty nameProperty() { return name; }
    public int getLotCount() { return lotCount.get(); }
}
