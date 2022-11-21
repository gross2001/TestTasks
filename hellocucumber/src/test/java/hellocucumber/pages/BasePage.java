package hellocucumber.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public void goByLink(String link) throws InterruptedException {
        String updatelink = driver.findElement(By.partialLinkText(link)).getAttribute("href");
        driver.get(updatelink);
    }
}
