package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

// 2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.

public class GoodsAddAdminTest extends BaseTestSelenide {

    @Test
    void goodsAdd() {
        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin("admin", "secret123");

        // ************* ДОБАВЛЕНИЕ ТОВАРА В АДМИНКЕ *************

        $("#n-name").shouldBe(visible).click();

        $("#n-name").setValue("Кефир");

        // Вводим цену
        $("#n-price").setValue("60");

        // Нажимаем кнопку "Создать"
        $("#add-btn").click();

        // ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ ТОВАР *************
        $(byText("Вернуться на сайт")).click();

        // Проверяем, что товар отображается на витрине
        $("[data-name='Кефир']").shouldBe(visible);
        $("[data-name='Кефир']").shouldHave(text("Кефир"));
        System.out.println("Созданный товар есть на витрине сайта!");

        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        $("[href='/admin']").click();
        deleteProduct("Кефир");

    }
}