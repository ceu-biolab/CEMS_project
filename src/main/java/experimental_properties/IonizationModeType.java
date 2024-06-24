package experimental_properties;

import cems_project.CEMSExperimentalConditions;
import exceptions.IonizationTypeNotFound;
import exceptions.SampleTypeNotFound;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class IonizationModeType {

    public static final Map<CEMSExperimentalConditions.IonizationMode, Integer> MAPIONIZATIONMODETYPE;

    static {
        Map<CEMSExperimentalConditions.IonizationMode, Integer> mapChemAlphabetTMP = new LinkedHashMap<>();
        mapChemAlphabetTMP.put(CEMSExperimentalConditions.IonizationMode.POSITIVE, 1);
        mapChemAlphabetTMP.put(CEMSExperimentalConditions.IonizationMode.NEGATIVE, 2);

        // TO DO ALVARO FILL
        MAPIONIZATIONMODETYPE = Collections.unmodifiableMap(mapChemAlphabetTMP);
    }

    public static int getIonizationModeIntFromIonizationModeType(IonizationModeType ionizationModeType)
            throws IonizationTypeNotFound {
        Integer ionizationTypeInt = MAPIONIZATIONMODETYPE.get(ionizationModeType);
        if(ionizationTypeInt == null)
        {
            throw new IonizationTypeNotFound("IonizationModeType " + ionizationModeType + " NOT FOUND. Check your excel and the nomenclature of sample types. ");
        }
        return ionizationTypeInt;
    }

}
