/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cems_project;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import dbmanager.PubchemRest;
import exceptions.EffMobNotAvailable;
import experimental_properties.SampleType;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import patternFinders.RegexInChI;

import static constants.Constants.PUBCHEM_ENDPOINT_COMPOUND_NAME;
import static dbmanager.ConstantQueries.*;
import static org.apache.poi.ss.usermodel.CellType.*;

/**
 * @author maria
 */
public class Fichero_Original {

    //Introduce String compoundsFileName, String experimentalConditionsFileName (dentro de leerFichero)
    public static List<CEMSCompound> leerFichero(String fileName, CEMSExperimentalConditions cemsExperimentalConditions) {  //utilizar interfaces: devolver List, no LinkedList (para no centrarnos solo en esta implementación)

        // TODO code application logic here
        File excelFile = new File(fileName);
        List<CEMSCompound> CEMSCompound = new LinkedList<CEMSCompound>();

        // inicializar según valores leídos

        // we create an XSSF Workbook object for our XLSX Excel File
        //It is a class that is used to represent both high and low level Excel file formats.
        XSSFSheet sheet;

        int line = 2;
        try (FileInputStream fis = new FileInputStream(excelFile); // we create an XSSF Workbook object for our XLSX Excel File
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            // we get first sheet
            sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            System.out.println("READING " + (totalRows - 1) + " compounds\n");

            Iterator<Row> rowIt = sheet.iterator();
            // skip the header
            rowIt.next();   //lee la linea en la que pone "Patrones"
            //rowIt.next(); //se salta la linea en la que pone COMPOUND, FORMULA...

            //creamos todas las variables que vamos a ir necesitando
            String name;
            Integer id_code;
            String formula;
            Double M;
            String level;
            String sample;
            Double mz;
            Double MT;    //datos no primitivos para poder asignar null
            Double RMT;
            Double eff_mob;
            String inchi;
            String casId;
            Integer compound_type;
            Integer compound_status;
            Identifier identifiersOwn;
            Integer pc_id;
            Identifier identifiersParent;
            Integer sampleInt;
            Integer levelInt;


            while (rowIt.hasNext()) {   //este bucle va avanzando linea a linea

                Row row = rowIt.next();

                //iterate on cells for the current row
                Iterator<Cell> cellIterator = row.cellIterator();   //con este iterador nos vamos moviendo por las columnas dentro de una misma fila

                //vamos leyendo cada columna y guardando los valores en una variable para luego crear los metabolitos
                name = cellIterator.next().getStringCellValue();
                try{
                    identifiersOwn = PubchemRest.getIdentifierFromName(name);

                    //para algunas de las siguientes propiedades, hay algunas que están sin rellenar, en tal caso, se asigna un valor imposible
                    try {
                        id_code = (int) cellIterator.next().getNumericCellValue();
                    } catch (IllegalStateException ise) {
                        id_code = null;
                        System.out.println("Chequear ID linea " + line);
                    }
                    Cell c0 = cellIterator.next();
                    try {
                        if (c0.getCellType() == STRING){
                            formula = RegexInChI.getFormulaFromInChI(identifiersOwn.getInchi());
                        } else{
                            formula = c0.getStringCellValue();
                        }
                    } catch (IllegalStateException ise) {
                        formula = null;
                        System.out.println("Chequear Formula linea " + line);
                    }
                    Cell c1 = cellIterator.next();
                    try {
                        if (c1.getCellType() == STRING){
                            M = PubchemRest.getCompoundFromName(name).getMonoisotopicMass();
                        } else{
                            M = c1.getNumericCellValue();
                        }
                    } catch(NumberFormatException nfe) {
                        M = null;
                        System.out.println("Chequear M linea " + line);
                    }
                    Cell c2 = cellIterator.next();
                    try {
                        if (c2.getCellType() == STRING){
                            mz = null;
                        } else{
                            mz = c2.getNumericCellValue();
                        }
                    } catch(NumberFormatException nfe) {
                        mz = null;
                        System.out.println("Chequear mz linea " + line);
                    }
                    try {
                        level = cellIterator.next().getStringCellValue();
                        level = level.replaceAll("[^0-9]", " ");
                        level = level.trim();
                        levelInt = Integer.parseInt(level);
                    } catch (IllegalStateException ise) {
                        level = null;
                        levelInt = -1;
                        System.out.println("Chequear Level linea " + line);
                    }
                    try {
                        sample = cellIterator.next().getStringCellValue();
                        sampleInt = SampleType.MAPSAMPLETYPES.get(sample);
                        //este método devuelve un double, por lo tanto no hay que hacer ningín cast
                    } catch (IllegalStateException | NullPointerException ise) {
                        sample = null;
                        sampleInt = 0;
                        System.out.println("Chequear Sample linea " + line);
                    }
                    Cell c3 = cellIterator.next();
                    try {
                        if (c3.getCellType() == STRING){
                            MT = null;
                        } else{
                            MT = c3.getNumericCellValue();
                        }
                    } catch (IllegalStateException ise) {
                        MT = null;
                        System.out.println("Chequear MT linea " + line);
                    }
                    Cell c4 = cellIterator.next();
                    try {
                        if (c4.getCellType() == STRING){
                            RMT = null;
                        } else{
                            RMT = c4.getNumericCellValue();
                        }
                    } catch (IllegalStateException ise) {
                        RMT = null;
                        System.out.println("Chequear RMT linea " + line);
                    }
                    // IF ELSE o TRY/CATCH
                    Cell c5 = cellIterator.next();
                    try {
                        if (c5.getCellType() == NUMERIC){
                            eff_mob = c5.getNumericCellValue();
                        }else {
                            if (cemsExperimentalConditions.getMtCompoundA() != null){
                                if (MT != null){
                                    eff_mob = cemsExperimentalConditions.getEffMob2Markers(MT);
                                }else{
                                    eff_mob = cemsExperimentalConditions.getEffMob2Markers(RMT);
                                }
                            }
                            else {
                                if (MT != null){
                                    eff_mob = cemsExperimentalConditions.getEffMobFromExpConditions(MT);
                                }else{
                                    eff_mob = cemsExperimentalConditions.getEffMobFromExpConditions(RMT);
                                }
                            }
                        }
                    }
                    catch ( EffMobNotAvailable emna)
                    {
                        eff_mob = -Double.MIN_VALUE;
                    }
                    casId = null;
                    pc_id = identifiersOwn.getPc_id();
                    line++;
                    //vamos creando cada metabolito y lo añadimos a la lista
                    CEMSCompound m = new CEMSCompound(id_code, name, casId,
                            null, null, formula, M, mz,
                            identifiersOwn, sampleInt,
                            MT, RMT, null, null, eff_mob, levelInt);
                    CEMSCompound.add(m);
                }catch(IOException e){
                    System.out.println("Check this compound name (IOException pubchem) " + name);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Fichero.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NoSuchElementException nsee)
        {
            System.out.println("Check line " + line);
        }
        catch (IOException ex) {
            Logger.getLogger(Fichero.class.getName()).log(Level.SEVERE, null, ex);
        }
        return CEMSCompound;
    }

    public static void writeExcel(List<CEMSCompound> listMetabolito, String excelFilePath) throws Exception {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new Exception("invalid file name, should be xls or xlsx");
        }
        Sheet sheet = workbook.createSheet();

        Iterator<CEMSCompound> iterator = listMetabolito.iterator();
        int rowIndex = 0;

        // create headers
        Row row = sheet.createRow(rowIndex++);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("Name");
        Cell cell1 = row.createCell(1);
        cell1.setCellValue("Eff Mob");
        Cell cell2 = row.createCell(2);
        cell2.setCellValue("Monoisotopic Mass");
        Cell cell3 = row.createCell(3);
        cell3.setCellValue("M/Z");
        Cell cell4 = row.createCell(4);
        cell4.setCellValue("Level");
        Cell cell5 = row.createCell(5);
        cell5.setCellValue("Sample");
        Cell cell6 = row.createCell(6);
        cell6.setCellValue("MT");
        Cell cell7 = row.createCell(7);
        cell7.setCellValue("RMT");
        Cell cell8 = row.createCell(8);
        cell8.setCellValue("ID");
        Cell cell9 = row.createCell(9);
        cell9.setCellValue("Inchi");

        while (iterator.hasNext()) {
            CEMSCompound CEMSCompound = iterator.next();
            row = sheet.createRow(rowIndex++);
            cell0 = row.createCell(0);
            cell0.setCellValue(CEMSCompound.getCompoundName());
            System.out.println(CEMSCompound.getCompoundName());
            cell1 = row.createCell(1);
            cell1.setCellValue(CEMSCompound.getEff_mobility());
            System.out.println(CEMSCompound.getEff_mobility());
            cell2 = row.createCell(2);
            if (CEMSCompound.getMonoisotopicMass() == null){
                cell2.setCellValue("null");
            }else{
                cell2.setCellValue(CEMSCompound.getMonoisotopicMass());
            }
            cell3 = row.createCell(3);
            if (CEMSCompound.getExperimentalMZ() == null){
                cell3.setCellValue("null");
            }else{
                cell3.setCellValue(CEMSCompound.getExperimentalMZ());
            }
            cell4 = row.createCell(4);
            cell4.setCellValue(CEMSCompound.getIdentificationLevel());
            cell5 = row.createCell(5);
            cell5.setCellValue(CEMSCompound.getSampleTypeInt());
            cell6 = row.createCell(6);
            if (CEMSCompound.getMT() == null){
                cell6.setCellValue("null");
            }else{
                cell6.setCellValue(CEMSCompound.getMT());
            }
            cell7 = row.createCell(7);
            if (CEMSCompound.getRMT() == null){
                cell7.setCellValue("null");
            }else{
                cell7.setCellValue(CEMSCompound.getRMT());
            }
            cell8 = row.createCell(8);
            cell8.setCellValue(CEMSCompound.getCompound_id());
            cell9 = row.createCell(9);
            cell9.setCellValue(CEMSCompound.getINCHI());
        }

        FileOutputStream fos = new FileOutputStream(excelFilePath);
        workbook.write(fos);
        fos.close();
        System.out.println(excelFilePath + " written successfully");

    }
}


