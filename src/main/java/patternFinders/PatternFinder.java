package patternFinders;


import chemicalFormula.Element;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chemicalFormula.PeriodicTable.*;

/**
 *
 * @author USUARIO
 * @version: 4.0, 20/07/2016. Modified by Alberto Gil de la Fuente
 */
public class PatternFinder {

    int start = 0;

    /**
     * Function searchWithReplacement
     *
     * @param content String of content
     * @param pattern pattern to search
     * @param stringToRemove stringToremove in content
     * @return a String with content which reaches the pattern
     */
    public static String searchWithReplacement(String content, String pattern, String stringToRemove) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        String response = "";

        while (m.find()) {
            response += m.group().replaceAll(stringToRemove, "") + "\n";
        }

        return response;
    }

    /**
     * Function searchWithoutReplacement
     *
     * @param content String of content
     * @param pattern pattern to search
     * @return a String with content which reaches the pattern
     */
    public static String searchWithoutReplacement(String content, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        String respuesta = "";

        while (m.find()) {
            respuesta += m.group() + "\n";
        }

        return respuesta;
    }



    /**
     * contains the pattern
     *
     * @param content String of content
     * @param pattern pattern to search
     * @return wether the content contains the pattern to search
     */
    public static boolean containsPattern(String content, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        if (m.find()) {
            return true;
        }

        return false;
    }

    /**
     * Function searchFirstOcurrence
     *
     * @param content String of content
     * @param pattern pattern to search
     * @return a String with content which reaches the pattern
     */
    public static String searchFirstOcurrence(String content, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        String respuesta = null;
        if (m.find()) {
            respuesta = m.group();
        }

        return respuesta;
    }

    /**
     * Function searchSecondOcurrence
     *
     * @param content String of content
     * @param pattern pattern to search
     * @return a String with content which reaches the pattern
     */
    public static String searchSecondOcurrence(String content, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        String respuesta = "";
        if (m.find()) {
            if (m.find()) {
                respuesta = m.group();
            }
        }

        return respuesta;
    }

    /**
     * Function searchListWithReplacement. Find the pattern in content and
     * removes the String stringToRemove in the results
     *
     * @param content String of content
     * @param pattern pattern to search
     * @param stringToRemove stringToremove in content
     * @return arraylist with content which reaches the pattern
     */
    public static List<String> searchListWithReplacement(String content, String pattern, String stringToRemove) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        List<String> respuesta = new LinkedList<>();

        while (m.find()) {
            respuesta.add(m.group().replaceAll(stringToRemove, ""));

        }

        return respuesta;
    }

    /**
     * Function searchListWithoutReplacement Find the pattern in content.
     *
     * @param content String of content
     * @param pattern pattern to search
     * @return arraylist with content which reaches the pattern
     */
    public static List<String> searchListWithoutReplacement(String content, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        List<String> respuesta = new LinkedList<>();

        while (m.find()) {
            String ocurrence = m.group();
            respuesta.add(ocurrence);
        }

        return respuesta;
    }

    /**
     * Function searchListWithoutReplacement Find the pattern in content.
     *
     * @param content String of content
     * @param pattern pattern to search
     * @return arraylist with content which reaches the pattern
     */
    public static Set<String> searchSetWithoutReplacement(String content, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);

        Matcher m = p.matcher(content);

        Set<String> compiledSet = new TreeSet<String>();

        while (m.find()) {
            compiledSet.add(m.group());
        }

        return compiledSet;
    }

    /**
     * Function searchInDifferentCalls
     *
     * @param content StringBuilder of content
     * @param pattern pattern to search
     * @param stringToRemove stringToremove in content
     * @param result StringBuilder to save the result
     * @return arraylist with content which reaches the pattern
     */
    // What does this function do exactly?
    // Why delete at the beginning all the results?
    // I think it is only to reuse the stringBuilder
    public boolean searchInDifferentCalls(StringBuilder content, String pattern, String stringToRemove, StringBuilder result) {
        result.delete(0, result.length());
        Pattern patternContextoDato = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher matcherContextoDato = patternContextoDato.matcher(content);

        boolean found = matcherContextoDato.find(start);
        if (found) {
            start = matcherContextoDato.end();

            String localResult = matcherContextoDato.group().replaceAll(stringToRemove, "");

            localResult = localResult.replaceAll("\"", "''");
            localResult = localResult.replaceAll("\n", "");
            result.append(localResult);
        }
        return found;
    }

    /**
     * Method that returns the first double in content. If there is no double in
     * content, the method returns 0
     *
     * @param content
     * @return
     */
    public static double searchDouble(String content) {
        double result = 0.0d;
        Pattern p = Pattern.compile("(\\d+(?:\\.\\d+))");
        Matcher m = p.matcher(content);
        if (m.find()) {
            result = Double.parseDouble(m.group(1));
//            System.out.println(result);
        }

        return result;
    }

    /**
     * Method that returns pc_id from a pubchem link or null otherwise
     *
     * @param pubchemLink of the type:
     * http://pubchem.ncbi.nlm.nih.gov/compound/150816 or
     * https://pubchem.ncbi.nlm.nih.gov/compound/100005
     * https://pubchem.ncbi.nlm.nih.gov/compound/16067346#section=Top
     * @return
     */
    public static Integer searchPCID(String pubchemLink) {
        Integer result = null;
        String pattern1 = "http://pubchem.ncbi.nlm.nih.gov/compound/";
        String pattern2 = "https://pubchem.ncbi.nlm.nih.gov/compound/";
        String suffix = "#(.)*";
        pubchemLink = pubchemLink.replaceAll(suffix, "");
        if (pubchemLink.contains(pattern1)) {
            String pc_id_string = pubchemLink.replaceAll(pattern1, "");
            try {
                result = Integer.parseInt(pc_id_string);
            } catch (NumberFormatException nfe) {
                System.out.println("Trying to obtain pubchemlink of: " + pubchemLink + " and failed");
            }
        } else if (pubchemLink.contains(pattern2)) {
            String pc_id_string = pubchemLink.replaceAll(pattern2, "");
            try {
                result = Integer.parseInt(pc_id_string);
            } catch (NumberFormatException nfe) {
                System.out.println("Trying to obtain pubchemlink of: " + pubchemLink);
            }
        }

        return result;
    }

    public static String getGenusSpecieFromGenusSpecieAndSubspecies(String species) {

        String[] wordSpecies = species.split("\\s");

        if (wordSpecies.length < 3) {
            return species;
        } else {
            String finalSpecie = wordSpecies[0] + " " + wordSpecies[1];
            return finalSpecie;
        }
    }

    public static String getSubspecieFromGenusSpecieAndSubspecie(String species) {

        String[] wordSpecies = species.split("\\s");

        if (wordSpecies.length < 3) {
            return null;
        } else {
            String finalCells = "";
            finalCells = wordSpecies[2];
            for (int indexSpecie = 3; indexSpecie < wordSpecies.length; indexSpecie++) {
                finalCells = finalCells + " " + wordSpecies[indexSpecie];
            }
            return finalCells.substring(0, finalCells.length());
        }
    }

    public static String getGenusFromGenusAndSpecie(String genusAndSpecie) throws Exception {

        String[] wordSpecies = genusAndSpecie.split("\\s");

        if (wordSpecies.length > 2) {
            throw new Exception("GenusAndSpecie cannot have more than 1 space");
        } else if (wordSpecies.length == 2) {
            return wordSpecies[0];
        } else {
            return genusAndSpecie;
        }
    }

    public static String getSpecieFromGenusAndSpecie(String genusAndSpecie) throws Exception {

        String[] wordSpecies = genusAndSpecie.split("\\s");

        if (wordSpecies.length > 2) {
            throw new Exception("GenusAndSpecie cannot have more than 1 space");
        } else if (wordSpecies.length == 2) {
            return wordSpecies[1];
        } else {
            return "";
        }
    }

    /**
     * Read javadoc for getLipidType
     *
     * @param common_name
     * @return
     */
    public static String getLipidTypeFromName(String common_name) {
        String lipidType = "";
        if (!common_name.equalsIgnoreCase("")) {
            lipidType = getLipidType(common_name);
        }
        return lipidType;
    }

    /**
     * Read javadoc for getNumberOfCarbons
     *
     * @param common_name
     * @return
     */
    public static int getNumberOfCarbonsFromName(String common_name) {
        int numCarbons = 0;
        String lipidType = getLipidTypeFromName(common_name);
        if (!lipidType.equalsIgnoreCase("")) {
            numCarbons = getNumberOfCarbons(common_name);
        }
        return numCarbons;
    }

    /**
     * Read javadoc for getNumberOfDoubleBonds
     *
     * @param common_name
     * @return
     */
    public static int getNumberOfDoubleBondsFromName(String common_name) {
        int numDoubleBonds = 0;
        String lipidType = getLipidTypeFromName(common_name);
        if (!lipidType.equalsIgnoreCase("")) {
            numDoubleBonds = getNumberOfDoubleBonds(common_name);
        }
        return numDoubleBonds;
    }

    /**
     *
     * @param content
     * @return
     */
    public static String getPrimaryHMDBIDFromHTML(String content) {
        String hmdbId = "";
        String patternHMDBID = "HMDB[\\d]{7}";
        Pattern p = Pattern.compile(patternHMDBID);
        Matcher m = p.matcher(content);
        if (m.find()) {
            hmdbId = m.group();
        } else {
            System.out.println("HMDB PRIMARY ID NOT FOUND");
            return "NOT FOUND";
        }
        return hmdbId;
    }

    /**
     * Method for get the Lipid Type from Lipid Nomenclature as
     * PE(22:4(7Z,10Z,13Z,16Z)/22:6(4Z,7Z,10Z,13Z,16Z,19Z))
     * <LIPID_TYPE>([<CONNECTION_TYPE>-]<CARBONS>:<DOUBLE_BONDS>[(<DB_POSITIONs>)]/<CARBONS>:<DOUBLE_BONDS>[(<DB_POSITIONs>)])
     *
     * Where: Expressions within [] are optional
     * <LIPID_TYPE> is defined with two capital letters
     * <CONNECTION_TYPE> is defined with a LETTER and "-". It may appear or not.
     * <CARBONS> and <DOUBLE_BONDS> are integers and they are separated with ":"
     * <DB_POSITION> is between "(" and ")" and may appear or not. After
     * <DOUBLE_BONDS> or <DB_POSITIONS> (if it appears), a new expression with
     * same pattern may appear 0 or more times.
     *
     * @param content
     * @return
     */
    public static String getLipidType(String content) {
        String lipidType = "";
        // [a-zA-Z]{2}([a-zA-Z])*(-([a-zA-Z])+)?\(([a-zA-Z](-)?)?[0-9]+:
        // First of All, look if the name follows the pattern.
        String patternLipidType = "[a-zA-Z]{2}([a-zA-Z0-9])*(-([a-zA-Z0-9])+)?\\(([a-zA-Z](-)?)?[0-9]+:";
        patternLipidType = patternLipidType + "|[a-zA-Z]{2}([a-zA-Z0-9])*(-([a-zA-Z0-9])+)?\\(C[0-9]+";
        Pattern p = Pattern.compile(patternLipidType);
        Matcher m = p.matcher(content);
        if (m.find()) {
            String lipidTypePartial = m.group();
            // If the name follows the pattern, extract the lipid type name.
            String patternLipidTypeName = "[a-zA-Z]{2}([a-zA-Z0-9])*(-([a-zA-Z0-9])+)?\\(([a-zA-Z](-)?)?";
            Pattern p2 = Pattern.compile(patternLipidTypeName);
            Matcher m2 = p2.matcher(lipidTypePartial);
            if (m2.find()) {
                lipidType = m2.group();
            }
            //System.out.println("LIPID TYPE: " + lipidType);
        }
        return lipidType;
    }

    /**
     * Method for get the number of Carbons from Lipid Nomenclature as
     * PE(22:4(7Z,10Z,13Z,16Z)/22:6(4Z,7Z,10Z,13Z,16Z,19Z))
     * <LIPID_TYPE>([<CONNECTION_TYPE>-]<CARBONS>:<DOUBLE_BONDS>[(<DB_POSITIONs>)]/<CARBONS>:<DOUBLE_BONDS>[(<DB_POSITIONs>)])
     *
     * Where: Expressions within [] are optional
     * <LIPID_TYPE> is defined with two capital letters
     * <CONNECTION_TYPE> is defined with a LETTER and "-". It may appear or not.
     * <CARBONS> and <DOUBLE_BONDS> are integers and they are separated with ":"
     * <DB_POSITION> is between "(" and ")" and may appear or not. After
     * <DOUBLE_BONDS> or <DB_POSITIONS> (if it appears), a new expression with
     * same pattern may appear 0 or more times.
     *
     * @param content
     * @return
     */
    public static int getNumberOfCarbons(String content) {
        int numCarbons = 0;
        // System.out.println("PROCESSING NUMBER OF CARBONS OF: " + content);
        Pattern p = Pattern.compile("[1-9][0-9]*:");
        Matcher m = p.matcher(content);
        int i = 1;
        while (m.find()) {
            String carbons = m.group();
            carbons = carbons.substring(0, carbons.length() - 1);
            //System.out.println("PARTIAL NUMBER CARBONS: " + carbons);
            numCarbons += Integer.parseInt(carbons);
        }
        //System.out.println("TOTAL NUMBER CARBONS: " + numCarbons);
        return numCarbons;
    }

    /**
     * Method for get the number of Double Bonds from Lipid Nomenclature as
     * PE(22:4(7Z,10Z,13Z,16Z)/22:6(4Z,7Z,10Z,13Z,16Z,19Z))
     * <LIPID_TYPE>([<CONNECTION_TYPE>-]<CARBONS>:<DOUBLE_BONDS>[(<DB_POSITIONs>)]/<CARBONS>:<DOUBLE_BONDS>[(<DB_POSITIONs>)])
     *
     * Where: Expressions within [] are optional
     * <LIPID_TYPE> is defined with two capital letters
     * <CONNECTION_TYPE> is defined with a LETTER and "-". It may appear or not.
     * <CARBONS> and <DOUBLE_BONDS> are integers and they are separated with ":"
     * <DB_POSITION> is between "(" and ")" and may appear or not. After
     * <DOUBLE_BONDS> or <DB_POSITIONS> (if it appears), a new expression with
     * same pattern may appear 0 or more times.
     *
     * @param content
     * @return
     */
    public static int getNumberOfDoubleBonds(String content) {
        int numDoubleBonds = 0;
        // System.out.println("PROCESSING NUMBER OF DOUBLE BONDS OF: " + content);
        Pattern p = Pattern.compile(":[0-9]*");
        Matcher m = p.matcher(content);
        int i = 1;
        while (m.find()) {
            String doubleBonds = m.group();
            doubleBonds = doubleBonds.substring(1, doubleBonds.length());
            //System.out.println("PARTIAL NUMBER DOUBLE BONDS: " + doubleBonds);
            numDoubleBonds += Integer.parseInt(doubleBonds);
        }
        //System.out.println("TOTAL NUMBER DOUBLEBONDS: " + numDoubleBonds);
        return numDoubleBonds;
    }

    /**
     * Method for get the oxidation type from Lipid Abbreviature as
     * PE(22:4/22:6(OH))
     * <LIPID_TYPE>([<CONNECTION_TYPE>-]<CARBONS>:<DOUBLE_BONDS>[(OXIDATION)]/<CARBONS>:<DOUBLE_BONDS>[(OXIDATION)])
     *
     * Where: Expressions within [] are optional
     * <LIPID_TYPE> is defined with two capital letters
     * <CONNECTION_TYPE> is defined with a LETTER and "-". It may appear or not.
     * <CARBONS> and <DOUBLE_BONDS> are integers and they are separated with ":"
     * <OXIDATION> is between "(" and ")" and may appear or not. After
     * <DOUBLE_BONDS> or <OXIDATION> (if it appears), a new expression with same
     * pattern may appear 0 or more times.
     *
     * @param content
     * @return
     */
    public static String getOxidationFromAbbrev(String content) {
        Pattern p = Pattern.compile("\\([a-zA-Z]+\\)");
        Matcher m = p.matcher(content);
        int i = 1;
        String oxidationType = "";
        if (m.find()) {
            oxidationType = m.group();
            oxidationType = oxidationType.substring(1, oxidationType.length() - 1);
        }
        if ("Ke".equals(oxidationType)) {
            //oxidationType="O";
        }
        if ("COH".equals(oxidationType)) {
            oxidationType = "CHO";
        }
        //System.out.println("TOTAL NUMBER DOUBLEBONDS: " + numDoubleBonds);
        return oxidationType;
    }

    /**
     * Get the list of the chains from the abbreviated form of the lipids:
     * PE(P-16:0(OH)/20:4) -> 16:0(OH) and 20:4
     *
     * @param content
     * @return
     */
    public static List<String> getListOfChains(String content) {
        List<String> chains = new LinkedList<>();
        //System.out.println("PROCESSING NUMBER OF DOUBLE BONDS OF: " + content);
        String lipidType = getLipidTypeFromName(content);
        if (!lipidType.equalsIgnoreCase("")) {
            Pattern p = Pattern.compile("[1-9][0-9]*:[0-9](\\([a-zA-Z]+\\))*");
            Matcher m = p.matcher(content);
            while (m.find()) {
                String chain = m.group();
                chains.add(chain);
            }
        }
        //System.out.println("TOTAL NUMBER DOUBLEBONDS: " + numDoubleBonds);
        return chains;
    }

    public static String getCodeLMJSON(String sentence) {
        String code = "";
        //System.out.println("PROCESSING NUMBER OF DOUBLE BONDS OF: " + content);
        Pattern p = Pattern.compile("\\[[a-zA-Z0-9]*\\]");
        Matcher m = p.matcher(sentence);
        while (m.find()) {
            code = m.group();
            code = code.substring(1, code.length() - 1);
        }
        //System.out.println("TOTAL NUMBER DOUBLEBONDS: " + numDoubleBonds);
        return code;
    }

    public static boolean checkFormulaElements(String formula) {
        boolean found = true;

        Pattern p;
        Matcher m;
        // p = Pattern.compile("[A-Z][a-z]?\\d*");
        p = Pattern.compile("[A-Z][a-z]?");
        String elementString;
        // DELETE all . in order to check every element
        String oldFormula = formula;
        String rest = formula.replaceAll("\\.", "");
        m = p.matcher(rest);
        while (m.find()) {
            elementString = m.group();
            Element element = Element.valueOf(elementString);
            boolean isElement = MAPELEMENTS.containsKey(element);
            //System.out.println("element: " + element + "" + oldFormula);
            if (!isElement) {
                // System.out.println("element: " + element + "" + oldFormula);
                return false;
            } else {

            }
            // OLD VERSION!
            // To check R and X from compounds from kegg
            // String r = searchFirstOcurrence(element, "[R]\\d*");
            // String x = searchFirstOcurrence(element, "[X]\\d*");
            // if (!r.equals("") || !x.equals("")) {
            //    return false;
            // } else {
            //}
            rest = rest.replaceFirst(elementString, "");
        }
        p = Pattern.compile("[a-z]");
        m = p.matcher(rest);
        while (m.find()) //if(m.find())
        {
            return false;
        }
        return found;
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

    public static boolean debugFormula(String content) {
        boolean found = true;
        /*
        Mg2(Al,Fe)3Si4O10(OH)Na2O
        C19H15NaB(Na)
        (C12H16O15S2R2)n"
        C23H16O6.(C11H14ClN5)2"
        C9H8O4.C2H4NO2.CO3.Al.Mg.2OH
        C28H46N5O29P5R2(R1)(C5H8O6PR)n
        C11H16N5O8P(C5H8O6PR)n(C5H8O6PR)n
         */
        Pattern p;
        Matcher m;
        /*
        String pattern = "[(](.*?)[)]\\d*";
        List<String> splitContent = new ArrayList<>();
        p = Pattern.compile(pattern);
        m = p.matcher(content);
        while (m.find()) {
            String element = m.group();
            element = element.replaceAll("(", "");
            element = element.replaceAll(")", "");
            splitContent.add(element);
        }
         */

        p = Pattern.compile("[A-Z][a-z]?\\d*");
        String element;
        // DELETE all . in order to check every element
        String rest = content.replaceAll("\\.", "");

        System.out.println("\nrest: " + rest);
        m = p.matcher(rest);
        while (m.find()) {
            element = m.group();
            String r = searchFirstOcurrence(element, "[R]\\d*");
            String x = searchFirstOcurrence(element, "[X]\\d*");
            System.out.println("\nR: " + r + "  X: " + x);
            if (!r.equals("") || !x.equals("")) {
                System.out.println("\n FIND R or X: " + element);
                //return false;
            } else {
                System.out.println("element: " + element);
            }
            rest = rest.replaceFirst(element, "");
        }
        p = Pattern.compile("[a-z]");
        m = p.matcher(rest);
        System.out.println("\n REST: " + rest);
        while (m.find()) //if(m.find())
        {
            System.out.println("\n FIND: " + m.group());
            //return false;
        }
        return found;
    }


    /**
     * Return the list of ancestors from the HTML. It follow the pattern of the
     * CHEMONTID:{7} numbers
     *
     * @param htmlString
     * @return
     */
    public static List<String> getNodesAncestorsCLASSYFIRE(String htmlString) {

        List<String> ancestors = new LinkedList<String>();
        Pattern p = Pattern.compile("CHEMONTID:[0-9]{7}");
        Matcher m = p.matcher(htmlString);
        while (m.find()) {
            String nodeId = m.group();
            ancestors.add(nodeId);
        }

        return ancestors;
    }

    /**
     * analyze the SMILES and return an array with two values which contains the
     * charge (array[0]) (neutral - 0 ,positive - 1 and negative -2 ) and the
     * number of charges (array[1]
     *
     * @param Smiles
     * @return
     */
    public static int[] getChargeFromSmiles(String Smiles) {
        int[] arrayCharges = new int[2];
        Pattern p = Pattern.compile("(\\+)([0-9]*)\\]");
        Matcher m = p.matcher(Smiles);
        int positiveCharges = 0;
        int negativeCharges = 0;
        while (m.find()) {
            String positiveCharge = m.group();
            // when length is 2 it means + or - ]]
            if (positiveCharge.length() == 2) {
                positiveCharges = positiveCharges + 1;
            } else {
                positiveCharges = positiveCharges
                        + Integer.parseInt(positiveCharge.substring(1, positiveCharge.length() - 1));
            }
        }

        p = Pattern.compile("(-)([0-9]*)\\]");
        m = p.matcher(Smiles);
        while (m.find()) {
            String negativeCharge = m.group();
            if (negativeCharge.length() == 2) {
                negativeCharges = negativeCharges + 1;
            } else {
                negativeCharges = negativeCharges
                        + Integer.parseInt(negativeCharge.substring(1, negativeCharge.length() - 1));
            }
        }

        int totalCharges = positiveCharges - negativeCharges;
        if (totalCharges == 0) {
            arrayCharges[0] = 0;
        } else if (totalCharges > 0) {
            arrayCharges[0] = 1;
        } else if (totalCharges < 0) {
            arrayCharges[0] = 2;
        }
        arrayCharges[1] = Math.abs(totalCharges);

        return arrayCharges;
    }

}
