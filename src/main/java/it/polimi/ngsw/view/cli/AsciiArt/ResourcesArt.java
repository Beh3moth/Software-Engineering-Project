package it.polimi.ngsw.view.cli.AsciiArt;

import it.polimi.ngsw.model.DevCardColour;
import it.polimi.ngsw.model.Resource;

import java.io.Serializable;

public class ResourcesArt implements Serializable {

    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String money(){
        return (Color.ANSI_BRIGHT_BOLD_YELLOW.escape() + "@" + Color.RESET);
    }

    public String stone(){
        return (Color.ANSI_BRIGHT_BLACK.escape() + "@" + Color.RESET);
    }

    public String slave(){
        return (Color.ANSI_MAGENTA.escape() + "@" + Color.RESET);
    }

    public String shield(){
        return (Color.ANSI_CYAN.escape() + "@" + Color.RESET);
    }

    public String faithPoint(){
        return (Color.ANSI_RED.escape() + "@" + Color.RESET);
    }

    public String whiteMarble(){
        return ("@");
    }

    public String getColour(DevCardColour colour){
        switch (colour) {
            case BLUE:
                return Color.ANSI_BLUE.escape();
            case YELLOW:
                return Color.ANSI_YELLOW.escape();
            case PURPLE:
                return Color.ANSI_PURPLE.escape();
            case GREEN:
                return Color.ANSI_GREEN.escape();
            default:
                return Color.RESET;
        }
    }

    public String getColour(Resource resource){
        switch (resource) {
            case SHIELD:
                return Color.ANSI_BLUE.escape();
            case MONEY:
                return Color.ANSI_YELLOW.escape();
            case SLAVE:
                return Color.ANSI_PURPLE.escape();
            case STONE:
                return Color.ANSI_BRIGHT_BLACK.escape();
            case FAITHPOINT:
                return Color.ANSI_RED.escape();
            default:
                return Color.RESET;
        }
    }

    public String getReset(){
        return Color.RESET;
    }

}

