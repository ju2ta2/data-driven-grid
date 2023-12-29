package com.nostromo.datadriven.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

public class WebDriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Properties config = new Properties();

    public static void setDriver(String browser) throws MalformedURLException {
        RemoteWebDriver rwd;
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPlatformName(Platform.ANY.name());
                WebDriverManager.chromedriver().setup();
                rwd = new RemoteWebDriver(chromeOptions);
                break;

            case "fireFox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPlatformName(Platform.ANY.name());
                rwd = new RemoteWebDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                FirefoxOptions edgeOptions = new FirefoxOptions();
                edgeOptions.setPlatformName(Platform.ANY.name());
                rwd = new RemoteWebDriver(edgeOptions);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + browser);
        }
        driver.set(Objects.requireNonNull(rwd));
        prepareBrowser();
    }

    private static void prepareBrowser(){
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait"))));
    }

    public static WebDriver getDriver(){
        return Objects.requireNonNull(driver.get());
    }

    public static void closeBrowser() {
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait"))));
        getDriver().close();
        getDriver().quit();
    }
}
