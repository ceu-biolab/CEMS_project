package cems_project;

import constants.Constants;
import experimental_properties.SampleType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class JSONToExperimentalConditions {
    public static CEMSExperimentalConditions getConditionsByLabel(String targetLabel) {
        List<CEMSExperimentalConditions> listConditions = main();
        if (listConditions == null) {
            return null; // Error handling if list is null
        }
        for (CEMSExperimentalConditions condition : listConditions) {
            if (condition.getLabel().equals(targetLabel)) {
                return condition; // Return the condition with matching ID
            }
        }
        return null; // Return null if no condition with matching ID is found
    }
    public static List<CEMSExperimentalConditions> main() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(Constants.RESOURCES_PATH +
                    "json/experimental_conditions_v8.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray fileList = (JSONArray) jsonObject.get("file");
            List<CEMSExperimentalConditions> listConditions = new LinkedList<>();
            for (int i = 0; i < fileList.size(); i++) {
                Object value = fileList.get(i);
                JSONObject value_json = (JSONObject) value;
                Object label = value_json.get("id");
                String label_string = label.toString();
                Object buffer = value_json.get("buffer");
                String buffer_string = buffer.toString();
                Object temperature = value_json.get("temperature");
                String temp_string = temperature.toString();
                Integer temp_int = Integer.parseInt(temp_string);
                Object ionizationMode = value_json.get("ionizationMode");
                String ionizationMode_string = ionizationMode.toString();
                Object polarity = value_json.get("polarity");
                String polarity_string = polarity.toString();
                Object ref_compound_id_RMT = value_json.get("ref_compound_id_RMT");
                String ref_compound_id_RMT_string = ref_compound_id_RMT.toString();
                Integer ref_compound_id_RMT_int = null;
                try
                {
                    if(ref_compound_id_RMT_string != null)
                        ref_compound_id_RMT_int = Integer.parseInt(ref_compound_id_RMT_string);
                }
                catch (NumberFormatException e)
                {
                    ref_compound_id_RMT_int = null;
                }
                Object capillaryLength = value_json.get("capillaryLength");
                String capillaryLength_string = capillaryLength.toString();
                Integer capillaryLength_int = Integer.parseInt(capillaryLength_string);
                Object voltage = value_json.get("voltage");
                String voltage_string = voltage.toString();
                Integer voltage_int = Integer.parseInt(voltage_string);
                Object mtEOF = value_json.get("mtEOF");
                String mtEOF_string = "";
                Double mtEOF_double = null;
                try
                {
                    if(mtEOF != null)
                        mtEOF_string = mtEOF.toString();
                    mtEOF_double = Double.parseDouble(mtEOF_string);
                }
                catch (NumberFormatException e)
                {
                    mtEOF_double = null;
                }
                Object mtCompoundA = value_json.get("mtCompoundA");
                String mtCompoundA_string = "";
                Double mtCompoundA_double = null;
                try
                {
                    if(mtCompoundA != null)
                        mtCompoundA_string = mtCompoundA.toString();
                    mtCompoundA_double = Double.parseDouble(mtCompoundA_string);
                }
                catch (NumberFormatException e)
                {
                    mtCompoundA_double = null;
                }
                Object mtCompoundB = value_json.get("mtCompoundB");
                String mtCompoundB_string = "";
                Double mtCompoundB_double = null;
                try
                {
                    if(mtCompoundB != null)
                        mtCompoundB_string = mtCompoundB.toString();
                    mtCompoundB_double = Double.parseDouble(mtCompoundB_string);
                }
                catch (NumberFormatException e)
                {
                    mtCompoundB_double = null;
                }
                Object effMobCompoundA = value_json.get("effMobCompoundA");
                String effMobCompoundA_string = "";
                Double effMobCompoundA_double = null;
                try
                {
                    if(effMobCompoundA != null)
                        effMobCompoundA_string = effMobCompoundA.toString();
                    effMobCompoundA_double = Double.parseDouble(effMobCompoundA_string);
                }
                catch (NumberFormatException e)
                {
                    effMobCompoundA_double = null;
                }
                Object effMobCompoundB = value_json.get("effMobCompoundB");
                String effMobCompoundB_string = "";
                Double effMobCompoundB_double = null;
                try
                {
                    if(effMobCompoundB != null)
                        effMobCompoundB_string = effMobCompoundB.toString();
                    effMobCompoundB_double = Double.parseDouble(effMobCompoundB_string);
                }
                catch (NumberFormatException e)
                {
                    effMobCompoundB_double = null;
                }
                Object sampleType = value_json.get("sampleType");
                String sampleType_string = sampleType.toString();
                int sampleType_int = SampleType.MAPSAMPLETYPES.get(sampleType_string);
                Double electricField =  ((double) voltage_int/ (double) capillaryLength_int);
                CEMSExperimentalConditions cemsExperimentalConditions = new CEMSExperimentalConditions(
                        label_string,
                        BufferEnum.valueOf(buffer_string),
                        temp_int,
                        CEMSExperimentalConditions.IonizationMode.valueOf(ionizationMode_string),
                        CEMSExperimentalConditions.Polarity.valueOf(polarity_string),
                        ref_compound_id_RMT_int,
                        sampleType_int,
                        mtCompoundA_double, effMobCompoundA_double, mtCompoundB_double, effMobCompoundB_double,
                        capillaryLength_int, voltage_int, electricField,
                        mtEOF_double, 0d, null, null);
                listConditions.add(cemsExperimentalConditions);
            }
            return listConditions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



    }
}

