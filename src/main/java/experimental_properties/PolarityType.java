package experimental_properties;

import cems_project.CEMSExperimentalConditions;
import exceptions.SampleTypeNotFound;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PolarityType {

    public static final Map<CEMSExperimentalConditions.Polarity, Integer> MAPPOLARITYTYPE;

    static {
        Map<CEMSExperimentalConditions.Polarity, Integer> mapChemAlphabetTMP = new LinkedHashMap<>();
        mapChemAlphabetTMP.put(CEMSExperimentalConditions.Polarity.DIRECT, 1);
        mapChemAlphabetTMP.put(CEMSExperimentalConditions.Polarity.REVERSE, 2);

        // TO DO ALVARO FILL
        MAPPOLARITYTYPE = Collections.unmodifiableMap(mapChemAlphabetTMP);
    }

    public static int getSampleTypeIntFromSampleType(String sampleType) throws SampleTypeNotFound {
        Integer sampleTypeInt = MAPPOLARITYTYPE.get(sampleType);
        if(sampleTypeInt == null)
        {
            throw new SampleTypeNotFound("Sample type " + sampleType + " NOT FOUND. Check your excel and the nomenclature of sample types. ");
        }
        return sampleTypeInt;
    }

}
