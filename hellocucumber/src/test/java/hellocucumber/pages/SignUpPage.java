package hellocucumber.pages;

import hellocucumber.setup.Setup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignUpPage extends BasePage {

    @FindBy(id = "UserLogin_username")
    public WebElement userName;

    @FindBy(id = "UserLogin_password")
    public WebElement password;

    @FindBy(name = "yt0")
    public WebElement signUp;

    public SignUpPage(WebDriver driver) {
        super(driver);
    }
    public void loginAsStandartUser() throws InterruptedException {
        userName.sendKeys(Setup.getProps().getProperty("userName"));
        password.sendKeys(Setup.getProps().getProperty("password"));
        signUp.click();
    }
}
