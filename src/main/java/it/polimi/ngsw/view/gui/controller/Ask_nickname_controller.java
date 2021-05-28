package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.controller.ClientController;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.util.Map;

public class Ask_nickname_controller extends ViewObservable implements GenericSceneController{
    private final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button join;

    @FXML
    private TextField insertNickname;

    @FXML
    public void initialize() {
        join.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinButtonClick);
    }

        private void onJoinButtonClick(Event event) {
            String nickname = insertNickname.getText();

            notifyObserver(obs -> obs.onUpdateNickname(nickname));

        }
    }


