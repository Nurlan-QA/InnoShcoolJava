package ui.SelenideTest.pages;

import com.codeborne.selenide.SelenideElement;
import ui.SelenideTest.config.ConfigProvider;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProductCleanup {

    private final LoginPage loginPage = new LoginPage();
    private static final int COUNT = 3;

    /**
     * Удаляет тестовые товары Notebook_1, Notebook_2, Notebook_3 из таблицы в админке.
     */
    public void removeTestProducts() {
        for (int i = 1; i <= COUNT; i++) {
            String productName = ConfigProvider.getProductName() + "_" + i;
            removeProductByName(productName);
        }
    }

    /**
     * Удаляет товар с произвольным именем из таблицы в админке.
     * Сам открывает /admin, логинится и ждёт загрузку таблицы.
     */
    public void removeProductByName(String productName) {
        open("/admin");

        SelenideElement usernameField = $("#username");

        if (usernameField.isDisplayed()) {
            loginPage.enterLogin(ConfigProvider.getAdminLogin());
            loginPage.enterPassword(ConfigProvider.getAdminPassword());
            loginPage.clickLoginButton();

            $("#tbody").shouldBe(visible, Duration.ofSeconds(10));
        }

        $("#tbody").shouldBe(visible, Duration.ofSeconds(5));

        String selector = "input[value='" + productName + "']";

        SelenideElement inputField = $(selector);
        if (!inputField.exists()) {
            System.out.println("Товар '" + productName + "' не найден, пропускаем.");
            return;
        }

        SelenideElement row = inputField.closest("tr");

        row.$("button[data-action='delete']")
                .shouldBe(enabled)
                .click();

        confirm();

        $(selector).shouldNot(exist, Duration.ofSeconds(5));
        System.out.println("Товар '" + productName + "' успешно удалён!");
    }
}
