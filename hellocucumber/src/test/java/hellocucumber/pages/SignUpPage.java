package hellocucumber.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {

    private WebDriver driver;

    @FindBy(id = "UserLogin_username")
    public WebElement userName;

    @FindBy(id = "UserLogin_password")
    public WebElement password;

    @FindBy(name = "yt0")
    public WebElement signUp;

    public SignUpPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
