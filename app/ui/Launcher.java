package app.ui;

import javafx.application.Application;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Launcher {
    
    public static void main(String[] args) {
        Application.launch(MainWindow.class, args);
    }

    public static File getApplicationDirectory() {
        try {
            Path location = Path.of(
                Launcher.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
            );
        
            if (!java.nio.file.Files.isDirectory(location)) {
                return location.getParent().getParent().toFile();
            }

            if (location.endsWith("classes")) {
                return location.getParent().getParent().toFile();
            }

            return location.toFile();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(
                "Cannot locate application directory", e
            );
        }
    }
}
