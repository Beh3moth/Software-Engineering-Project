package it.polimi.ngsw.view.cli.AsciiArt;

public enum Color {

    ANSI_RED("\u001B[31m"),
    ANSI_MAGENTA("\033[0;35m"),
    ANSI_CYAN("\033[0;36m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BRIGHT_BOLD_YELLOW("\033[1;93m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_BRIGHT_BLACK("\033[0;90m"),
    ANSI_PURPLE("\u001B[35m");


    static final String RESET = "\u001B[0m";

    private String escape;

    Color(String escape) {
        this.escape = escape;
    }

    public String escape(){
        return escape;
    }

}
