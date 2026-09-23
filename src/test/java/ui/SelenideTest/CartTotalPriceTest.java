package ui.SelenideTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.pages.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTotalPriceTest extends BaseTestSelenide {

    private final GoodsPage goodsPage = new GoodsPage();
    private final CartPage cartPage = new CartPage();
    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private static final int COUNT = 3;
    private final int basePriceFromConfig = Integer.parseInt(ConfigProvider.getProductPrice());

    @AfterEach
    void cleanUp() {
        productCleanup.removeTestProducts();
    }

    @Test
    void cartTotalPrice() {
        String baseName = ConfigProvider.getProductName();
        int sum = 0;

        // Вход в админку через LoginPage
        open("/admin");
        loginPage.assertPageLoaded();
        loginPage.login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());

        // Проверяем, что админка загрузилась
        adminPage.assertPageLoaded();

        // Создаём товары через AdminPage
        for (int i = 1; i <= COUNT; i++) {
            String name = baseName + "_" + i;
            int price = basePriceFromConfig + i;
            sum += price;
            adminPage.createProduct(name, String.valueOf(price));
            System.out.println("Добавлен товар: " + name + ", цена: " + price);
        }

        System.out.println("Ожидаемая сумма: " + sum);

        // Возврат на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // Добавляем в корзину через GoodsPage
        for (int i = 1; i <= COUNT; i++) {
            goodsPage.addProductToCart(baseName + "_" + i);
        }

        // Открываем корзину и проверяем
        goodsPage.openCart();
        cartPage.assertItemsCount(COUNT);

        int totalPrice = cartPage.getTotalPrice();
        System.out.println("Сумма в корзине (UI): " + totalPrice);

        assertEquals(sum, totalPrice, "Сумма в корзине не совпадает с расчётной");
        System.out.println("Сумма товаров в корзине верная!");

        cartPage.closeCartModal();
    }
}
