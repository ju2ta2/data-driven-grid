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

import static org.testng.Assert.assertTrue;

public class OpenAccountTest extends TestBase {

    @Test(dataProviderClass = DataProviders.class, dataProvider = "bankManagerDP")
    public void openAccountTest(Hashtable<String, String> data) throws IOException, InterruptedException {
        super.setUp();
        setExtentReporter(extentReporter.createTest("Open Account Test " + data.get("browser")));
        log.info("Open Account Test   " + data.get("browser"));

        ExcelReader excelReader = new ExcelReader(Constants.SUITE_BM_EXEL_PATH);
        DataUtil.checkExecution("BankManagerSuite", "OpenAccountTest", data.get("Runmode"), excelReader);
        openBrowser(data.get("browser"));
        navigate("testSiteUrl");

        click("bankMngLogBtn_CSS");
        click("openAccountBtn_CSS");
        select("selectCustomerNameDrDown_CSS", data.get("customer"));
        select("selectCurrencyDrDown_CSS", data.get("currency"));
        click("openAccountProcessBtn_CSS");

//        Alert alert = getWaitDriver().until(ExpectedConditions.alertIsPresent());
//        assertTrue(alert.getText().contains(data.get("alerttext")));
//        alert.accept();

//        addRecordInExtReport("pass", OpenAccountTest.class.getSimpleName().toUpperCase() + " - PASS", null);
        addLog("info", OpenAccountTest.class.getSimpleName() + " test - PASSED");

//        Assert.fail("Test Alert");
    }

    @AfterMethod
    public void tearDown() {
        addLog("debug", "OpenAccountTest execution completed!");
        if (getDriver() != null) {
            getDriver().quit();
        }
    }
}
