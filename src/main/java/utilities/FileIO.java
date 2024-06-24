/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author alberto.gildelafuent
 */
public class FileIO {

    /**
     * Method to return the String from a plain text file
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readStringFromFile(String filePath) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        return new String(encoded, Charset.defaultCharset()).replace("\r", "");
    }

    public static String readStringFromURL(String urlName) throws IOException {
        URL url = new URL(urlName);
        InputStream in = url.openStream();
        byte[] bytes = in.readAllBytes();
        return new String(bytes);

    }

    public static void write(String content, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                //System.out.println("\nCreating file: " + path);
                file.createNewFile();
            }
            FileWriter fileWritter;
            fileWritter = new FileWriter(file, true);
            BufferedWriter bw;
            bw = new BufferedWriter(fileWritter);
            bw.write("\n");
            bw.write(content);
            bw.close();
        } catch (IOException ex) {
            System.out.println("exception occurred writing in file" + path + "\n" + ex);
        }
    }


    public static void main(String[] args) {
        try {
            String filename = "src/main/resources/connectionData.pass";

            String result = FileIO.readStringFromFile(filename);
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
