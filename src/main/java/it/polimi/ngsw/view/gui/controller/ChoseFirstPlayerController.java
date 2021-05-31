package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ChoseFirstPlayerController extends ViewObservable implements GenericSceneController {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField playerNameTextField;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButton);
    }

    private void onConfirmButton(Event event) {
        String firstPlayerNickname = playerNameTextField.getText();
        new Thread(() -> notifyObserver(obs -> obs.onUpdateFirstPlayer(firstPlayerNickname))).start();
    }


}
