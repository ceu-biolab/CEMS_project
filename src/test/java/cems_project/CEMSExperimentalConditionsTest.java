package cems_project;

import dbmanager.ConstantQueries;
import exceptions.EffMobNotAvailable;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CEMSExperimentalConditionsTest {
    public static String LABEL = "1_A";
    public static BufferEnum BUFFERENUM = BufferEnum.FORMIC_ACID_1M;
    public static CEMSExperimentalConditions.IonizationMode IONMODE = CEMSExperimentalConditions.IonizationMode.POSITIVE;
    public static CEMSExperimentalConditions.Polarity POLARITY = CEMSExperimentalConditions.Polarity.DIRECT;

    public static Integer TEMPERATURE = 20;

    @Test
    void getEffMob2Markers() {
        Integer ref_compound_id_RMT = ConstantQueries.REFERENCE_COMPOUND_ID_METS;
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

    @Test
    void getEffMobFromExpConditions() {

        Integer capillaryLengthMilimeters = 1000;
        Integer sampleType_int = 2;
        Integer voltageKV = 30;
        Double timeEOF = 10d;
        Integer ref_compound_id_RMT = ConstantQueries.REFERENCE_COMPOUND_ID_METS;
        CEMSExperimentalConditions cemsExpCond = new CEMSExperimentalConditions(
                LABEL, BUFFERENUM, TEMPERATURE, IONMODE, POLARITY, ref_compound_id_RMT,sampleType_int,capillaryLengthMilimeters, voltageKV, null, timeEOF
        );
        Double expectedResult = 370.3704d;
        Double actualResult = null;
        try {
            actualResult = cemsExpCond.getEffMobFromExpConditions(9d);
            assertEquals(expectedResult,actualResult, 0.1d);
        } catch (EffMobNotAvailable e) {
            fail(e.getMessage());
        }
    }


}

