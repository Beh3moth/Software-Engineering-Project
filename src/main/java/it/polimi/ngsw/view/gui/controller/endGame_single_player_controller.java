package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class endGame_single_player_controller extends ViewObservable implements GenericSceneController{

    private int PV;
    private int lawrenceCrossPosition;
    boolean Winner;

    @FXML
    private Button quitButton;
    @FXML
    private Label PVnumber;
    @FXML
    private Label winner;


    public endGame_single_player_controller(int PV, int lawrenceCrossPosition, boolean winner){
        this.PV = PV;
        this.lawrenceCrossPosition = lawrenceCrossPosition;
        this.Winner = winner;
    }

    public void initialize(){
        upDatePV();
        upDateWinner();
        quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }

    public void upDatePV(){
        PVnumber.setText(String.valueOf(PV));
    }

    public void upDateWinner(){
        if(Winner){
            winner.setText("hai vinto!!!");
        }
        else{
            winner.setText("hai perso");
        }
    }

}
