package dbmanager;

import exceptions.WrongRequestException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ChemSpiderRESTTest {

    @Test
    void getINCHIKeyFromInchi() {
        String inchi = "InChI=1S/C10H18N4O6/c11-5(8(17)18)2-1-3-13-10(12)14-6(9(19)20)4-7(15)16/h5-6H,1-4,11H2,(H,15,16)(H,17,18)(H,19,20)(H3,12,13,14)/t5-,6-/m0/s1";
        String expectedInchiKey = "KDZOASGQNOPSCU-WDSKDSINSA-N";
        try {
            String actualInchiKey = ChemSpiderREST.getINCHIKeyFromInchi(inchi);
        } catch (IOException e) {
            fail("check getINCHIKeyFromInchi");
        } catch (WrongRequestException e) {
            fail("check getINCHIKeyFromInchi");
        }
    }
}