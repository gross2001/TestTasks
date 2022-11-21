package hellocucumber.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class UsersPage extends BasePage {

    public UsersPage(WebDriver driver) {
        super(driver);
    }

    public List<WebElement> findElements(String columnName) {
        String xpath = "//table/tbody/tr/td[count(//table/thead/tr/th[.=\"" + columnName + "\"]/preceding-sibling::th)+1]";
        List<WebElement> column = driver.findElements(By.xpath(xpath));
        return  column;
    }

}
