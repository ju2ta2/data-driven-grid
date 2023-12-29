package com.nostromo.utilities;

import com.nostromo.datadriven.base.TestBase;
import com.nostromo.datadriven.base.WebDriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class DataUtil extends TestBase {

    public static String screenshotName;

    public static void captureScreenshot() throws IOException {
        File screenFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        screenshotName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                .replace(":", "_")
                .replace(".", "_")
                .replace("-", "_")
                .replace(" ", "_") + ".jpg";
        FileUtils.copyFile(screenFile, new File("reports/" + screenshotName));
    }

    public static boolean isSuiteRunnable(String suiteName) {

        ExcelReader excelReader = new ExcelReader(Constants.SUITE_EXEL_PATH);
        int rows = excelReader.getRowCount(Constants.SUITE_SHEET);

        for(int rowNum = 2; rowNum <= rows; rowNum++) {
            String data = excelReader.getCellData(Constants.SUITE_SHEET, Constants.SUITE_NAME_COLUMN, rowNum);
            if(data.equals(suiteName)) {
                String runMode = excelReader.getCellData(Constants.SUITE_SHEET, Constants.RUNMODE_COLUMN, rowNum);
                return runMode.equals(Constants.RUNMODE_YES);
            }
        }
        return false;
    }

    public static boolean isTestRunnable(String testCaseName, ExcelReader excelReader) {
        int rows = excelReader.getRowCount(Constants.TC_SHEET);

        for(int rowNum = 2; rowNum <= rows; rowNum++) {
            String data = excelReader.getCellData(Constants.TC_SHEET, Constants.TC_NAME_COLUMN, rowNum);
            if(data.equals(testCaseName)) {
                String runMode = excelReader.getCellData(Constants.TC_SHEET, Constants.RUNMODE_COLUMN, rowNum);
                return runMode.equals(Constants.RUNMODE_YES);
            }
        }
        return false;
    }

//    public static boolean isTestRunnable(String testName, ExcelReader excel) {
//        String sheetName = "test_suite";
//        int rows = excel.getRowCount(sheetName);
//
//        for(int rowNum = 2; rowNum <= rows; rowNum++) {
//            String testCase = excel.getCellData(sheetName, "TCID", rowNum);
//
//            if(testCase.equalsIgnoreCase(testName)) {
//                String runMode = excel.getCellData(sheetName, "runmode", rowNum);
//                return runMode.equalsIgnoreCase("Y");
//            }
//        }
//        return false;
//    }

    @DataProvider
    public static Object[][] getData(String testCase, ExcelReader excelReader) {
        int rows = excelReader.getRowCount(Constants.DATA_SHEET);

        System.out.println("Total rows are: " + rows);

        // Find the test case start row
        int testCaseRowNum;

        for(testCaseRowNum = 1; testCaseRowNum <= rows; testCaseRowNum++) {
            String testCaseName = excelReader.getCellData(Constants.DATA_SHEET, 0, testCaseRowNum);

            if(testCaseName.equalsIgnoreCase(testCase)) {
                break;
            }
        }
        System.out.println("Test case starts from row number: " + testCaseRowNum);

        // Checking total rows in test case
        int dataStartRowNum = testCaseRowNum + 2;
        int testRows = 0;
        while(!excelReader.getCellData(Constants.DATA_SHEET, 0, dataStartRowNum + testRows).isEmpty()) {
            testRows++;
        }
        System.out.println("Total rows of data are: " + testRows);

        // Checking total columns in test case
        int dataStartColNum = testCaseRowNum + 1;
        int testCols = 0;
        while(!excelReader.getCellData(Constants.DATA_SHEET, testCols, dataStartColNum).isEmpty()) {
            testCols++;
        }
        System.out.println("Total cols of data are: " + testCols);

        // Data preparation

//        Object[][] data = new Object[testRows][testCols];
//
//        for(int rNum = dataStartRowNum; rNum < (dataStartRowNum + testRows); rNum++) {
//            for(int cNum = 0; cNum < testCols; cNum++) {
//                data[rNum - dataStartRowNum][cNum] = excelReader.getCellData(Constants.DATA_SHEET, cNum, rNum);
//            }
//        }

        Object[][] data = new Object[testRows][1];

        int i = 0;
        for(int rNum = dataStartRowNum; rNum < (dataStartRowNum + testRows); rNum++) {
            Hashtable<String , String> table = new Hashtable<>();
            for(int cNum = 0; cNum < testCols; cNum++) {
                String testData = excelReader.getCellData(Constants.DATA_SHEET, cNum, rNum);
                String colName = excelReader.getCellData(Constants.DATA_SHEET, cNum, dataStartColNum);
                table.put(colName, testData);
            }
            data[i][0] = table;
            i++;
        }
        return data;
    }

    @DataProvider(name="dp")
    public Object[][] getData(Method m, ExcelReader excelReader) {

        String sheetName = m.getName();
        int rows = excelReader.getRowCount(sheetName);
        int cols = excelReader.getColumnCount(sheetName);

        Object[][] data = new Object[rows - 1][1];
        Hashtable<String, String> table;

        for(int rowNum = 2; rowNum <= rows; rowNum++) {
            table = new Hashtable<>();
            for(int colNum = 0; colNum < cols; colNum++) {
                table.put(excelReader.getCellData(sheetName, colNum, 1), excelReader.getCellData(sheetName, colNum, rowNum));
                data[rowNum - 2][0] = table;
            }
        }
        return data;
    }

    public static void checkExecution(String suiteName, String testName, String runMode, ExcelReader excelReader) {
        if(!isSuiteRunnable(suiteName)) {
            throw new SkipException("Skipping the test case: " + testName + " as the Runmode of the Test Suite: " + suiteName + " is NO");
        }

        if(!isTestRunnable(testName, excelReader)) {
            throw new SkipException("Skipping the test case: " + testName + " as the Runmode of the Test Case is NO");
        }

        if(runMode.equalsIgnoreCase(Constants.RUNMODE_NO)) {
            throw new SkipException("Skipping the test: " + testName + " as the Runmode of Data Set is NO");
        }
    }
}
