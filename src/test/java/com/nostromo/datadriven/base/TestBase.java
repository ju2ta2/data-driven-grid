package com.nostromo.datadriven.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.nostromo.utilities.DataUtil;
import com.nostromo.utilities.MailConfig;
import com.nostromo.utilities.MonitoringMail;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class TestBase {

    public static ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();
    public RemoteWebDriver remoteWebDriver = null;
    public Properties or = new Properties();
    public Properties config = new Properties();
    public FileInputStream fis;
    public static final Logger log = LogManager.getLogger(TestBase.class.getName());
//    public WebDriverWait driverWait;
//    public String browser;
    public static ExtentTest extentTest;
    public static ExtentReports extentReporter = new ExtentReports();
    public static ExtentSparkReporter sparkReporter;
    public static ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();
    public String emailMessageBody;


    public void setUp() throws IOException {

        Date d = new Date();
        String reportName = "Spark_" + d.toString().replace(":", "_").replace(" ", "_").replace("+", "_") + ".html";
        sparkReporter = new ExtentSparkReporter("reports/" + reportName);

        // extent reporter setup
        sparkReporter.loadXMLConfig(Paths.get("src/test/resources/extentconfig/spark-config.xml").toAbsolutePath().toString());
        sparkReporter.config(
                ExtentSparkReporterConfig.builder()
                        .theme(Theme.DARK)
                        .documentTitle("MyReport")
                        .reportName("1.0-SNAPSHOT")
                        .build()
        );
        extentReporter.attachReporter(sparkReporter);

        // Configuration properties files initialization
        if (remoteWebDriver == null) {
            try {
                fis = new FileInputStream(Paths.get("src/test/resources/properties/Config.properties").toAbsolutePath().toString());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                config.load(fis);
                log.debug("Config file loaded!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                fis = new FileInputStream(Paths.get("src/test/resources/properties/OR.properties").toAbsolutePath().toString());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                or.load(fis);
                log.debug("OR file loaded!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    @AfterSuite
//    public void tearDown() {
//        getDriver().quit();
//        addLog("debug", "Testsuite execution completed!");
//    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public void setRemoteWebDriver(RemoteWebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public void setWaitDriver(WebDriverWait wait) {
        waitThreadLocal.set(wait);
    }

    public static WebDriverWait getWaitDriver() {
        return waitThreadLocal.get();
    }

    public void setExtentReporter(ExtentTest et) {
        extentTestThreadLocal.set(et);
    }

    public ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }

    public String getThreadID(Object value) {
        String text = value.toString();
        String[] nextText = text.split(" ");
        return nextText[nextText.length - 1].replace("(", "").replace(")", "");
    }

    public String getThreadBrowser(Object value) {
        String text = value.toString();
        String[] nextText = text.split(" ");
        return nextText[nextText.length - 4];
    }

    public void navigate(String url) throws IOException {
        getDriver().get(config.getProperty(url));
        addLog("info", "Navigating to: " + config.getProperty(url));
        addRecordInExtReport("info", "Navigating to: " + config.getProperty(url), null);
    }

    public void openBrowser(String browser) throws IOException {
        switch (browser) {
            case "firefox" -> {
//                WebDriverFactory.setDriver("firefox");
//                getDriver().get("http://192.168.0.173:4447");
//                log.info("firefox opened successfully");
//                addRecordInExtReport("info", "firefox opened successfully", null);
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPlatformName(Platform.ANY.name());
                remoteWebDriver = new RemoteWebDriver(new URL("http://192.168.0.173:4447"), firefoxOptions);
                setRemoteWebDriver(remoteWebDriver);
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait"))));
                getDriver().manage().window().maximize();
                log.info(firefoxOptions.getBrowserName() + " opened successfully");
                addRecordInExtReport("info", firefoxOptions.getBrowserName() + " opened successfully", null);
            }
            case "edge" -> {
//                WebDriverFactory.setDriver("edge");
//                getDriver().get("http://192.168.0.173:4447");
//                log.info("edge opened successfully");
//                addRecordInExtReport("info", "edge opened successfully", null);
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPlatformName(Platform.ANY.name());
                remoteWebDriver = new RemoteWebDriver(new URL("http://192.168.0.173:4447"), edgeOptions);
                setRemoteWebDriver(remoteWebDriver);
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait"))));
                getDriver().manage().window().maximize();
                log.info(edgeOptions.getBrowserName() + " opened successfully");
                addRecordInExtReport("info", edgeOptions.getBrowserName() + " opened successfully", null);
            }
            case "chrome" -> {
//                WebDriverFactory.setDriver("chrome");
//                getDriver().get("http://192.168.0.173:4447");
//                log.info("chrome opened successfully");
                addRecordInExtReport("info", "chrome opened successfully", null);
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPlatformName(Platform.ANY.name());
                remoteWebDriver = new RemoteWebDriver(new URL("http://192.168.0.173:4447"), chromeOptions);
                setRemoteWebDriver(remoteWebDriver);
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait"))));
                getDriver().manage().window().maximize();
                log.info(chromeOptions.getBrowserName() + " opened successfully");
                addRecordInExtReport("info", chromeOptions.getBrowserName() + " opened successfully", null);
            }
        }
    }

    public boolean isElementPresent(String locator) {
        By by = null;

        if(locator.endsWith("_CSS")){
            by = By.cssSelector(or.getProperty(locator));
        } else if(locator.endsWith("_XPATH")) {
            by = By.xpath(or.getProperty(locator));
        } else if(locator.endsWith("_ID")) {
            by = By.id(or.getProperty(locator));
        }

        try {
            getDriver().findElement(by);
            return true;
        } catch(NoSuchElementException e) {
            return false;
        }
    }

    public void click(String locator) throws IOException, InterruptedException {
        try {
            if(locator.endsWith("_CSS")){
                getDriver().findElement(By.cssSelector(or.getProperty(locator))).click();
            } else if(locator.endsWith("_XPATH")) {
                getDriver().findElement(By.xpath(or.getProperty(locator))).click();
            } else if(locator.endsWith("_ID")) {
                getDriver().findElement(By.id(or.getProperty(locator))).click();
            }
            addLog("info", "Clicking on an Element: " + locator);
            addRecordInExtReport("info", "Clicking on an Element: \"" + locator + "\"", null);
        } catch (Throwable t) {
            Thread.sleep(1000);
            DataUtil.captureScreenshot();

            addLog("error", "Failing while clicking on an Element " + locator);
            addRecordInExtReport("fail", "Failing while clicking on an element " + locator, DataUtil.screenshotName);
        }
    }

    public void type(String locator, String value) throws IOException, InterruptedException {
        try {
            if(locator.endsWith("_CSS")){
                getDriver().findElement(By.cssSelector(or.getProperty(locator))).sendKeys(value);
            } else if(locator.endsWith("_XPATH")) {
                getDriver().findElement(By.xpath(or.getProperty(locator))).sendKeys(value);
            } else if(locator.endsWith("_ID")) {
                getDriver().findElement(By.id(or.getProperty(locator))).sendKeys(value);
            }
            addLog("info", "Typing in: " + locator + " entered value as " + value);
            addRecordInExtReport("info", "\"" + locator + "\" field filled as \"" + value + "\"", null);
        } catch (Throwable t) {
            Thread.sleep(1000);
            DataUtil.captureScreenshot();

            addLog("error", "Failing while clicking on an element " + locator);
            addRecordInExtReport("fail", "Failing while typing an element " + locator, DataUtil.screenshotName);
        }
    }

    public void select(String locator, String value) throws IOException, InterruptedException {
        try {
            WebElement dropdown = null;
            if(locator.endsWith("_CSS")){
                dropdown = getDriver().findElement(By.cssSelector(or.getProperty(locator)));
            } else if(locator.endsWith("_XPATH")) {
                dropdown = getDriver().findElement(By.xpath(or.getProperty(locator)));
            } else if(locator.endsWith("_ID")) {
                dropdown = getDriver().findElement(By.id(or.getProperty(locator)));
            }
            Select select;
            if (dropdown != null) {
                select = new Select(dropdown);
                select.selectByVisibleText(value);
            }
            addLog("info", "Selecting from dropdown: " + locator + " value as " + value);
            addRecordInExtReport("info", "Selecting from dropdown: " + locator + " value as " + value, null);
        } catch (Throwable t) {
            Thread.sleep(1000);
            DataUtil.captureScreenshot();

            addLog("error", "Failing while clicking on an element " + locator);
            addRecordInExtReport("fail", "Failing while selecting an element " + locator, DataUtil.screenshotName);
        }
    }

    public void verifyEquals(String expected, String actual) throws IOException, InterruptedException {
        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable t) {
            Thread.sleep(1000);
            DataUtil.captureScreenshot();

            addLog("error", "Failed with exception: expected [" + expected + "] but found [" + actual + "]");
            addRecordInExtReport("fail", "Failed with exception: " + t.getMessage(), DataUtil.screenshotName);
        }
    }

    public void addRecordInExtReport(String status, String message, String screenshot) throws IOException {
        switch (status) {
            case "info" -> getExtentTest().log(Status.INFO, message);
            case "fail" -> getExtentTest().log(Status.FAIL, message);
            case "skip" -> getExtentTest().log(Status.SKIP, message);
            case "pass" -> getExtentTest().log(Status.PASS, message);
        }
        if(screenshot != null) {
            getExtentTest().fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
        }
        extentReporter.flush();
    }

    public void addLog(String status, String message) {
        switch (status) {
            case "info" -> log.info("Thread: " + getThreadID(driverThreadLocal.get()) + ". Browser: " + getThreadBrowser(driverThreadLocal.get()) + ". " + message);
            case "error" -> log.error("Thread: " + getThreadID(driverThreadLocal.get()) + ". Browser: " + getThreadBrowser(driverThreadLocal.get()) + ". " + message);
            case "debug" -> log.debug("Thread: " + getThreadID(driverThreadLocal.get()) + ". Browser: " + getThreadBrowser(driverThreadLocal.get()) + ". " + message);
            case "fatal" -> log.fatal("Thread: " + getThreadID(driverThreadLocal.get()) + ". Browser: " + getThreadBrowser(driverThreadLocal.get()) + ". " + message);
        }
    }

    public void sendEmailWithJenkinsTestResults() throws UnknownHostException {
        MonitoringMail mail = new MonitoringMail();
        emailMessageBody = "http://" + InetAddress.getLocalHost().getHostAddress() + ":8080/job/data-driven-framework-idea/HTML_20Report";
        System.out.println(emailMessageBody);
        try {
            mail.sendMail(MailConfig.server, MailConfig.from, MailConfig.to, MailConfig.subject, emailMessageBody);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAttributeContains(String locator, String value, String testName) throws IOException {
        WebElement element = null;
        if(locator.endsWith("_CSS")){
            element = getDriver().findElement(By.cssSelector(or.getProperty(locator)));
        } else if(locator.endsWith("_XPATH")) {
            element = getDriver().findElement(By.xpath(or.getProperty(locator)));
        } else if(locator.endsWith("_ID")) {
            element = getDriver().findElement(By.id(or.getProperty(locator)));
        }
        if(element != null && element.getAttribute("textContent").contains(value)) {
            addLog("info", testName + " success: Attribute \"" + locator + "\" contains value \"" + value + "\"");
            addRecordInExtReport("info", testName + " success: Attribute \"" + locator + "\" contains value \"" + value + "\"", null);
            return true;
        } else {
            addLog("error", testName + " fail: Attribute \"" + locator + "\" not contains value \"" + value + "\"");
            addRecordInExtReport("fail", testName + " fail: Attribute \"" + locator + "\" not contains value \"" + value + "\"", DataUtil.screenshotName);
            return false;
        }
    }
}
