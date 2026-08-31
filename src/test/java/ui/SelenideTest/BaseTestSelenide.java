package ui.SelenideTest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public abstract class BaseTestSelenide {

    @BeforeEach
    void setup() {
        // Настройка Selenide
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000; // 10 секунд
        Configuration.baseUrl = "http://localhost:8080";

        open("/");
    }

    @AfterEach
    void quitTests() {
        Selenide.closeWebDriver();
    }

    protected void loginToAdmin(String username, String password) {
        $("[href='/admin']").click();

//        $("#username").clear();
        $("#username").setValue(username);

//        $("#password").clear();
        $("#password").setValue(password);

        $(".primary").click();
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