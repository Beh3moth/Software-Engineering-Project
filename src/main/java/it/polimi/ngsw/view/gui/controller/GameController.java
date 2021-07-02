package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class GameController extends ViewObservable implements GenericSceneController {

    private LightModel lightModel;
    List<LeaderCard> leaderCardList;

    @FXML
    private Button take_marble;
    @FXML
    private Button devCardMarket;
    @FXML
    private Button view_other_player;
    @FXML
    private Label moneyNumber;
    @FXML
    private Label shieldNumber;
    @FXML
    private Label stoneNumber;
    @FXML
    private Label slaveNumber;
    @FXML
    private ImageView FIRSTSHELF;
    @FXML
    private ImageView SECONDSHELF1;
    @FXML
    private ImageView SECONDSHELF2;
    @FXML
    private ImageView THIRDSHELF1;
    @FXML
    private ImageView THIRDSHELF2;
    @FXML
    private ImageView THIRDSHELF3;
    @FXML
    private ImageView FIRSTLEADERSHELF1;
    @FXML
    private ImageView FIRSTLEADERSHELF2;
    @FXML
    private ImageView SECONDLEADERSHELF1;
    @FXML
    private ImageView SECONDLEADERSHELF2;
    @FXML
    private ImageView DEVCARD1;
    @FXML
    private ImageView DEVCARD2;
    @FXML
    private ImageView DEVCARD3;
    @FXML
    public Pane faithPath;
    @FXML
    private ImageView papalCard1;
    @FXML
    private ImageView papalCard2;
    @FXML
    private ImageView papalCard3;
    @FXML
    private Label PVnumber;
    @FXML
    private Button reorder;
    @FXML
    private Button production;
    @FXML
    private ImageView leaderCardOneImageView;
    @FXML
    private ImageView leaderCardTwoImageView;

    //FaithPath


    @FXML
    public void initialize() {
        take_marble.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTakeMarbleButtonClick);
        view_other_player.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onViewOtherPlayerButtonClick);
        upDateValuesOfChest();
        upDateValuesOfWarehouse();
        upDateDevCard();
        upDatePapalCard();
        upDatePV();
        upDateLeaderShelf();
        upDateLeaderCard();
        devCardMarket.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDevCardMarket);
        ArrayList<Node> faithPathList = new ArrayList<>();
        faithPathList.add(faithPath.getChildren().get(24));
        setCrossPosition(lightModel.getCrossPosition(), lightModel.getLawrencePosition());
        reorder.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onReorder);
        production.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onProduction);
    }

    /**
     * The method changes the scene to the setProductionChoiceController scene.
     */
    public void onProduction(Event event) {
        ProductionChoiceController controller = new ProductionChoiceController();
        controller.setProductionChoiceController(lightModel);
        controller.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(controller, "production_choice_scene.fxml"));
    }

    /**
     * The method changes the scene to the ReorderWarehouseController scene.
     */
    public void onReorder(Event event) {
        ReorderWarehouseController controller = new ReorderWarehouseController();
        controller.setReorderWarehouseController(lightModel, lightModel.getFirstShelf(), lightModel.getSecondShelf(), lightModel.getSecondShelfNumber(), lightModel.getThirdShelf(), lightModel.getThirdShelfNumber(), lightModel.getFsr(), lightModel.getFsn(), lightModel.getSsr(), lightModel.getSsn(), null, true);
        controller.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(controller, "reorder_warehouse_scene.fxml"));
    }

    /**
     * The method sets the LightModel of the class.
     */
    public void setLightModel(LightModel lightModel) {
        this.lightModel = lightModel;
    }

    /**
     * The method changes the scene to the takeMarbleController scene.
     */
    public void onTakeMarbleButtonClick(Event event) {
        take_marble_controller takeMarbleController = new take_marble_controller(this.lightModel);
        takeMarbleController.addAllObservers(observers);
        SceneController.changeScene(takeMarbleController, "take_marble_scene.fxml");
    }

    /**
     * The method changes the scene to the DevCardSceneController scene.
     */
    public void onDevCardMarket(Event event) {
        DevCardSceneController controller = new DevCardSceneController();
        controller.setDevCardMarket(lightModel.getDevCardMarket(), lightModel);
        controller.addAllObservers(observers);
        SceneController.changeScene(controller, "dev_card_market_scene.fxml");
    }

    /**
     * The method updates the values of the chest's labels.
     */
    public void upDateValuesOfChest() {
        moneyNumber.setText(lightModel.getChest().get(Resource.MONEY).toString());
        shieldNumber.setText(lightModel.getChest().get(Resource.SHIELD).toString());
        stoneNumber.setText(lightModel.getChest().get(Resource.STONE).toString());
        slaveNumber.setText(lightModel.getChest().get(Resource.SLAVE).toString());
    }

    /**
     * The method updates the values of the warehouse.
     */
    public void upDateValuesOfWarehouse() {

        if (lightModel.getFirstShelf() == Resource.EMPTY) {
            FIRSTSHELF.setImage(null);
        } else {
            Image firstshelf = new Image("images/icons/" + getTypeResourceForImage(lightModel.getFirstShelf()) + ".png");
            FIRSTSHELF.setImage(firstshelf);
        }

        if (lightModel.getSecondShelfNumber() == 0) {
            SECONDSHELF1.setImage(null);
            SECONDSHELF2.setImage(null);
        } else {
            Image secondshelf = new Image("images/icons/" + getTypeResourceForImage(lightModel.getSecondShelf()) + ".png");
            SECONDSHELF1.setImage(secondshelf);
            if (lightModel.getSecondShelfNumber() == 2) {
                SECONDSHELF2.setImage(secondshelf);
            } else {
                SECONDSHELF2.setImage(null);
            }
        }

        if (lightModel.getThirdShelfNumber() == 0) {
            THIRDSHELF1.setImage(null);
            THIRDSHELF2.setImage(null);
            THIRDSHELF3.setImage(null);
        } else {
            Image thirdshelf = new Image("images/icons/" + getTypeResourceForImage(lightModel.getThirdShelf()) + ".png");
            THIRDSHELF1.setImage(thirdshelf);
            if (lightModel.getThirdShelfNumber() >= 2) {
                THIRDSHELF2.setImage(thirdshelf);
            } else {
                THIRDSHELF2.setImage(null);
                THIRDSHELF3.setImage(null);
            }
            if (lightModel.getThirdShelfNumber() == 3) {
                THIRDSHELF3.setImage(thirdshelf);
            } else {
                THIRDSHELF3.setImage(null);
            }
        }
    }

    /**
     * The method returns a string of the resource given as parameter. If the resource is a FAITHPOINT or if it is EMPTY the method returns null.
     * @param resource is the resource to receive the string.
     * @return a string of the resource given as parameter.
     */
    public String getTypeResourceForImage(Resource resource) {
        switch (resource) {
            case MONEY:
                return "coin";
            case STONE:
                return "stone";
            case SLAVE:
                return "servant";
            case SHIELD:
                return "shield";
            default:
                return null;
        }
    }

    /**
     * The method updates the images of the DevCards owned by the player.
     */
    public void upDateDevCard() {
        if (lightModel.getActiveDevCardMap().get(0) != null) {
            Image devcard1 = new Image("images/devCard/" + lightModel.getActiveDevCardMap().get(0).getCardColour().toString() + lightModel.getActiveDevCardMap().get(0).getDevLevel() + lightModel.getActiveDevCardMap().get(0).getPV() + ".png");
            DEVCARD1.setImage(devcard1);
        } else {
            DEVCARD1.setImage(null);
        }
        if (lightModel.getActiveDevCardMap().get(1) != null) {
            Image devcard2 = new Image("images/devCard/" + lightModel.getActiveDevCardMap().get(1).getCardColour().toString() + lightModel.getActiveDevCardMap().get(1).getDevLevel() + lightModel.getActiveDevCardMap().get(1).getPV() + ".png");
            DEVCARD2.setImage(devcard2);
        } else {
            DEVCARD2.setImage(null);
        }
        if (lightModel.getActiveDevCardMap().get(2) != null) {
            Image devcard3 = new Image("images/devCard/" + lightModel.getActiveDevCardMap().get(2).getCardColour().toString() + lightModel.getActiveDevCardMap().get(2).getDevLevel() + lightModel.getActiveDevCardMap().get(2).getPV() + ".png");
            DEVCARD3.setImage(devcard3);
        } else {
            DEVCARD3.setImage(null);
        }
    }

    /**
     * The method set the cross position image of the player and of Lawrence the Magnificent in case of single-player match.
     * @param crossPosition is the cross position of the player.
     * @param lawrencePosition is the cross position of Lawrence the Magnificent.
     */
    public void setCrossPosition(int crossPosition, Integer lawrencePosition) {
        if (lawrencePosition != null) {
            for (int i = 0; i < 25; i++) {
                ImageView imageView = (ImageView) faithPath.getChildren().get(i);

                if (i == (24 - crossPosition) && crossPosition != lawrencePosition) {
                    imageView.setImage(new Image("images/icons/croce.png"));
                } else if (i == (24 - lawrencePosition) && crossPosition != lawrencePosition) {
                    imageView.setImage(new Image("images/icons/croceLorenzo.png"));
                } else if (i == (24 - crossPosition) && i == (24 - lawrencePosition) && crossPosition == lawrencePosition) {
                    imageView.setImage(new Image("images/icons/lorenzoPlusPlayer.png"));
                } else {
                    imageView.setImage(null);
                }
            }
        } else {
            for (int i = 0; i < 25; i++) {
                ImageView imageView = (ImageView) faithPath.getChildren().get(i);
                if (i != (24 - crossPosition)) {
                    imageView.setImage(null);
                } else {
                    imageView.setImage(new Image("images/icons/croce.png"));
                }
            }
        }
    }

    /**
     * this method update the papalCard
     */
    public void upDatePapalCard() {
        if (lightModel.isPapalCardOne()) {
            papalCard1.setImage(new Image("images/icons/checkMark.png"));
        } else {
            papalCard1.setImage(new Image("images/icons/quadrato giallo.png"));
        }

        if (lightModel.isPapalCardTwo()) {
            papalCard2.setImage(new Image("images/icons/checkMark.png"));
        } else {
            papalCard2.setImage(new Image("images/icons/quadrato arancione.png"));
        }

        if (lightModel.isPapalCardThree()) {
            papalCard3.setImage(new Image("images/icons/checkMark.png"));
        } else {
            papalCard3.setImage(new Image("images/icons/quadrato rosso.png"));
        }
    }

    /**
     * this method update the pv of the player
     */
    public void upDatePV() {
        PVnumber.setText(String.valueOf((lightModel.getVictoryPoints())));
    }

    /**
     * this method update the special shelf on the warehouse
     */
    public void upDateLeaderShelf() {
        if (lightModel.getFsn() > 0) {
            Image img = new Image("images/icons/" + getTypeResourceForImage(lightModel.getFsr()) + ".png");
            FIRSTLEADERSHELF1.setImage(img);
            if (lightModel.getFsn() == 2) {
                FIRSTLEADERSHELF2.setImage(img);
            } else {
                FIRSTLEADERSHELF2.setImage(null);
            }
        } else {
            FIRSTLEADERSHELF1.setImage(null);
            FIRSTLEADERSHELF2.setImage(null);
        }

        if (lightModel.getSsn() > 0) {
            Image img = new Image("images/icons/" + getTypeResourceForImage(lightModel.getFsr()) + ".png");
            SECONDLEADERSHELF1.setImage(img);
            if (lightModel.getSsn() == 2) {
                SECONDLEADERSHELF2.setImage(img);
            } else {
                SECONDLEADERSHELF2.setImage(null);
            }
        } else {
            SECONDLEADERSHELF1.setImage(null);
            SECONDLEADERSHELF2.setImage(null);
        }
    }

    /**
     * this method permit to change the scene to the viewOherPlayer scene
     * @param event
     */
    public void onViewOtherPlayerButtonClick(Event event) {
        choose_nickname_player_controller cnp = new choose_nickname_player_controller();
        cnp.setLightModel(this.lightModel);
        cnp.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(cnp, "choose_nickname_player_scene.fxml"));
    }

    /**
     * this method update the leader card
     */
    public void upDateLeaderCard() {

        if (lightModel.getLeaderCardStatus()[0] != 0) {
            Image leader1 = new Image(("images/leader/" + lightModel.getLeaderCardList().get(0).getAbilityName() + lightModel.getLeaderCardList().get(0).getLeaderCardId() + ".png"));
            if (lightModel.getLeaderCardStatus()[0] == 2) {
                leaderCardOneImageView.setImage(leader1);
            } else {
                leaderCardOneImageView.setImage(null);
            }
        }
            else{
                leaderCardOneImageView.setImage(null);
            }


        if (lightModel.getLeaderCardStatus()[1] != 0) {
            Image leader1 = new Image(("images/leader/" + lightModel.getLeaderCardList().get(1).getAbilityName() + lightModel.getLeaderCardList().get(1).getLeaderCardId() + ".png"));
            if (lightModel.getLeaderCardStatus()[1] == 2) {
                leaderCardTwoImageView.setImage(leader1);
            } else {
                leaderCardTwoImageView.setImage(null);
            }
        }
        else{
            leaderCardTwoImageView.setImage(null);
        }

    }

}

