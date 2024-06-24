package cems_project;
import constants.Constants;
import java.util.LinkedList;
import java.util.List;
import dbmanager.DBManager_v2;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ejecutable_Original {
    public static void main(String[] args) throws Exception {
        //imprimimos la lista de metabolitos que tenemos

        DBManager_v2 db = new DBManager_v2();
        try {

            String dbName = "metabolites";
            String dbUser = "root";
            String dbPassword = "alberto";

            db.connectToDB("jdbc:mysql://localhost/" + dbName +
                    "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", dbUser, dbPassword);

        } catch (Exception ioe) {
            Logger.getLogger(DBManager_v2.class.getName()).log(Level.SEVERE, null, ioe);
        }

        List<CEMSExperimentalConditions> listConditions = JSONToExperimentalConditions.main();

        List<String> fileNames = new LinkedList<>();
        fileNames.add("1_A");
        fileNames.add("1_C");
        fileNames.add("2_A");
        fileNames.add("2_C");
        fileNames.add("3_A");
        fileNames.add("4");
        fileNames.add("6_A");
        fileNames.add("6_C");
        fileNames.add("7");
        fileNames.add("10");
        fileNames.add("11");
        fileNames.add("12");
        fileNames.add("13");
        fileNames.add("14");
        //fileNames.add("16_P");
        //fileNames.add("16_R");
        fileNames.add("18_A");
        fileNames.add("18_C");
        //fileNames.add("20");
        fileNames.add("32_A");
        fileNames.add("38_A_P");
        fileNames.add("38_A_U");
        fileNames.add("38_C_P");
        fileNames.add("38_C_U");
        fileNames.add("40_A");
        fileNames.add("41");
        fileNames.add("50_A");
        fileNames.add("50_C");
        fileNames.add("54_A");
        fileNames.add("56_C");
        fileNames.add("60_C");
        fileNames.add("75_A");

        String outputFileName = null;
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        for (int i = 0; i < fileNames.size(); i++) {

            //no deberiamos seguir el orden del json.
            //deberiamos acceder a la experimental condition que tiene el mismo nombre que el excel
            //CEMSExperimentalConditions c = listConditions.get(i);
            CEMSExperimentalConditions c = JSONToExperimentalConditions.getConditionsByLabel(fileNames.get(i));

            String fullPathFileName = Constants.CEFilePath + fileNames.get(i) + ".xlsx";
            outputFileName = Constants.OUTPUT_DIRECTORY + fileNames.get(i) + "_output.xlsx";

            List<CEMSCompound> cemsCompounds = Fichero_Original.leerFichero(fullPathFileName, c);
            for (CEMSCompound m : cemsCompounds) {
                System.out.println(m);
            }

            //Compilación de excels de salida
            Fichero_Original.writeExcel(cemsCompounds, outputFileName);

            //Inserción de condiciones experimentales

            //estos dos metodos son get y set del atributo
            int eff_mob_exp_prop_id = db.get_eff_mob_exp_prop_id(c);
            //System.out.println(eff_mob_exp_prop_id);
            Integer ce_exp_prop_id = db.getCeExpPropId_Label(c);
            //System.out.println(ce_exp_prop_id);

            if (ce_exp_prop_id == null) {
                db.insertCeExpProp(c);
            }

            //insertamos los metabolitos leidos
            for (CEMSCompound m : cemsCompounds) {
                db.insertMetabolite(m, c);
            }


        }
    }



}