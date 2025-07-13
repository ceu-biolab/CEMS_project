package GCMS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static GCMS.ReadTxt.*;


public class GCMS_insert {

    private static final Logger LOGGER = Logger.getLogger(ReadTxt.class.getName());

    //Main to read the info of alkanes excel and try different functions
    /*public static void main(String[] args) {
        //Poner direccion en la que se tenga el excel del calculo de RI de ALkanes
        String filexcel = "src/main/resources/gcms_excels/Calculo_RI_Alkanes.xlsx";

        try {
            //reads the alkane excel
            List<CompoundGC> compoundgcList = ReadExcel.readexcelalkanes(filexcel);
            GCMS gcms = new GCMS(compoundgcList);
            //prints the info of the alkane excel
            System.out.println("\nList Size: "+gcms.getCompoundgcList().size());
            //System.out.println("INFO EXCEL ALKANES:"+gcms.toString());
            System.out.println("INFO EXCEL ALKANES:"+gcms);

            //upload the info of the alkane excel into a database (tfg_pruebas.compoundgcms)
            int i = 1;
            /*CompoundGC compoundgc;
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                DBManagerMioPruebas.insertGCCompoundintoCompoundgcms(compoundgc);
            }*/
            /*CompoundGC compoundgc;
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println("DERTYPE: "+compoundgc.getDertype());
            }*/
            /*Compound c;
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                //System.out.println("NAME: "+compoundgc.getCompoundName());
                c = PubchemRest.getCompoundFromName(compoundgc.getCompoundName());
                System.out.println(i+": "+c);
            }*/

