package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

// 2.2. Добавить товар в корзину и проверить, что он отображается.

public class GoodsAddToCartTest extends BaseTestSelenide {

    @Test
    void goodsAddInCart() {
        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin("admin", "secret123");

        // ************* ДОБАВЛЕНИЕ ТОВАРА В АДМИНКЕ *************
        $("#n-name").shouldBe(visible);
        $("#n-name").setValue("Кефир");
        $("#n-price").setValue("60");
        $("#add-btn").click();

        // ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ НАЛИЧИЕ ТОВАРА *************
        $x("//a[normalize-space()='Вернуться на сайт']").click();

        // Ждем, пока элемент с data-name='Кефир' появится в DOM и будет видимым
        $x("//*[@data-name='Кефир']").shouldBe(visible);

        // ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ И ПРОВЕРЯЕМ *************
        // Нажимаем на кнопку 'В корзину' у нашего товара
        $("button[data-name='Кефир']").click();

        // Нажимаем на кнопку корзины
        $("#open-cart-btn").click();

        // Проверяем наличие добавленного товара в корзине
        $(".cart-item").shouldHave(text("Кефир"));
        // Проверка, что корзина не пуста
        $x("//div[@id='cart-items']//*[text()='Кефир']").shouldBe(visible);
        System.out.println("Добавленный товар в корзину отображается в ней!");


        // Закрываем модальное окно корзины
        $("#close-modal").click();

        // *********** УДАЛЕНИЕ ТОВАРА *************
        $("[href='/admin']").click();
        deleteProduct("Кефир");

    }
}