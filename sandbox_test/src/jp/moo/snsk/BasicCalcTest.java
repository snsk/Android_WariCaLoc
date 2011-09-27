package jp.moo.snsk;

import java.util.List;

import com.google.android.testing.nativedriver.client.AndroidNativeDriver;
import com.google.android.testing.nativedriver.client.AndroidNativeDriverBuilder;
import com.google.android.testing.nativedriver.client.AndroidNativeElement;
import com.google.android.testing.nativedriver.common.AndroidKeys;
import com.google.android.testing.nativedriver.common.AndroidNativeBy;

import junit.framework.TestCase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BasicCalcTest extends TestCase {
	  private AndroidNativeDriver driver;
	  private List<AndroidNativeElement> elementsWithResult;
	  
	  @Override
	  protected void setUp() {
	    driver = getDriver();
	  }

	  @Override
	  protected void tearDown() {
		elementsWithResult.clear();
	    driver.quit();
	  }

	  protected AndroidNativeDriver getDriver() {
	    return new AndroidNativeDriverBuilder()
	        .withDefaultServer()
	        .build();
	  }

	  public void testCalculateNormalCase(){
		  util_startWariCaLocActivity();
		  WebElement calclateBtn = driver.findElement(By.id("Button01"));
		  
		  // 10000 / 5 = [2000]
		  util_SetWarikanValue("EditText01", 10000, "EditText02", 5, "EditText03", 0);
		  calclateBtn.click();
		  elementsWithResult = driver.findAndroidNativeElements(AndroidNativeBy.partialText("2000"));
		  assertEquals(1, elementsWithResult.size());		  		  
	  }

	  public void testCalculateModCase(){
		  util_startWariCaLocActivity();
		  WebElement calclateBtn = driver.findElement(By.id("Button01"));
		  
		  //10001 / 5 = [2000 mod 1]
		  util_SetWarikanValue("EditText01", 10001, "EditText02", 5, "EditText03", 0);
		  calclateBtn.click();
		  elementsWithResult = driver.findAndroidNativeElements(AndroidNativeBy.partialText("1"));
		  assertEquals(1, elementsWithResult.size());
	  }

	  public void testCalculateDisCount() throws Exception{
		  util_startWariCaLocActivity();
		  WebElement calclateBtn = driver.findElement(By.id("Button01"));
		  
		  //(2 person 50% off)10000 / 5 = [1000 2666 mod 2]
		  util_SetWarikanValueWithDisCount("EditText01", 10000, "EditText02", 5, "EditText03", 0, 2, 50);
		  calclateBtn.click();
		  elementsWithResult = driver.findAndroidNativeElements(AndroidNativeBy.partialText("2666"));
		  assertEquals(1, elementsWithResult.size());
	  }
	  
	  private void util_startWariCaLocActivity() {
	    driver.startActivity("jp.moo.snsk.WariCaLocActivity");	    
	  }
	  	  
	  //inbound value type is int, ensured by layout.xml.
	  private void util_SetWarikanValue(String totalBillsId, int totalBillsVal,
			  String totalHeadCntId, int totalHeadCntVal, String prePayedId, int prePayedVal){
		  
		  WebElement totalBills = driver.findElement(By.id(totalBillsId));
		  totalBills.clear();
		  totalBills.sendKeys(String.valueOf(totalBillsVal));
		  totalBills.sendKeys(AndroidKeys.DPAD_DOWN);//TODO: Could not focus next EditText, Why?
		  
		  WebElement totalHead = driver.findElement(By.id(totalHeadCntId));
		  totalHead.clear();
		  totalHead.sendKeys(String.valueOf(totalHeadCntVal));
		  totalHead.sendKeys(AndroidKeys.DPAD_DOWN);
		  
		  WebElement prePayed = driver.findElement(By.id(prePayedId));
		  prePayed.clear();
		  prePayed.sendKeys(String.valueOf(prePayedVal));
	  }
	  
	  private void util_SetWarikanValueWithDisCount(String totalBillsId, int totalBillsVal,
			  String totalHeadCntId, int totalHeadCntVal, String prePayedId, int prePayedVal,
			  int disCountHeadCnt, int disCountPercentage) throws Exception{
		  
		  WebElement totalBills = driver.findElement(By.id(totalBillsId));
		  totalBills.clear();
		  totalBills.sendKeys(String.valueOf(totalBillsVal));
		  totalBills.sendKeys(AndroidKeys.DPAD_DOWN);
		  
		  WebElement totalHead = driver.findElement(By.id(totalHeadCntId));
		  totalHead.clear();
		  totalHead.sendKeys(String.valueOf(totalHeadCntVal));
		  totalHead.sendKeys(AndroidKeys.DPAD_DOWN);
		  
		  WebElement prePayed = driver.findElement(By.id(prePayedId));
		  prePayed.clear();
		  prePayed.sendKeys(String.valueOf(prePayedVal));
		  prePayed.sendKeys(AndroidKeys.DPAD_DOWN);
		  
		  WebElement disCountCheck = driver.findElement(By.id("CheckBox01"));
		  disCountCheck.click(); //TODO: Could not move next UI when it is enable.
		  for(int i=0; i<5; i++){
			  disCountCheck.sendKeys(AndroidKeys.DPAD_DOWN);
		  }
		  
		  WebElement disCountHeadCntHd = driver.findElement(By.id("EditText04"));
		  disCountHeadCntHd.sendKeys(String.valueOf(disCountHeadCnt));
		  disCountCheck.sendKeys(AndroidKeys.DPAD_RIGHT);
		  
		  WebElement disCountHeadPercentageHd = driver.findElement(By.id("EditText05"));
		  disCountHeadPercentageHd.sendKeys(String.valueOf(disCountPercentage));
		  
	  }
}
