package app.ui;
import java.io.File;
import java.io.IOException;

import app.data.Projects;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindow extends Application {

    private Projects projects;
    private Label total;
    private Label completedTotal;
    private Label initialCost;
    private Label designTotal;
    private Label researchTotal;
    private Label manufactureTotal;
    private TextField costInput;
    private MonthsContainer content;
    private DeleteArea deleteArea;

    @Override
    public void start(Stage stage) {
        
        // Initialise projects data
        projects = new Projects();

        // Topbar Elements

        // -- Initial cost input field
        costInput = new TextField();
        costInput.setTextFormatter(CurrencyTextFormatter.create());
        costInput.setText("$0");
        costInput.setMaxWidth(100);
        costInput.textProperty().addListener((observable, oldValue, newValue) -> {
            int value = CurrencyTextFormatter.getValue(newValue);
            projects.setInitialCost(value);
            refreshUI();
        });
        
        // -- Cost Labels
        initialCost = new Label("Initial Cost:");
        total = new Label(String.format("Total Cost: $%,d", projects.getTotalCost()));
        completedTotal = new Label(String.format("Completed Cost: $%,d", projects.getCompletedCost()));
        designTotal = new Label(String.format("Design Cost: $%,d", projects.getCostByType("Design")));
        researchTotal = new Label(String.format("Research Cost: $%,d", projects.getCostByType("Research")));
        manufactureTotal = new Label(String.format("Manufacture Cost: $%,d", projects.getCostByType("Manufacture")));

        // -- Project Type Filter
        ComboBox<String> filter = new ComboBox<>();
        filter.getItems().addAll("All", "Design", "Research", "Manufacture");
        filter.setValue("All");
        filter.setOnAction(event -> {
            content.setProjectFilter(filter.getValue());
        });

        // -- Save & Load
        Button saveButton = new Button("Save Plan");
        saveButton.setOnAction(event -> saveProjects(stage));

        Button loadButton = new Button("Load Plan");
        loadButton.setOnAction(event -> loadProjects(stage));
        
        // Create Topbar & Add Elements
        HBox topbar = new HBox(10);
        topbar.getChildren().addAll(
            initialCost,
            costInput,
            total, 
            completedTotal,
            designTotal, 
            researchTotal, 
            manufactureTotal,
            filter, 
            saveButton,
            loadButton
            );
        
        // -- ScrollPane Elements --
        
        // -- Initialise MonthsContainer
        content = new MonthsContainer(
            projects,
            () -> refreshUI(),
            () -> showDeleteArea(),
            () -> hideDeleteArea()
        );
        
        

        // Create Scrollable Pane
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setOnScroll(event -> {
            scrollPane.setVvalue(
                scrollPane.getVvalue() - event.getDeltaY() / 1000.0
            );
            event.consume();
        });

        // -- Lower Elements --
       
        // -- Initialise Delete Area
        deleteArea = new DeleteArea(
            projects, 
            () -> refreshUI()
        );
        deleteArea.setPrefHeight(50);
        deleteArea.setVisible(false);
        deleteArea.setManaged(false);

        // Create Border Pane
        BorderPane root = new BorderPane();

        // Assign Elements to Border Pane
        root.setTop(topbar);
        root.setCenter(scrollPane);
        root.setBottom(deleteArea);

        // Scene Creation
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
            getClass().getResource("main.css").toExternalForm()
        );

        // Stage Creation
        stage.setTitle("Project Planner");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setMinHeight(300);
        stage.show();
        stage.setHeight(300);
    }

    // Show & Hide Delete Area
    public void showDeleteArea() {
        deleteArea.setVisible(true);
        deleteArea.setManaged(true);
    }

    public void hideDeleteArea() {
        deleteArea.setVisible(false);
        deleteArea.setManaged(false);
    }

    // Save & Load Projects
    private void saveProjects(Stage stage) {
        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("SaveProject");
        saveChooser.setInitialFileName("projects.project");

        File directory = new File("saves");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        saveChooser.setInitialDirectory(directory);

        saveChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Project Files (*.project)", "*.project")
        );

        File file = saveChooser.showSaveDialog(stage);
        if (file != null) {
            try {
            projects.saveToFile(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProjects(Stage stage) {
        FileChooser loadChooser = new FileChooser();

        loadChooser.setTitle("Load Projects");

        File directory = new File("saves");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        loadChooser.setInitialDirectory(directory);

        loadChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Project Files (*.project)", "*.project"
            )
        );

        File file = loadChooser.showOpenDialog(stage);

        if (file != null) {

            try {
                projects.loadFromFile(file.getAbsolutePath());
                costInput.setText(Integer.toString(projects.getIntialCost()));
                stage.sizeToScene();
                refreshUI();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // UI Refresh
    private void refreshUI(){
        content.refresh();
        total.setText(String.format("Total Cost: $%,d", projects.getTotalCost()));
        completedTotal.setText(String.format("Completed Cost: $%,d", projects.getCompletedCost()));
        designTotal.setText(String.format("Design Cost: $%,d", projects.getCostByType("Design")));
        researchTotal.setText(String.format("Research Cost: $%,d", projects.getCostByType("Research")));
        manufactureTotal.setText(String.format("Manufacture Cost: $%,d", projects.getCostByType("Manufacture")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}