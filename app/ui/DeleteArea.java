package app.ui;
import app.data.Projects;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

public class DeleteArea extends StackPane{
    
    public DeleteArea(Projects projects, Runnable onProjectsChanged) {

        Label label = new Label("Drop here to delete");
        getChildren().add(label);

        setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                int projectIndex = Integer.parseInt(db.getString());
                projects.deleteProjectBlock(projectIndex);
                onProjectsChanged.run();
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
}
