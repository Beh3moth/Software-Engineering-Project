package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Ask_players_number_controller extends ViewObservable implements GenericSceneController {
    @FXML
    private Button uno;
    @FXML
    private Button due;
    @FXML
    private Button tre;
    @FXML
    private Button quattro;

    @FXML
    public void initialize() {
        uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
        due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
        tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
        quattro.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onQuattroButtonClick);
    }

    private void onUnoButtonClick(Event event) {
        disableButton();
        notifyObserver(obs -> obs.onUpdatePlayersNumber(1));
    }

    private void onDueButtonClick(Event event) {
        disableButton();
        notifyObserver(obs -> obs.onUpdatePlayersNumber(2));
    }

    private void onTreButtonClick(Event event) {
        disableButton();
        notifyObserver(obs -> obs.onUpdatePlayersNumber(3));
    }

    private void onQuattroButtonClick(Event event) {
        disableButton();
        notifyObserver(obs -> obs.onUpdatePlayersNumber(4));
    }

    public void disableButton(){
        uno.setDisable(true);
        due.setDisable(true);
        tre.setDisable(true);
        quattro.setDisable(true);
    }
}
