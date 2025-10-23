package dbmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class KeggRESTAPI {

    public static List<String[]> getPathwaysFromKegg(String keggId) throws IOException {
        List<String[]> pathways = new ArrayList<>();
        if (keggId == null || keggId.isEmpty() || keggId.equals("-")) return pathways;

        String url = "https://rest.kegg.jp/link/pathway/" + keggId;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String pathwayMap = parts[1].replace("path:", "");
                    // Fetch pathway name
                    String nameUrl = "https://rest.kegg.jp/list/" + pathwayMap;
                    try (BufferedReader nameReader = new BufferedReader(new InputStreamReader(new URL(nameUrl).openStream()))) {
                        String nameLine = nameReader.readLine();
                        if (nameLine != null) {
                            String[] nameParts = nameLine.split("\t");
                            if (nameParts.length == 2)
                                pathways.add(new String[]{pathwayMap, nameParts[1]});
                        }
                    }
                }
            }
        }
        return pathways;
    }
}
