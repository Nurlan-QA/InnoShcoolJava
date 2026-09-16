package ui.SelenideTest;

import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.config.ConfigPrinter;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

// 3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.

public class CartTotalPriceTest extends BaseTestSelenide {

    // Количество товаров для теста
    private static final int COUNT = 3;

    // Базовая цена из конфига. Итоговая цена будет basePrice + i
    private final int basePriceFromConfig = Integer.parseInt(ConfigProvider.getProductPrice());

    @Test
    void cartTotalPrice() {
        String baseProductName = ConfigProvider.getProductName();

        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin();

        int sum = 0;

        // ************* ДОБАВЛЕНИЕ ТОВАРОВ В АДМИНКЕ *************
        for (int i = 1; i <= COUNT; i++) {
            // Формируем уникальное имя: Notebook_1, Notebook_2...
            String currentProductName = baseProductName + "_" + i;

            // Рассчитываем цену: базовая из конфига + смещение
            int price = basePriceFromConfig + i;
            sum += price;

            $("#n-name")
                    .shouldBe(visible)
                    .setValue(currentProductName);

            $("#n-price")
                    .shouldBe(visible)
                    .setValue(String.valueOf(price));

            $("#add-btn").click();

            System.out.println("Добавлен товар: " + currentProductName + ", цена: " + price);
        }

        System.out.println("Ожидаемая сумма (расчет): " + sum);

        // ************* ВОЗВРАТ НА САЙТ И ДОБАВЛЕНИЕ В КОРЗИНУ *************
        $x("//a[normalize-space()='Вернуться на сайт']").click();

        for (int i = 1; i <= COUNT; i++) {
            String goodName = baseProductName + "_" + i;

            // Проверка наличия товара на витрине
            $("[data-name='" + goodName + "']").shouldBe(visible);

            // Добавление в корзину
            $("button[data-name='" + goodName + "']")
                    .shouldBe(visible)
                    .click();
        }

        // ************* ПРОВЕРКА СУММЫ В КОРЗИНЕ *************
        $("#open-cart-btn")
                .shouldBe(visible)
                .click();

        $$(".cart-item")
                .shouldHave(size(COUNT))
                .filterBy(text(baseProductName)) // Проверяем, что в корзине товары с нашим префиксом
                .shouldBe();

        String totalPriceText = $("#total-price").getText();
        int totalPrice = Integer.parseInt(totalPriceText);

        System.out.println("Сумма товаров в корзине (UI): " + totalPrice);

        if (totalPrice == sum) {
            System.out.println("✅ Сумма товаров в корзине верная!");
        } else {
            throw new AssertionError(
                    "❌ Сумма товаров (" + totalPrice + ") не соответствует ожидаемому значению (" + sum + ")"
            );
        }

        // Закрываем модальное окно корзины
        $("#close-modal").click();

        // ************* ОЧИСТКА ДАННЫХ (Удаление товаров) *************
        $("[href='/admin']").click(); // Переход в админку (логин уже выполнен в начале)

        for (int i = 1; i <= COUNT; i++) {
            String productName = baseProductName + "_" + i;
            String selector = "input[value*='" + productName + "']";

            $$(selector).shouldHave(sizeGreaterThan(0));

            SelenideElement inputField = $$(selector).first();
            SelenideElement row = inputField.closest("tr");

            row.$("button[data-action='delete']")
                    .shouldBe(enabled)
                    .click();

            switchTo().alert().accept();

            $(selector).shouldNot(exist);
            System.out.println("Товар '" + productName + "' успешно удален.");
        }
    }
}
