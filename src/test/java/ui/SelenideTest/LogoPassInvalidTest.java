package ui.SelenideTest;

import org.junit.jupiter.api.Test;
import ui.SelenideTest.pages.LoginPage;

// Попытаться войти в админку с неверным логином и паролем.

public class LogoPassInvalidTest extends BaseTestSelenide {
    private final LoginPage loginPage = new LoginPage();

    @Test
    void invalidLoginTest() {

        // Вход в админку с неверными данными
        loginToAdminInvalid("invalidlogin", "invalidpassword");

        // Проверка результата: должно появиться сообщение об ошибке
        loginPage.assertLoginErrorVisible();
    }
}