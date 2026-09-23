package ui.SelenideTest;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.pages.*;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;


public class DragAndDropTest extends BaseTestSelenide {

    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private static final long UNIQUE_SUFFIX = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + UNIQUE_SUFFIX;
    private final String productPrice = ConfigProvider.getProductPrice();

    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(productName);
    }

    @Test
    void checkoutWithExpensiveItem() {

        // Вход в админку
        loginToAdmin();

        // Создание товара
        adminPage.assertPageLoaded();
        adminPage.createProduct(productName, productPrice);
        adminPage.assertToastContains("Товар успешно добавлен");
        System.out.println("Уведомление: Товар успешно добавлен!");

        // Возврат на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // Проверка наличия товара
        goodsPage.assertProductVisible(productName);
        goodsPage.assertProductHasText(productName);
        System.out.println("Товар '" + productName + "' есть на витрине!");

        // Drag-and-drop в корзину
        goodsPage.dragProductToCart(productName);
        System.out.println("Товар добавлен в корзину через DnD!");

        // Drag-and-drop в корзину
        goodsPage.dragProductToCart(productName);
        System.out.println("Товар добавлен в корзину через DnD!");

        // Проверяем, что в корзине два товара
        goodsPage.assertCartCount(2);
        System.out.println("Товар добавлен в корзину в количестве 2 штук!");

    }
}
