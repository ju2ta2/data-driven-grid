package com.nostromo.utilities;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviders {

    @DataProvider(name = "bankManagerDP", parallel = true)
    public static Object[][] getDataBankManagerSuite(Method method) {
        ExcelReader excelReader = new ExcelReader(Constants.SUITE_BM_EXEL_PATH);
        String testCase = method.getName();
        return DataUtil.getData(testCase, excelReader);
    }

    @DataProvider(name = "customerDP")
    public static Object[][] getDataCustomerSuite(Method method) {
        ExcelReader excelReader = new ExcelReader(Constants.SUITE_CUSTOM_EXEL_PATH);
        String testCase = method.getName();
        return DataUtil.getData(testCase, excelReader);
    }
}
