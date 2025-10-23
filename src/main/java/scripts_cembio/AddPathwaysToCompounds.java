package scripts_cembio;

import cems_project.Compound;
import cems_project.Identifier;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import dbmanager.ChebiDatabase;
import dbmanager.DBManager;
import dbmanager.PubchemRest;
import exceptions.ChebiException;
import exceptions.CompoundNotFoundException;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.Arrays;

public class AddPathwaysToCompounds {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/compounds";
    private static final String USER = "alberto";
    private static final String PASS = "alberto";

    public static void main(String[] args) {
        String inputCsv = "/home/ceu/Escritorio/cmm_temporal/compound_chebis.csv";
        String outputCsv = "/home/ceu/Escritorio/cmm_temporal/compound_chebis_with_pathways.csv";
        DBManager db = new DBManager();
        db.connectToDB(DB_URL, USER, PASS);

        try {

            CSVReader reader = new CSVReaderBuilder(new FileReader(inputCsv))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .withSkipLines(1).build();

            CSVWriter writer = new CSVWriter(
                    new FileWriter(outputCsv),
                    ';',
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );

            // Write new header
            String[] header = {"Group", "HMDB", "PubChem", "KEGG", "ChEBI"};
            writer.writeNext(header);

            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println(Arrays.toString(line));

                String compoundName = line[0].equals("-") ? null : line[0];
                String hmdbId = line[1].equals("-") ? null : line[1];
                Integer pubchemId = null;
                try {
                    pubchemId = Integer.parseInt(line[2].trim());
                } catch (NumberFormatException e) {
                    pubchemId = null;
                }
                String keggId = line[3].equals("-") ? null : line[3];

                Integer compoundId = 0;
                Integer chebiId = null;
                try {
                    // 🔍 1️⃣ Try HMDB → PubChem → KEGG
                    Compound compound = null;
                    try {
                        compoundId = db.getCompoundIdByPubChem(pubchemId);
                        compound = db.getCompoundByCompoundId(compoundId);
                    } catch (CompoundNotFoundException compoundPCNotFoundException) {
                        try {
                            compoundId = db.getCompoundIdByHmdb(hmdbId);
                            compound = db.getCompoundByCompoundId(compoundId);
                        } catch (CompoundNotFoundException hmdbNotFoundException) {
                            try {
                                compoundId = db.getCompoundIdByKegg(keggId);
                                compound = db.getCompoundByCompoundId(compoundId);
                            } catch (CompoundNotFoundException keggNotFoundException) {

                                if (compoundId == 0) {
                                    try {
                                        compound = PubchemRest.getCompoundFromPCID(pubchemId);
                                    } catch (Exception exception) {
                                        compound = PubchemRest.getCompoundFromName(compoundName);
                                    }
                                    if (compound == null) {
                                        compoundId = db.insertCompound(compound);
                                        db.insertCompoundIdentifiers(compoundId, compound.getINCHI());
                                        if (hmdbId != null && !hmdbId.isEmpty() && !hmdbId.equals("-")) {
                                            db.insertHMDB(compoundId, hmdbId);
                                        }
                                        if (pubchemId != null) {
                                            db.insertPC(compoundId, pubchemId);
                                        }
                                        if (keggId != null && !keggId.isEmpty() && !keggId.equals("-")) {
                                            db.insertKEGG(compoundId, keggId);
                                        }
                                    } else {
                                        System.out.println("Compound not found in any database for row: " + compoundName);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    String fullHmdbId = null;
                    try {
                        fullHmdbId = db.getHmdbIdFromCompoundId(compoundId);
                    } catch (CompoundNotFoundException e) {
                    }

                    Integer fullPubchemId = null;
                    try {
                        fullPubchemId = db.getPubchemIdFromCompoundId(compoundId);
                    } catch (CompoundNotFoundException e) {
                    }
                    String fullKeggId = null;
                    try {
                        fullKeggId = db.getKeggIdFromCompoundId(compoundId);
                    } catch (CompoundNotFoundException cnfe) {
                    }
                    try {
                        chebiId = db.getChebiIdFromCompoundId(compoundId);
                    } catch (CompoundNotFoundException cnfe) {
                        Identifier identifiers = compound.getIdentifiersOwn();
                        double similarity = 0.95d;
                        try {
                            chebiId = ChebiDatabase.getChebiFromSmiles(identifiers.getSmiles(), similarity);
                            db.insertChebi(compoundId, chebiId);
                        }
                        catch (ChebiException chebiException) {
                            System.out.println("Chebi not found for compound " + compoundName + " with SMILES " + identifiers.getSmiles());
                        }
                    }


                    // 🧪 3️⃣ Write results to output
                    writer.writeNext(new String[]{
                            compoundName,
                            fullHmdbId != null ? fullHmdbId : hmdbId,
                            fullPubchemId != null ? fullPubchemId.toString() : pubchemId.toString(),
                            fullKeggId != null ? fullKeggId : keggId,
                            chebiId != null ? chebiId.toString() : ""
                    });

                } catch (SQLException e) {
                    System.err.printf("SQL error on row %s: %s%n", compoundName, e.getMessage());
                }
            }

            System.out.println("✅ Processing finished. Output written to " + outputCsv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
