package it.polimi.ngsw.model;
//package Marble
//import enum delle marble color
//import le enum delle risorse
//import libreria player

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The abstract class of the marbles
 */
abstract class Marble{
    private Resource typeResource;
    private MarbleColour marblecolour;

    public void actionMarble(Player player){
    }

    public  Resource getResource(){return typeResource;};
}