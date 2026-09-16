package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Selectors;
import ui.SelenideTest.config.ConfigProvider;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

// 2.4. Проверить сохранение товаров в корзине после обновления страницы.
// Так как товар не сохраняется в корзине, то проверяем, что товар отсутствует в корзине

public class NotSaveGoodsTest extends BaseTestSelenide {

    @Test
    void goodsAddAndRefresh() {

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
        $x("//div[@data-name='" + productName + "']//button[@data-action='add-to-cart']").click();
        $("#open-cart-btn").click();

        // Проверяем, что товар ЕСТЬ в корзине ДО рефреша
        $x("//div[@id='cart-items']//*[text()='" + productName + "']").shouldBe(visible);
        System.out.println("До обновления страницы в корзине есть товар!");

        // Закрываем модальное окно корзины
        $("#close-modal").click();

        // *********** РЕФРЕШИМ СТРАНИЦУ И ПРОВЕРЯЕМ КОРЗИНУ *************

        // Обновляем страницу
        Selenide.refresh();

        // Ждем загрузки body
        $("body").shouldBe(visible);

        // Нажимаем на кнопку корзины
        $("#open-cart-btn").click();

        // Проверяем отсутствие добавленного товара в корзине
        $x("//div[@id='cart-items']//*[text()='" + productName + "']").shouldNot(exist);
        System.out.println("Корзина после обновления страницы пустая!");


        // Закрываем модальное окно корзины
        $("#close-modal").click();

        // *********** ИДЕМ В АДМИНКУ И УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        $("[href='/admin']").click();
        deleteProduct(productName);
    }
}