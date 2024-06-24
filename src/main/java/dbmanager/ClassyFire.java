package dbmanager;

import cems_project.ClassyfireClassification;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import exceptions.CompoundNotClassifiedException;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import utilities.FileIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static constants.Constants.CLASSYFIRE_ONLINE_NODES_PATH;

public class ClassyFire {


    /**
     * @param inchi
     * @param inchiKey
     * @return the classyfire inchi key from the inchi introduced. If the Inchi
     * key is already classified it returns the inchi key
     * @throws exceptions.CompoundNotClassifiedException
     * @throws java.io.IOException
     */

    public static String getInChIKeyClassyFireFromInChI(String inchi, String inchiKey) throws CompoundNotClassifiedException, IOException {
        String uriString = Constants.CLASSYFIRE_ONLINE_RESOURCES_PATH + inchiKey + ".json";
        String content = "";
        try {
            content = FileIO.readStringFromURL(uriString);
            if (!content.equals("")) {
                FileIO.write(content, Constants.CLASSYFIRE_RESOURCES_PATH + inchiKey + ".json");
                return inchiKey;
            }
        } catch (FileNotFoundException ex) {
        }
        String labelQuery = inchiKey + "_label";
        Request request = Request.post(CLASSYFIRE_ONLINE_NODES_PATH + "queries.json");
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Connection", "keep-alive");
        // TODO JSON
        String json = "{\"label\": \"" + labelQuery + "\", \"query_type\":\"STRUCTURE\", \"query_input\":\"" + inchi + "\"}";
        Request request2 = request.bodyString(json, ContentType.APPLICATION_JSON);
        Response response = request2.execute();

        Content jsonResponse = response.returnContent();
        String jsonResponseString = jsonResponse.asString();
        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();

        Integer query_id = jsonrepsonse.get("id").getAsInt();
        String inchiKeyFromQueryId = getInChIKeyFromQueryId(query_id);
        return inchiKeyFromQueryId;
    }


    /**
     * Get the InchiKey of the entity
     *
     * @param query_id
     * @return the InChIKey
     * @throws CompoundNotClassifiedException if the query did not contain any
     *                                        valid inchi key
     */
    public static String getInChIKeyFromQueryId(Integer query_id) throws CompoundNotClassifiedException {
        String uriString = CLASSYFIRE_ONLINE_NODES_PATH + "queries/" + query_id + ".json";

        try {
            Request request = Request.get(uriString);
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");
            Response response = request.execute();
            Content jsonResponse = response.returnContent();
            String jsonResponseString = jsonResponse.asString();
            JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
            JsonArray entities_array = jsonrepsonse.get("entities").getAsJsonArray();
            JsonObject properties = entities_array.get(0).getAsJsonObject();
            String inchiKey = properties.get("inchikey").getAsString();
            inchiKey = inchiKey.replaceFirst("InChIKey=", "");
            return inchiKey;
        } catch (IOException | IndexOutOfBoundsException | NullPointerException npe) {
            throw new CompoundNotClassifiedException("query_id Does not contain any structure classified: ", CompoundNotClassifiedException.ERROR_TYPE.NOT_FOUND);
        }
    }


    /**
     * Download the classification file for the inchi key inchikey from
     * Classyfire.
     *
     * @param inchiKey
     */
    public static void downloadCLASSYFIREJSONFile(String inchiKey) {
        File classyifire_file = new File(Constants.CLASSYFIRE_RESOURCES_PATH + inchiKey + ".json");
        String uriString = Constants.CLASSYFIRE_ONLINE_RESOURCES_PATH + inchiKey + ".json";

        try {

            String content = FileIO.readStringFromURL(uriString);
            if (!content.equals("")) {
                FileIO.write(content, Constants.CLASSYFIRE_RESOURCES_PATH + inchiKey + ".json");
            } else {
                System.out.println("CREATE QUERY AND DOWNLOAD FROM QUERY: " + inchiKey);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Error reading: " + uriString + " . -> It does not exist");
        } catch (IOException ex) {
            System.out.println("NO CLASSIFIED INCHI: " + inchiKey);
            Logger.getLogger(ClassyFire.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ClassyfireClassification getClassificationFromClassyFire(String inchi, String inchiKey) throws CompoundNotClassifiedException, IOException {
        String uriString = Constants.CLASSYFIRE_ONLINE_RESOURCES_PATH + inchiKey + ".json";

        try {

            String content = FileIO.readStringFromURL(uriString);
            if (content.equals("")) {
                inchiKey = getInChIKeyClassyFireFromInChI(inchi, inchiKey);
                uriString = Constants.CLASSYFIRE_ONLINE_RESOURCES_PATH + inchiKey + ".json";
                content = FileIO.readStringFromURL(uriString);
            }
            if (content.equals("")) {
                System.out.println("error clasificando el compuesto " + inchi);
                throw new CompoundNotClassifiedException(inchi + " not classified", CompoundNotClassifiedException.ERROR_TYPE.NOT_FOUND);
            }
            else{
                JsonObject jsonResponse = JsonParser.parseString(content).getAsJsonObject();

                String kingdom = jsonResponse.get("kingdom").getAsJsonObject().get("name").getAsString();
                String superClass = jsonResponse.get("superclass").getAsJsonObject().get("name").getAsString();
                String ownClass = "";
                try {
                    ownClass = jsonResponse.get("class").getAsJsonObject().get("name").getAsString();
                } catch(Exception e)
                {

                }
                String subClass = "";
                try {
                    subClass = jsonResponse.get("subclass").getAsJsonObject().get("name").getAsString();
                } catch(Exception e)
                {

                }
                String directParent = "";
                try {
                    directParent = jsonResponse.get("direct_parent").getAsJsonObject().get("name").getAsString();
                } catch(Exception e)
                {

                }

                Set<String> alternativeParentsSet = new TreeSet<>();
                try {
                    JsonArray alternativeParentsJsonArray= jsonResponse.get("alternative_parents").getAsJsonArray();
                    for(int i = 0; i<alternativeParentsJsonArray.size();i++){
                        alternativeParentsSet.add(alternativeParentsJsonArray.get(i).getAsJsonObject().get("name").getAsString());
                    }
                } catch(Exception e)
                {

                }

                ClassyfireClassification classification = new ClassyfireClassification(kingdom,superClass,ownClass,subClass,directParent,alternativeParentsSet);
                return classification;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error reading: " + uriString + " . -> It does not exist");
            return null;
        } catch (IOException ex) {
            System.out.println("NO CLASSIFIED INCHI: " + inchiKey);
            Logger.getLogger(ClassyFire.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
