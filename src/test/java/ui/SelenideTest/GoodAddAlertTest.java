package ui.SelenideTest;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

// 3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.

public class GoodAddAlertTest extends BaseTestSelenide {

    @Test
    void goodsAdd() {
        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin("admin", "secret123");

        // ************* ДОБАВЛЕНИЕ ТОВАРА В АДМИНКЕ *************

        $("#n-name").shouldBe(visible).click();

        $("#n-name").setValue("Товар");

        // Вводим цену
        $("#n-price").setValue("100");

        // Нажимаем кнопку "Создать"
        $("#add-btn").click();

        // Проверяем наличие тостера об успешном оформлении
        $(".toast").shouldHave(text("Товар успешно добавлен!")).shouldBe(visible);
        System.out.println("Уведомление: Товар успешно добавлен!");

        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************

        deleteProduct("Товар");
    }
}