/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbmanager;

/**
 *
 * @author Alberto Gil de la Fuente
 */

import cems_project.Identifier;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.ChebiException;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static constants.Constants.CHEBI_COMPOUND_ENDPOINT;
import static constants.Constants.CHEBI_STRUCTURE_SEARCH_ENDPOINT;

/**
 * Class to perform queries against the chebi Database to retrieve information
 * of compounds with the chebi Identifier
 *
 * @author Alberto Gil de la Fuente
 * @version $Revision: 5.1.1.1 $
 * @since Build 4.1.0.0 07-nov-2019
 */
public abstract class ChebiDatabase {

    private static String getOrNull(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }

    /**
     * Get the identifiers from chebi ID using the chebi REST API
     * @param chebiId
     * @return
     * @throws ChebiException
     * @throws IOException
     */
    public static Identifier getIdentifierFromChebiID(int chebiId) throws ChebiException, IOException {
        String chebi_endpoint = CHEBI_COMPOUND_ENDPOINT + chebiId;

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        JsonObject structure = obj.getAsJsonObject("default_structure");

        if (structure == null) {
            throw new ChebiException("No default_structure found for ChEBI ID " + chebiId);
        }

        String smiles = getOrNull(structure, "smiles");
        String inchi = getOrNull(structure, "standard_inchi");
        String inchiKey = getOrNull(structure, "standard_inchi_key");

        return new Identifier(inchi, inchiKey, smiles);

    }

    /**
     * Returns the ASCII name of the chebi ID using the CHEBI REST API
     *
     * @param chebiId
     * @return
     * @throws ChebiException
     * @throws IOException
     */
    public static String getInChIKeyFromChebID(int chebiId) throws ChebiException, IOException {
        Identifier identifier = ChebiDatabase.getIdentifierFromChebiID(chebiId);
        return identifier.getInchi_key();
    }

    public static String getAsciiName(int chebiId) throws ChebiException, IOException {
        String chebi_endpoint = CHEBI_COMPOUND_ENDPOINT + chebiId;

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        // Parse JSON
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        String ascii_name = obj.get("ascii_name").getAsString();

        if (ascii_name == null) {
            throw new ChebiException("No compound found for ChEBI ID " + chebiId);
        }

        return ascii_name;
    }

    /**
     * Get the formula from chebi ID using the chebi REST API
     * @param chebiId
     * @return
     * @throws ChebiException
     * @throws IOException
     */
    public static String getFormulaFromChebID(int chebiId) throws ChebiException, IOException {
        String chebi_endpoint = CHEBI_COMPOUND_ENDPOINT + chebiId;

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        // Parse JSON
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        JsonObject chemicalProperties = obj.getAsJsonObject("chemical_data");

        if (chemicalProperties == null) {
            throw new ChebiException("No compound found for ChEBI ID " + chebiId);
        }

        String formula = getOrNull(chemicalProperties, "formula");
        return formula;
    }

    /**
     * Get the monoisotopic mass from chebi ID using the chebi REST API
     * @param chebiId
     * @return
     * @throws ChebiException
     * @throws IOException
     */
    public static Double getMonoIsotopicMassFromChebID(int chebiId) throws ChebiException, IOException {
        String chebi_endpoint = CHEBI_COMPOUND_ENDPOINT + chebiId;

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        // Parse JSON
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        JsonObject chemicalProperties = obj.getAsJsonObject("chemical_data");

        if (chemicalProperties == null) {
            throw new ChebiException("No compound found for ChEBI ID " + chebiId);
        }

        String monoisotopicMassString = getOrNull(chemicalProperties, "monoisotopic_mass");
        Double monoisotopicMass = monoisotopicMassString != null ? Double.parseDouble(monoisotopicMassString) : null;
        return monoisotopicMass;
    }

    /**
     * Get the HMDB link from chebi ID using the chebi REST API
     * @param chebiId
     * @return
     * @throws ChebiException
     * @throws IOException
     */
    public static String getHMDBLink(int chebiId) throws ChebiException, IOException{
        String chebi_endpoint = CHEBI_COMPOUND_ENDPOINT + chebiId;

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        // Parse JSON
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        JsonObject databaseAccessions = obj.getAsJsonObject("database_accessions");

        if (databaseAccessions == null) {
            throw new ChebiException("No compound found for ChEBI ID " + chebiId);
        }
        JsonArray manualRefs = databaseAccessions.getAsJsonArray("MANUAL_X_REF");
        if (manualRefs == null) {
            throw new ChebiException("No MANUAL_X_REF section found for ChEBI ID " + chebiId);
        }
        for (JsonElement elem : manualRefs) {
            JsonObject ref = elem.getAsJsonObject();
            String sourceName = ref.get("source_name").getAsString();

            if ("HMDB".equalsIgnoreCase(sourceName)) {
                return ref.get("accession_number").getAsString();
            }
        }
        throw new ChebiException("No HMDB found found for ChEBI ID " + chebiId);
    }


    /**
     * Calls the ChEBI REST API similarity search and returns the first matching chebi_accession.
     *
     * @param smiles     SMILES string of the molecule
     * @param similarity similarity threshold (e.g., 0.95)
     * @return ChEBI ID (e.g. "CHEBI:19543") if found, otherwise null
     * @throws IOException          if network or parsing fails
     * @throws ChebiException if no compound is found
     */
    public static int getChebiFromSmiles(String smiles, double similarity)
            throws ChebiException, IOException {
        // --- Fixed parameters ---
        boolean threeStarOnly = true;
        int page = 1;
        int size = 15;
        boolean download = false;

        String baseUrl = CHEBI_STRUCTURE_SEARCH_ENDPOINT;
        String encodedSmiles = URLEncoder.encode(smiles, StandardCharsets.UTF_8);
        String chebi_endpoint = String.format(Locale.US,
                "%s?smiles=%s&search_type=similarity&similarity=%.2f&three_star_only=%b&page=%d&size=%d&download=%b",
                baseUrl, encodedSmiles, similarity, threeStarOnly, page, size, download);

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray results = root.getAsJsonArray("results");

        if (results == null || results.size() == 0) {
            throw new ChebiException("Identifier not found in ChEBI for SMILES: " + smiles);
        }

        // Get the first result from the similarity search
        JsonObject firstResult = results.get(0).getAsJsonObject();
        JsonObject source = firstResult.getAsJsonObject("_source");
        if (source == null || !source.has("chebi_accession")) {
            throw new ChebiException("Identifier not found in ChEBI for SMILES: " + smiles);
        }

        return ChebiDatabase.getChebiNumber(source.get("chebi_accession").getAsString());

    }

    public static Integer getChebiNumber(String chebiId) {
        return Integer.parseInt(chebiId.replaceAll("CHEBI:", ""));
    }
}
