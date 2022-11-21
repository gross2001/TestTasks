package hellocucumber;

import hellocucumber.pages.SignUpPage;
import hellocucumber.pages.UsersPage;
import hellocucumber.setup.Setup;
import io.cucumber.java.ru.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.*;
import java.util.stream.Collectors;

public class StepDefinitions {
    private static SignUpPage signUpPage;
    private static UsersPage usersPage;

    public StepDefinitions() {
        signUpPage = PageFactory.initElements(Setup.getDriver(), SignUpPage.class);
        usersPage = PageFactory.initElements(Setup.getDriver(), UsersPage.class);
    }

    @Дано("авторизоваться под стандартным именем и паролем")
    public void loginAsStandartUser() throws InterruptedException {
        signUpPage.loginAsStandartUser();
    }

    @Дано("нажать на ссылку {string}")
    public void goByLink(String link) throws InterruptedException {
        signUpPage.goByLink(link);
    }

    @Тогда("проверить сортировку столбца {string}")
    public void checkColumn(String columnName) {
        List<WebElement> column = usersPage.findElements(columnName);
        List<String> columnText = column.stream().map(webElement -> webElement.getText()).collect(Collectors.toList());
        Assertions.assertTrue(
                columnText.stream().sorted().collect(Collectors.toList()).equals(columnText)
        );
    }
}