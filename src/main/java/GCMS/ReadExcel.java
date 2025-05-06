package GCMS;

import cems_project.Compound;
import dbmanager.PubchemRest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {

    /**
     * Reads the Alkane excel and tranform the data into java objects
     */
    public static List<CompoundGC> readexcelalkanes (String direxcel) throws IOException{

        FileInputStream fis = new FileInputStream((direxcel));
        Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

        int numsheets = wb.getNumberOfSheets();

        int i;
        String infocelltxt = "";
        double infocellnum1 = -1;
        double infocellnum2 = -1;
        CompoundGC compoundgc = null;
        Compound compoundInfo = null;
        List<CompoundGC> compoundgcList = null;
        compoundgcList = new ArrayList<>();

        int j=1;

        //Para este excel no haria falta leer el numero de sheets ya que sabemos que solo tiene una
        for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
            Sheet sheet = wb.getSheetAt(i);
            for (Row row : sheet) { //recorre filas de una hoja
                if(row.getRowNum() != 0) { //Row 0 has the titles (name, RI, RT)
                    //System.out.println("\nNUM FILA: "+j);
                    for (Cell cell : row) {//recorre columnas de la fila
                        if (cell.getColumnIndex() == 0) {//column 0 is the name
                            infocelltxt = cell.getStringCellValue();
                            //compoundgc.setName(infocelltxt);
                        } else if (cell.getColumnIndex() == 1) {//column 1 is the RI
                            infocellnum1 = cell.getNumericCellValue();
                            //compoundgc.setRI(infocellnum1);
                        } else if (cell.getColumnIndex() == 2) {//column 2 is the RT
                            infocellnum2 = cell.getNumericCellValue();
                            //compoundgc.setRT(infocellnum2);
                        }
                    }
                    //System.out.println("Antes sacar compound valores: "+infocelltxt+" 546547");

                    /*
                    El excel se seguira leyendo pero solo si hay un nombre de compuesto (infocelltxt)
                    se incluira en la lista y se buscara su informacion
                     */
                    if(!infocelltxt.isBlank()){
                        compoundInfo = PubchemRest.getCompoundFromName(infocelltxt);

                        /*System.out.println("INFO EXTRA: \n"+compoundInfo.getCompound_id() +
                                compoundInfo.getCompoundName() + " "+
                                compoundInfo.getCasId() + " "+ compoundInfo.getFormula() + " "+ compoundInfo.getMonoisotopicMass() + " "+ compoundInfo.getCompound_status() + " "+
                                compoundInfo.getCompound_type() + " "+ compoundInfo.getLogP() + " "+ compoundInfo.getIdentifiersOwn() + " "+ compoundInfo.getIdentifiersParent() + " "+
                                infocellnum1 + " "+ infocellnum2 + "\n");*/

                        compoundgc = new CompoundGC(compoundInfo.getCompound_id(),
                                compoundInfo.getCompoundName(),
                                compoundInfo.getCasId(), compoundInfo.getFormula(), compoundInfo.getMonoisotopicMass(), compoundInfo.getCompound_status(),
                                compoundInfo.getCompound_type(), compoundInfo.getLogP(), compoundInfo.getIdentifiersOwn(), compoundInfo.getIdentifiersParent(),
                                infocellnum1, infocellnum2);

                        compoundgcList.add(compoundgc);
                        infocelltxt = "";
                    }
                    j++;
                }
            }
        }
        //System.out.println("\nTam lista: "+compoundgcList.size());

        wb.close();
        fis.close();
        return compoundgcList;
    }

    /**
     * @param direxcel
     * @return
     * @throws IOException
     */
    /*public static List<CompoundGC> readexcelcompounds (String direxcel) throws IOException{

        FileInputStream fis = new FileInputStream((direxcel));
        Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

        int numsheets = wb.getNumberOfSheets();

        int i;
        String infocelltxt = "";
        double infocellnum1 = -1;
        double infocellnum2 = -1;
        CompoundGC compoundgc = null;
        Compound compoundInfo = null;
        List<CompoundGC> compoundgcList = null;
        compoundgcList = new ArrayList<>();

        int j=1;

        //Para este excel no haria falta leer el numero de sheets ya que sabemos que solo tiene una
        for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
            Sheet sheet = wb.getSheetAt(i);
            for (Row row : sheet) { //recorre filas de una hoja
                if(row.getRowNum() != 0) { //Row 0 has the titles (name, RI, RT)
                    //System.out.println("\nNUM FILA: "+j);
                    for (Cell cell : row) {//recorre columnas de la fila
                        if (cell.getColumnIndex() == 0) {//column 0 is the name
                            infocelltxt = cell.getStringCellValue();
                            //compoundgc.setName(infocelltxt);
                        } else if (cell.getColumnIndex() == 1) {//column 1 is the RI
                            infocellnum1 = cell.getNumericCellValue();
                            //compoundgc.setRI(infocellnum1);
                        } else if (cell.getColumnIndex() == 2) {//column 2 is the RT
                            infocellnum2 = cell.getNumericCellValue();
                            //compoundgc.setRT(infocellnum2);
                        }
                    }
                    //System.out.println("Antes sacar compound valores: "+infocelltxt+" 546547");

                    /*
                    El excel se seguira leyendo pero solo si hay un nombre de compuesto (infocelltxt)
                    se incluira en la lista y se buscara su informacion
                     *
                    if(!infocelltxt.isBlank()){
                        compoundInfo = PubchemRest.getCompoundFromName(infocelltxt);

                        /*System.out.println("INFO EXTRA: \n"+compoundInfo.getCompound_id() +
                                compoundInfo.getCompoundName() + " "+
                                compoundInfo.getCasId() + " "+ compoundInfo.getFormula() + " "+ compoundInfo.getMonoisotopicMass() + " "+ compoundInfo.getCompound_status() + " "+
                                compoundInfo.getCompound_type() + " "+ compoundInfo.getLogP() + " "+ compoundInfo.getIdentifiersOwn() + " "+ compoundInfo.getIdentifiersParent() + " "+
                                infocellnum1 + " "+ infocellnum2 + "\n");*

                        compoundgc = new CompoundGC(compoundInfo.getCompound_id(),
                                compoundInfo.getCompoundName(),
                                compoundInfo.getCasId(), compoundInfo.getFormula(), compoundInfo.getMonoisotopicMass(), compoundInfo.getCompound_status(),
                                compoundInfo.getCompound_type(), compoundInfo.getLogP(), compoundInfo.getIdentifiersOwn(), compoundInfo.getIdentifiersParent(),
                                infocellnum1, infocellnum2);

                        compoundgcList.add(compoundgc);
                        infocelltxt = "";
                    }
                    j++;
                }
            }
        }
        //System.out.println("\nTam lista: "+compoundgcList.size());

        wb.close();
        fis.close();
        return compoundgcList;
    }*/

    /**
     * Reads the excel with the names of compounds, rt, ri and inchi
     * @param direxcel
     * @return
     * @throws IOException
     */
    public static List<CompoundGC> readexcelcompoundrtriinchi (String direxcel) throws IOException{
        //String filexcel2 = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\Compound_RT-RI-Inchi.xlsx";
        String filexcel2 = direxcel;
        FileInputStream fis = new FileInputStream((filexcel2));
        Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

        int numsheets = wb.getNumberOfSheets();

        CompoundGC compoundgc = null;
        Compound compoundInfo = null;
        List<CompoundGC> compoundgcList = null;
        compoundgcList = new ArrayList<>();

        int i;
        int j=0;
        String infocelltxt0 = "";
        String infocelltxt1 = "";
        double infocellnum2 = -1;
        double infocellnum3 = -1;
        String infocelltxt4 = "";


        for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
            System.out.print("\nNUM HOJA: "+i+":  ");
            Sheet sheet = wb.getSheetAt(i);
            if(i==0){
                for (Row row : sheet) { //recorre filas de una hoja
                    System.out.print("\nNUM ROW: "+ row.getRowNum() + ":  ");
                    if(row.getRowNum() != 0) { //Row 0 has the titles
                        //System.out.print("NUM FILA: "+j+":  ");
                        for (Cell cell : row) {//recorre columnas de la fila
                            /*if (cell.getColumnIndex() == 0) {//column 0 is the name in excel
                                infocelltxt0 = cell.getStringCellValue();
                            }else */if (cell.getColumnIndex() == 0) {//column 1 is the name in the compound table
                                infocelltxt1 = cell.getStringCellValue();
                            }else if (cell.getColumnIndex() == 1) {//Rt
                                infocellnum2 = cell.getNumericCellValue();
                            } else if (cell.getColumnIndex() == 2) {//RI
                                infocellnum3 = cell.getNumericCellValue();
                            }/*else if (cell.getColumnIndex() == 4) {//inchi
                                infocelltxt4 = cell.getStringCellValue();
                            }*/
                        }
                        //if(!infocelltxt0.isBlank()){
                            //System.out.println(infocelltxt0 + "  "+ infocelltxt1 + "  "+infocellnum2 + "    "+
                              //      infocellnum3 + "    "+ infocelltxt4);

                            if(infocelltxt1.isBlank()){
                                System.out.println("EL COMPOUESTO NO SE HA ENCONTRADO");
                            } else{
                                //This if is because in the page that is used polylimonene is not found but is the same as limonene
                                if(infocelltxt1.equalsIgnoreCase("Polylimonene")){
                                    String name = "limonene";
                                    compoundInfo = PubchemRest.getCompoundFromName(name);

                                    compoundgc = new CompoundGC(compoundInfo.getCompound_id(),
                                            infocelltxt1, compoundInfo.getCasId(), compoundInfo.getFormula(),
                                            compoundInfo.getMonoisotopicMass(),
                                            compoundInfo.getCompound_status(),
                                            compoundInfo.getCompound_type(), compoundInfo.getLogP(),
                                            compoundInfo.getIdentifiersOwn(),
                                            compoundInfo.getIdentifiersParent(),
                                            infocellnum3, infocellnum2);

                                    System.out.println("Compound: "+compoundgc);
                                    compoundgcList.add(compoundgc);
                                } else{
                                compoundInfo = PubchemRest.getCompoundFromName(infocelltxt1);
                                //PubchemRest.getCompoundsFromInchis();

                                compoundgc = new CompoundGC(compoundInfo.getCompound_id(),
                                        compoundInfo.getCompoundName(),
                                        compoundInfo.getCasId(), compoundInfo.getFormula(), compoundInfo.getMonoisotopicMass(), compoundInfo.getCompound_status(),
                                        compoundInfo.getCompound_type(), compoundInfo.getLogP(), compoundInfo.getIdentifiersOwn(), compoundInfo.getIdentifiersParent(),
                                        infocellnum3, infocellnum2);

                                System.out.println("Compound: "+compoundgc);
                                compoundgcList.add(compoundgc);
                                infocelltxt0 = "";
                                }
                            }

                        //}
                        //j++;
                    }
                }
            }
        }

        wb.close();
        fis.close();

        return compoundgcList;

    }

    /**
     * Reads the excel with the names of the compounds and its mz and intensities
     * @param direxcel
     * @return List<CompoundGC>
     * @throws IOException
     */
    public static List<CompoundGC> readexcelcompoundspectrum(String direxcel) throws IOException{
        String filexcel2 = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\CEU_Mass_Mediator_mz_Rel_Abund.xlsx";
        //String filexcel2 = direxcel;
        FileInputStream fis = new FileInputStream((filexcel2));
        Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

        int numsheets = wb.getNumberOfSheets();

        CompoundGC compoundgc = null;
        Compound compoundInfo = null;
        List<CompoundGC> compoundgcList = null;
        compoundgcList = new ArrayList<>();

        int i;
        int j=0;
        String infocelltxt0 = "";
        double infocellnum1;
        double infocellnum2;

        for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
            //System.out.print("\nNUM HOJA: "+i+":  ");
            Sheet sheet = wb.getSheetAt(i);
            String sheetname = sheet.getSheetName();//is the name of the sheet which is the name of the compound
            GCMS_Spectrum spectrum = null; //for every sheet it will reset

            List<GCMS_Peaks> gcms_peaksList = new ArrayList<>();

            for (Row row : sheet) { //recorre filas de una hoja
                //System.out.print("\nNUM ROW: "+ row.getRowNum() + ":  ");
                if(row.getRowNum() != 0) { //Row 0 has the titles
                    //System.out.print("NUM FILA: "+j+":  ");
                    GCMS_Peaks gcms_peaks = new GCMS_Peaks();
                    for (Cell cell : row) {//recorre columnas de la fila
                        if (cell.getColumnIndex() == 0) {//column 1 is the mz
                            infocellnum1 = cell.getNumericCellValue();
                            //System.out.println("NUM 1 "+infocellnum1);
                            String info1 = Double.toString(infocellnum1);
                            //System.out.println("INFO MZ: " + info1);
                            if (!info1.equals("")) {
                                //System.out.println("Entra not blank 1");
                                //gcms_peaks.setMz(-2);
                                gcms_peaks.setMz(infocellnum1);//add mz
                            }
                        }
                        if (cell.getColumnIndex() == 1) {//column 2 is the intensity
                            infocellnum2 = cell.getNumericCellValue();
                            //System.out.println("NUM 2 " + infocellnum2);
                            String info2 = Double.toString(infocellnum2);
                            //System.out.println("INFO INTENSITY: " + info2);
                            if (!info2.equals("")) {
                                //System.out.println("Entra not blank 2");
                                //gcms_peaks.setIntensity(-2);
                                gcms_peaks.setIntensity(infocellnum2);//add intensity
                            }
                        }
                    }
                    if (gcms_peaks.getMz() != -1 || gcms_peaks.getIntensity() != -1){
                        gcms_peaksList.add(gcms_peaks);//add peaks to the list
                    }
                }
            }
            spectrum = new GCMS_Spectrum(gcms_peaksList);

            compoundInfo = PubchemRest.getCompoundFromName(sheetname);
            //PubchemRest.getCompoundsFromInchis();

            compoundgc = new CompoundGC(compoundInfo.getCompound_id(),
                    compoundInfo.getCompoundName(),
                    compoundInfo.getCasId(), compoundInfo.getFormula(), compoundInfo.getMonoisotopicMass(), compoundInfo.getCompound_status(),
                    compoundInfo.getCompound_type(), compoundInfo.getLogP(), compoundInfo.getIdentifiersOwn(), compoundInfo.getIdentifiersParent(),
                    spectrum);

            //System.out.println("Compound: "+compoundgc);
            compoundgcList.add(compoundgc);
            infocelltxt0 = "";

        }
        wb.close();
        fis.close();

        return compoundgcList;
    }

    /*public static void main(String[] args) {
        //Poner direccion en la que se tenga el excel del calculo de RI de ALkanes
        //String filexcel = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Calculo_RI_Alkanes.xlsx";
        String filexcel1 = "src/main/resources/gcms_excels/Calculo_RI_Alkanes.xlsx";

        try {
            List<CompoundGC> compoundgcList = readexcelalkanes(filexcel1);
            GCMS gcms = new GCMS(compoundgcList);

            System.out.println("\nList Size: "+gcms.getCompoundgcList().size());
            System.out.println("INFO EXCEL ALKANES:"+gcms.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*public static double solvecubicequationwithx(double x){
        double solution = 0;
        solution = 0.1235 * x*x*x + 2.9235 * x*x -21.41 *x + 842.05;
        return solution;
    }

    public static double solvecubicequationwithoutx(double y){
        double solution = 0;
        //0.1235 * x*x*x + 2.9235 * x*x -21.41 *x + 842.05 - y;
        return solution;
    }

    public static void main(String[] args) {
        //ax^3 + bx^2 + cx +d = 0

        double x = 2;
        //double solution = solvecubicequationwithx(x);

    }*/

    //THIS MAIN IS WRONG!!!
    /*public static void main(String[] args) {
        //Poner direccion en la que se tenga el excel del calculo de RI de ALkanes
        String filexcel2 = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\Erica_RI_Calculation.xlsx";
        //String filexcel1 = "src/main/resources/gcms_excels/Calculo_RI_Alkanes.xlsx";

        try {
            //List<CompoundGC> compoundgcList = readexcelcompounds(filexcel2);

            FileInputStream fis = new FileInputStream((filexcel2));
            Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

            int numsheets = wb.getNumberOfSheets();

            int i;
            String infocelltxt = "";
            double infocellnum1 = -1;
            double infocellnum2 = -1;
            double infocellnum3 = -1;
            double infocellnum4 = -1;
            double infocellnum5 = -1;
            double infocellnum6 = -1;
            double infocellnum7 = -1;


            int j=1;

            System.out.println("        Library nam"+"      RTanalyte"+"    RTCn"+"     n"+
                    "    RTCn+1"+	"IRcalc"+"      IRLit"+"    ΔRI");
            for (i = 0; i < 2; i++) { //for(Sheet sheet : wb)
                System.out.print("\nNUM HOJA: "+i+":  ");
                Sheet sheet = wb.getSheetAt(i);
                if(i==0){
                    for (Row row : sheet) { //recorre filas de una hoja
                        if(row.getRowNum() != 0) { //Row 0 has the titles (name, RI, RT)
                            System.out.print("NUM FILA: "+j+":  ");
                            for (Cell cell : row) {//recorre columnas de la fila
                                if (cell.getColumnIndex() == 0) {//column 0 is the name
                                    infocelltxt = cell.getStringCellValue();
                                } else if (cell.getColumnIndex() == 1) {
                                    infocellnum1 = cell.getNumericCellValue();
                                } else if (cell.getColumnIndex() == 2) {
                                    infocellnum2 = cell.getNumericCellValue();
                                }
                            }
                            System.out.println(infocelltxt + "  "+infocellnum1 + "    "+ infocellnum2);
                            j++;
                        }
                    }
                }
                j=0;
                if(i==1){
                    for (Row row : sheet) { //recorre filas de una hoja
                        System.out.println("PRueba ini: "+row.getRowNum());
                        if(row.getRowNum() > 9/*row.getRowNum() != 0 || row.getRowNum() !=1 || row.getRowNum() !=2 || row.getRowNum() !=3
                            || row.getRowNum() !=4 || row.getRowNum() !=5 || row.getRowNum() !=6
                            || row.getRowNum() !=7 || row.getRowNum() !=8 || row.getRowNum() !=9/) {

                            j = row.getRowNum();
                            System.out.print("\nNUM FILA: "+j+":  ");

                            for (Cell cell : row) {//recorre columnas de la fila
                                /*if(cell.getStringCellValue().equalsIgnoreCase("#N/D")){
                                    System.out.println("Not found");
                                }*/
                                /*System.out.println("Tyepe: "+cell.getCellType());
                                if(cell.getCellType().equals("STRING")){
                                    System.out.println("HOLA");
                                }else/{
                                    if (cell.getColumnIndex() == 0) {//column 0 is the name
                                        infocelltxt = cell.getStringCellValue();
                                        System.out.println("CELL VALUE 0: "+ infocelltxt);
                                    } else if (cell.getColumnIndex() == 1) {//column 1 is the RT analyte
                                        System.out.println("CELL VALUE 1: "+ cell.getNumericCellValue());
                                        //String infocellprueba = cell.getStringCellValue();
                                        //System.out.println("Info PRueba: "+ infocellprueba);
                                        infocellnum1 = cell.getNumericCellValue();
                                        System.out.println("CELL VALUE 1: "+ infocellnum1);
                                    } else if (cell.getColumnIndex() == 2) {//column 2 is the RT Cn
                                        System.out.println("CELL VALUE 2 ERROR: "+ cell.getCellType());
                                        CellType errortype = cell.getCellType();
                                        if(cell.getCellType()==errortype){
                                            System.out.println("CELL VALUE 2 ERROR: "+ errortype);
                                        }
                                        infocellnum2 = cell.getNumericCellValue();
                                        System.out.println("CELL VALUE 2: "+ infocellnum2);
                                    } else if (cell.getColumnIndex() == 3) {//column 3 is the n
                                        infocellnum3 = cell.getNumericCellValue();
                                        System.out.println("CELL VALUE 3: "+ infocellnum3);
                                    } else if (cell.getColumnIndex() == 4) {//column 4 is the RT Cn+1
                                        infocellnum4 = cell.getNumericCellValue();
                                        System.out.println("CELL VALUE 4: "+ infocellnum4);
                                    } else if (cell.getColumnIndex() == 5) {//column 5 is the IR Calc
                                        infocellnum5 = cell.getNumericCellValue();
                                        System.out.println("CELL VALUE 5: "+ infocellnum5);
                                    } else if (cell.getColumnIndex() == 6) {//column 6 is the IR Lit
                                        infocellnum6 = cell.getNumericCellValue();
                                        System.out.println("CELL VALUE 6: "+ infocellnum6);
                                    } else if (cell.getColumnIndex() == 7) {//column 7 is the ARI
                                        infocellnum7 = cell.getNumericCellValue();
                                        System.out.println("CELL VALUE 7: "+ infocellnum7);
                                    }
                                }
                            }

                            System.out.println(infocelltxt + "  "+
                                    infocellnum1 + "    "+ infocellnum2 + "    "+ infocellnum3 + "    "+
                                    infocellnum4 + "    "+ infocellnum5 + "    "+ infocellnum6 + "    "+
                                    infocellnum7);
                            j++;
                        }
                    }
                }
            }
            //System.out.println("\nTam lista: "+compoundgcList.size());

            wb.close();
            fis.close();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*public static void main(String[] args) {
        String filexcel2 = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\Compound_RT-RI-Inchi.xlsx";
        //String filexcel1 = "src/main/resources/gcms_excels/Calculo_RI_Alkanes.xlsx";

        try {
            FileInputStream fis = new FileInputStream((filexcel2));
            Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

            int numsheets = wb.getNumberOfSheets();

            int i;
            int j=0;
            String infocelltxt = "";
            double infocellnum1 = -1;
            double infocellnum2 = -1;
            String infocelltxt3 = "";


            for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
                System.out.print("\nNUM HOJA: "+i+":  ");
                Sheet sheet = wb.getSheetAt(i);
                if(i==0){
                    for (Row row : sheet) { //recorre filas de una hoja
                        System.out.print("\nNUM ROW: "+ row.getRowNum() + ":  ");
                        if(row.getRowNum() != 0) { //Row 0 has the titles
                            //System.out.print("NUM FILA: "+j+":  ");
                            for (Cell cell : row) {//recorre columnas de la fila
                                if (cell.getColumnIndex() == 0) {//column 0 is the name
                                    infocelltxt = cell.getStringCellValue();
                                } else if (cell.getColumnIndex() == 1) {
                                    infocellnum1 = cell.getNumericCellValue();
                                } else if (cell.getColumnIndex() == 2) {
                                    infocellnum2 = cell.getNumericCellValue();
                                }else if (cell.getColumnIndex() == 3) {
                                    infocelltxt3 = cell.getStringCellValue();
                                }
                            }
                            if(!infocelltxt.isBlank()){
                                System.out.println(infocelltxt + "  "+infocellnum1 + "    "+ infocellnum2 + "    "+ infocelltxt3);
                            }
                            //j++;
                        }
                    }
                }
            }

        wb.close();
        fis.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/

    //Main to try readexcelcompoundrtriinchi()
   /*public static void main(String[] args) {
       //String filexcel = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\Compound_RT-RI-Inchi.xlsx";
       String filexcel2 = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\CorrectInfo_CompoundsRT-RI.xlsx";
       try{
           List<CompoundGC> compoundgcList = readexcelcompoundrtriinchi(filexcel2);
           CompoundGC compoundgc;
           int i;
           System.out.println(compoundgcList.get(0));
           for (i=1; i<=compoundgcList.size(); i++){
               compoundgc = compoundgcList.get(i-1);
               System.out.println(i+": "+compoundgc.toString());
           }
           System.out.println("algo");
       }catch (IOException e) {
           throw new RuntimeException(e);
       }
   }*/

    //main to try readexcelcompoundspectrum()
    public static void main(String[] args) {
        String filexcel2 = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Excels\\CEU_Mass_Mediator_mz_Rel_Abund.xlsx";
        //Change the file with the correct one once i have it
        try{
            List<CompoundGC> compoundgcList = readexcelcompoundspectrum(filexcel2);
            CompoundGC compoundgc;
            int i;
            //System.out.println(compoundgcList.get(0));
            System.out.println("\nstart list: ");
            for (i=1; i<=compoundgcList.size(); i++){
                compoundgc = compoundgcList.get(i-1);
                System.out.println(i+": "+compoundgc.toString());
            }
            //System.out.println("algo");
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
