package pages;

import config.UserModel;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static io.restassured.RestAssured.given;

public class SignUppage {
    @FindBy(id = "firstName")
    WebElement txtfirstName;
    @FindBy(id = "lastName")
    WebElement txtlastName;
    @FindBy(id = "email")
    WebElement txtemail;
    @FindBy(id = "password")
    WebElement txtpassword;
    @FindBy(id = "phoneNumber")
    WebElement txtphoneNumber;
    @FindBy(id = "address")
    WebElement txtaddress;
    @FindBy(css="[type=radio]")
    List<WebElement> rbGender;
    @FindBy(css="[type=checkbox]")
    WebElement chkTerms;
    @FindBy(id="register")
    WebElement btnRegister;

    public SignUppage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }
    public void signup(UserModel userModel){
        txtfirstName.sendKeys(userModel.getFirstname());
        txtlastName.sendKeys(userModel.getLastname()!=null?userModel.getLastname():"");
        txtemail.sendKeys(userModel.getEmail());
        txtpassword.sendKeys(userModel.getPassword());
        txtphoneNumber.sendKeys(userModel.getPhonenumber());
        txtaddress.sendKeys(userModel.getAddress()!=null?userModel.getAddress():"");
        rbGender.get(0).click();
        chkTerms.click();
        btnRegister.click();
    }

}
