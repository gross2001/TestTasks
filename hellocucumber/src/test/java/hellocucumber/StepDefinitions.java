package hellocucumber;

import hellocucumber.pages.SignUpPage;
import io.cucumber.java.ru.*;

import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

public class StepDefinitions {
    private static SignUpPage signUpPage;

    @Дано("авторизоваться под стандартным именем и паролем")
    public void loginAsStandartUser() throws InterruptedException {

        SignUpPage signUpPage = PageFactory.initElements(Setup.getDriver(), SignUpPage.class);

        signUpPage.userName.sendKeys(Setup.getProps().getProperty("userName"));
        signUpPage.password.sendKeys(Setup.getProps().getProperty("password"));
        signUpPage.signUp.click();
    }

    @Дано("нажать на ссылку {string}")
    public void goByLink(String link) throws InterruptedException {
//        driver.findElement(By.partialLinkText(link)).click();
        String updatelink = Setup.getDriver().findElement(By.partialLinkText(link)).getAttribute("href");
        Setup.getDriver().get(updatelink);
    }

    @Тогда("проверить сортировку столбца {string}")
    public void checkColumn(String columnName) {
        String xpath = "//table/tbody/tr/td[count(//table/thead/tr/th[.=\"" + columnName + "\"]/preceding-sibling::th)+1]";
        List<WebElement> column = Setup.getDriver().findElements(By.xpath(xpath));
        List<String>columnText = new ArrayList<String>();

        for (WebElement element : column) {
            columnText.add(element.getText());
            System.out.println(element.getText());
        }

        List<String>sorted = new ArrayList<String>(columnText);
        Collections.sort(sorted);

        Assertions.assertIterableEquals(columnText, sorted);
    }
}