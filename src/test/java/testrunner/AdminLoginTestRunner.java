package testrunner;

import config.CSVReader;
import config.Setup;
import config.UserModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.SignUppage;
import utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AdminLoginTestRunner extends Setup {
    @Test(priority =1,description = "Admin can login successfully with valid email and password",groups = "smoke")
    public void doLogin() throws IOException {
        LoginPage loginPage=new LoginPage(driver);
        if (System.getProperty("email")!=null && System.getProperty("password")!=null){
            loginPage.userLogin(System.getProperty("email"),System.getProperty("password"));
        }
        else
            loginPage.userLogin("admin@test.com","admin123");

        String txtHeaderActual=driver.findElement(By.tagName("h2")).getText();
        String txtHeaderExpected="Admin Dashboard";
        Assert.assertEquals(txtHeaderActual,txtHeaderExpected);
        Utils.getToken(driver);
    }
    @Test(priority = 2,description = "Admin Search Updated Gmail")
    public void searchByEmail() throws IOException, ParseException, InterruptedException {
        String updatedEmail = Utils.readEmailFromUserData("./src/test/resources/users.json");
        WebElement searchBox =driver.findElement(By.cssSelector("[type=text]"));
        searchBox.sendKeys(updatedEmail);
        driver.findElement(By.cssSelector("[type=text]")).click();
        Thread.sleep(3000);

        WebElement resultEmail = driver.findElement(By.xpath("//table//td[contains(text(),'" + updatedEmail + "')]"));
        Assert.assertTrue(resultEmail.isDisplayed(), " Email is found in admin dashboard!");
        DashboardPage dashboardPage=new DashboardPage(driver);
        dashboardPage.doLogout();
    }

    @Test(priority = 3, dataProvider = "CSVReader",dataProviderClass = CSVReader.class)
    public void registerUserFromCSV(String firstName, String lastName, String email,
                                    String password, String phoneNumber, String address) throws InterruptedException {
        driver.findElement(By.partialLinkText("Register")).click();

        UserModel userModel = new UserModel();
        userModel.setFirstname(firstName);
        userModel.setLastname(lastName);
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setPhonenumber(phoneNumber);
        userModel.setAddress(address);

        SignUppage signUpPage = new SignUppage(driver);
        signUpPage.signup(userModel);

        Thread.sleep(2000);
        driver.navigate().to("https://dailyfinance.roadtocareer.net/");
    }
    @Test(priority = 4)
    public void adminLoginAndExtractUsersTest() throws IOException, InterruptedException {
        driver.findElement(By.id("email")).sendKeys("admin@test.com");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.tagName("button")).click();

        Thread.sleep(2000);

        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));

        StringBuilder allUserData = new StringBuilder();
        for (WebElement row : rows) {
            allUserData.append(row.getText()).append("\n");
        }

        FileWriter writer = new FileWriter("./src/test/resources/all-users.txt");
        writer.write(allUserData.toString());
        writer.close();
        System.out.println("All users saved to file.");
    }



}

