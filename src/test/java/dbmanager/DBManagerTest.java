package dbmanager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

class DBManagerTest {

    @Test
    void getce_eff_mob_id() {
        DBManager dbManager = new DBManager();
        dbManager.connectToDB("compounds","root","alberto");
        Integer ce_compound_id = 180838;
        Integer eff_mob_exp_prop_id = 1;
        Integer expectedResult = 1;
        Integer actualResult;
        try {
            fail("This test is a prototype");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}