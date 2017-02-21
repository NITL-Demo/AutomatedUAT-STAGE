package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class UAT {
	WebDriver webdriver;
	Properties prop = new Properties();
	InputStream input  = null;
	ExtentReports extentreport;
	String outputFileLocation="test-output/";
	@BeforeTest
	public void init(){
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platform", Platform.ANY);
		capabilities.setCapability("binary", "/usr/bin/firefox");
		webdriver = new FirefoxDriver(capabilities);
		try {
			input  = new FileInputStream("config/config.properties");
			extentreport= new ExtentReports(outputFileLocation+"ExtentReport/"+"UAT.html");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void service() throws Exception{
		prop.load(input);
		ExtentTest test= extentreport.startTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		String url = prop.getProperty("url");
		webdriver.get(url);
		getscreenshot("Homepage");
		flushReport(test,LogStatus.PASS, "Website URL", url, "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/Homepage.png" + ">Screenshot</a></div>");
		
		webdriver.findElement(By.cssSelector(".nav > li:nth-child(3) > a:nth-child(1)")).click();
		Thread.sleep(1000);
		webdriver.findElement(By.cssSelector("input.form-control:nth-child(1)")).sendKeys(prop.getProperty("PName"));
		webdriver.findElement(By.xpath("(.//*[@class='panel-heading']/h4/strong)[text()[contains(.,'"+prop.getProperty("PName")+"')]]/../../..//./div[2]/div[2]/button")).click();
		Thread.sleep(1000);
		webdriver.switchTo().activeElement();
		Thread.sleep(3000);
		getscreenshot("Edit");
		webdriver.findElement(By.xpath("//*[@id='pizzaModal']/div/div/div[3]/form/div[3]/div[3]/label/input")).click();
		webdriver.findElement(By.cssSelector("div.checkbox:nth-child(3) > label:nth-child(1) > input:nth-child(1)")).click();
		Thread.sleep(1000);
		flushReport(test,LogStatus.PASS,"Pizza Update" ,"Done Update" , "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/Edit.png" + ">Screenshot</a></div>");
		webdriver.findElement(By.id("editbutton")).click();
		Thread.sleep(1000);		
		String getText=webdriver.findElement(By.xpath("(.//*[@class='panel-heading']/h4/strong)[text()[contains(.,'"+prop.getProperty("PName")+"')]]/../../..//./div[2]/div/dl/dd[2]/span")).getText();
		if(prop.getProperty("cText").equalsIgnoreCase(getText)){
			getscreenshot("success_compare");
			flushReport(test,LogStatus.PASS, "(Assertion Correct) Desired Result :"+prop.getProperty("cText"),"Expected Result :"+getText  , "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/success_compare.png" + ">Screenshot</a></div>");
		}else{
			getscreenshot("failed_compare");
			flushReport(test,LogStatus.FAIL, "(Assertion Failed) Desired Result :"+prop.getProperty("cText"),"Expected Result :"+getText  , "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/failed_compare.png" + ">Screenshot</a></div>");

		}
		extentreport.endTest(test);
	}
	@AfterTest
	public void destroy(){
		webdriver.quit();
	}
	
	 public void getscreenshot(String filename) throws Exception 
     {
             File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
             FileUtils.copyFile(scrFile, new File(outputFileLocation+"Images/"+filename+".png"));          
     }
	 
	 public void flushReport(ExtentTest test,LogStatus status,String Data,String Data1,String path){
		 test.log(status,Data,Data1+path);
		 extentreport.flush();
	 }

}
