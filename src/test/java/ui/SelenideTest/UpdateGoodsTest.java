package ui.SelenideTest;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;

import java.util.UUID;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

// 3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.

public class UpdateGoodsTest extends BaseTestSelenide {

    private final long uniqueSuffix = System.nanoTime() % 1_000_000;
    private final String originalProductName  = ConfigProvider.getProductName() + "_" + uniqueSuffix;
    private final String updatedProductName = originalProductName + "_updated";

    @Test
    void goodsAdd() {
        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin();

        // ************* ДОБАВЛЕНИЕ ТОВАРА В АДМИНКЕ *************

        $("#n-name").setValue(originalProductName);
        $("#n-price").setValue("100");
        $("#add-btn").click();
        $(".toast").shouldHave(text("Товар успешно добавлен!")).shouldBe(visible);
        System.out.println("Уведомление: Товар успешно добавлен!");

        // ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ ТОВАР *************
        $(byText("Вернуться на сайт")).click();

        // *********** ПРОВЕРЯЕМ НАЛИЧИЕ ТЕСТОВОГО ТОВАРА *************
        $("[data-name='" + originalProductName + "']").shouldBe(visible);
        $("[data-name='" + originalProductName + "']").shouldHave(text(originalProductName));
        System.out.println("Созданный товар '" + originalProductName + "' есть на витрине сайта!");

        // *********** ВОЗВРАЩАЕМСЯ В АДМИНКУ, ЧТОБЫ ИЗМЕНИТЬ ТОВАР *************
        $("[href='/admin']").shouldBe(visible).click();

        // Сначала находим поле по старому значению
        SelenideElement nameInput = $("[value='" + originalProductName + "']");
        nameInput.shouldBe(visible);

        //Запоминаем строку таблицы, в которой находится это поле
        SelenideElement row = nameInput.closest("tr");

        nameInput.click();
        nameInput.setValue(updatedProductName);

        row.$("[data-action='update']").click();

        // ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ ИЗМЕНЕННЫЙ ТОВАР *************
        $(byText("Вернуться на сайт")).click();
        $("[data-name='" + updatedProductName + "']").shouldBe(visible);
        $("[data-name='" + updatedProductName + "']").shouldHave(text(updatedProductName));
        System.out.println("Созданный товар '" + updatedProductName + "' есть на витрине сайта!");



        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        $("[href='/admin']").click();
        deleteProduct(updatedProductName);
    }
}