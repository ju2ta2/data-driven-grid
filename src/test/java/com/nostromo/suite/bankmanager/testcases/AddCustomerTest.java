package com.nostromo.suite.bankmanager.testcases;

import com.nostromo.datadriven.base.TestBase;
import com.nostromo.utilities.Constants;
import com.nostromo.utilities.DataProviders;
import com.nostromo.utilities.DataUtil;
import com.nostromo.utilities.ExcelReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Hashtable;

public class AddCustomerTest extends TestBase {

    @Test(dataProviderClass = DataProviders.class, dataProvider = "bankManagerDP")
    public void addCustomerTest(Hashtable<String, String> data) throws IOException, InterruptedException {
        super.setUp();
        setExtentReporter(extentReporter.createTest("Add Customer Test " + data.get("browser")));
        log.info("Add Customer Test   " + data.get("browser"));

        ExcelReader excelReader = new ExcelReader(Constants.SUITE_BM_EXEL_PATH);
        DataUtil.checkExecution("BankManagerSuite", "AddCustomerTest", data.get("Runmode"), excelReader);
        openBrowser(data.get("browser"));
        navigate("testSiteUrl");

        click("bankMngLogBtn_CSS");
        click("addCustomerBtn_CSS");
        type("firstname_CSS", data.get("firstname"));
        type("lastname_CSS", data.get("lastname"));
        type("postcode_CSS", data.get("postcode"));
        click("addBtn_CSS");

//        Alert alert = getWaitDriver().until(ExpectedConditions.alertIsPresent());
//        alert.accept();

//        addRecordInExtReport("pass", AddCustomerTest.class.getSimpleName().toUpperCase() + " - PASS", null);
        addLog("info", AddCustomerTest.class.getSimpleName() + " test - PASSED");
    }

    @AfterMethod
    public void tearDown() {
        addLog("debug", "AddCustomerTest execution completed!");
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
}
