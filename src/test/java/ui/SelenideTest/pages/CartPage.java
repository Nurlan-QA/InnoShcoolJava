package ui.SelenideTest.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

public class CartPage {

    private final SelenideElement totalPriceElement = $("#total-price");
    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement makeOrderBtn = $("#makeOrder");
    private final SelenideElement closeModalBtn = $("#close-modal");
    private final ElementsCollection cartItems = $$(".cart-item");

    // --- Методы взаимодействия ---
    public void makeOrder() {
        makeOrderBtn.click();
    }

    public void closeCartModal() {
        closeModalBtn.click();
    }

    public int getCartCount() {
        return Integer.parseInt(cartCount.getText());
    }

    public int getTotalPrice() {
        String raw = totalPriceElement.getText();
        String digitsOnly = raw.replaceAll("[^\\d]", "");
        return digitsOnly.isEmpty() ? 0 : Integer.parseInt(digitsOnly);
    }

    // Удалить товар из корзины по имени
    public void removeProductFromCart(String productName) {
        SelenideElement cartItemRow = $$(".cart-item")
                .filterBy(text(productName))
                .shouldHave(size(1))
                .get(0);
        cartItemRow.$(byAttribute("data-action", "remove"))
                .shouldBe(visible)
                .click();
        cartItemRow.shouldNotBe(exist);
    }

    public void shouldBeGoodInCart(String productName){
        SelenideElement cartItemCarts = $(".cart-item").shouldHave(text(productName));
    }

    public void goodNotInCart(String productName){
        SelenideElement cartItemCarts = $(".cart-item").shouldNot(exist);
    }

    // --- Проверки ---
    public void assertTotalPriceVisible() {
        PageAssert.isVisible(totalPriceElement);
    }

    public void assertMakeOrderButtonVisible() {
        PageAssert.isVisible(makeOrderBtn);
    }

    public void assertItemsCount(int expectedCount) {
        cartItems.shouldHave(size(expectedCount));
    }

    public void assertItemsContainText(String expectedText) {
        cartItems.filterBy(text(expectedText)).shouldHave(sizeGreaterThan(0));
    }

    public void assertOrderAccepted() {
        $$(".toast")
                .filterBy(text("Заказ принят"))
                .shouldHave(sizeGreaterThan(0));
    }
}
