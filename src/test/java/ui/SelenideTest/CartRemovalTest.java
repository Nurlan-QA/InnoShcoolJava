package ui.SelenideTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.pages.*;

public class CartRemovalTest extends BaseTestSelenide {

    private final LoginPage loginPage = new LoginPage();
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
    void cartRemoval() {
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
        goodsPage.assertCartCount(1);
        System.out.println("Товар добавлен в корзину через DnD!");

        // Открыть корзину
        goodsPage.openCart();

        // Удалить товар из корзины
        cartPage.removeProductFromCart(productName);
        System.out.println("Товар удалён из корзины!");

        // Закрыть корзину и проверить счётчик
        cartPage.closeCartModal();
        goodsPage.assertCartCount(0);
        System.out.println("Счётчик корзины: 0 товаров.");

        // Очистка: удалить товар из админки
        goodsPage.goToAdmin();
        deleteProduct(productName);
    }
}
