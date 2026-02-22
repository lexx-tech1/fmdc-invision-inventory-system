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
import javafx.beans.property.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Page_Projects {

    private static final TableView<ProjectModel> table = new TableView<>();
    private static final ObservableList<ProjectModel> data = FXCollections.observableArrayList();

    public static BorderPane createPage(MainWindow mainWindow) {

        BorderPane root = new BorderPane();
        root.setLeft(Panel_Sidebar.createSidebar(mainWindow, "Projects"));

        BorderPane rightSide = new BorderPane();
        VBox topPanel = new VBox(
                Panel_Header.createHeader(mainWindow),
                Panel_Title.createTitle("PROJECTS")
        );
        rightSide.setTop(topPanel);

        loadData(); // Load subdivisions

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #FFFFFF;");
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        TableView<ProjectModel> projectsTable = createProjectsTable(mainWindow);
        VBox.setVgrow(projectsTable, Priority.ALWAYS);
        mainContent.getChildren().setAll(projectsTable);

        ScrollPane scrollPane = Panel_Scrollbar.createScrollPane(mainContent);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        rightSide.setCenter(scrollPane);
        root.setCenter(rightSide);

        return root;
    }

    private static TableView<ProjectModel> createProjectsTable(MainWindow mainWindow) {

        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(90);

        table.setStyle(
                "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent;"
        );

        // Subdivision Column
        TableColumn<ProjectModel, String> subdivisionCol = new TableColumn<>("Subdivision");
        subdivisionCol.setCellValueFactory(c -> c.getValue().subdivisionProperty());
        subdivisionCol.setCellFactory(col -> new TableCell<>() {
            private final Label nameLabel = new Label();
            private final VBox box = new VBox(4);

            {
                nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                box.setAlignment(Pos.CENTER_LEFT);
                box.setPadding(new Insets(0, 0, 0, 20));
                box.getChildren().add(nameLabel);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                ProjectModel project = getTableRow().getItem();
                if (project == null) {
                    setGraphic(null);
                    return;
                }

                nameLabel.setText(project.getSubdivision());
                setGraphic(box);
                setText(null);
            }
        });

        // View Blocks Button Column
        TableColumn<ProjectModel, Void> viewCol = new TableColumn<>("");
        viewCol.setMaxWidth(120);
        viewCol.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("VIEW");

            {
                viewBtn.setStyle(
                        "-fx-background-color: #1f5a17;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 16px;" +
                                "-fx-cursor: hand;"
                );
                viewBtn.setOnAction(e -> {
                    ProjectModel sub = getTableRow().getItem();
                    if (sub != null) {
                        mainWindow.showProjectBlocks(sub.getSubdivisionId());
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

        table.getColumns().setAll(subdivisionCol, viewCol);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ProjectModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    getStyleClass().remove("project-row");
                } else if (!getStyleClass().contains("project-row")) {
                    getStyleClass().add("project-row");
                }
            }
        });
        table.setSelectionModel(null);
        return table;
    }

    private static void loadData() {
        data.clear();

        String sql = "SELECT subdivision_id, subdivision_name FROM subdivisions";

        try (Connection conn = (Connection) DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new ProjectModel(
                        rs.getInt("subdivision_id"),
                        rs.getString("subdivision_name")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ProjectModel for Subdivision
class ProjectModel {

    private final IntegerProperty subdivisionId;
    private final StringProperty subdivision;

    public ProjectModel(int subdivisionId, String subdivision) {
        this.subdivisionId = new SimpleIntegerProperty(subdivisionId);
        this.subdivision = new SimpleStringProperty(subdivision);
    }

    public int getSubdivisionId() { return subdivisionId.get(); }
    public String getSubdivision() { return subdivision.get(); }

    public StringProperty subdivisionProperty() { return subdivision; }
}
