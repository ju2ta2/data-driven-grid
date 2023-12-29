package com.nostromo.utilities;

import java.nio.file.Paths;

public class Constants {
    public static String DATA_SHEET = "TestData";
    public static String SUITE_SHEET = "Suite";
    public static String TC_SHEET = "TestCases";
    public static String SUITE_NAME_COLUMN = "SuiteName";
    public static String TC_NAME_COLUMN = "TestCases";
    public static String RUNMODE_COLUMN = "Runmode";
    public static String RUNMODE_YES = "Y";
    public static String RUNMODE_NO = "N";
    public static String SUITE_BM_EXEL_PATH = Paths.get("src/test/resources/testdata/BankManagerSuite.xlsx").toAbsolutePath().toString();
    public static String SUITE_CUSTOM_EXEL_PATH = Paths.get("src/test/resources/testdata/CustomerSuite.xlsx").toAbsolutePath().toString();
    public static String SUITE_EXEL_PATH = Paths.get("src/test/resources/testdata/Suite.xlsx").toAbsolutePath().toString();
}
