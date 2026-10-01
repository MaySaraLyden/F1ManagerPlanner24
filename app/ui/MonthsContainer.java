package app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import app.data.ProjectBlock;
import app.data.Projects;
import javafx.scene.layout.HBox;

public class MonthsContainer extends HBox {

    private Projects projects;
    private Runnable onProjectsChanged;
    private Runnable onDragStarted;
    private Runnable onDragFinished;
    private String projectFilter = "All";

    public MonthsContainer(Projects projects, Runnable onProjectsChanged, Runnable onDragStarted, Runnable onDragFinished){
        this.projects = projects;
        this.onProjectsChanged = onProjectsChanged;
        this.onDragStarted = onDragStarted;
        this.onDragFinished = onDragFinished;
        refresh();
    }

    public void setProjectFilter(String filter) {
        projectFilter = filter;
        refresh();
    }
    public void refresh() {
        getChildren().clear();
        List<MonthColumn> months = new ArrayList<>();
        for(int month = 1; month <= 12; month += 1){
            List<ProjectBlock> monthList = projects.getBlocksForMonth(month);
            ListIterator<ProjectBlock> listIterator = monthList.listIterator();
            while(listIterator.hasNext()) {
                ProjectBlock block = listIterator.next();
                if(!projectFilter.equals("All") && !projectFilter.equals(block.getProjectType())) {
                    listIterator.remove();
                }
            }
            MonthColumn thisMonth = new MonthColumn(monthList, this, projects, month, 
                onProjectsChanged, onDragStarted, onDragFinished);
            months.add(thisMonth);
        }

        getChildren().addAll(months);
    }
}
