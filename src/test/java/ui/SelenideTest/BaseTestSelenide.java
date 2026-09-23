package ui.SelenideTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.config.ConfigPrinter;
import ui.SelenideTest.pages.AdminPage;
import ui.SelenideTest.pages.GoodsPage;
import ui.SelenideTest.pages.LoginPage;

import static com.codeborne.selenide.Selenide.*;

public abstract class BaseTestSelenide {

    protected final LoginPage loginPage = new LoginPage();
    protected final GoodsPage goodsPage = new GoodsPage();
    protected final AdminPage adminPage = new AdminPage();

    @BeforeEach
    void setup() {
        ConfigPrinter.printConfig();

        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = ConfigProvider.getTimeout();
        Configuration.baseUrl = ConfigProvider.getBaseUrl();

        open("/");
    }

    @AfterEach
    void quitTests() {
        Selenide.closeWebDriver();
    }

    /**
     * Вход в админку через PageObject.
     * Логин и пароль берутся из config.properties.
     */
    protected void loginToAdmin() {
        goodsPage.goToAdmin();
        loginPage.assertPageLoaded();
        loginPage.login(
                ConfigProvider.getAdminLogin(),
                ConfigProvider.getAdminPassword()
        );
    }

    /**
     * Вход в админку с произвольными параметрами.
     * Используется для негативных тестов.
     */
    protected void loginToAdminInvalid(String username, String password) {
        goodsPage.goToAdmin();
        loginPage.login(username, password);
    }

    /**
     * Удаление товара через PageObject.
     * Сначала открывает /admin, логинится, затем удаляет.
     */
    protected void deleteProduct(String productName) {
        open("/admin");

        // Если видна форма логина — входим
        if (loginPage.loginField.isDisplayed()) {
            loginPage.login(
                    ConfigProvider.getAdminLogin(),
                    ConfigProvider.getAdminPassword()
            );
        }

        adminPage.deleteProductByName(productName);
        System.out.println("Тестовые данные успешно удалены!");
    }
}
