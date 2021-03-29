package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


/**
 * Unit test for FaithPath class.
 */
public class FaithPathTest {

    FaithPath faithPath = new FaithPath();

    @Test
    public void increaseCrossPositionTest() {
        for(int i=1; i<=24; i++){
            faithPath.increaseCrossPosition();
            assertEquals(i, faithPath.getCrossPosition());
        }

    }

    @Test
    public void increaseBlackCrossPositionTest() {
        for(int i=1; i<=24; i++){
            faithPath.increaseBlackCrossPosition();
            assertEquals(i, faithPath.getBlackCrossPosition());
        }
    }

    @Test
    public void getPVTest(){
        for(int i=1; i<=24; i++){
            faithPath.increaseCrossPosition();
            if(i==3){
                assertEquals(1, faithPath.getPV());
            }
            if(i==6){
                assertEquals(3, faithPath.getPV());
            }
            if(i==9){
                assertEquals(7, faithPath.getPV());
            }
            if(i==12){
                assertEquals(13, faithPath.getPV());
            }
            if(i==15){
                assertEquals(22, faithPath.getPV());
            }
            if(i==18){
                assertEquals(34, faithPath.getPV());
            }
            if(i==21){
                assertEquals(50, faithPath.getPV());
            }
            if(i==24){
                assertEquals(70, faithPath.getPV());
            }
        }
    }


}
