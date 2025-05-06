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
            //String filexcel = "src/main/resources/gcms_excels/Calculo_RI_Alkanes.xlsx";
            String filexcel = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\Compound_RT-RI-Inchi.xlsx";
            List<CompoundGC> compoundgcList = ReadExcel.readexcelcompoundrtriinchi(filexcel);

            CompoundGC compoundgc;
            //compoundgc = compoundgcList.get(3);
            //DBManagerMioPruebas.insertDerivatizationMethod(compoundgc);
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                //DBManagerMioPruebas.insertDerivatizationMethod(compoundgc);
                //DBManagerMioPruebas.insertGCCompoundintoCompoundgcms(compoundgc);

                //DBManagerMioPruebas.insertDerivatizationMethod(compoundgc); //Como actualemnte solo hay un metodo solo hay que añadirlo una vez
                //DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc);
                //DBManagerMioPruebas.insertRIRT(compoundgc);
                //DBManagerMioPruebas.insertGCColumn(compoundgc);

                //DBManagerMioPruebas.insertgcmsSpectrum(compoundgc);
                //DBManagerMioPruebas.insertGCMSPeaks(compoundgc);

                System.out.println(i+" HECHO");
            }
            System.out.println("AÑADIDO");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    //MAIN TO INSERT RT RI --> all inserted
    /*public static void main(String[] args) {
        int i;
        try{
            String filexcel = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\CorrectInfo_CompoundsRT-RI.xlsx";

            List<CompoundGC> compoundgcList = ReadExcel.readexcelcompoundrtriinchi(filexcel);

            CompoundGC compoundgc;
            //compoundgc = compoundgcList.get(3);
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println("C "+i+": "+compoundgc);
                int id = -1;
                compoundgc.setCompound_id(DBManagerMioPruebas.getCompoundIdFromInchi(compoundgc.getINCHI()));
                System.out.println("ID COmpo: "+compoundgc.getCompound_id());
                id = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                System.out.println("ID tabla: "+id);
                if(id == 0){
                    /* This two compounds are not found if we used the name
                    if((compoundgc.getCompound_id()==302139) || (compoundgc.getCompound_id()==302137)){
                        DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc);
                        System.out.println("insert both ids er");
                        DBManagerMioPruebas.insertRIRT(compoundgc);
                        System.out.println("insert rtri");
                    } else{*
                    DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc);
                    System.out.println("insert both ids");
                    DBManagerMioPruebas.insertRIRT(compoundgc);
                    System.out.println("insert rtri");
                    //}
                }else{
                    DBManagerMioPruebas.insertRIRT(compoundgc);
                    System.out.println("insert rtri else");
                }

                //System.out.println(i+" HECHO");
            }
            //System.out.println("AÑADIDO");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/


    //MAIN TO INSERT SPECTRUM
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
                id_der = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                System.out.println("ID tabla: "+id_der);
                if(id_der == 0){//if it is not in the tables
                    DBManagerMioPruebas.insertCompoundsDerMethods(compoundgc);
                    System.out.println("insert both ids");
                    //new_id_der = DBManagerMioPruebas.getCompoundsIdDerMethods(compoundgc.getCompound_id());
                    DBManagerMioPruebas.insertgcmsSpectrum(compoundgc);
                    System.out.println("insert spectrum");

                    //Insert Peaks
                    num_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().size();
                    for (j=1; j<=num_peaks; j++){
                        GCMS_Peaks gcms_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().get(j-1);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc, gcms_peaks);
                    }

                }else{
                    DBManagerMioPruebas.insertgcmsSpectrum(compoundgc);
                    num_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().size();
                    for (j=1; j<=num_peaks; j++){
                        GCMS_Peaks gcms_peaks = compoundgc.getGcmsSpectrum().getGcms_peaksList().get(j-1);
                        DBManagerMioPruebas.insertGCMSPeaks(compoundgc, gcms_peaks);
                    }
                    System.out.println("insert spectrum else");
                }

                System.out.println(i+" HECHO");
            }
            System.out.println("AÑADIDO");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}