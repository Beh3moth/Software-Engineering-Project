package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;


public class choose_nickname_player_controller extends ViewObservable implements GenericSceneController{
    @FXML
    private Button join;
    @FXML
    private Button backButton;
    @FXML
    private TextField insertNickname;

    private LightModel lightModel;

    @FXML
    public void initialize() {
        join.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinButtonClick);
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackButton);
    }

    private void onJoinButtonClick(Event event) {
        join.setDisable(true);
        String nickname = insertNickname.getText();
        new Thread(() -> notifyObserver(obs -> obs.onUpdateWatchInfo(nickname))).start();
    }

    private void onBackButton(Event event){
        join.setDisable(true);
        backButton.setDisable(true);
        GameController gameController = new GameController();
        gameController.addAllObservers(observers);
        gameController.setLightModel(lightModel);
        Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
    }

    public void setLightModel(LightModel lightModel){
        this.lightModel = lightModel;
    }
}
