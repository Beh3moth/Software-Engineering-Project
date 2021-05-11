package it.polimi.ngsw.view.cli.AsciiArt;

import it.polimi.ngsw.model.DevCard;

public class RectangleArt {

    public String getRightTopAngle(DevCard devCard){
        return (getDevCardColour(devCard) + "╗" + Color.RESET );
    }

    public String getLeftTopAngle(DevCard devCard){
        return (getDevCardColour(devCard) + "╔" + Color.RESET );
    }

    public String getLeftDownAngle(DevCard devCard){
        return (getDevCardColour(devCard) + "╚" + Color.RESET );
    }

    public String getRightDownAngle(DevCard devCard){
        return (getDevCardColour(devCard) + "╝" + Color.RESET );
    }

    public String getTopDownBorder(DevCard devCard){
        return (getDevCardColour(devCard) + "═" + Color.RESET);
    }

    public String getLeftRightBorder(DevCard devCard){
        return (getDevCardColour(devCard) + "║" + Color.RESET);
    }

    public String getDevCardColour(DevCard devCard){
        switch (devCard.getCardColour()) {
            case BLUE:
                return Color.ANSI_BLUE.escape();
            case GREEN:
                return Color.ANSI_GREEN.escape();
            case PURPLE:
                return Color.ANSI_PURPLE.escape();
            case YELLOW:
                return Color.ANSI_YELLOW.escape();
            default:
                break;
        }
        return Color.RESET;
    }

}
