package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LogoController extends ViewObservable implements GenericSceneController {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button playButton;
    @FXML
    private Button quitButton;

    @FXML
    public void initialize() {
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPlayBtnClick);
        quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }

    /**
     * Handles click on Play button.
     *
     * @param event the mouse click event.
     */
    private void onPlayBtnClick(Event event) {
        SceneController.changeRootPane(observers, event, "server_connection_scene.fxml");
    }

}
