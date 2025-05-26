package GCMS;

import cems_project.Compound;
import dbmanager.DBManagerMioPruebas;
import dbmanager.PubchemRest;

import java.io.IOException;
import java.util.List;


public class GCMS_insert {

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
            //DBManagerMioPruebas.insertintoCompound(c);
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

    //MAIN TO INSERT RT RI & OTHER FOR SPECTRUM IS THE LAST MAIN
    public static void main(String[] args) {
        int i;
        try{
            String filexcel = "src/main/resources/gcms_excels/CorrectInfo_CompoundsRT-RI.xlsx";

            List<CompoundGC> compoundgcList = ReadExcel.readexcelcompoundrtriinchi(filexcel);

            CompoundGC compoundgc;
            /*compoundgc = compoundgcList.get(41); //list beguins in 0!!
            System.out.println("c41: "+compoundgc.getCompoundName());*/

            int size = compoundgcList.size();
            System.out.println("Size: "+size +"\n");

            /*for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println("i: "+i+": "+compoundgc.getCompoundName());
            }*/

            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println("C "+i+": "+compoundgc);
                int id = -1;

                //if (compoundgc.getCompoundName().equalsIgnoreCase("Dimethylsulfide")){
                    //System.out.println(" SOLO SI ");
                    compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));
                    System.out.println("ID COmpo: " + compoundgc.getCompound_id());

                    if (compoundgc.getCompound_id() == 0) {//The compound is not found - we have to insert everything
                        DBManagerMioPruebas.insertintoCompound(compoundgc); //inserts into table of compounds
                        System.out.println("insert into compound");
                        //compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));
                        compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdfromName(compoundgc.getCompoundName()));
                        System.out.println("CID: "+compoundgc.getCompound_id());
                        DBManagerMioPruebas.insertCompoundIdentifiers(compoundgc); //inserts into identifiers
                        System.out.println("insert into identifiers");
                        //compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));

                        DBManagerMioPruebas.insertDerivatizationMethod(compoundgc); //inserts derivatization method
                        System.out.println("insert dermethod");
                        /*I dont need it anymore
                        DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc); //inserts relation compound-derivatization method
                        System.out.println("insert relation");*/

                        DBManagerMioPruebas.insertGCColumn(compoundgc); //inserts the column type
                        System.out.println("insert gccolumn");
                        DBManagerMioPruebas.insertRIRT(compoundgc); //inserts the RI and RT of the compound
                        System.out.println("insert rirt");

                        /*For the Spectrum use other main
                        DBManagerMioPruebas.insertgcmsSpectrum(compoundgc);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc);*/
                    }

                    //String der_type = knowDerType(compoundgc.getDertype());

                    else { //if the compound exists in the tables
                        //id = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                        String dertype = compoundgc.getDertype().toString();
                        id = DBManagerMioPruebas.getDerivatizationMethodIdfromDerType(dertype);
                        System.out.println("ID tabla: " + id);
                        if (id == 0) { //If the derivatization method do not exist
                            /* This two compounds are not found if we used the name
                            if((compoundgc.getCompound_id()==302139) || (compoundgc.getCompound_id()==302137)){
                                DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc);
                                System.out.println("insert both ids er");
                                DBManagerMioPruebas.insertRIRT(compoundgc);
                                System.out.println("insert rtri");
                            } else{*/
                            DBManagerMioPruebas.insertDerivatizationMethod(compoundgc); //inserts derivatization method
                            //DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc); //inserts the relation compound-derivatization method
                            System.out.println("insert der id");
                            DBManagerMioPruebas.insertGCColumn(compoundgc); //inserts the column
                            System.out.println("insert gccolumn");
                            DBManagerMioPruebas.insertRIRT(compoundgc); //inserts RI & RT
                            System.out.println("insert rtri");

                            //}
                        } else { //If the derivatization_method exists
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
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc);*/
                    }
                    //System.out.println(i+" HECHO");
                //}
            }
            //System.out.println("AÑADIDO");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*//MAIN TO INSERT SPECTRUM -- REST MUST BE INSERTED --> USE THE OTHER MAIN
    public static void main(String[] args) {
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
                //id_der = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                String dertype = compoundgc.getDertype().toString();
                id_der = DBManagerMioPruebas.getDerivatizationMethodIdfromDerType(dertype);
                System.out.println("ID tabla: "+id_der);
                if(id_der == 0){//if derivatizationmethod is not in the tables
                    DBManagerMioPruebas.insertDerivatizationMethod(compoundgc);
                    //DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc); //inserts compound-derivatizationmethod
                    System.out.println("insert der id");
                    //new_id_der = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                    DBManagerMioPruebas.insertgcmsSpectrum(compoundgc); //inserts the spectrum
                    System.out.println("insert spectrum");

                    //Insert Peaks
                    num_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().size();
                    for (j=1; j<=num_peaks; j++){
                        GCMS_Peaks gcms_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().get(j-1);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc, gcms_peaks);
                    }

                }else{ //if the derivation method exists
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

}