package dbmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;

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

        System.out.println(System.getProperty("user.dir"));
        System.out.println("getInChIFromChebIDTest");
        try {
            String expResult = "InChI=1S/C8H11NO3/c9-4-8(12)5-1-2-6(10)7(11)3-5/h1-3,8,10-12H,4,9H2/t8-/m0/s1";
            String result = ChebiDatabase.getInChIFromChebID(1);
            System.out.println(result);
            assertEquals(expResult, result);
        }
        catch(ChebiWebServiceFault_Exception ex)
        {
            fail("Check the exception " + ex);
        }
    }

    @Test
    void getIdentfiersFromChebiId() {
        fail("Prototype");
    }

    @Test
    void getAsciiName() {
        fail("Prototype");
    }

    @Test
    void getInChIKeyFromChebID() {
        fail("Prototype");
    }

    @Test
    void getFormulaFromChebID() {
        fail("Prototype");
    }

    @Test
    void getMonoIsotopicMassFromChebID() {
        fail("Prototype");
    }

    @Test
    void getHMDBLink() {
        fail("Prototype");
    }

    @Test
    void getChebiFromIdentifiers() {
        fail("Prototype");
    }

    @Test
    void getChebiNumber() {
        fail("Prototype");
    }
}