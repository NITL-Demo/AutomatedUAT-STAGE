package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Functional_Test {
	WebDriver webdriver;
	Properties prop = new Properties();
	InputStream input  = null;
	ExtentReports extentreport;
	String outputFileLocation="test-output/";
	@BeforeTest
	public void init(){
		 System.setProperty("webdriver.gecko.driver","/root/artifacts/resources/geckodriver");		
		 webdriver = new FirefoxDriver();
		try {
			input  = new FileInputStream("config/config.properties");
			extentreport= new ExtentReports(outputFileLocation+"ExtentReport/"+"FunctionalTest.html");
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
		// Add Base
		webdriver.findElement(By.cssSelector("button.btn-primary:nth-child(1)")).click();
		webdriver.switchTo().activeElement();
		Thread.sleep(3000);
		webdriver.findElement(By.id("base-name")).sendKeys(prop.getProperty("basename"));
		getscreenshot("Base_Added");
		flushReport(test, LogStatus.PASS, "Base Name ", prop.getProperty("basename"), "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/Base_Added.png" + ">Screenshot</a></div>");
		Thread.sleep(1000);
		webdriver.findElement(By.id("savebutton")).click();
		
		// Add Topings
		Thread.sleep(1000);
		webdriver.findElement(By.cssSelector(".nav > li:nth-child(2) > a:nth-child(1)")).click();
		Thread.sleep(1000);
		webdriver.findElement(By.cssSelector("button.btn-primary:nth-child(1)")).click();
		webdriver.switchTo().activeElement();
		Thread.sleep(3000);
		webdriver.findElement(By.id("topping-name")).sendKeys(prop.getProperty("toppingname"));
		getscreenshot("Toppings_Added");
		flushReport(test,LogStatus.PASS, "Topping Name ", prop.getProperty("toppingname"), "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/Toppings_Added.png" + ">Screenshot</a></div>");
		Thread.sleep(1000);
		webdriver.findElement(By.id("savebutton")).click();
		
		//Add Pizza
		Thread.sleep(1000);
		webdriver.findElement(By.cssSelector(".nav > li:nth-child(3) > a:nth-child(1)")).click();
		Thread.sleep(1000);
		webdriver.findElement(By.cssSelector("button.btn-primary:nth-child(1)")).click();
		webdriver.switchTo().activeElement();
		Thread.sleep(3000);
		webdriver.findElement(By.cssSelector("#pizza-name")).sendKeys(prop.getProperty("PName"));
		Select oSelect = new Select(webdriver.findElement(By.cssSelector("#base-name")));
		oSelect.selectByVisibleText(prop.getProperty("basename"));
		webdriver.findElement(By.xpath("//*[@id='pizzaModal']/div/div/div[3]/form/div[3]/div[4]/label/input")).click();
		webdriver.findElement(By.id("pizza-price")).sendKeys(prop.getProperty("price"));
		getscreenshot("Pizza_Added");
		flushReport(test,LogStatus.PASS, "Pizza Price ", prop.getProperty("price"), "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/Pizza_Added.png" + ">Screenshot</a></div>");
		Thread.sleep(1000);
		webdriver.findElement(By.id("savebutton")).click();
		
		//Add Order
		Thread.sleep(1000);
		webdriver.findElement(By.cssSelector(".nav > li:nth-child(4) > a:nth-child(1)")).click();
		Thread.sleep(1000);
		webdriver.findElement(By.cssSelector(".col-md-6")).click();
		webdriver.switchTo().activeElement();
		Thread.sleep(3000);
		webdriver.findElement(By.xpath("//*[@id='orderModal']/div/div/div[3]/form/div/div/div[2]/div/div/label/input")).click();
		getscreenshot("Order_Added");
		flushReport(test, LogStatus.PASS, "Order ", "Done" , "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/Order_Added.png" + ">Screenshot</a></div>");
		Thread.sleep(1000);
		webdriver.findElement(By.id("savebutton")).click();
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
