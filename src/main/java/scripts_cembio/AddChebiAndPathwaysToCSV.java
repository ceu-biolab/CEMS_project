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
import exceptions.CompoundNotFoundException;

import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class AddChebiAndPathwaysToCSV {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/compounds";
    private static final String USER = "alberto";
    private static final String PASS = "alberto";

    public static void main(String[] args) {
        String inputCsv = "/home/ceu/Escritorio/cmm_temporal/chebi_ids2.csv";
        String outputCsv = "/home/ceu/Escritorio/cmm_temporal/chebi_ids_with_pathways.csv";
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
                String hmdbId = line[1].equals("-") ? null : line[0];
                Integer pubchemId = null;
                try {
                    pubchemId = Integer.parseInt(line[2].trim());
                } catch (NumberFormatException e) {
                    pubchemId = null;
                }
                String keggId = line[3].equals("-") ? null : line[0];

                Integer compoundId = null;
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
                        } catch (CompoundNotFoundException hmdbNotFoundException) {
                            try {
                                compoundId = db.getCompoundIdByKegg(keggId);
                            } catch (CompoundNotFoundException keggNotFoundException) {
                                compoundId = compound.getCompound_id();
                                if(compoundId == 0)
                                {
                                    compoundId = db.insertCompound(compound);
                                    db.insertCompoundIdentifiers(compoundId, compound.getINCHI());
                                    if(hmdbId != null && !hmdbId.isEmpty() && !hmdbId.equals("-")) {
                                        db.insertHMDB(compoundId, hmdbId);
                                    }
                                    if(pubchemId != null ) {
                                        db.insertPC(compoundId, pubchemId);
                                    }
                                    if(keggId != null && !keggId.isEmpty() && !keggId.equals("-")) {
                                        db.insertKEGG(compoundId, keggId);
                                    }
                                }
                            }
                        }
                    }

                    if (compoundId == null) {
                        compound = PubchemRest.getCompoundFromPCID(pubchemId);
                        compoundId = compound.getCompound_id();
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
                    }
                    catch(CompoundNotFoundException cnfe)
                    {
                        Identifier identifiers = compound.getIdentifiersOwn();
                        //ChebiDatabase.getChebiFromIdentifiers(identifiers);
                        db.insertChebi(compoundId, chebiId);
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
