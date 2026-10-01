package app.ui;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.data.ProjectBlock;
import app.data.Projects;
import app.data.ResearchBlock;
import app.data.DesignBlock;
import app.data.ManufactureBlock;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ProjectBlockView extends HBox {
    
    public ProjectBlockView(ProjectBlock project, Projects projects, Runnable onProjectsChanged, 
        Runnable onDragStarted, Runnable onDragFinished) {
        
        if(project instanceof DesignBlock) {
            getStyleClass().add("project-block");
        }
        if(project instanceof ResearchBlock){
            getStyleClass().addAll("project-block", "research-block");
        }
        if(project instanceof ManufactureBlock){
            getStyleClass().addAll("project-block", "manufacture-block");
        }

        Label costLabel = new Label(String.format("Cost: $%,d", project.getCost()));
        costLabel.setMaxWidth(Double.MAX_VALUE);
        costLabel.setAlignment(Pos.CENTER);

        List<ComboBox<?>> dropdowns = new ArrayList<>();

        ComboBox<String> partOptions = new ComboBox<>();
        partOptions.getStyleClass().add("dropdown");
        partOptions.setMaxWidth(Double.MAX_VALUE);
        partOptions.getItems().addAll(Projects.getDesignCosts().keySet());
        partOptions.setValue(project.getPart());
        partOptions.valueProperty().addListener((obs, oldValue, newValue) -> {
            project.setPart(newValue);
            costLabel.setText(String.format("Cost: $%,d", project.getCost()));
            onProjectsChanged.run();
        });
        dropdowns.add(partOptions);

        ComboBox<String> approachOptions = new ComboBox<>();
        approachOptions.getStyleClass().add("dropdown");
        approachOptions.setMaxWidth(Double.MAX_VALUE);
        if(project.getProjectType() == "Manufacture"){
            approachOptions.getItems().addAll(Projects.getManufactureApproachMultiplier().keySet());
        } else {
            approachOptions.getItems().addAll(Projects.getApproachMultiplier().keySet());
        }
        approachOptions.setValue(project.getApproach());
        approachOptions.valueProperty().addListener((obs, oldValue, newValue) -> {
            project.setApproach(newValue);
            costLabel.setText("Cost: $" + project.getCost());
            onProjectsChanged.run();
        });
        dropdowns.add(approachOptions);

        if(project.getProjectType() == "Manufacture") {
            ManufactureBlock manProject = (ManufactureBlock) project;
            ComboBox<Integer> quantityOptions = new ComboBox<>();
            quantityOptions.getStyleClass().add("dropdown");
            quantityOptions.setMaxWidth(Double.MAX_VALUE);
            List<Integer> quantities = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            quantityOptions.getItems().addAll(quantities);
            quantityOptions.setValue(manProject.getQuantity());
            quantityOptions.valueProperty().addListener((obs, oldValue, newValue) -> {
                manProject.setQuantity(newValue);
                costLabel.setText(String.format("Cost: $%,d", manProject.getCost()));
                onProjectsChanged.run();
            });
            dropdowns.add(quantityOptions);

        }

        CheckBox completedBox = new CheckBox("Completed");
        completedBox.setSelected(project.isComplete());
        completedBox.setOnAction(event -> {
            project.setComplete(completedBox.isSelected());
            onProjectsChanged.run();
        });
        
        VBox content = new VBox(2);
        content.getChildren().addAll(dropdowns);
        content.getChildren().add(costLabel);
        content.getChildren().add(completedBox);
        content.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(content, Priority.ALWAYS);

        Region dragHandle = new Region();
        dragHandle.getStyleClass().add("drag-handle");
        dragHandle.setPrefWidth(6);
        dragHandle.setMinWidth(6);
        dragHandle.setMaxWidth(6);

        getChildren().addAll(content, dragHandle);

        dragHandle.setOnDragDetected(event -> {
            onDragStarted.run();
            setOpacity(0.5);
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipContent = new ClipboardContent();
            clipContent.putString(String.valueOf(project.getIndex()));
            db.setContent(clipContent);
            WritableImage image = new WritableImage(
                (int) getBoundsInLocal().getWidth(),
                (int) getBoundsInLocal().getHeight()
            );
            snapshot(null, image);
    
            db.setDragView(
                image,
                image.getWidth() / 2,
                image.getHeight() / 2
            );
            event.consume();
        });

        setOnDragDone(event -> {
            setOpacity(1.0);
            onDragFinished.run();
            event.consume();
        });
    }
}
