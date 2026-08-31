package ui.SelenideTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static com.codeborne.selenide.Selenide.*;

// 3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.

public class CartTotalPriceTest extends BaseTestSelenide {

    @Test
    void CartTotalPrice() {

    // ************* ВХОД В АДМИНКУ *************
    loginToAdmin("admin", "secret123");

    int count = 3;
    int sum = 0;

    for (int i = 1; i <= count; i++) {
            $("#n-name").shouldBe(visible);
            $("#n-name").setValue("Товар-" + i);
            int price = 50 + i;
            $("#n-name").shouldBe(visible, enabled);
            $("#n-price").setValue("" + price);
            $("#add-btn").click();
            sum = sum + price;
    }

    System.out.println("Сумма товаров " + sum);

    $x("//a[normalize-space()='Вернуться на сайт']").click();

    // 3. Проверяем и добавляем эти три товара в корзину
        for (int i = 1; i <= count; i++) {
            String good = "Товар-" + i;
            $x("//*[@data-name='" + good + "']").shouldBe(visible);
            $("button[data-name='" + good + "']").shouldBe(visible).click();
        }

        // Нажимаем на кнопку корзины
        $("#open-cart-btn")
                .shouldBe(visible)
                .click();

        $$(".cart-item")
                .shouldHave(size(count))
                .filterBy(text("Товар-"))
                .shouldBe();

        int totalPrice = Integer.parseInt($("#total-price").getText());
        System.out.println("Сумма товаров в корзине " + totalPrice);

        if (totalPrice == sum ) {
            System.out.println("Сумма товаров в корзине верная!");
        } else {
            throw new AssertionError("Сумма товаров (" + totalPrice + ") не соответствует ожидаемому значению");
        }

        // Закрываем модальное окно корзины
        $("#close-modal").click();

        // Отрываем админку
        $("[href='/admin']").click();

        for (int i = 1; i <= count; i++) {

            String productName = "Товар-" + i;

            String selector = "input[value*='" + productName + "']";
            $$(selector).shouldHave(sizeGreaterThan(0));

            SelenideElement inputField = $$(selector).first();
            SelenideElement row = inputField.closest("tr");

            // Находим кнопку удаления ВНУТРИ этой строки и кликаем
            row.$("button[data-action='delete']")
                    .shouldBe(enabled)
                    .click();
            switchTo().alert().accept();

            // Проверяем, что товар удален
            $(selector).shouldNot(exist);
            $$(selector).shouldHave(size(0));
            System.out.println("Тестовые данные успешно удалены!");
        }

    }
}
