package app.ui;

import java.util.Optional;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import app.data.Projects;

public class AddProjectButton extends Button{

    private MonthsContainer monthsContainer;
    private Runnable onProjectsChanged;
    
    public AddProjectButton(MonthsContainer monthsContainer, Projects projects, int month, Runnable onProjectsChanged){
        super("Add Project");
        this.monthsContainer = monthsContainer;
        this.onProjectsChanged = onProjectsChanged;
        getStyleClass().add("add-project-button");
        setMaxWidth(Double.MAX_VALUE);
        setOnAction(event -> showAddProjectDialog(projects, month));
    }

    private void showAddProjectDialog(Projects projects, int month) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Project");
        dialog.setHeaderText("Select project type");

        ComboBox<String> typeDropdown = new ComboBox<>();

        typeDropdown.getItems().addAll(
            "Design", 
            "Research", 
            "Manufacture"
        );

        typeDropdown.setValue("Design");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(
            addButtonType, 
            ButtonType.CANCEL
        );

        VBox content = new VBox(10);

        Label label = new Label("Project type:");

        content.getChildren().addAll(
            label,
            typeDropdown
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == addButtonType) {
                return typeDropdown.getValue();
            }
    
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(type -> {
            if (type.equals("Design")) {
                projects.createDesignBlock(month);
                monthsContainer.refresh();
                onProjectsChanged.run();
            }
            if (type.equals("Research")) {
                projects.createResearchBlock(month);
                monthsContainer.refresh();
                onProjectsChanged.run();
            }
            if (type.equals("Manufacture")) {
                projects.createManufactureBlock(month);
                monthsContainer.refresh();
                onProjectsChanged.run();
            }
        });
    }
}
