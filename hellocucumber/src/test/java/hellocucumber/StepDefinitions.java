package hellocucumber;

import hellocucumber.pages.SignUpPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.ru.*;

import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;

public class StepDefinitions {

    static WebDriver driver;
    private static SignUpPage signUpPage;
    private static Properties testsProps;

    @BeforeAll
    public static void readProps() {
        testsProps = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/tests.properties")) {
            testsProps.load(input);
        }  catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Before
    public static void  startSession() {
        System.setProperty("webdriver.chrome.driver", testsProps.getProperty("webdriver.chrome.driver"));

        ChromeOptions capabilities = new ChromeOptions();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setImplicitWaitTimeout(Duration.ofSeconds(5));

        driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();

        driver.get(testsProps.getProperty("homePage"));
    }

    @After
    public static void endSession() {
        driver.close();
    }

    @Дано("авторизоваться под стандартным именем и паролем")
    public void loginAsStandartUser() throws InterruptedException {

        SignUpPage signUpPage = PageFactory.initElements(driver, SignUpPage.class);

        signUpPage.userName.sendKeys(testsProps.getProperty("userName"));
        signUpPage.password.sendKeys(testsProps.getProperty("password"));
        signUpPage.signUp.click();

    }

    @Дано("нажать на ссылку {string}")
    public void goByLink(String link) throws InterruptedException {
//        driver.findElement(By.partialLinkText(link)).click();
        String updatelink = driver.findElement(By.partialLinkText(link)).getAttribute("href");
        driver.get(updatelink);
    }



    @Тогда("проверить сортировку столбца {string}")
    public void checkColumn(String columnName) {
        String xpath = "//table/tbody/tr/td[count(//table/thead/tr/th[.=\"" + columnName + "\"]/preceding-sibling::th)+1]";
        List<WebElement> column = driver.findElements(By.xpath(xpath));
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