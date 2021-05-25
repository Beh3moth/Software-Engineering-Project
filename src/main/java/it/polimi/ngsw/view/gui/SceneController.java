package it.polimi.ngsw.view.gui;

import it.polimi.ngsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class SceneController extends ViewObservable {

    @FXML
    private Pane mainPane;

    @FXML
    public void initialize(){
        mainPane.setVisible(true);
    }
}
