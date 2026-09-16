package ui.SelenideTest;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;


public class DragAndDropTest extends BaseTestSelenide {

    private final long uniqueSuffix = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + uniqueSuffix;
    private final String productPrice = ConfigProvider.getProductPrice();

    // Задаем элементы для DnD
    SelenideElement testProductCart = $(byAttribute("data-name", productName));
    SelenideElement cardButton = $x("//*[@id = 'open-cart-btn']");

    @Test
    void checkoutWithExpensiveItem() {

    // ************* ВХОД В АДМИНКУ *************
    loginToAdmin();

    // ************* ДОБАВЛЕНИЕ ТЕСТОВОГО ТОВАРА В АДМИНКЕ *************
    $("#n-name").shouldBe(visible).setValue(productName);
    $("#n-price").shouldBe(visible).setValue(productPrice);
    $("#add-btn").click();

    // *********** ВОЗВРАЩАЕМСЯ НА САЙТ *************
    $(byText("Вернуться на сайт")).click();

    // *********** ПРОВЕРЯЕМ НАЛИЧИЕ ТЕСТОВОГО ТОВАРА *************
    $("[data-name='" + productName + "']").shouldBe(visible);
    $("[data-name='" + productName + "']").shouldHave(text(productName));
    System.out.println("Созданный товар '" + productName + "' есть на витрине сайта!");

    // *********** ДОБАВЛЯЕМ ТЕСТОВЫЙ ТОВАР МЕТОДОМ Drag-And-Drop *************
        testProductCart.dragAndDrop(DragAndDropOptions.to(cardButton));
        testProductCart.dragAndDrop(DragAndDropOptions.to(cardButton));

    // *********** ПРОВЕРЯЕМ, ЧТО ТОВАР ДОБАВИЛСЯ В КОРЗИНУ В КОЛИЧЕСТВЕ ДВУХ ШТУК *************
        cardButton.shouldBe(visible);
        $("#cart-count").shouldHave(text("2"));
        System.out.println("Товар добавлен в корзину в количестве 2 штук!");

    // *********** ОТКРЫВАЕМ АДМИНКУ ***********
        $("[href='/admin']").click();

    // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        deleteProduct(productName);
    }
}
