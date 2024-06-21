package patternFinders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexInChITest {

    @Test
    void getFormulaFromInChI() {
        String inchi = "InChI=1S/C9H17NO4/c1-7(11)14-8(5-9(12)13)6-10(2,3)4/h8H,5-6H2,1-4H3/p+1/t8-/m1/s1";
        String expectedFormula="C9H17NO4";
        String actualFormula = RegexInChI.getFormulaFromInChI(inchi);
        assertEquals(expectedFormula,actualFormula);
    }

    @Test
    void getMainPart() {
        String inchi = "InChI=1S/C9H17NO4/c1-7(11)14-8(5-9(12)13)6-10(2,3)4/h8H,5-6H2,1-4H3/p+1/t8-/m1/s1";
        String expectedMainPart="InChI=1S/C9H17NO4/c1-7(11)14-8(5-9(12)13)6-10(2,3)4";
        String actualMainPart = RegexInChI.getMainPart(inchi);
        assertEquals(expectedMainPart,actualMainPart);


    }
}