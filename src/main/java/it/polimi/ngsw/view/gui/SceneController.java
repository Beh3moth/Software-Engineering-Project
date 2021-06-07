package it.polimi.ngsw.view.gui;

import it.polimi.ngsw.network.client.Client;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.observer.ViewObserver;
import it.polimi.ngsw.view.gui.controller.GenericSceneController;
import it.polimi.ngsw.view.gui.controller.Generic_error_controller;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.List;

public class SceneController extends ViewObservable {

    private static Scene activeScene;
    private static GenericSceneController activeController;

    public static Scene getActiveScene() {
        return activeScene;
    }

    public static GenericSceneController getActiveController() {
        return activeController;
    }

    public static <T> T changeScene(List<ViewObserver> observerList, Scene scene, String fxml) {
        T controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            ((ViewObservable) controller).addAllObservers(observerList);

            activeController = (GenericSceneController) controller;
            activeScene = scene;
            activeScene.setRoot(root);
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
        }
        return controller;
    }

    public static <T> T changeScene(List<ViewObserver> observerList, String fxml) {
        return changeScene(observerList, activeScene, fxml);
    }


    public static <T> T changeScene(List<ViewObserver> observerList, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeScene(observerList, scene, fxml);
    }


    public static void changeScene(GenericSceneController controller, Scene scene, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));

            // Setting the controller BEFORE the load() method.
            loader.setController(controller);
            activeController = controller;
            Parent root = loader.load();

            activeScene = scene;
            activeScene.setRoot(root);
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
        }
    }


    public static void changeScene(GenericSceneController controller, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        changeScene(controller, scene, fxml);
    }


    public static void changeScene(GenericSceneController controller, String fxml) {
        changeScene(controller, activeScene, fxml);
    }

    public static void showProblem(String title, String message) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/generic_error_scene.fxml"));

        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
            return;
        }
        Generic_error_controller generic_error_controller = loader.getController();
        Scene errorScene = new Scene(parent);
        generic_error_controller.setScene(errorScene);
        generic_error_controller.setMessage(message);
        generic_error_controller.displayMessage();
    }


}
