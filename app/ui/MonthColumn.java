package app.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.data.ProjectBlock;
import app.data.Projects;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

public class MonthColumn extends VBox{

    private Projects projects;
    private int month;
    private List<ProjectBlock> monthList;
    private MonthsContainer monthsContainer;
    private Runnable onProjectsChanged;
    private Runnable onDragStarted;
    private Runnable onDragFinished;
    
    public MonthColumn(List<ProjectBlock> monthList, MonthsContainer monthsContainer, 
        Projects projects, int month, Runnable onProjectsChanged, Runnable onDragStarted, 
        Runnable onDragFinished) {
            this.monthList = monthList;
            this.monthsContainer = monthsContainer;
            this.projects = projects;
            this.month = month;
            this.onProjectsChanged = onProjectsChanged;
            this.onDragStarted = onDragStarted;
            this.onDragFinished = onDragFinished;
        
            setStyle("-fx-border-color: black;");
            setMinWidth(108);

            setOnDragOver(event -> {
                if(event.getGestureSource() != this && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            });

            setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    int projectIndex = Integer.parseInt(db.getString());
                    projects.moveBlock(projectIndex, month);
                    monthsContainer.refresh();
                    onProjectsChanged.run();
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            });

        refresh();
    }

    public void refresh() {
        getChildren().clear();

        List<ProjectBlockView> designProjects = new ArrayList<>();
        List<ProjectBlockView> researchProjects = new ArrayList<>();
        List<ProjectBlockView> manufactureProjects = new ArrayList<>();
        Iterator<ProjectBlock> iterator = monthList.iterator();
        while(iterator.hasNext()){
            ProjectBlock thisProject = iterator.next();
            switch(thisProject.getProjectType()){
                case "Design":
                    designProjects.add(new ProjectBlockView(thisProject, projects, 
                        onProjectsChanged, onDragStarted, onDragFinished));
                    break;
                case "Research":
                    researchProjects.add(new ProjectBlockView(thisProject, projects, 
                        onProjectsChanged, onDragStarted, onDragFinished));
                    break;
                case "Manufacture":
                    manufactureProjects.add(new ProjectBlockView(thisProject, projects, 
                        onProjectsChanged, onDragStarted, onDragFinished));
                    break;
                default:
                    break;
            }
        }

        getChildren().add(new MonthLabel("Month "+ month));
        getChildren().add(new Separator());
        getChildren().add(new Label(String.format("$%,d", projects.getCostForMonth(month))));
        getChildren().addAll(designProjects);
        getChildren().addAll(researchProjects);
        getChildren().addAll(manufactureProjects);
        getChildren().add(new AddProjectButton(monthsContainer, projects, month, onProjectsChanged));
    }
}
