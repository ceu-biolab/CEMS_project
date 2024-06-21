/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author alberto.gildelafuent
 */
public class fileIOTest {


    public fileIOTest() {
    }

    /**
     * Test of readStringFromFile method, of class fileIO.
     */
    @Test
    public void testReadStringFromFile() throws Exception {

        String filename = "src/main/resources/connectionData.pass";
        System.out.println(System.getProperty("user.dir"));
        System.out.println("readStringFromFile");
        String expResult = "{\n"
                + "	\"db_name\" : \"CEMS\",\n"
                + "	\"db_user\" : \"root\",\n"
                + "	\"db_password\" : \"password\"\n"
                + "}";
        String result = FileIO.readStringFromFile(filename);
        assertEquals(expResult, result);
    }

}
