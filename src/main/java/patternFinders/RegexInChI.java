/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patternFinders;

/**
 * @author maria
 */

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexInChI {

    /**
     * return the formula generated from InChI structure
     *
     * @param inChI
     * @return the formula contained in the InChI
     */
    public static String getFormulaFromInChI(String inChI) {
        PatternFinder pf = new PatternFinder();
        if (inChI.equals("") || inChI.equals("NULL") || inChI.equals("null")) {
            return null;
        }
        String formula;
        String formulaStart = "[/]";
        String formulaEnd = "[/]";
        formula = pf.searchFirstOcurrence(inChI, formulaStart + "(.*?)" + formulaEnd);
        // Quit the first / and the last /
        if (formula != null) {
            formula = formula.substring(1, formula.length() - 1);
        }

        return formula;
    }

    public static String getMainPart(String inchi) throws IllegalArgumentException {

        List<String> inchisMain = new LinkedList<String>();
        String[] inchiParts = inchi.split("/");
        try {
            String mainPart = inchiParts[0] + "/" + inchiParts[1] + "/" + inchiParts[2];
            return mainPart;
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("INCHI NOT VALID");
        }


    }

}
