package dbmanager;

import cems_project.ClassyfireClassification;
import exceptions.CompoundNotClassifiedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClassyFireTest {

    @Test
    void getInChIKeyClassyFireFromInChI() {
        String inchi = "InChI=1S/C9H17NO4.ClH/c1-7(11)14-8(5-9(12)13)6-10(2,3)4;/h8H,5-6H2,1-4H3;1H/t8-;/m1./s1";
        String expectedInchiKey = "JATPLOXBFFRHDN-DDWIOCJRSA-N";
        String actualInchiKey = null;
        try {
            actualInchiKey = ClassyFire.getInChIKeyClassyFireFromInChI(inchi,expectedInchiKey);
        } catch (CompoundNotClassifiedException e) {
            actualInchiKey = "";
        } catch (IOException e) {
            actualInchiKey = "";
        }
        assertEquals(expectedInchiKey,actualInchiKey);

    }

    @Test
    void getInChIKeyFromQueryId() {
        Integer query_id = 11123068;
        String expectedInchiKey = "HNDVDQJCIGZPNO-YFKPBYRVSA-N";
        String actualInchiKey = null;
        try {
            actualInchiKey = ClassyFire.getInChIKeyFromQueryId(query_id);
        } catch (CompoundNotClassifiedException e) {
            actualInchiKey = "";
        }
        assertEquals(expectedInchiKey,actualInchiKey);

    }

    @Test
    void getClassificationFromClassyFire() {
        String inchi = "InChI=1S/C9H17NO4.ClH/c1-7(11)14-8(5-9(12)13)6-10(2,3)4;/h8H,5-6H2,1-4H3;1H/t8-;/m1./s1";
        String inchiKey = "JATPLOXBFFRHDN-DDWIOCJRSA-N";
        String expectedClass = "Fatty Acyls";
        String actualClass;
        try {
            ClassyfireClassification classification = ClassyFire.getClassificationFromClassyFire(inchi,inchiKey);
            actualClass = classification.ownClass();
        } catch (IOException e) {
            actualClass = "";
        }  catch (CompoundNotClassifiedException e) {
            actualClass = "";
        }
        assertEquals(expectedClass,actualClass);
    }
}