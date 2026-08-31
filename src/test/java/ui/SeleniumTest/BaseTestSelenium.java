package ui.SeleniumTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseTestSelenium {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void quitTests() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void loginToAdmin(String username, String password) {
        driver.findElement(By.xpath("//*[@href='/admin']")).click();

        WebElement usernameField = driver.findElement(By.xpath("//*[@id='username']"));
        usernameField.clear();
        usernameField.sendKeys(username);

        WebElement passwordField = driver.findElement(By.xpath("//*[@id='password']"));
        passwordField.clear();
        passwordField.sendKeys(password);

        driver.findElement(By.xpath("//*[@class='primary']")).click();
    }

    protected void deleteProduct(String productName) {
        driver.findElement(By.xpath("//*[@href='/admin']")).click();
        driver.findElement(By.xpath("//tr[.//input[@value='" + productName + "']]//button[@data-action='delete']")).click();

        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());
        alert.accept();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@value='" + productName + "']")));
    }
}