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

public class ServerConnectionController extends ViewObservable implements GenericSceneController {

    private final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField serverAddress;

    @FXML
    private TextField serverPort;

    @FXML
    private Button connect;

    @FXML
    private Button back;

    @FXML
    public void initialize() {
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectButtonClick);
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackClick);
    }

    private void onConnectButtonClick(Event event) {
        String address = serverAddress.getText();
        String port = serverPort.getText();

        boolean isValidIpAddress = ClientController.isValidIpAddress(address);
        boolean isValidPort = ClientController.isValidPort(port);

        serverAddress.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValidIpAddress);
        serverPort.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValidPort);

        if (isValidIpAddress && isValidPort) {
            back.setDisable(true);
            connect.setDisable(true);
            Map<String, String> serverInfo = Map.of("address", address, "port", port);
            new Thread(() -> notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo))).start();
        }

    }

    private void onBackClick(Event event) {
        back.setDisable(true);
        connect.setDisable(true);
        SceneController.changeScene(observers, event, "logo_scene.fxml");
    }

}
