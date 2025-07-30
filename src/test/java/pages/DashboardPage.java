package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DashboardPage {
    @FindBy(css = "[data-testid=AccountCircleIcon]")
    WebElement btnProfileIcon;
    @FindBy(tagName = "li")
    List<WebElement > comboMenu;

    public DashboardPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    public void doLogout(){
        btnProfileIcon.click();
        comboMenu.get(1).click();
    }

}
