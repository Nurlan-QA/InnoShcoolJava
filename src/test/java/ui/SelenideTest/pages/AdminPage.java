package ui.SelenideTest.pages;

import com.codeborne.selenide.SelenideElement;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;

public class AdminPage {
    private final SelenideElement nameField = $("#n-name");
    private final SelenideElement priceField = $("#n-price");
    private final SelenideElement addButton = $("#add-btn");
    private final SelenideElement backToSiteLink = $(byText("Вернуться на сайт"));
    private final SelenideElement toast = $(".toast");
    private final SelenideElement tbody = $("#tbody");

    // --- Методы взаимодействия ---
    public void createProduct(String name, String price) {
        nameField.setValue(name);
        priceField.setValue(price);
        addButton.click();
    }

    public void updateProduct(String oldName, String newName) {
        SelenideElement nameInput = $("[value='" + oldName + "']");
        PageAssert.isVisible(nameInput);
        SelenideElement row = nameInput.closest("tr");
        nameInput.click();
        nameInput.setValue(newName);
        row.$("[data-action='update']").click();
    }

    public void deleteProductByName(String productName) {
        SelenideElement nameInput = $("[value='" + productName + "']");
        nameInput.shouldBe(visible);

        SelenideElement row = nameInput.closest("tr");
        row.$("button[data-action='delete']")
                .shouldBe(enabled)
                .click();

        confirm();
        $("[value='" + productName + "']").shouldNot(exist);
    }


    public void goToSite() {
        backToSiteLink.click();
    }

    public void goToAdmin() {
        $("[href='/admin']").click();
    }

    public String getToastText() {
        return toast.shouldBe(visible).getText();
    }

    // --- Проверки ---
    public void assertNameFieldVisible() {
        PageAssert.isVisible(nameField);
    }

    public void assertPriceFieldVisible() {
        PageAssert.isVisible(priceField);
    }

    public void assertAddButtonVisible() {
        PageAssert.isVisible(addButton);
    }

    public void assertBackToSiteVisible() {
        PageAssert.isVisible(backToSiteLink);
    }

    public void assertToastContains(String expectedText) {
        PageAssert.containsText(toast, expectedText);
    }

    public void assertPageLoaded() {
        assertNameFieldVisible();
        assertPriceFieldVisible();
        assertAddButtonVisible();
    }

}



