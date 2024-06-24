package dbmanager;

import cems_project.Compound;
import cems_project.Identifier;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import patternFinders.PatternFinder;
import utilities.FileIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static constants.Constants.*;
import static patternFinders.RegexInChI.getFormulaFromInChI;
import static utilities.FileIO.readStringFromFile;
import static utilities.FileIO.readStringFromURL;

public class CheckerCas {


    static PatternFinder pf = new PatternFinder();
    static Random randomGenerator = new Random();

    public void deleteFilesCasIds() {

        File dir = new File(CAS_RESOURCES_PATH);
        String[] ficheros = dir.list();

        for (String cas : ficheros) {
            deleteFileCasId(cas);
        }
    }

    public void deleteFileCasId(String casId) {
        File file;
        file = new File(CAS_RESOURCES_PATH + casId + ".html");
        file.delete();
    }


    /**
     * @param casId
     * @return the InChI of the compound according to Fiehn Chemical Translator Service. If poosible, the inchi corresponding with the non salt form
     */
    public static String getInChICasIdFromChemicalTranslatorService(String casId) {
        String uriString = FIEHN_TRANSLATOR_SERVICE + casId.trim();

        try {
            Request request = Request.get(uriString);
            request.addHeader("Connection", "keep-alive");

            Response response = request.execute();
            Content jsonResponse = response.returnContent();
            String jsonResponseString = jsonResponse.asString();

            JsonArray jsonresponse = JsonParser.parseString(jsonResponseString).getAsJsonArray();

            JsonArray inchisJson = jsonresponse.get(0).getAsJsonObject().getAsJsonArray("results");
            List<String> inchis = new ArrayList<>();
            for (int i = 0; i < inchisJson.size(); i++) {
                String inchiAux = inchisJson.get(i).getAsString();
                inchis.add(inchiAux);
            }
            return getInChINoSaltFromInChIs(inchis);


        } catch (IOException ioe) {
            System.out.println("FILE NOT FOUND " + ioe);
        }
        return null;
    }

    private static String getInChINoSaltFromInChIs(List<String> inchis) {
        // It returns the inchi with no salt if poosible
        for (String inchi : inchis) {
            String formula = getFormulaFromInChI(inchi);
            if (!formula.contains(".")) {
                return inchi;
            }
        }
        // If all inchis are salts, then return one randomly

        return inchis.size() > 0 ? inchis.get(0) : null;
    }


    /**
     * @param casId
     * @return the InChI of the compound. If there is no InChI returns ""
     */
    public static String getInChICasId(String casId) {
        String fileName = CAS_RESOURCES_PATH + casId + ".html";

        File casIdFile = new File(fileName);
        if (casIdFile.exists()) {
        } else {
            downloadCASIDFile(casId);
        }

        try {
            String content = Files.readString(Path.of(fileName));

            String inChIId = "InChI=";
            String inChIIdEnd = "</p>";
            // Look the content where the InChI is written
            String inChI = PatternFinder.searchFirstOcurrence(content, inChIId + "(.*?)" + inChIIdEnd);
            inChI = inChI.replaceAll(inChIIdEnd, "");
            inChI = inChI.trim();
            // Delete the special characters
            inChI = inChI.replaceAll("\\s+", "");
            //System.out.println("\n cas id: " + casId + "    InchI block: \n" + inChIBlock);
            //System.out.println("\n Inchi: " + inChI);
            // String formula = PatternFinder.searchWithoutReplacement(content, preFormula + "(.*?)");
            return inChI;
        } catch (IOException ioe) {
            System.out.println("FILE NOT FOUND " + ioe);
        }
        return null;
    }

    /**
     * @param casId
     * @return the InChIKey of the compound. If there is no InChIKey returns ""
     */
    public static String getInChIKeyCasId(String casId) {
        String fileName = CAS_RESOURCES_PATH + casId + ".html";

        File casIdFile = new File(fileName);
        if (casIdFile.exists()) {
        } else {
            downloadCASIDFile(casId);
        }

        try {
            String content = Files.readString(Path.of(fileName));

            String inChIKeyId = "InChIKey=";
            String inChIKeyIdEnd = "</p>";
            // Look the content where the InChIKey is written
            String inChIKey = PatternFinder.searchFirstOcurrence(content, inChIKeyId + "(.*?)" + inChIKeyIdEnd);
            inChIKey = inChIKey.replaceAll(inChIKeyId, "");
            inChIKey = inChIKey.replaceAll(inChIKeyIdEnd, "");
            inChIKey = inChIKey.trim();
            // Delete the special characters
            inChIKey = inChIKey.replaceAll("\\s+", "");
            //System.out.println("\n cas id: " + casId + "    InchIKey block: \n" + inChIBlock);
            //System.out.println("\n InchiKey: " + inChIKey);
            // String formula = PatternFinder.searchWithoutReplacement(content, preFormula + "(.*?)");
            return inChIKey;
        } catch (IOException ioe) {
            System.out.println("FILE NOT FOUND " + ioe);
        }
        return null;
    }

    /**
     * Still to do
     *
     * @param casId
     * @return
     */
    public static String getSmilesCasId(String casId) {

        //TODO!
        String fileName = CAS_RESOURCES_PATH + casId + ".html";

        File casIdFile = new File(fileName);
        if (casIdFile.exists()) {
            //casIdFile.delete();
        } else {
            downloadCASIDFile(casId);
        }

        try {
            String content = Files.readString(Path.of(fileName));
            //TODO
            String structDesc = "id=\"structureDescs\"";
            String inChIKeyId = "<h3>InChIKey</h3>";
            String inChIKeyIdEnd = "<br>";
            // Look the content where the InChIKey is written
            String inChIBlock = PatternFinder.searchWithoutReplacement(content, structDesc + "(.*?)" + inChIKeyId + "(.*?)"
                    + "(" + inChIKeyIdEnd + ")");
            String inChIKey = PatternFinder.searchWithReplacement(inChIBlock, inChIKeyId + "(.*?)" + inChIKeyIdEnd,
                    inChIKeyId + "|" + inChIKeyIdEnd);
            inChIKey = inChIKey.trim();
            System.out.println("\n cas id: " + casId + "    InchI block: \n" + inChIBlock);
            System.out.println("\n InchiKey: " + inChIKey);
            // String formula = PatternFinder.searchWithoutReplacement(content, preFormula + "(.*?)");
            return inChIKey;
        } catch (IOException ioe) {
            System.out.println("FILE NOT FOUND " + ioe);
        }
        return null;
    }

    private static void downloadCASIDFile(String casId) {
        String fileName = CAS_RESOURCES_PATH + casId + ".html";
        File casIdFile = new File(fileName);
        if (casIdFile.exists()) {
            casIdFile.delete();
        } else {
            int tries = 0;
            while (tries < 5) {
                try {
                    String content = readStringFromURL(CAS_ONLINE_PATH + casId);
                    FileIO.write(content.toString(), fileName);
                    return;
                } catch (IOException ioe) {
                    try {
                        int randomInt;
                        randomInt = randomGenerator.nextInt((10000 - 1000) + 1) + 1000;
                        Thread.sleep(randomInt);                 //1000 milliseconds is one second.

                    } catch (InterruptedException ex) {
                        System.out.println("Thread interrupted");
                        Thread.currentThread().interrupt();
                    }
                    tries++;
                }
            }
        }
    }

}
