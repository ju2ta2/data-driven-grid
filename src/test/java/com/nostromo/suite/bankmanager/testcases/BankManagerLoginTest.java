package com.nostromo.suite.bankmanager.testcases;

import com.nostromo.datadriven.base.TestBase;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class BankManagerLoginTest extends TestBase {

    @Test
    public void bankManagerLoginTest() throws IOException, InterruptedException {
    	click("bankMngLogBtn_CSS");
        assertTrue(isElementPresent("addCustomerBtn_CSS"), "Login not successful");
        log.debug("LoginTest successfully executed!");
        
//        Assert.fail("Login not successful!");
    }
}
