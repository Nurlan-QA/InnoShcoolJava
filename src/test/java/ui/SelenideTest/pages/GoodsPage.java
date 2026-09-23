package ui.SelenideTest.pages;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

public class GoodsPage {

    // Элементы (не менее 5)
    private final SelenideElement logoSmartShop = $("#main-title");
    private final SelenideElement adminLink = $("[href='/admin']");
    private final SelenideElement openCartBtn = $("#open-cart-btn");
    private final ElementsCollection goodsList = $$(".product-card");
    private final ElementsCollection addToCartButtons = $$("button[data-action='add-to-cart']");
    private final SelenideElement successNotification = $(".notification-success");


    // --- Методы взаимодействия ---
    public void openCart() {
        openCartBtn.click();
    }

    public void goToAdmin() {
        adminLink.click();
    }

    public void addProductToCart(String productName) {
        $(String.format("button[data-name='%s']", productName)).click();
    }

    public String getNotificationText() {
        return successNotification.shouldBe(com.codeborne.selenide.Condition.visible).getText();
    }

    // --- Проверки ---
    public void assertLogoVisible() {
        PageAssert.isVisible(logoSmartShop);
    }

    public void assertAdminLinkVisible() {
        PageAssert.isVisible(adminLink);
    }

    public void assertCartButtonVisible() {
        PageAssert.isVisible(openCartBtn);
    }

    public void assertProductVisible(String productName) {
        String selector = String.format("[data-name='%s']", productName);
        PageAssert.isVisible($(selector));
    }

    public void assertProductHasText(String productName) {
        String selector = String.format("[data-name='%s']", productName);
        PageAssert.containsText($(selector), productName);
    }

    // Drag-and-drop товара в корзину
    public void dragProductToCart(String productName) {
        SelenideElement product = $(byAttribute("data-name", productName));
        SelenideElement cartBtn = $("#open-cart-btn");
        product.dragAndDrop(DragAndDropOptions.to(cartBtn));
    }

    // Проверка счётчика корзины
    public void assertCartCount(int expected) {
        $("#cart-count").shouldHave(text(String.valueOf(expected)));
    }

    public void assertPageLoaded() {
        assertLogoVisible();
        assertAdminLinkVisible();
        assertCartButtonVisible();
    }
}