            /*System.out.println("PRUEBAS IDETNIFIERS");
            Compound c;
            c = PubchemRest.getCompoundFromName("Octane");
            //System.out.println(c.getIdentifiersOwn());//imprime info inchi, inchikey, smiles
            //System.out.println(c);//imprime info compuesto entero
            System.out.println("ID: "+c.getCompound_id());
            //DBManagerMioPruebas.insertintoCopiaCompound(c);
            //System.out.println("SUBIDO");
            int id = DBManagerMioPruebas.getCompoundIdfromName("Nonane");
            System.out.println("ID-add: "+id);*

            CompoundGC compoundgc;
            //Obtains the id of the compound from the excel alkanes from the database
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                int id = DBManagerMioPruebas.getCompoundIdfromName(compoundgc.getCompoundName().trim());
                //System.out.println("id: "+id);
                compoundgc.setCompound_id(id);
                System.out.println("c id: "+ compoundgc.getCompound_id());

                if(id==0){ //HABRIA Q AÑADIRLO
                    System.out.println("not in database");
                }
                else{//añadirlo a gcms
                    //DBManagerMioPruebas.insertDerivatizationMethod(compoundgc);
                }
                //System.out.println("COMPOUNDS:");
                //System.out.println(compoundgc);
            }


            /*System.out.println("SEE ID");
            compoundgc = compoundgcList.get(12);
            System.out.println("C1: "+compoundgc);
            //is giving me id=0!!!!
            //System.out.println("NaME: "+compoundgc.getCompoundName().toLowerCase());
            //String namecompound1 = compoundgc.getCompoundName().toLowerCase();
            //System.out.println("NAME: '"+namecompound1+"'");

            String namecompound = compoundgc.getCompoundName().trim();
            //System.out.println("NAME TRIM: '"+namecompound+"'");

            //int id = DBManagerMioPruebas.getCompoundIdfromName(compoundgc.getCompoundName().toLowerCase());
            //int id = DBManagerMioPruebas.getCompoundIdfromName("octane");
            int id = DBManagerMioPruebas.getCompoundIdfromName(namecompound);
            System.out.println("ID: "+id);

            compoundgc.setCompound_id(id);
            System.out.println("C ID: "+compoundgc.getCompound_id());
            *
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    //Main to try different things using the file Compound_RT-RI-Inchi
    /*public static void main(String[] args) {
        int i;
        try{
            String filexcel = "src/main/resources/gcms_excels/Compound_RT-RI-Inchi.xlsx";
            List<CompoundGC> compoundgcList = ReadExcel.readexcelcompoundrtriinchi(filexcel);

            CompoundGC compoundgc;
            //compoundgc = compoundgcList.get(3);
            //DBManagerMioPruebas.insertDerivatizationMethod(compoundgc);
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                //DBManagerMioPruebas.insertDerivatizationMethod(compoundgc);
                //DBManagerMioPruebas.insertGCCompoundintoCompoundgcms(compoundgc);

                //DBManagerMioPruebas.insertDerivatizationMethod(compoundgc); //Since we only use one method it only needs to be inserted one time
                //DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc);
                //DBManagerMioPruebas.insertRIRT(compoundgc);
                //DBManagerMioPruebas.insertGCColumn(compoundgc);

                //DBManagerMioPruebas.insertgcmsSpectrum(compoundgc);
                //DBManagerMioPruebas.insertGCMSPeaks(compoundgc);

                System.out.println(i+" DONE");
            }
            System.out.println("ADDED");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*//MAIN TO INSERT RT RI & OTHER FOR SPECTRUM IS THE LAST MAIN
    public static void main(String[] args) {
        int i;
        try{
            String filexcel = "src/main/resources/gcms_excels/CorrectInfo_CompoundsRT-RI.xlsx";

            List<CompoundGC> compoundgcList = ReadExcel.readexcelcompoundrtriinchi(filexcel);

            CompoundGC compoundgc;
            /*compoundgc = compoundgcList.get(41); //list beguins in 0!!
            System.out.println("c41: "+compoundgc.getCompoundName());*

            int size = compoundgcList.size();
            System.out.println("Size: "+size +"\n");

            /*for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println("i: "+i+": "+compoundgc.getCompoundName());
            }*

            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println("C "+i+": "+compoundgc);
                int id = -1;

                //if (compoundgc.getCompoundName().equalsIgnoreCase("Dimethylsulfide")){
                    //System.out.println(" SOLO SI ");
                    compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));
                    System.out.println("ID COmpo: " + compoundgc.getCompound_id());

                    if (compoundgc.getCompound_id() == 0) {//The compound is not found - we have to insert everything
                        DBManagerMioPruebas.insertintoCopiaCompound(compoundgc); //inserts into table of compounds
                        System.out.println("insert into compound");
                        //compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));
                        compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdfromName(compoundgc.getCompoundName()));
                        System.out.println("CID: "+compoundgc.getCompound_id());
                        DBManagerMioPruebas.insertCopiaCompoundIdentifiers(compoundgc); //inserts into identifiers
                        System.out.println("insert into identifiers");
                        //compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));

                        DBManagerMioPruebas.insertDerivatizationMethod(compoundgc); //inserts derivatization method
                        System.out.println("insert dermethod");
                        DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc); //inserts relation compound-derivatization method
                        System.out.println("insert relation");

                        DBManagerMioPruebas.insertGCColumn(compoundgc); //inserts the column type
                        System.out.println("insert gccolumn");
                        DBManagerMioPruebas.insertRIRT(compoundgc); //inserts the RI and RT of the compound
                        System.out.println("insert rirt");

                        /*For the Spectrum use other main
                        DBManagerMioPruebas.insertgcmsSpectrum(compoundgc);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc);*
                    }

                    //String der_type = knowDerType(compoundgc.getDertype());

                    else { //if the compound exists in the tables
                        id = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                        System.out.println("ID tabla: " + id);
                        if (id == 0) { //If the relation between compound and derivatization method do not exist
                            /* This two compounds are not found if we used the name
                            if((compoundgc.getCompound_id()==302139) || (compoundgc.getCompound_id()==302137)){
                                DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc);
                                System.out.println("insert both ids er");
                                DBManagerMioPruebas.insertRIRT(compoundgc);
                                System.out.println("insert rtri");
                            } else{*
                            DBManagerMioPruebas.insertDerivatizationMethod(compoundgc); //inserts derivatization method
                            DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc); //inserts the relation compound-derivatization method
                            System.out.println("insert both ids");
                            DBManagerMioPruebas.insertGCColumn(compoundgc); //inserts the column
                            System.out.println("insert gccolumn");
                            DBManagerMioPruebas.insertRIRT(compoundgc); //inserts RI & RT
                            System.out.println("insert rtri");

                            //}
                        } else { //If the relation exists
                            id = DBManagerMioPruebas.getgcrirtIdfromCompoundId(compoundgc.getCompound_id());
                            System.out.println("ID tabla: " + id);
                            if (id == 0) {//the ri rt are not inserted
                                DBManagerMioPruebas.insertGCColumn(compoundgc); //inserts column
                                System.out.println("insert gccolumn");
                                DBManagerMioPruebas.insertRIRT(compoundgc); //inserts RI & RT
                                System.out.println("insert rtri else");
                            }
                            System.out.println("Everything is inserted");
                        }
                        /*For the Spectrum use other main
                        DBManagerMioPruebas.insertgcmsSpectrum(compoundgc);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc);*
                    }
                    //System.out.println(i+" HECHO");
                //}
            }
            //System.out.println("AÑADIDO");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/


    //MAIN TO INSERT SPECTRUM -- REST MUST BE INSERTED --> USE THE OTHER MAIN
    /*public static void main(String[] args) {
        int i;
        int j;
        try{
            String file = "";

            //In the file also indicate the compounds not only the spectrum
            //1spectro-1lista de picos-> parejas de mz-intensity
            //creates compounds with the info of the spectrum
            List<CompoundGC> compoundgcList = ReadExcel.readexcelcompoundspectrum(file);

            CompoundGC compoundgc;
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println("C "+i+": "+compoundgc);
                int id_der = -1;
                int new_id_der;
                int num_peaks;

                compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));
                System.out.println("ID COmpo: "+compoundgc.getCompound_id());
                id_der = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                System.out.println("ID tabla: "+id_der);
                if(id_der == 0){//if compound-derivatizationmethod is not in the tables
                    DBManagerMioPruebas.insertDerivatizationMethod(compoundgc);
                    DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc); //inserts compound-derivatizationmethod
                    System.out.println("insert both ids");
                    //new_id_der = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                    DBManagerMioPruebas.insertgcmsSpectrum(compoundgc); //inserts the spectrum
                    System.out.println("insert spectrum");

                    //Insert Peaks
                    num_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().size();
                    for (j=1; j<=num_peaks; j++){
                        GCMS_Peaks gcms_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().get(j-1);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc, gcms_peaks);
                    }

                }else{ //if the relation exists
                    DBManagerMioPruebas.insertgcmsSpectrum(compoundgc); //inserts spectrum
                    //inserts peaks
                    num_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().size();
                    for (j=1; j<=num_peaks; j++){
                        GCMS_Peaks gcms_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().get(j-1);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc, gcms_peaks);
                    }
                    System.out.println("insert spectrum else");
                }

                System.out.println(i+" DONE");
            }
            System.out.println("ADDED");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /**
     * Printing to know which compounds from the txt list fail when are used to obtain their complete information from PubChem
     * @param compoundGCAndPossibleErrors
     */
    private static void printingValidation(CompoundGCAndPossibleErrors compoundGCAndPossibleErrors){
        Map<Integer, CompoundGC> compoundGCMap = compoundGCAndPossibleErrors.getCompoundGC();
        Map<Integer, CompoundGCInformationTxt> casIdNotfoundMap = compoundGCAndPossibleErrors.getCasIdNotFound();
        Map<Integer, CompoundGCInformationTxt> nameNotFoundMap = compoundGCAndPossibleErrors.getNameNotFound();
        Map<Integer, CompoundGCInformationTxt> inchiNotFoundMap = compoundGCAndPossibleErrors.getInchiNotFound();
        Map<Integer, CompoundGCInformationTxt> smilesNotFoundMap = compoundGCAndPossibleErrors.getSmilesNotFound();

        int i;
        List<String> infoError = compoundGCAndPossibleErrors.getError();
        System.err.println("ERROR WARNING: ");
        for(i=0; i<infoError.size(); i++){
            System.err.println(infoError.get(i));
        }

        i=0;
        System.out.println("COMPOUNDS: ");
        for (Map.Entry<Integer, CompoundGC> compounGC : compoundGCMap.entrySet()) {
                /*System.out.println("ORIGINAL LIST POSITION: " + compounGC.getKey()
                                + "\n" + compounGC.getValue().toString());*/
            System.out.println(i+1 + ": ORIGINAL LIST POSITION: " + compounGC.getKey() + "; " +
                    compounGC.getValue().getCompoundName() + "; "+compounGC.getValue().getINCHI());
            i++;
        }
        System.out.println("Total compounds found: "+i);
        System.out.println("Total compounds found count: "+compoundGCMap.size());

        i=0;
        System.out.println("\nCASID NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> casIdNotFound : casIdNotfoundMap.entrySet()) {
            System.out.println("ORIGINAL LIST POSITION: " + casIdNotFound.getKey() + "; NAME: " + casIdNotFound.getValue().getCName());
            i++;
        }
        System.out.println("Total compounds casId not found txt file: "+i);
        System.out.println("Total compounds casId not found txt file count: "+casIdNotfoundMap.size());

        i=0;
        System.out.println("\nNAME NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> nameNotFound : nameNotFoundMap.entrySet()) {
            System.out.println(i+1 +": ORIGINAL LIST POSITION: " + nameNotFound.getKey() + "; NAME: " + nameNotFound.getValue().getCName());
            i++;
        }
        System.out.println("Total compounds name not found on PubChem: "+i);
        System.out.println("Total compounds name not found on PubChem count: "+nameNotFoundMap.size());

        i=0;
        System.out.println("\nINCHI NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> inchiNotFound : inchiNotFoundMap.entrySet()) {
            System.out.println("ORIGINAL LIST POSITION: " + inchiNotFound.getKey() + "; NAME: " + inchiNotFound.getValue().getCName());
            i++;
        }
        System.out.println("Total compounds inchi not found txt file: "+i);
        System.out.println("Total compounds inchi not found txt file count: "+inchiNotFoundMap.size());

        i=0;
        System.out.println("\nSMILES NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> smilesNotFound : smilesNotFoundMap.entrySet()) {
            System.out.println("ORIGINAL LIST POSITION: " + smilesNotFound.getKey() + "; NAME: " + smilesNotFound.getValue().getCName());
            i++;
        }
        System.out.println("Total compounds smiles not found txt file: "+i);
        System.out.println("Total compounds smiles not found txt file count: "+ smilesNotFoundMap.size());

        i=0;
        System.out.println("\nCOMPOUNDS ALL INFO: ");
        for (Map.Entry<Integer, CompoundGC> compounGC : compoundGCMap.entrySet()) {
                /*System.out.println("ORIGINAL LIST POSITION: " + compounGC.getKey()
                                + "\n" + compounGC.getValue().toString());*/
            System.out.println(i+1 + ": ORIGINAL LIST POSITION: " + compounGC.getKey() + "; \n\t" +
                    compounGC.getValue().toString());
            i++;
        }
        System.out.println("Total compounds found: "+i);
        System.out.println("Total compounds found count: "+compoundGCMap.size());
    }

    /**
     * Upload/insert compoundGC to the DataBase
     * @param compoundGCList
     */
    private static void uploadInformationCompoundGCDB(List<CompoundGC> compoundGCList) throws IOException {
        CompoundGC compoundGC;
        int i;
        int size = compoundGCList.size();
        System.out.println("Size: " + size + "\n");

        /*for (i = 1; i <= compoundGCList.size(); i++) {
            compoundGC = compoundGCList.get(i - 1);
            System.out.println("C " + i + ": " + compoundGC);
            int id = -1;
            int idSpectrum = -1;

            //TODO change the funciones a corretctas tablas
            //todo hacer bucle para los spectros/picos

            compoundGC.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundGC.getINCHI()));
            System.out.println("ID COmpo: " + compoundGC.getCompound_id());

            if (compoundGC.getCompound_id() == 0) {//The compound is not found - we have to insert everything
                DBManagerMioPruebas.insertintoCopiaCompound(compoundGC); //inserts into table of compounds
                System.out.println("insert into compound");

                compoundGC.setCompound_id(DBManagerMioPruebas.getCompoundIdfromName(compoundGC.getCompoundName()));
                System.out.println("CID: "+compoundGC.getCompound_id());

                DBManagerMioPruebas.insertCopiaCompoundIdentifiers(compoundGC); //inserts into identifiers
                System.out.println("insert into identifiers");

                DBManagerMioPruebas.insertDerivatizationMethod(compoundGC); //inserts derivatization method
                System.out.println("insert dermethod");

                DBManagerMioPruebas.insertGCColumn(compoundGC); //inserts the column type
                System.out.println("insert gccolumn");
                DBManagerMioPruebas.insertRIRT(compoundGC); //inserts the RI and RT of the compound
                System.out.println("insert rirt");

                //todo hacer bucle para todos los picos/spectrum -> crear funcion aparte
                DBManagerMioPruebas.insertgcmsSpectrum(compoundGC); //INSERTS THE SPECTRUM
                System.out.println("insert Spectrum");
                DBManagerMioPruebas.insertGCMSPeaks(compoundGC); //INSERTS THE PEAKS OF THE SPECTRUM
                System.out.println("insert Peaks");

            } else { //if the compound exists in the tables
                id = DBManagerMioPruebas.getgcrirtIdfromCompoundId(compoundGC.getCompound_id());
                System.out.println("ID tabla: " + id);
                if (id == 0) {//the ri rt are not inserted
                    DBManagerMioPruebas.insertDerivatizationMethod(compoundGC); //inserts derivatization method
                    DBManagerMioPruebas.insertGCColumn(compoundGC); //inserts column
                    System.out.println("insert gccolumn");
                    DBManagerMioPruebas.insertRIRT(compoundGC); //inserts RI & RT
                    System.out.println("insert rtri else");

                    //idSpectrum = DBManagerMioPruebas.getspectrumIdfromCompoundId(compoundGC.getCompound_id());
                    //if(idSpectrum == 0) {//Spectrum does not exists
                        //todo hacer bucle para todos los picos/spectrum -> crear funcion aparte
                        DBManagerMioPruebas.insertgcmsSpectrum(compoundGC); //INSERTS THE SPECTRUM
                        System.out.println("insert Spectrum");
                        DBManagerMioPruebas.insertGCMSPeaks(compoundGC); //INSERTS THE PEAKS OF THE SPECTRUM
                        System.out.println("insert Peaks");
                    //}
                } else { //ri rt are inserted
                    //idSpectrum = DBManagerMioPruebas.getspectrumIdfromCompoundId(compoundGC.getCompound_id());
                    //if(idSpectrum == 0) {//Spectrum does not exists
                        //todo hacer bucle para todos los picos/spectrum -> crear funcion aparte
                        DBManagerMioPruebas.insertgcmsSpectrum(compoundGC); //INSERTS THE SPECTRUM
                        System.out.println("insert Spectrum");
                        DBManagerMioPruebas.insertGCMSPeaks(compoundGC); //INSERTS THE PEAKS OF THE SPECTRUM
                        System.out.println("insert Peaks");
                    //}
                }

                System.out.println("Everything is inserted");

            }
        }*/
    }


    //MAIN TO INSERT COMPOUNDS FROM TXT getGCMSCloroformiatesLibrary()
    public static void main(String[] args) {
        //String fileTxt = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\correctData\\Pruebas_usartxt.txt";
        String fileTxt = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\correctData\\gcms_cloroformiates_library.txt";
        try{
            int i;

            List<CompoundGCInformationTxt> compoundGCInformationTxts = getCompoundFromTxt(fileTxt);

            //TODO 2-BUSCOR COMPOUESTOS NO NOMBRE -> Usar inchi/smiles -> esperar resto
            //Compound and errors: compounds not found by name, casId, inchi or smile
            CompoundGCAndPossibleErrors compoundGCAndPossibleErrors = getGCMSCloroformiatesFromTxt(compoundGCInformationTxts);

            System.out.println("\nMAIN");
            CompoundGC compoundgc;

            printingValidation(compoundGCAndPossibleErrors);

            Map<Integer, CompoundGC> compoundGCMap = compoundGCAndPossibleErrors.getCompoundGC();
            //THEY ARE NOT INDEPENDENT COPIES, IF THE INFORMATION OF compoundGCList IS MODIFIED THEN compoundGCMap IS ALSO CHANGED
            List<CompoundGC> compoundGCList = new ArrayList<>(compoundGCMap.values());

            /*//TODO 3-SUBIR DATOS A DB -> en ello
            /uploadInformationCompoundGCDB(compoundGCList);*/

            System.out.println("\nFINISH");


        }
        catch (IOException e) {
            //System.out.println("Error: " + e.getStackTrace() + "\n");
           //System.out.println("Error: " + e + "\n");
            throw new RuntimeException(e);
        }
    }

}