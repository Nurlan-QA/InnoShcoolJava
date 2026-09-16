package ui.SelenideTest;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class CartRemovalTest extends BaseTestSelenide {

    private final long uniqueSuffix = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + uniqueSuffix;
    private final String productPrice = ConfigProvider.getProductPrice();

    // Задаем элементы для DnD
    SelenideElement testProductCart = $(byAttribute("data-name", productName));
    SelenideElement cardButton = $x("//*[@id = 'open-cart-btn']");

    @Test
    void cartRemoval() {

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
        sleep(1000);
        testProductCart.dragAndDrop(DragAndDropOptions.to(cardButton));
        sleep(2000);

        // *********** ПРОВЕРЯЕМ, ЧТО ТОВАР ДОБАВИЛСЯ В КОРЗИНУ В КОЛИЧЕСТВЕ ОДНОЙ ШТУКИ *************
        cardButton.shouldBe(visible);
        $("#cart-count").shouldHave(text("1"));
        System.out.println("Товар добавлен в корзину в количестве одной штуки!");

        // *********** ОТКРЫВАЕМ КОРЗИНУ *************
        $("#open-cart-btn").shouldBe(visible).click();
        // Ждём, пока в корзине появится хотя бы один товар
        $(".cart-item").shouldBe(visible);

        // ОТЛАДКА: выводим текст всех элементов корзины
        $$(".cart-item").forEach(item ->
                System.out.println(">>> CART ITEM TEXT: [" + item.getText() + "]")
        );

        SelenideElement cartItemRow = $$(".cart-item")
                .filterBy(text(productName))
                .shouldHave(size(1))
                .get(0);
        cartItemRow.shouldBe(visible);
        System.out.println("Строка товара найдена в корзине.");

        // *********** КЛИК ПО КРЕСТИКУ ДЛЯ УДАЛЕНИЯ *************
        SelenideElement removeButton = cartItemRow.$(byAttribute("data-action", "remove"));
        removeButton.shouldBe(visible).click();

        System.out.println("Нажали на крестик для удаления товара.");

        // *********** ПРОВЕРКА, ЧТО ТОВАР УДАЛЕН *************

        // Проверяем, что строка товара исчезла из DOM
        cartItemRow.shouldNotBe(exist);
        System.out.println("Строка товара удалена из списка корзины.");

        // Закрываем и проверяем, что счетчик корзины стал 0 (Можно проверять и сумму товаров в корзине)
        $("#close-modal").click();
        $("#open-cart-btn").shouldBe(visible);
        $("#cart-count").shouldHave(text("0"));

        System.out.println("Счетчик корзины обновлен: 0 товаров.");

        // *********** ОТКРЫВАЕМ АДМИНКУ ***********
        $("[href='/admin']").click();

        // *********** УДАЛЯЕМ ТЕСТОВЫЙ ТОВАР *************
        deleteProduct(productName);
    }

}
