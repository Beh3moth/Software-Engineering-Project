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

    /**
     * The method sets the players nicknames.
     * @param playersNicknames is the list of the players' nicknames.
     */
    public void setPlayersNicknames(List<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }

    /**
     * The method sets the number of players' label.
     */
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    /**
     * The method updates the labes of the players' nicknames and of the number of players in logged in the match.
     */
    public void upDateValues(){
        playersNicknamesLabel.setText(String.join(", ", this.playersNicknames));
        playersNumberLabel.setText(this.playersNicknamesLabel + "/" + this.playersNumber);
    }

}
