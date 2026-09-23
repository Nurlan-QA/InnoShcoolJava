package ui.SelenideTest;


import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.pages.LoginPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

// 2.3. Попытаться войти в админку с неверным логином и паролем.

public class LogoPassInvalidTest extends BaseTestSelenide {
    private final LoginPage loginPage = new LoginPage();

    @Test
    void invalidLoginTest() {
        // ************* ВХОД В АДМИНКУ С НЕВЕРНЫМИ ДАННЫМИ *************
        loginToAdminInvalid("invalidlogin", "invalidpassword");

        // Проверка результата: должно появиться сообщение об ошибке
        loginPage.assertLoginErrorVisible();
        System.out.println("Аутентификация ползователя неуспешная: \nНеверные учетные данные пользователя!");

    }
}