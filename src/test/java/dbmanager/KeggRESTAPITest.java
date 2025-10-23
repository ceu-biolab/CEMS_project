package dbmanager;

import cems_project.CEMSExperimentalConditions;
import exceptions.EffMobNotAvailable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeggRESTAPITest {


    @Test
    void getKeggPathways() {

        Integer sampleType_int = 2;
        Double mtCompoundA = 10d;
        Double effMobCompoundA = 0d;
        Double mtCompoundB = 1d;
        Double effMobCompoundB = 100d;
        CEMSExperimentalConditions cemsExpCond = new CEMSExperimentalConditions(
                LABEL, BUFFERENUM, TEMPERATURE, IONMODE, POLARITY, ref_compound_id_RMT, sampleType_int, mtCompoundA, effMobCompoundA, mtCompoundB, effMobCompoundB
        );
        Double expectedResult = 1.2346;
        Double actualResult = null;
        try {
            actualResult = cemsExpCond.getEffMob2Markers(9d);
            assertEquals(expectedResult,actualResult,0.1d);
        } catch (EffMobNotAvailable e) {
            fail(e.getMessage());
        }
    }


}