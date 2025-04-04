package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openqa.selenium.PageLoadStrategy.NONE;

public class SeleniumDriverTests {
    WebDriver driver;

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void chromeTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.setPlatformName("Windows 10"); //set remote OS
        chromeOptions.setBrowserVersion("123.0.6312.86");
        chromeOptions.setPageLoadStrategy(NONE);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setPageLoadTimeout(Duration.ofSeconds(60));
        chromeOptions.setScriptTimeout(Duration.ofSeconds(30));
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(5));
        driver = new ChromeDriver(chromeOptions);

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String html = driver.getPageSource();
        assertEquals("Hands-On Selenium WebDriver with Java", driver.getTitle());
        assertThat(html).contains("Hands-On Selenium WebDriver with Java");
    }

    @Test
    void iframeTest() {
        driver = new ChromeDriver();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/iframes.html");

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("lead")));

        WebElement iframeElement = driver.findElement(By.id("my-iframe"));
        driver.switchTo().frame(iframeElement);

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("display-6")));
        assertThat(driver.findElement(By.className("lead")).getText()).contains("Lorem ipsum dolor sit amet ");

        driver.switchTo().defaultContent();
        assertThat(driver.findElement(By.className("display-6")).getText()).contains("IFrame");
    }

    @Test
    void dialogBoxesTest() throws InterruptedException {
        driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");

        driver.findElement(By.id("my-alert")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("Hello world!");
        alert.accept();
        Thread.sleep(2000);

        driver.findElement(By.id("my-confirm")).click();
        driver.switchTo().alert().accept();
        assertThat(driver.findElement(By.id("confirm-text")).getText()).isEqualTo("You chose: true");
        driver.findElement(By.id("my-confirm")).click();
        driver.switchTo().alert().dismiss();
        assertThat(driver.findElement(By.id("confirm-text")).getText()).isEqualTo("You chose: false");
        Thread.sleep(2000);

        driver.findElement(By.id("my-prompt")).click();
        driver.switchTo().alert().sendKeys("Test");
        driver.switchTo().alert().accept();
        assertThat(driver.findElement(By.id("prompt-text")).getText()).isEqualTo("You typed: Test");
        driver.findElement(By.id("my-prompt")).click();
        driver.switchTo().alert().dismiss();
        assertThat(driver.findElement(By.id("prompt-text")).getText()).isEqualTo("You typed: null");
        Thread.sleep(2000);

        driver.findElement(By.id("my-modal")).click();
        WebElement save = driver.findElement(By.xpath("//button[normalize-space() = 'Save changes']"));
        wait.until(ExpectedConditions.elementToBeClickable(save));
        save.click();
        Thread.sleep(2000);
        assertThat(driver.findElement(By.id("modal-text")).getText()).isEqualTo("You chose: Save changes");
        driver.findElement(By.id("my-modal")).click();
        WebElement close = driver.findElement(By.xpath("//button[text() = 'Close']"));
        wait.until(ExpectedConditions.elementToBeClickable(close));
        close.click();
        assertThat(driver.findElement(By.id("modal-text")).getText()).isEqualTo("You chose: Close");
        Thread.sleep(2000);
    }

    @Test
    void navigateTest() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        Thread.sleep(2000);
        driver.navigate().to("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        Thread.sleep(2000);
        driver.navigate().back();
        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/", driver.getCurrentUrl());
        Thread.sleep(2000);
        driver.navigate().forward();
        Thread.sleep(2000);
        driver.navigate().refresh();
        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/web-form.html", driver.getCurrentUrl());
    }

    @Test
    void testNewTab() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String initHandle = driver.getWindowHandle();
        Thread.sleep(2000);

        driver.switchTo().newWindow(WindowType.TAB);
        Thread.sleep(2000);
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        assertThat(driver.getWindowHandles()).hasSize(2);
        Thread.sleep(2000);

        driver.switchTo().window(initHandle);
        Thread.sleep(2000);
        driver.close();
        assertThat(driver.getWindowHandles()).hasSize(1);
    }

    @Test
    void testNewWindow() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String initHandle = driver.getWindowHandle();
        Thread.sleep(2000);

        driver.switchTo().newWindow(WindowType.WINDOW);
        Thread.sleep(2000);
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        assertThat(driver.getWindowHandles()).hasSize(2);
        Thread.sleep(2000);

        driver.switchTo().window(initHandle);
        Thread.sleep(2000);
        driver.close();
        assertThat(driver.getWindowHandles()).hasSize(1);
    }

    @Test
    void WindowTest() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        WebDriver.Window window = driver.manage().window();

        Point initialPosition = window.getPosition();
        Dimension initialSize = window.getSize();
        System.out.printf("Initial window: position {%s } -- size {%s}\n ", initialPosition, initialSize);
        Thread.sleep(2000);

        window.maximize();

        Point maximizedPosition  = window.getPosition();
        Dimension maximizedSize  = window.getSize();
        System.out.printf("Maximized window: position {%s} -- size {%s}\n", maximizedPosition, maximizedSize);
        Thread.sleep(2000);

        assertThat(initialPosition).isNotEqualTo(maximizedPosition);
        assertThat(initialSize).isNotEqualTo(maximizedSize);
    }
}
