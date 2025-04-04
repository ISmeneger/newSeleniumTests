package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SeleniumWaitTests {
    WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @BeforeEach
    void start() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void loadingImagesImplicitWaitTest() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/loading-images.html");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement landscape = driver.findElement(By.id("landscape"));
        assertThat(landscape.getAttribute("src")).containsIgnoringCase("landscape");
    }

    @Test
    void loadingImagesExplicitWaitTest() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/loading-images.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//        WebElement landscape = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("landscape")));
        List<WebElement> images =  wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//div[@id = 'image-container']/img"), 4));
//        assertThat(images.getAttribute("src")).containsIgnoringCase("landscape");
//        assertThat(images.size();
//        assertEquals(4, images);
        assertEquals(4, images.size());
        assertThat(images).hasSize(4);
    }

    @Test
    void loadingImagesFluentWaitTest() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/loading-images.html");
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement landscape = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#landscape")));
        assertThat(landscape.getAttribute("src")).containsIgnoringCase("landscape");
    }

    @Test
    void checkSlowCalcSumTest() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");

        //locators
        By calcButtonLocator = By.xpath("//a[text() = 'Slow calculator']");
        By oneButtonLocator = By.xpath("//div[@class = 'keys']/span[text() = '1']");
        By plusButtonLocator = By.xpath("//div[@class = 'keys']/span[text() = '+']");
        By equalButtonLocator = By.xpath("//div[@class = 'keys']/span[text() = '=']");
        By resultField = By.xpath("//div[@class = 'screen']");

        //actions
        driver.findElement(calcButtonLocator).click();
        driver.findElement(oneButtonLocator).click();
        driver.findElement(plusButtonLocator).click();
        driver.findElement(oneButtonLocator).click();
        driver.findElement(equalButtonLocator).click();

        //assertions
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBe(resultField, "2"));
    }
}
