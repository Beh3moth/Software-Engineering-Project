package it.polimi.ngsw.view.cli.AsciiArt;

public class ResourcesArt {

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

}
