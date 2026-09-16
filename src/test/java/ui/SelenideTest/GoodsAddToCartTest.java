package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

// 2.2. Добавить товар в корзину и проверить, что он отображается.

public class GoodsAddToCartTest extends BaseTestSelenide {

    @Test
    void goodsAddInCart() {

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

        // ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ НАЛИЧИЕ ТОВАРА *************
        $x("//a[normalize-space()='Вернуться на сайт']").click();

        // *********** ПРОВЕРЯЕМ НАЛИЧИЕ ТЕСТОВОГО ТОВАРА *************
        $("[data-name='" + productName + "']").shouldBe(visible);
        $("[data-name='" + productName + "']").shouldHave(text(productName));
        System.out.println("Созданный товар '" + productName + "' есть на витрине сайта!");

        // ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ И ПРОВЕРЯЕМ *************
        // Нажимаем на кнопку 'В корзину' у нашего товара
        $("button[data-name='" + productName + "']").click();

        // Нажимаем на кнопку корзины
        $("#open-cart-btn").click();

        // Проверяем наличие добавленного товара в корзине
        $(".cart-item").shouldHave(text(productName));

        // Проверка, что корзина не пуста
        $x("//div[@id='cart-items']//*[text()='" + productName + "']").shouldBe(visible);
        System.out.println("Добавленный товар в корзину отображается в ней!");

        // Закрываем модальное окно корзины
        $("#close-modal").click();

        // *********** ОТКРЫВАЕМ АДМИНКУ ***********
        $("[href='/admin']").click();

        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        deleteProduct(productName);

    }
}