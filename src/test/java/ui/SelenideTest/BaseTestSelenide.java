package ui.SelenideTest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.config.ConfigPrinter;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public abstract class BaseTestSelenide {

    @BeforeEach
    void setup() {

        // Выводим конфигурацию в консоль перед каждым тестом, кроме credentials
        ConfigPrinter.printConfig();

        // Настройка Selenide из конфига
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = ConfigProvider.getTimeout();
        Configuration.baseUrl = ConfigProvider.getBaseUrl();

        open("/");
    }

    @AfterEach
    void quitTests() {
        Selenide.closeWebDriver();
    }


    // Вспомогательный метод, чтобы не дублировать код клика кнопки
    private void performLogin(String username, String password) {
        $("[href='/admin']").click();
        $("#username").setValue(username);
        $("#password").setValue(password);
        $(".primary").click();
    }

    /**
     * Стандартный вход: берет логин и пароль из config.properties.
     * Используется в позитивных тестах.
     */
    protected void loginToAdmin() {

        String username = ConfigProvider.getAdminLogin();
        String password = ConfigProvider.getAdminPassword();
        performLogin(username, password);
    }

    /**
     * Вход с переданными параметрами.
     * Используется специально для негативных тестов (неверный логин/пароль).
     */
    protected void loginToAdminInvalid(String username, String password) {
        performLogin(username, password);
    }

    protected void deleteProduct(String productName) {

        String selector = "input[value*='" + productName + "']";
        $$(selector).shouldHave(sizeGreaterThan(0));

        SelenideElement inputField = $$(selector).first();
        SelenideElement row = inputField.closest("tr");

        // 6. Находим кнопку удаления ВНУТРИ этой строки и кликаем
        row.$("button[data-action='delete']")
                .shouldBe(enabled) // Ждем, пока кнопка станет активной
                .click();
        switchTo().alert().accept();

        $x("//*[@value='" + productName + "']").shouldNot(exist);
        System.out.println("Тестовые данные успешно удалены!");
    }
}