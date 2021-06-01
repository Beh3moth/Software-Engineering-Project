package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class distribute_initial_resources_controller extends ViewObservable implements GenericSceneController{

    private int resourceNumber, firstPos, secondPos;
    private Resource resourceOne, resourceTwo;
    private int n;

    @FXML
    private Button money;
    @FXML
    private Button stone;
    @FXML
    private Button shield;
    @FXML
    private Button slave;
    @FXML
    private Button uno;
    @FXML
    private Button due;
    @FXML
    private Button tre;

    public distribute_initial_resources_controller(){
        this.firstPos = 0;
        this.secondPos = 0;
        this.resourceOne = Resource.EMPTY;
        this.resourceTwo = Resource.EMPTY;
        this.n = 1;
    }
    @FXML
    public void initialize(){
            activeButton(money);
            activeButton(shield);
            activeButton(stone);
            activeButton(slave);
            disableButton(uno);
            disableButton(due);
            disableButton(tre);
            money.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMoneyButtonClick);
            slave.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlaveButtonClick);
            stone.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStoneButtonClick);
            shield.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShieldButtonClick);
    }

   public void onMoneyButtonClick(Event event){
        disableButton(money);
        disableButton(slave);
        disableButton(stone);
        disableButton(shield);
        if(n == 1){
            this.resourceOne = Resource.MONEY;
            activeButton(uno);
            activeButton(due);
            activeButton(tre);
            uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
            due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
            tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
        }
        if(n == 2){
            this.resourceTwo = Resource.MONEY;
            if(this.resourceOne == Resource.MONEY){
                switch(firstPos){
                    case 1:
                        activeButton(uno);
                        break;
                    case 2:
                        activeButton(due);
                        break;
                    case 3:
                        activeButton(tre);
                        break;
                }
            }
            else{
                switch(firstPos){
                    case 1:
                        activeButton(due);
                        activeButton(tre);
                        break;
                    case 2:
                        activeButton(uno);
                        activeButton(tre);
                        break;
                    case 3:
                        activeButton(uno);
                        activeButton(due);
                        break;
                }
            }
            uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
            due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
            tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
        }
   }

   public void onSlaveButtonClick(Event event){
       disableButton(money);
       disableButton(slave);
       disableButton(stone);
       disableButton(shield);
       if(n == 1){
           this.resourceOne = Resource.SLAVE;
           activeButton(uno);
           activeButton(due);
           activeButton(tre);
           uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
           due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
           tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
       }
       if(n == 2){
           this.resourceTwo = Resource.SLAVE;
           if(this.resourceOne == Resource.SLAVE){
               switch(firstPos){
                   case 1:
                       activeButton(uno);
                       break;
                   case 2:
                       activeButton(due);
                       break;
                   case 3:
                       activeButton(tre);
                       break;
               }
           }
           else{
               switch(firstPos){
                   case 1:
                       activeButton(due);
                       activeButton(tre);
                       break;
                   case 2:
                       activeButton(uno);
                       activeButton(tre);
                       break;
                   case 3:
                       activeButton(uno);
                       activeButton(due);
                       break;
               }
           }
           uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
           due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
           tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
       }
   }

   public void onStoneButtonClick(Event event){
       disableButton(money);
       disableButton(slave);
       disableButton(stone);
       disableButton(shield);
       if(n == 1){
           this.resourceOne = Resource.STONE;
           activeButton(uno);
           activeButton(due);
           activeButton(tre);
           uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
           due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
           tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
       }
       if(n == 2){
           this.resourceTwo = Resource.STONE;
           if(this.resourceOne == Resource.STONE){
               switch(firstPos){
                   case 1:
                       activeButton(uno);
                       break;
                   case 2:
                       activeButton(due);
                       break;
                   case 3:
                       activeButton(tre);
                       break;
               }
           }
           else{
               switch(firstPos){
                   case 1:
                       activeButton(due);
                       activeButton(tre);
                       break;
                   case 2:
                       activeButton(uno);
                       activeButton(tre);
                       break;
                   case 3:
                       activeButton(uno);
                       activeButton(due);
                       break;
               }
           }
           uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
           due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
           tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
       }
   }

   public void onShieldButtonClick(Event event){
       disableButton(money);
       disableButton(slave);
       disableButton(stone);
       disableButton(shield);
       if(n == 1){
           this.resourceOne = Resource.SHIELD;
           activeButton(uno);
           activeButton(due);
           activeButton(tre);
           uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
           due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
           tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
       }
       if(n == 2){
           this.resourceTwo = Resource.SHIELD;
           if(this.resourceOne == Resource.SHIELD){
               switch(firstPos){
                   case 1:
                       activeButton(uno);
                       break;
                   case 2:
                       activeButton(due);
                       break;
                   case 3:
                       activeButton(tre);
                       break;
               }
           }
           else{
               switch(firstPos){
                   case 1:
                       activeButton(due);
                       activeButton(tre);
                       break;
                   case 2:
                       activeButton(uno);
                       activeButton(tre);
                       break;
                   case 3:
                       activeButton(uno);
                       activeButton(due);
                       break;
               }
           }
           uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
           due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
           tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
       }
   }

   public void onUnoButtonClick(Event event){
        if(n == 1){
            this.firstPos = 1;}
        if(n == 2){
            this.secondPos = 1;
            new Thread(() -> notifyObserver(obs -> obs.onUpdatePickedResources(resourceNumber, resourceOne, resourceTwo, firstPos, secondPos))).start();
        }
        if(resourceNumber == 1){
            new Thread(() -> notifyObserver(obs -> obs.onUpdatePickedResources(resourceNumber, resourceOne, null, firstPos, 0))).start();
        }
        if(resourceNumber == 2){
            n++;
            initialize();
        }
   }

    public void onDueButtonClick(Event event){
        if(n == 1){
            this.firstPos = 2;}
        if(n == 2){
            this.secondPos = 2;
            new Thread(() -> notifyObserver(obs -> obs.onUpdatePickedResources(resourceNumber, resourceOne, resourceTwo, firstPos, secondPos))).start();}
        if(resourceNumber == 1){
            new Thread(() -> notifyObserver(obs -> obs.onUpdatePickedResources(resourceNumber, resourceOne, null, firstPos, 0))).start();
        }
        if(resourceNumber == 2){
            n++;
            initialize();
        }
    }

    public void onTreButtonClick(Event event){
        if(n == 1){
            this.firstPos = 3;}
        if(n == 2){
            this.secondPos = 3;
            new Thread(() -> notifyObserver(obs -> obs.onUpdatePickedResources(resourceNumber, resourceOne, resourceTwo, firstPos, secondPos))).start();}
        if(resourceNumber == 1){
            new Thread(() -> notifyObserver(obs -> obs.onUpdatePickedResources(resourceNumber, resourceOne, null, firstPos, 0))).start();
        }
        if(resourceNumber == 2){
            n++;
            initialize();
        }
    }

   public void disableButton(Button button){button.setDisable(true);}
   public void activeButton(Button button){button.setDisable(false);}

    public void setResourceNumber(int resourceNumber){this.resourceNumber = resourceNumber;}
}
