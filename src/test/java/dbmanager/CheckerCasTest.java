package dbmanager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerCasTest {

    @Test
    void getInChICasId() {
        String inchi = CheckerCas.getInChICasId("6920-35-0");
        String expectedInChI = "InChI=1S/C13H25NO4.ClH/c1-5-6-7-8-13(17)18-11(9-12(15)16)10-14(2,3)4;/h11H,5-10H2,1-4H3;1H";
        System.out.println(inchi);
        assertEquals(expectedInChI, inchi);
    }

    @Test
    void getInChIKeyCasId() {
        String inchiKey = CheckerCas.getInChIKeyCasId("6920-35-0");
        String expectedInChIKey = "DTHGTKVOSRYXOK-UHFFFAOYSA-N";
        System.out.println(inchiKey);
        assertEquals(expectedInChIKey, inchiKey);
    }

    @Test
    void getInChICasIdFromChemicalTranslatorService() {
        String inchi = CheckerCas.getInChICasIdFromChemicalTranslatorService("6920-35-0");
        String expectedInChI = "InChI=1S/C13H25NO4/c1-5-6-7-8-13(17)18-11(9-12(15)16)10-14(2,3)4/h11H,5-10H2,1-4H3";
        System.out.println(inchi);
        assertEquals(expectedInChI, inchi);
    }

    @Test
    void getSmilesCasId() {
        fail("This test is a prototype");
    }

}