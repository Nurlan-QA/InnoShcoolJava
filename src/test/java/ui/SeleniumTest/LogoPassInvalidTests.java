package ui.SeleniumTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

//1.3. Попытаться войти в админку с неверным логином и паролем.

public class LogoPassInvalidTests extends BaseTestSelenium {

    @Test
    void invalidLoginTest() {


//        ************* ВХОД В АДМИНКУ *************

//        Передаем невалидные логин и пароль
        loginToAdmin("invalidlogin", "invalidpassword");

        WebElement nameGoodsField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Неверные учетные данные пользователя']")));

//        Проверка результата
        Assertions.assertThat(driver.findElement(By.xpath("//*[text()='Неверные учетные данные пользователя']"))
                        .isDisplayed())
                .as("Должно отображаться сообщение о неверных учетных данных")
                .isTrue();
    }
}
