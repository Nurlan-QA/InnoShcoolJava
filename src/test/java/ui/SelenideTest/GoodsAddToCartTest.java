package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.pages.*;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

// 2.2. Добавить товар в корзину и проверить, что он отображается.

public class GoodsAddToCartTest extends BaseTestSelenide {

    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();
    private final CartPage cartPage = new CartPage();

    private static final long UNIQUE_SUFFIX = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + UNIQUE_SUFFIX;
    private final String productPrice = ConfigProvider.getProductPrice();

    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(productName);
    }

    @Test
    void goodsAddInCart() {

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

        // ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ И ПРОВЕРЯЕМ *************
        goodsPage.addProductToCart(productName);

        // Открываем корзину
        goodsPage.openCart();

        // Проверяем наличие добавленного товара в корзине
        cartPage.shouldBeGoodInCart(productName);
        System.out.println("Добавленный товар в корзине, отображается в ней!");

        // Закрываем модальное окно корзины
        cartPage.closeCartModal();

    }
}