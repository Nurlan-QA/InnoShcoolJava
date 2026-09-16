package ui.SelenideTest;

import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

// 3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.

public class GoodAddAlertTest extends BaseTestSelenide {

    private final long uniqueSuffix = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + uniqueSuffix;
    private final String productPrice = ConfigProvider.getProductPrice();

    @Test
    void goodsAdd() {

        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin();

        // ************* ДОБАВЛЕНИЕ ТОВАРА ДОРОЖЕ В АДМИНКЕ *************
        $("#n-name").shouldBe(visible).click();
        $("#n-name").shouldBe(visible).setValue(productName);
        $("#n-price").shouldBe(visible).setValue(productPrice);
        $("#add-btn").click();


        // Проверяем наличие тостера об успешном оформлении
        $(".toast").shouldHave(text("Товар успешно добавлен!")).shouldBe(visible);
        System.out.println("Уведомление: Товар успешно добавлен!");

        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************

        deleteProduct(productName);
    }
}