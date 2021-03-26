package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ngsw.model.FaithPath;
import org.junit.jupiter.api.Test;


/**
 * Unit test for FaithPath class.
 */
public class FaithPathTest {

    FaithPath faithPath = new FaithPath();

    @Test
    public void increaseCrossPositionTest() {
        faithPath.increaseCrossPosition();
        assertEquals(1, faithPath.getCrossPosition());
    }

    @Test
    public void increaseBlackCrossPositionTest() {
        faithPath.increaseBlackCrossPosition();
        assertEquals(1, faithPath.getBlackCrossPosition());
    }


}
