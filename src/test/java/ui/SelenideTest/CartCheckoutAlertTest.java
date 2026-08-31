package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

// 2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ».
// Проверить, что отображается JS Alert.

public class CartCheckoutAlertTest extends BaseTestSelenide {

    @Test
    void checkoutWithExpensiveItem() {

        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin("admin", "secret123");

        // ************* ДОБАВЛЕНИЕ ТОВАРА ДОРОЖЕ 300 РУБ В АДМИНКЕ *************
        $("#n-name").shouldBe(visible).setValue("Телевизор");
        $("#n-price").shouldBe(visible).setValue("350");
        $("#add-btn").click();

        // ************* ВОЗВРАЩАЕМСЯ НА САЙТ И ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ *************
        $(byText("Вернуться на сайт")).click();
        $(byAttribute("data-name", "Телевизор")).shouldBe(visible);

        $(byAttribute("data-name", "Телевизор"))
                .find("button[data-action='add-to-cart']")
                .click();

        // ************* ОТКРЫВАЕМ КОРЗИНУ И ПЫТАЕМСЯ ОФОРМИТЬ ТОВАР *************
        $("#open-cart-btn").shouldBe(visible).click();
        $(withText("Оформить заказ")).shouldBe(visible).click();


        var alert = Selenide.switchTo().alert();
        String alertText = alert.getText();
        System.out.println("Получен алерт: " + alertText);
        alert.accept();

        // *********** ЗАКРЫВАЕМ МОДАЛЬНОЕ ОКНО КОРЗИНЫ *************
        $("#close-modal").click();

        // *********** ОТКРЫВАЕМ АДМИНКУ *************
        $("[href='/admin']").click();

        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        deleteProduct("Телевизор");
    }
}
