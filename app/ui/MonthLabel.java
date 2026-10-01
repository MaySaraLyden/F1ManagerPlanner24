package app.ui;

import javafx.scene.control.Label;

public class MonthLabel extends Label{
    
    public MonthLabel(String title){
        super(title);
        getStyleClass().add("month-label");
    }
}
