package CCS;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cems_project.Identifier;
import chemicalFormula.Element;
import dbmanager.PubchemRest;
import exceptions.WrongRequestException;
import org.apache.poi.ss.usermodel.*;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.descriptors.molecular.XLogPDescriptor;

import static chemicalFormula.PeriodicTable.*;
import static dbmanager.PubchemRest.getIdentifiersFromInChIPC;
import static dbmanager.PubchemRest.getIdentifiersFromSMILESPC;


public class CCS_insert {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        String filePathPos = "/home/ceu/research/CCS/CCS_oxylipin_database_ceumass_positive.xlsx";
        String filePathNeg = "/home/ceu/research/CCS/CCS_oxylipin_database_ceumass_negative.xlsx";
        List<Integer> compoundIDpresent = new ArrayList<>();
        List<Integer> compoundIDnot = new ArrayList<>();

        dataBaseProcess(filePathPos);
        dataBaseProcess(filePathNeg);

    }

    public static void dataBaseProcess(String filePath) {
        List<String> smilesList = new ArrayList<>();
        PubchemRest pubchem = new PubchemRest();

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(file)) {
            // Assume the first sheet
            Sheet sheet = workbook.getSheetAt(0);
            int smilesColumnIndex = -1;
            int compoundColumnIndex = -1;
            int formulaColumnIndex = -1;
            int adductColumnIndex = -1;
            int averageExperimentalCCSColumnIndex = -1;
            int chargeColumnIndex = -1;

            // Iterate through rows
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Get the header row and find the column index
                if (row.getRowNum() == 0) {
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.STRING) {
                            String header = cell.getStringCellValue().trim();
                            if ("SMILES".equalsIgnoreCase(header)) {
                                smilesColumnIndex = cell.getColumnIndex();
                            } else if ("Compound".equalsIgnoreCase(header)) {
                                compoundColumnIndex = cell.getColumnIndex();
                            } else if ("Formula".equalsIgnoreCase(header)) {
                                formulaColumnIndex = cell.getColumnIndex();
                            } else if ("Adduct".equalsIgnoreCase(header)) {
                                adductColumnIndex = cell.getColumnIndex();
                            } else if ("Average Experimental CCS".equalsIgnoreCase(header)) {
                                averageExperimentalCCSColumnIndex = cell.getColumnIndex();
                            } else if ("Charge".equalsIgnoreCase(header)) {
                                chargeColumnIndex = cell.getColumnIndex();
                            }
                        }
                    }
                    if (smilesColumnIndex == -1 || compoundColumnIndex == -1 || formulaColumnIndex == -1 ||
                            adductColumnIndex == -1 || averageExperimentalCCSColumnIndex == -1 || chargeColumnIndex == -1) {
                        throw new IllegalArgumentException("One or more required columns are missing in the provided Excel file.");
                    }
                } else {
                    Cell smilesCell = row.getCell(smilesColumnIndex);
                    Cell compoundCell = row.getCell(compoundColumnIndex);
                    Cell formulaCell = row.getCell(formulaColumnIndex);
                    Cell adductCell = row.getCell(adductColumnIndex);
                    Cell averageExperimentalCCSCell = row.getCell(averageExperimentalCCSColumnIndex);
                    Cell chargeCell = row.getCell(chargeColumnIndex);

                    if (smilesCell != null && smilesCell.getCellType() == CellType.STRING) {
                        String smiles = smilesCell.getStringCellValue().trim();
                        String compound = compoundCell != null && compoundCell.getCellType() == CellType.STRING
                                ? compoundCell.getStringCellValue().trim() : null;
                        String formula = formulaCell != null && formulaCell.getCellType() == CellType.STRING
                                ? formulaCell.getStringCellValue().trim() : null;
                        String adduct = adductCell != null && adductCell.getCellType() == CellType.STRING
                                ? adductCell.getStringCellValue().trim() : null;
                        Double averageExperimentalCCS = averageExperimentalCCSCell != null && averageExperimentalCCSCell.getCellType() == CellType.NUMERIC
                                ? averageExperimentalCCSCell.getNumericCellValue() : null;
                        Double charge = chargeCell != null && chargeCell.getCellType() == CellType.NUMERIC
                                ? chargeCell.getNumericCellValue() : null;

                        // Process adduct to remove brackets and trailing ionization mode
                        if (adduct != null) {
                            adduct = processAdduct(adduct);
                        }

                        //Get identifier
                        Identifier identifier = getIdentifiersFromSMILESPC(smiles);

                        //Connection to compounds DDBB
                        String jdbcURLCMM = "jdbc:mysql://localhost:3306/metabolites";
                        String user = "alberto";
                        String password = "alberto";

                        try (Connection CMMConnection = DriverManager.getConnection(jdbcURLCMM, user, password)) {
                            Integer pc_id;
                            String sqlSelect = "SELECT compound_id FROM compound_identifiers WHERE inchi_key = ?";
                            try (PreparedStatement CMMQueryStatement = CMMConnection.prepareStatement(sqlSelect)) {
                                CMMQueryStatement.setString(1, identifier.getInchi_key());
                                try (ResultSet resultSet = CMMQueryStatement.executeQuery()) {
                                    if (resultSet.next()) { // It's already in the DDBB
                                        pc_id = resultSet.getInt("compound_id");
                                    } else { // It's NOT in the CMM DB
                                        pc_id = pubchem.getPCIDFromInchiKey(identifier.getInchi_key());
                                        String inchi = identifier.getInchi();
                                        String inchi_key = identifier.getInchi_key();

                                        checkCompoundsTable(CMMConnection, pc_id, compound, smiles);

                                        String sqlInsert = "INSERT INTO compound_identifiers (compound_id, inchi, inchi_key, smiles) VALUES (?, ?, ?, ?)";
                                        try (PreparedStatement insertSql = CMMConnection.prepareStatement(sqlInsert)) {
                                            insertSql.setInt(1, pc_id);
                                            insertSql.setString(2, inchi);
                                            insertSql.setString(3, inchi_key);
                                            insertSql.setString(4, smiles);

                                            insertSql.executeUpdate();
                                        }
                                    }
                                }
                            }

                            // 1. Get adduct_id
                            sqlSelect = "SELECT adduct_id FROM adduct WHERE adduct_type = ?";
                            Integer adduct_id;
                            try (PreparedStatement CMMQuery = CMMConnection.prepareStatement(sqlSelect)) {
                                CMMQuery.setString(1, adduct);
                                try (ResultSet result = CMMQuery.executeQuery()) {
                                    if (result.next()) { // It's in the DDBB
                                        adduct_id = result.getInt("adduct_id");
                                    } else { // It's NOT in the CMM DB
                                        String sqlInsert = "INSERT INTO adduct (ionization_mode, adduct_type) VALUES (?, ?)";
                                        try (PreparedStatement insertSql = CMMConnection.prepareStatement(sqlInsert)) {
                                            insertSql.setDouble(1, charge);
                                            insertSql.setString(2, adduct);

                                            insertSql.executeUpdate();
                                        }

                                        // Get the adduct_id to insert later into compound_css
                                        sqlSelect = "SELECT adduct_id FROM adduct WHERE adduct_type = ?";
                                        try (PreparedStatement fetchAdductId = CMMConnection.prepareStatement(sqlSelect)) {
                                            fetchAdductId.setString(1, adduct);
                                            try (ResultSet resultID = fetchAdductId.executeQuery()) {
                                                if (resultID.next()) {
                                                    adduct_id = resultID.getInt("adduct_id");
                                                } else {
                                                    throw new SQLException("Failed to retrieve new adduct_id.");
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // 2. Set buffer_gas_id = 1
                            Integer buffer_gas_id = 1;

                            // 3. Insert triplet compound_id, adduct_id, buffer_gas_id, ccs_value
                            // Check if the combination already exists in the compound_ccs table
                            String sqlSelectCcs = "SELECT COUNT(*) FROM compound_ccs WHERE compound_id = ? AND adduct_id = ? AND buffer_gas_id = ?";
                            try (PreparedStatement checkExistenceStmt = CMMConnection.prepareStatement(sqlSelectCcs)) {
                                checkExistenceStmt.setInt(1, pc_id);
                                checkExistenceStmt.setInt(2, adduct_id);
                                checkExistenceStmt.setInt(3, buffer_gas_id);

                                try (ResultSet resultSet = checkExistenceStmt.executeQuery()) {
                                    if (resultSet.next()) {
                                        int count = resultSet.getInt(1);
                                        if (count == 0 && averageExperimentalCCS!=null) {
                                            // Insert only if the entry does not exist
                                            String sqlInsertCcs = "INSERT INTO compound_ccs (compound_id, adduct_id, buffer_gas_id, ccs_value) VALUES (?, ?, ?, ?)";
                                            try (PreparedStatement insertSql = CMMConnection.prepareStatement(sqlInsertCcs)) {
                                                insertSql.setInt(1, pc_id);
                                                insertSql.setInt(2, adduct_id);
                                                insertSql.setInt(3, buffer_gas_id);
                                                insertSql.setDouble(4, averageExperimentalCCS);

                                                insertSql.executeUpdate();
                                            }
                                        } else {
                                            System.out.println("Entry already exists for compound_id: " + pc_id + ", adduct_id: " + adduct_id + ", buffer_gas_id: " + buffer_gas_id);
                                        }
                                    }
                                }
                            }

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            System.out.println("Process done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processAdduct(String adduct) {
        if (adduct.startsWith("[") && (adduct.endsWith("]+") || adduct.endsWith("]-"))) {
            adduct = adduct.substring(1, adduct.length() - 2); // remove brackets and trailing sign
        }
        return adduct;
    }


    public static void checkCompoundsTable(Connection CMMConnection, int pc_id, String compound, String smiles) throws SQLException {
        String checkCompoundsSql = "SELECT compound_id FROM compounds WHERE compound_id = ?";
        try (PreparedStatement checkStmt = CMMConnection.prepareStatement(checkCompoundsSql)) {
            checkStmt.setInt(1, pc_id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) { // It doesn't exist
                    // Do the CDK search to fetch compound information
                    CompoundInfo compoundInfo = fetchCompoundInfoFromSmiles(smiles, compound);

                    // Insert into compounds table
                    String insertCompoundsSql = "INSERT INTO compounds (compound_id, compound_name, formula, mass, charge_type, charge_number, formula_type, compound_type, compound_status, formula_type_int) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = CMMConnection.prepareStatement(insertCompoundsSql)) {
                        insertStmt.setInt(1, pc_id);
                        insertStmt.setString(2, compoundInfo.compoundName);
                        insertStmt.setString(3, compoundInfo.formula);
                        insertStmt.setDouble(4, compoundInfo.mass);
                        insertStmt.setInt(5, compoundInfo.chargeType);
                        insertStmt.setInt(6, compoundInfo.chargeNumber);
                        insertStmt.setString(7, compoundInfo.formulaType);
                        insertStmt.setInt(8, 1); //  compound_type int default 0, -- type of compound: 0 for metabolite, 1 for lipids, 2 for peptide
                        insertStmt.setInt(9, 0); //compound_status int default 0, -- status of compound: 0 expected, 1 detected, 2 quantified, 3 predicted (HMDB)
                        insertStmt.setInt(10, compoundInfo.formulaTypeInt);

                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    }
    public static CompoundInfo fetchCompoundInfoFromSmiles(String smiles, String compoundName) {
        CompoundInfo compoundInfo = new CompoundInfo();
        try {
            // Parse SMILES
            SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
            IAtomContainer molecule = smilesParser.parseSmiles(smiles);

            // Compute molecular formula
            IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(molecule);
            compoundInfo.formula = MolecularFormulaManipulator.getString(formula);

            // Compute exact mass
            compoundInfo.mass = MolecularFormulaManipulator.getTotalExactMass(formula);

            // Compute logP using XLogPDescriptor
            XLogPDescriptor logPDescriptor = new XLogPDescriptor();
            DescriptorValue logPValue = logPDescriptor.calculate(molecule);
            compoundInfo.logP = ((DoubleResult) logPValue.getValue()).doubleValue();

            // Compute charge type and number
            String chargeTypeString = detectChargeType(molecule);

            // (neutral - 0 ,positive - 1 and negative -2 )
            switch (chargeTypeString) {
                case "neutral":
                    compoundInfo.chargeType = 0;
                    break;
                case "positive":
                    compoundInfo.chargeType = 1;
                    break;
                case "negative":
                    compoundInfo.chargeType = 2;
                    break;
            }

            compoundInfo.chargeNumber = detectChargeNumber(molecule);

            // Additional compound information
            compoundInfo.compoundName = compoundName;
            compoundInfo.formulaType = getTypeFromFormula(formula.toString());

        } catch (CDKException e) {
            System.err.println("CDK Exception for SMILES: " + smiles);
            e.printStackTrace();
        }
        return compoundInfo;
    }

    /**
     *
     * @param formula
     * @return the type of the formula. If formula only has CHNOPS elements
     * return CHNOPS. If they have CHNOPS+CL, then returns CNHOPSCL. If formula
     * has another elements, return ALL. If formula has elements which are not
     * in the periodic table, then returns ""
     */
    public static String getTypeFromFormula(String formula) {
        if (formula == null || formula.equals("")) {
            return "";
        }
        String type;
        Set<String> setElements = new HashSet<String>();

        Pattern p;
        Matcher m;
        // p = Pattern.compile("[A-Z][a-z]?\\d*");
        p = Pattern.compile("[A-Z][a-z]?");
        String elementString;
        // DELETE all . in order to check every element
        String oldFormula = formula;
        String rest = formula.replaceAll("\\.", "");
        m = p.matcher(rest);
        // Look for compounds from periodic table
        while (m.find()) {
            elementString = m.group();

            try {
                Element element = Element.valueOf(elementString);
                boolean isElement = MAPELEMENTS.containsKey(element);
                // System.out.println("element: " + element + " new Formula: " + rest);
                if (!isElement) {
                    // The element is not in periodic table
                    // System.out.println("Not element --> GO OUT");
                    return "";
                } else {
                    // If the element is already in the set, it is not added there.
                    // System.out.println("Adding element: " + element);
                    setElements.add(elementString);
                }
                rest = rest.replaceFirst(elementString, "");
            } catch (IllegalArgumentException iae) {
                return "";
            }
        }

        // If there is some elements starting with lower case letter, the element is not in the periodic table
        p = Pattern.compile("[a-z]");
        m = p.matcher(rest);
        while (m.find()) //if(m.find())
        {
            // element = m.group();
            // System.out.println("Lower case Not element: " + element);
            return "";
        }
        //System.out.println("CHNOPS SET: " + SETCHNOPS);
        //System.out.println("CHNOPSCL SET: " + SETCHNOPSCL);
        //System.out.println("LIST: " + setElements);
        if (SETCHNOPS.containsAll(setElements)) {
            type = "CHNOPS";
        } else if (SETCHNOPSCL.containsAll(setElements)) {
            type = "CHNOPSCL";
        } else if (SETCHNOPSD.containsAll(setElements)) {
            type = "CHNOPSD";
        } else if (SETCHNOPSCLD.containsAll(setElements)) {
            type = "CHNOPSCLD";
        } else if (setElements.contains("D")) {
            type = "ALLD";
        } else {
            type = "ALL";
        }
        return type;
    }
    public static final Map<String, Integer> MAPCHEMALPHABET;

    static {
        Map<String, Integer> mapChemAlphabetTMP = new LinkedHashMap<>();
        mapChemAlphabetTMP.put("CHNOPS", 0);
        mapChemAlphabetTMP.put("CHNOPSD", 1);
        mapChemAlphabetTMP.put("CHNOPSCL", 2);
        mapChemAlphabetTMP.put("CHNOPSCLD", 3);
        mapChemAlphabetTMP.put("ALL", 4);
        mapChemAlphabetTMP.put("ALLD", 5);
        MAPCHEMALPHABET = Collections.unmodifiableMap(mapChemAlphabetTMP);
    }

    private static String detectChargeType(IAtomContainer molecule) {
        int charge = AtomContainerManipulator.getTotalFormalCharge(molecule);
        return charge > 0 ? "positive" : charge < 0 ? "negative" : "neutral";
    }

    private static int detectChargeNumber(IAtomContainer molecule) {
        return AtomContainerManipulator.getTotalFormalCharge(molecule);
    }

}
