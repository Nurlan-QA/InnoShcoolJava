package ui.SelenideTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.pages.*;

import static com.codeborne.selenide.Selenide.open;

public class UpdateGoodsTest extends BaseTestSelenide {

    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private final long uniqueSuffix = System.nanoTime() % 1_000_000;
    private final String originalProductName = ConfigProvider.getProductName() + "_" + uniqueSuffix;
    private final String updatedProductName = originalProductName + "_updated";

    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(updatedProductName);
    }

    @Test
    void goodsUpdate() {
        // Вход в админку через LoginPage
        open("/admin");
        loginPage.assertPageLoaded();
        loginPage.login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());

        adminPage.assertPageLoaded();

        // Создаём товар
        adminPage.createProduct(originalProductName, "100");
        adminPage.assertToastContains("Товар успешно добавлен");
        System.out.println("Уведомление: Товар успешно добавлен!");

        // Идём на витрину и проверяем
        adminPage.goToSite();
        goodsPage.assertPageLoaded();
        goodsPage.assertProductVisible(originalProductName);
        goodsPage.assertProductHasText(originalProductName);
        System.out.println("Созданный товар '" + originalProductName + "' есть на витрине сайта!");

        // Возвращаемся в админку для редактирования
        goodsPage.goToAdmin();
        adminPage.assertPageLoaded();

        // Редактируем товар через AdminPage
        adminPage.updateProduct(originalProductName, updatedProductName);

        // Идём на витрину и проверяем изменения
        adminPage.goToSite();
        goodsPage.assertPageLoaded();
        goodsPage.assertProductVisible(updatedProductName);
        goodsPage.assertProductHasText(updatedProductName);
        System.out.println("Обновлённый товар '" + updatedProductName + "' есть на витрине сайта!");
    }
}
