package dbmanager;

import exceptions.ChebiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ChebiDatabaseTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInChIFromChebID() {

        try {
            String expResult = "InChI=1S/C8H11NO3/c9-4-8(12)5-1-2-6(10)7(11)3-5/h1-3,8,10-12H,4,9H2/t8-/m0/s1";
            String result = ChebiDatabase.getIdentifierFromChebiID(1).getInchi();
            assertEquals(expResult, result);
        }
        catch(ChebiException ex) {
            fail("Check the exception " + ex);
        }
        catch (IOException ex) {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getAsciiName() {
        try {
            String expResult = "(R)-noradrenaline";
            String result = ChebiDatabase.getAsciiName(1);
            assertEquals(expResult, result);
        }
        catch(ChebiException ex) {
            fail("Check the exception " + ex);
        }
        catch (IOException ex) {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getInChIKeyFromChebID() {
        try {
            String expResult = "SFLSHLFXELFNJZ-QMMMGPOBSA-N";
            String result = ChebiDatabase.getInChIKeyFromChebID(1);
            assertEquals(expResult, result);
        }
        catch(ChebiException ex) {
            fail("Check the exception " + ex);
        }
        catch (IOException ex) {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getFormulaFromChebID() {
        try {
            String expResult = "C8H11NO3";
            String result = ChebiDatabase.getFormulaFromChebID(1);
            assertEquals(expResult, result);
        }
        catch(ChebiException ex) {
            fail("Check the exception " + ex);
        }
        catch (IOException ex) {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getMonoIsotopicMassFromChebID() {
        try {
            double expResult = 169.07389;
            Double result = ChebiDatabase.getMonoIsotopicMassFromChebID(1);
            assertEquals(expResult, result, 0.001d);
        }
        catch(ChebiException ex) {
            fail("Check the exception " + ex);
        }
        catch (IOException ex) {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getHMDBLink() {
        try {
            String expResult = "HMDB0000216";
            String result = ChebiDatabase.getHMDBLink(1);
            assertEquals(expResult, result);
        }
        catch(ChebiException ex) {
            fail("Check the exception " + ex);
        }
        catch (IOException ex) {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getChebiFromSmiles() {
        try {
            int expResult = 18357;
            int result = ChebiDatabase.getChebiFromSmiles("NC[C@H](O)c1ccc(O)c(O)c1",0.95d);
            assertEquals(expResult, result);
        }
        catch(ChebiException ex) {
            fail("Check the exception " + ex);
        }
        catch (IOException ex) {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getChebiNumber() {
            int expResult = 1;
            int result = ChebiDatabase.getChebiNumber("CHEBI:1");
            assertEquals(expResult, result);

    }
}