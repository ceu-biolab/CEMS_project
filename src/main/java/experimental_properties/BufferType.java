package experimental_properties;

import cems_project.BufferEnum;
import exceptions.SampleTypeNotFound;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BufferType {

    public static final Map<BufferEnum, Integer> MAPBUFFERTYPES;

    static {
        Map<BufferEnum, Integer> mapChemAlphabetTMP = new LinkedHashMap<>();
        mapChemAlphabetTMP.put(BufferEnum.FORMIC_ACID_1M, 1);
        mapChemAlphabetTMP.put(BufferEnum.ACETIC_ACID_10PERCENT, 2);
        mapChemAlphabetTMP.put(BufferEnum.AMMONIUM_ACETATE_50mM, 4);  
        mapChemAlphabetTMP.put(BufferEnum.FORMIC_ACID_0DOT26M, 5);
        mapChemAlphabetTMP.put(BufferEnum.AMMONIUM_BICARBONATE_20mM, 6);
        mapChemAlphabetTMP.put(BufferEnum.FORMIC_ACID_0DOT5PERCENT_METHANOL_5PERCENT, 7);
        mapChemAlphabetTMP.put(BufferEnum.AMMONIUM_BICARBONATE_50mM, 8);
        mapChemAlphabetTMP.put(BufferEnum.AMMONIUM_ACETATE_50mM_METHANOL_5PERCENT, 9);
        mapChemAlphabetTMP.put(BufferEnum.FORMIC_ACID_1M_ACN_15PERCENT, 10);
        mapChemAlphabetTMP.put(BufferEnum.AMMONIUM_ACETATE_35mM_ACETONITRILE_70PERCENT_METHANOL_15PERCENT_H2O_10PERCENT_2PROPANOL_5PERCENT, 11);
        mapChemAlphabetTMP.put(BufferEnum.FORMIC_ACID_0DOT1M, 12);

        // TO DO ALVARO FILL
        MAPBUFFERTYPES = Collections.unmodifiableMap(mapChemAlphabetTMP);
    }

    public static int getSampleTypeIntFromSampleType(String sampleType) throws SampleTypeNotFound {
        Integer sampleTypeInt = MAPBUFFERTYPES.get(sampleType);
        if(sampleTypeInt == null)
        {
            throw new SampleTypeNotFound("Sample type " + sampleType + " NOT FOUND. Check your excel and the nomenclature of sample types. ");
        }
        return sampleTypeInt;
    }

}
