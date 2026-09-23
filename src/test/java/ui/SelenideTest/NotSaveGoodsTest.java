package ui.SelenideTest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Selectors;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.pages.AdminPage;
import ui.SelenideTest.pages.CartPage;
import ui.SelenideTest.pages.GoodsPage;
import ui.SelenideTest.pages.ProductCleanup;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

// 2.4. Проверить сохранение товаров в корзине после обновления страницы.
// Так как товар не сохраняется в корзине, то проверяем, что товар отсутствует в корзине

public class NotSaveGoodsTest extends BaseTestSelenide {

    private final CartPage cartPage = new CartPage();
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
    void goodsAddAndRefresh() {


        // Вход в админку
        loginToAdmin();

        // Проверяем, что админка загрузилась
        adminPage.assertPageLoaded();

        // Создаём товар через AdminPage
        adminPage.createProduct(productName, productPrice);

        // Проверяем уведомление
        adminPage.assertToastContains("Товар успешно добавлен");
        System.out.println("Уведомление: Товар успешно добавлен!");

        // Переходим на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // Проверяем наличие товара
        goodsPage.assertProductVisible(productName);
        goodsPage.assertProductHasText(productName);
        System.out.println("Созданный товар '" + productName + "' есть на витрине сайта!");

        // ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ И ПРОВЕРЯЕМ *************
        goodsPage.addProductToCart(productName);

        // Нажимаем на кнопку корзины
        goodsPage.openCart();

        // Проверяем, что товар ЕСТЬ в корзине ДО рефреша
        cartPage.shouldBeGoodInCart(productName);
        System.out.println("До обновления страницы в корзине есть товар!");

        // Закрываем модальное окно корзины
        cartPage.closeCartModal();

        // Обновляем страницу
        refresh();

        // Ждем загрузку логотипа
        goodsPage.assertLogoVisible();

        // Нажимаем на кнопку корзины
        goodsPage.openCart();

        // Проверяем отсутствие добавленного товара в корзине
        cartPage.goodNotInCart(productName);
        System.out.println("Корзина после обновления страницы пустая!");

        // Закрываем модальное окно корзины
        cartPage.closeCartModal();

    }
}