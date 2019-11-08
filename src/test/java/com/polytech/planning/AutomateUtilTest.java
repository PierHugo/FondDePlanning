package com.polytech.planning;

import com.polytech.planning.model.AutomateUtil;
import com.polytech.planning.model.exception.AutomateException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AutomateUtilTest {

    @Test
    void isCorrect() {

        // Right chain
        String rChain1 = "Ch. Lenté, 2hCM, 4hTPx3gr ; M.Slimane,18hCM,10hTDx3gr\n";
        String rChain2 = "Ch. Lenté, 2hCM, 4hTPx3gr ; M.Slimane,18hCM\n";
        String rChain3 = "Ch. Lenté, 2hCM, 4hTPx3gr \n";
        String rChain4 = "Ch. Lenté, 2hCM, 4hTPx3gr ; M.Slimane,10hTDx3gr\n";
        String rChain5 = "Ch. Lenté, 2hCM, 10hTDx3gr, 4hTPx3gr ; M.Slimane,18hCM,10hTDx3gr\n";
        String rChain6 = "Ch. Lenté, 2hCM, 10hTDx3gr, 4hTPx3gr\n";
        String rChain7 = "Ch. Lenté, 2hCM, 10hTDx3gr\n"; // must pass -> mundus year
        String rChain8 = "Ch. Lenté, 2hCM\n";
        String rChain9 = "Ch. Lenté, 10hTDx2gr\n";
        String rChain10 = "Ch. Lenté, 4hTPx2gr\n";
        String rChain11 = "Ch. Lenté, 10hTDx2gr, 4hTPx3gr\n";
        String rChain14 = "Ch. Lenté, 10hTDx2gr+Mundus, 4hTPx3gr+Mundus\n";
        String rChain12 = "Ch. Lenté, x3gr\n";
        String rChain13 = "Ch. Lenté, x2gr\n";
        String rChain15 = "Ch. Lenté\n";

        assertTrue(testChain(rChain1,3,1));
        assertTrue(testChain(rChain2,3,1));
        assertTrue(testChain(rChain3,3,1));
        assertTrue(testChain(rChain4,3,1));
        assertTrue(testChain(rChain5,3,1));
        assertTrue(testChain(rChain6,3,1));
        assertTrue(testChain(rChain7,1,1));
        assertTrue(testChain(rChain8,3,1));
        assertTrue(testChain(rChain9,3,1));
        assertTrue(testChain(rChain10,3,1));
        assertTrue(testChain(rChain11,3,1));
        assertTrue(testChain(rChain12,3,1));
        assertTrue(testChain(rChain13,3,1));
        assertTrue(testChain(rChain14,1,1));
        assertTrue(testChain(rChain15,1,1));
        assertTrue(testChain(null, 1,1));

        // Wrong chain
        String wChain1 = "Ch. Lenté, 2hCM, 4hTPxgr\n";
        String wChain2 = "Ch. Lenté, 2hCM, 10hTDx3gr, 4hTPx3gr  M.Slimane,18hCM,10hTDx3gr\n";
        String wChain3 = "Ch. Lenté, 2hCM, 10hTDx3gr, 4hTPx3gr ; M.Slimane,hCM,10hTDx3gr\n";
        String wChain4 = "Ch. Lenté, 2hCMhTPx3gr ; M.Slimane,18hCM,10hTDx3gr\n";
        String wChain5 = "Ch. Lenté 2hCM, 10hTDx3gr, 4hTPx3gr ; M.Slimane,18hCM,10hTDx3gr\n";
        String wChain6 = "Ch. Lenté, 2hCM, 10hTD, 4hTPx3gr ; M.Slimane,18hCM,10hTDx3gr\n";
        String wChain7 = "Ch. Lenté, 2hCM, 10hTDx3gr, 4hTPx3gr ; 18hCM,10hTDx3gr\n";
        String wChain8 = "Ch. Lenté, 2hCM, 4hTPx3gr, 10hTDx3gr ; M.Slimane,18hCM,10hTDx3gr\n";
        String wChain9 = "Ch. Lenté, 2hCM, 4hTPx3gr 10hTDx3gr ; M.Slimane,18hCM,10hTDx3gr\n";
        String wChain11 = "Ch. Lenté, 2hCM, 4hTDx3gr+Mundus, 10hTPx3gr+Mundus; M.Slimane,18hCM,10hTDx3gr\n";
        String wChain10 = "Ch. Lenté, 3gr\n";


        assertFalse(testChain(wChain1,1, 3));
        assertFalse(testChain(wChain2,1, 3));
        assertFalse(testChain(wChain3,1, 3));
        assertFalse(testChain(wChain4,1, 3));
        assertFalse(testChain(wChain5,1, 3));
        assertFalse(testChain(wChain6,1, 3));
        assertFalse(testChain(wChain7,1, 3));
        assertFalse(testChain(wChain8,1, 3));
        assertFalse(testChain(wChain9,1, 3));
        assertFalse(testChain(wChain10,1, 3));
        assertFalse(testChain(wChain11,3, 3));
    }

    public boolean testChain(String chain, int sheet, int row){
        try{
            return new AutomateUtil(chain, sheet,row).isCorrect();
        }catch(AutomateException e){
            return false;
        }
    }
}