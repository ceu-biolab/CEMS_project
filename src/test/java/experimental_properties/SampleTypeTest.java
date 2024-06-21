package experimental_properties;

import exceptions.SampleTypeNotFound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleTypeTest {

    @Test
    void getSampleTypeIntFromSampleTypeSerum() {
        String sampleType = "serum";
        Integer expectedSampleTypeInt = 5;
        try {
            Integer actualSampleTypeInt = SampleType.getSampleTypeIntFromSampleType(sampleType);
            assertEquals(expectedSampleTypeInt, actualSampleTypeInt);
        }
        catch(SampleTypeNotFound stnf)
        {
            fail("Revisa la función SampleType.getSampleTypeIntFromSampleType");
        }
    }

    @Test
    void getSampleTypeIntFromSampleTypeWrong() {
        String sampleType = "tuputamadre";
        Integer expectedSampleTypeInt = 5;
        try {
            Integer actualSampleTypeInt = SampleType.getSampleTypeIntFromSampleType(sampleType);
        }
        catch(SampleTypeNotFound stnf)
        {
            assertTrue(true,"Esperaba recibir una excepción, todo OK");
        }
    }

}