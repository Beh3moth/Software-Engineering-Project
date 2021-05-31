package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.observer.ViewObserver;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class LobbyController extends ViewObservable implements GenericSceneController {

    private List<String> playersNicknames = new ArrayList<>();
    private int playersNumber;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label playersNicknamesLabel;
    @FXML
    private Label playersNumberLabel;

    @FXML
    public void initialize() {
        playersNicknamesLabel.setText(String.join(", ", playersNicknames));
        playersNumberLabel.setText(playersNicknames.size() + "/" + playersNumber);
    }

    public void setPlayersNicknames(List<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public void upDateValues(){
        playersNicknamesLabel.setText(String.join(", ", this.playersNicknames));
        playersNumberLabel.setText(this.playersNicknamesLabel + "/" + this.playersNumber);
    }

}
