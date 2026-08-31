package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Selectors;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

// 2.4. Проверить сохранение товаров в корзине после обновления страницы.
// Так как товар не сохраняется в корзине, то проверяем, что товар отсутствует в корзине

public class NotSaveGoodsTest extends BaseTestSelenide {

    @Test
    void goodsAddAndRefresh() {
        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin("admin", "secret123");

        // ************* ДОБАВЛЕНИЕ ТОВАРА В АДМИНКЕ *************
        $("#n-name").shouldBe(visible);
        $("#n-name").setValue("Кефир");
        $("#n-price").setValue("60");
        $("#add-btn").click();

        // ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ НАЛИЧИЕ ТОВАРА *************
        $x("//a[normalize-space()='Вернуться на сайт']").click();
        $x("//*[@data-name='Кефир']").shouldBe(visible);

        // ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ И ПРОВЕРЯЕМ *************
        $x("//div[@data-name='Кефир']//button[@data-action='add-to-cart']").click();
        $("#open-cart-btn").click();

        // Проверяем, что товар ЕСТЬ в корзине ДО рефреша
        $x("//div[@id='cart-items']//*[text()='Кефир']").shouldBe(visible);
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
        $x("//div[@id='cart-items']//*[text()='Кефир']").shouldNot(exist);
        System.out.println("Корзина после обновления страницы пустая!");


        // Закрываем модальное окно корзины
        $("#close-modal").click();

        // *********** ИДЕМ В АДМИНКУ И УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        $("[href='/admin']").click();
        deleteProduct("Кефир");
    }
}