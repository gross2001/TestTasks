package hellocucumber.setup;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class Setup {

    private static WebDriver driver;
    private static Properties testsProps;

   @BeforeAll
    public static void setupClass() {
       testsProps = readProps();

        if (testsProps.get("webDriverManager").equals("true")) {
            WebDriverManager.chromedriver().setup();
        } else {
            System.setProperty("webdriver.chrome.driver", testsProps.getProperty("webdriver.chrome.driver"));
        }
    }

    @Before
    public static void  startSession() {

        ChromeOptions capabilities = new ChromeOptions();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setImplicitWaitTimeout(Duration.ofSeconds(5));

        driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();

        driver.get(testsProps.getProperty("homePage"));
    }

    @After
    public static void tearDown() {
        if (driver != null)
            driver.quit();
    }

    public static WebDriver getDriver() {
        if (driver != null)
            return driver;
        return  null;
    }

    public static Properties readProps() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/tests.properties")) {
            props.load(input);
        }  catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }

    public static Properties getProps() {
        if (testsProps != null)
            return testsProps;
        return null;
    }

}
