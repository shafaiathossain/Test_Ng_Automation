package testrunner;

import com.github.javafaker.Faker;
import config.Setup;
import config.UserModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
//import pages.DashboardPage;
import pages.DashboardPage;
import pages.SignUppage;
import services.GmailServices;
import utils.Utils;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SignUpTestRunner extends Setup {
    @Test(priority = 1,description = "User can Signup With all data")
    public void doSignup() throws IOException, ParseException, ConfigurationException, org.apache.commons.configuration.ConfigurationException, InterruptedException, org.json.simple.parser.ParseException {
        driver.findElement(By.partialLinkText("Register")).click();
        SignUppage signupPage=new SignUppage(driver);
        Faker faker=new Faker();
        String firstName=faker.name().firstName();
        String lastName =faker.name().lastName();;
        String email="shafaiathossaincse+"+ Utils.generateRandomNumber(1000,9999)+"@gmail.com";
        String password="1234";
        String phoneNumber="015"+ Utils.generateRandomNumber(10000000,99999999) ;
        String address=faker.address().fullAddress();

        UserModel userModel=new UserModel();
        userModel.setFirstname(firstName);
        userModel.setLastname(lastName);
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setPhonenumber(phoneNumber);
        userModel.setAddress(address);
        signupPage.signup(userModel);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("firstName",firstName);
        jsonObject.put("lastName",lastName);
        jsonObject.put("email",email);
        jsonObject.put("password",password);
        jsonObject.put("phoneNumber",phoneNumber);
        jsonObject.put("address",address);
        JSONArray usersArray = Utils.readUserData("./src/test/resources/users.json");
        usersArray.add(jsonObject);

        Utils.saveUserData(usersArray, "./src/test/resources/users.json");
        Thread.sleep(5000);
        GmailServices gmailService=new GmailServices();
        String myEmail=gmailService.readLatestGmail();
        System.out.println("Received Email: " + myEmail);

        String responseMsg = "Dear Rosalinda, Welcome to our platform! We&#39;re excited to have you onboard. Best regards, Road to Career";
        Assert.assertTrue(responseMsg.replace("&#39;", "'").toLowerCase().contains("welcome"));

    }

    @Test(priority = 2,description = "User cannot reset pass with Invalid Email")
    public void typeWrongEmail() throws InterruptedException, IOException, org.json.simple.parser.ParseException {

        driver.findElements(By.tagName("a")).get(0).click();
        driver.findElement(By.cssSelector("[type=email]")).sendKeys("wronguser@gmail.com");
        driver.findElements(By.tagName("button")).get(0).click();
        String actualText = driver.findElements(By.tagName("p")).get(0).getText();
        String expectedText="Your email is not registered";
        Assert.assertEquals(actualText,expectedText);
        Thread.sleep(2000);

        driver.navigate().refresh();
        Thread.sleep(2000);

        WebElement emailInput = driver.findElement(By.cssSelector("[type=email]"));
        emailInput.click();
        emailInput.sendKeys(Keys.CONTROL+ "A");
        emailInput.sendKeys(Keys.BACK_SPACE);
        driver.findElements(By.tagName("button")).get(0).click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String validationMessage = (String) js.executeScript("return arguments[0].validationMessage;", emailInput);
        Assert.assertEquals(validationMessage, "Please fill out this field.");
    }
   @Test(priority = 3,description = "User Can Login With valid gmail")
    public void validEmail() throws InterruptedException, IOException, ConfigurationException, org.apache.commons.configuration.ConfigurationException, org.json.simple.parser.ParseException {
        driver.navigate().back();
        Thread.sleep(2000);
        driver.findElements(By.tagName("a")).get(0).click();
        String email = Utils.readEmailFromUserData("./src/test/resources/users.json");

        driver.findElement(By.cssSelector("[type=email]")).sendKeys(email);
        driver.findElements(By.tagName("button")).get(0).click();

        Thread.sleep(5000);
        GmailServices gmailService=new GmailServices();
        String myEmail=gmailService.readLatestGmail();
        System.out.println("Received Email: " + myEmail);

        String resetLink = myEmail.split("Click on the following link to reset your password: ")[1].trim();
        driver.get(resetLink);
        driver.findElements(By.cssSelector("input[type='password']")).get(0).sendKeys("123456");
        driver.findElements(By.cssSelector("input[type='password']")).get(1).sendKeys("123456");
        driver.findElement(By.tagName("button")).click();

        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.tagName("button")).click();
    }
    @Test(priority = 4,description = "User Can Added Item For all Fields")
    public void addItemAllFields() throws InterruptedException {
        Thread.sleep(5000);
        driver.findElements(By.tagName("button")).get(1).click();
        Thread.sleep(5000);
        driver.findElement(By.id("itemName")).sendKeys("Chocolate");
        driver.findElement(By.id("amount")).sendKeys("10");
        driver.findElement(By.id("purchaseDate")).sendKeys("07-07-2025");
        driver.findElement(By.id("month")).sendKeys("September");
        driver.findElement(By.id("remarks")).sendKeys("100");
        driver.findElements(By.tagName("button")).get(3).click();
        Thread.sleep(3000);
        driver.switchTo().alert().accept();
        Thread.sleep(3000);
    }
    @Test(priority = 5,description = "User Can Added Item For Mandatory Fields")
    public void addItemMandatoryField() throws InterruptedException {
        Thread.sleep(5000);
        driver.findElements(By.tagName("button")).get(1).click();
        Thread.sleep(5000);
        driver.findElement(By.id("itemName")).sendKeys("Ice Cream");
        driver.findElement(By.id("amount")).sendKeys("50");
        driver.findElements(By.tagName("button")).get(3).click();
        Thread.sleep(3000);
        driver.switchTo().alert().accept();
        Thread.sleep(3000);
    }
    @Test(priority = 6)
    public void updateGmail() throws InterruptedException, IOException, org.json.simple.parser.ParseException {
        Thread.sleep(1000);
        driver.findElement(By.tagName("svg")).click();
        driver.findElements(By.tagName("li")).get(0).click();
        Thread.sleep(1000);
        driver.findElements(By.tagName("button")).get(1).click();

        WebElement emailInput = driver.findElement(By.cssSelector("[type=email]"));
        JSONArray usersArray = Utils.readUserData("./src/test/resources/users.json");
        JSONObject lastUser = (JSONObject) usersArray.get(usersArray.size() - 1);
        String oldEmail = (String) lastUser.get("email");
        lastUser.put("oldEmail", oldEmail);
        emailInput.sendKeys(Keys.CONTROL + "a");
        emailInput.sendKeys(Keys.BACK_SPACE);
        Thread.sleep(500);
        String updatedEmail = "shafaiathossaincse+" + Utils.generateRandomNumber(1000, 9999) + "@gmail.com";
        emailInput.sendKeys(updatedEmail);
        driver.findElements(By.tagName("button")).get(2).click();
        Thread.sleep(3000);
        driver.switchTo().alert().accept();
        Thread.sleep(3000);

        lastUser.put("email", updatedEmail);
        Utils.saveUserData(usersArray, "./src/test/resources/users.json");
    }

    @Test(priority = 7,description = "User Can Logout Successfully")
    public void logOut(){
        DashboardPage dashboardPage=new DashboardPage(driver);
        dashboardPage.doLogout();
    }

    @Test(priority = 8, description = "Login with old email should fail after email update")
    public void loginWithOldEmailShouldFail() throws IOException, org.json.simple.parser.ParseException, InterruptedException {
        JSONArray usersArray = Utils.readUserData("./src/test/resources/users.json");
        JSONObject lastUser = (JSONObject) usersArray.get(usersArray.size() - 1);
        String oldEmail = (String) lastUser.get("oldEmail");

        driver.findElement(By.id("email")).sendKeys(oldEmail);
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.tagName("button")).click();

        Thread.sleep(2000);
        String errorMsg = driver.findElement(By.tagName("p")).getText();
        Assert.assertEquals(errorMsg, "Invalid email or password");
    }

    @Test(priority = 9, description = "Login with updated Gmail should succeed")
    public void loginWithUpdatedEmailShouldSucceed() throws IOException, ParseException, InterruptedException, org.json.simple.parser.ParseException {
        String updatedEmail = Utils.readEmailFromUserData("./src/test/resources/users.json");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(Keys.CONTROL + "a");
        emailField.sendKeys(Keys.BACK_SPACE);
        emailField.sendKeys(updatedEmail);

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(Keys.CONTROL + "a");
        passwordField.sendKeys(Keys.BACK_SPACE);
        passwordField.sendKeys("123456");

        driver.findElement(By.tagName("button")).click();
        Thread.sleep(2000);
    }

}



