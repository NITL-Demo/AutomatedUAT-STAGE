package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Build_Verification_Test {
	WebDriver webdriver;
	Properties prop = new Properties();
	InputStream input  = null;
	ExtentReports extentreport;
	String outputFileLocation="test-output/";
	@BeforeTest
	public void init(){
		webdriver = new FirefoxDriver();
		try {
			input  = new FileInputStream("config/config.properties");
			extentreport= new ExtentReports(outputFileLocation+"ExtentReport/"+"BuildVerification.html");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void service(Method method) throws Exception{
			
			String homeWindow = null;
			String[] links = null;
			int linksCount = 0;
			prop.load(input);
			ExtentTest test= extentreport.startTest(method.getName());
			
			String url = prop.getProperty("url");
			webdriver.get(url);
		
			// Storing URL in String variable
			String actualUrl = webdriver.getCurrentUrl();
			getscreenshot("Homepage");
			
			test.log(LogStatus.PASS, "Website URL", actualUrl+ "<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/Homepage.png" + ">Screenshot</a></div>");
			if (actualUrl.equals(url)){
				System.out.println("Verification Successful - The correct Url is opened.");
				test.log(LogStatus.PASS, "URL matched with Actual URL", actualUrl);
			}else{
				System.out.println("Verification Failed - An incorrect Url is opened.");
				test.log(LogStatus.PASS, "URL Not matched with Actual URL", actualUrl);
				//In case of Fail, you like to print the actual and expected URL for the record purpose
				System.out.println("Actual URL is : " + actualUrl);
				System.out.println("Expected URL is : " + url);
			}

			List<WebElement> all_links_webpage = webdriver.findElements(By.tagName("a")); 
			// Print total no of links on the webpage
			System.out.println("Print total no of links on the webpage - ");
			linksCount = all_links_webpage.size();
			System.out.println(linksCount); 
			links= new String[linksCount];// Following instruction stores each link and Prints on console
			System.out.println("Print Links-");

			for(int i=0;i<linksCount;i++)
			{
				
			links[i] = all_links_webpage.get(i).getAttribute("href");
			System.out.println(all_links_webpage.get(i).getAttribute("href"));
			}
			// Following instruction Return an opaque handle to this window that uniquely identifies it within this driver instance.
			// This can be used to switch to this window at a later date
			homeWindow = webdriver.getWindowHandle().toString(); 
			// Visiting Each Link in on the Page

			System.out.println("Visiting Each Links"); 
			int j=1;
			String serviceName = null;
			for(int i=1;i<linksCount;i++)
			{
			
			try {
				String data[]=links[i].split("/");
				serviceName=data[data.length-1];
				
				System.out.println(links[i]);
				webdriver.navigate().to(links[i]);
				Thread.sleep(2000);
				getscreenshot(serviceName);
				flushReport(test, LogStatus.PASS, i+":Page of Website", links[i],"<div align='right' style='float:right' class='imagesrc'><a href=" + "./../Images/"+serviceName+".png" + ">Screenshot</a></div>");
				
				Thread.sleep(2000);
				
			} catch (InterruptedException e) {
				test.log(LogStatus.FAIL, "Page Not Available");
				e.printStackTrace();
			} catch (Exception e) {
				test.log(LogStatus.FAIL, "Page Not Available");
				e.printStackTrace();
			}
			j++;
			
			webdriver.switchTo().window(homeWindow);
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
