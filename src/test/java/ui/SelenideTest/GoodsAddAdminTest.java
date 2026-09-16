package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

// 2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.

public class GoodsAddAdminTest extends BaseTestSelenide {

    @Test
    void goodsAdd() {

        long uniqueSuffix = System.nanoTime() % 1_000_000;
        String productName = ConfigProvider.getProductName() + "_" + uniqueSuffix;
        String productPrice = ConfigProvider.getProductPrice();

        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin();

        // ************* ДОБАВЛЕНИЕ ТОВАРА ДОРОЖЕ АДМИНКЕ *************
        $("#n-name").shouldBe(visible).click();
        $("#n-name").shouldBe(visible).setValue(productName);
        $("#n-price").shouldBe(visible).setValue(productPrice);
        $("#add-btn").click();

        // ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ ТОВАР *************
        $(byText("Вернуться на сайт")).click();

        // *********** ПРОВЕРЯЕМ НАЛИЧИЕ ТЕСТОВОГО ТОВАРА *************
        $("[data-name='" + productName + "']").shouldBe(visible);
        $("[data-name='" + productName + "']").shouldHave(text(productName));
        System.out.println("Созданный товар '" + productName + "' есть на витрине сайта!");

        // *********** ОТКРЫВАЕМ АДМИНКУ ***********
        $("[href='/admin']").click();

        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        deleteProduct(productName);

    }
}