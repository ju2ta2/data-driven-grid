package com.nostromo.listeners;

import com.nostromo.datadriven.base.TestBase;
import com.nostromo.utilities.DataUtil;
import org.testng.*;

import java.io.IOException;

public class CustomListener extends TestBase implements ITestListener, ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        ISuiteListener.super.onStart(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        ISuiteListener.super.onFinish(suite);
    }

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        try {
            addRecordInExtReport("pass", result.getName().toUpperCase() + " - PASS", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        addLog("info", result.getInstance().getClass().getSimpleName() + " test - PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Thread.sleep(1000);
            DataUtil.captureScreenshot();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            addRecordInExtReport("fail", result + " Failed with exception: " + result.getThrowable(), DataUtil.screenshotName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addLog("error", result.getInstance().getClass().getSimpleName() + " test - FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
    }
}
