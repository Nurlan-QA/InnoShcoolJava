package ui.SelenideTest;

import api.api_config.ReqSpec;
import api.api_methods.Good;
import api.api_methods.GoodsApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;
import ui.SelenideTest.pages.*;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CartFlowOrderTest extends BaseTestSelenide {

    private final GoodsPage goodsPage = new GoodsPage();
    private final CartPage cartPage = new CartPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private final GoodsApi goodsApi = new GoodsApi();
    private static final int COUNT = 3;

    @AfterEach
    void cleanUp() {
        productCleanup.removeTestProducts();
    }

    @Test
    void addThreeGoodsWithApi() {
        List<Good> createdGoods = new ArrayList<>();
        for (int i = 1; i <= COUNT; i++) {
            String uniqueName = ConfigProvider.getProductName() + "_" + i;
            Good newGood = new Good(uniqueName, 80.00 + i);

            goodsApi.addGoods(newGood)
                    .then()
                    .statusCode(200);

            createdGoods.add(newGood);
            System.out.println("Создан тестовый товар: " + uniqueName);
        }

        Response response = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().response();

        List<Good> goodsList = response.jsonPath().getList("goods", Good.class);
        assertThat(goodsList).hasSizeGreaterThanOrEqualTo(COUNT);

        // Открываем витрину
        open("/");
        goodsPage.assertPageLoaded();

        // Добавляем 3 товара в корзину через PageObject
        for (int i = 1; i <= COUNT; i++) {
            goodsPage.addProductToCart(ConfigProvider.getProductName() + "_" + i);
        }

        // Открываем корзину через PageObject
        goodsPage.openCart();

        // Проверки через CartPage
        cartPage.assertItemsCount(COUNT);
        cartPage.assertItemsContainText(ConfigProvider.getProductName() + "_");

        int totalPrice = cartPage.getTotalPrice();
        System.out.println("Сумма в корзине: " + totalPrice);
        assertThat(totalPrice).isLessThanOrEqualTo(300);

        // Оформляем заказ
        cartPage.makeOrder();

        // Проверка уведомления
        cartPage.assertOrderAccepted();
        System.out.println("Уведомление: Заказ принят в обработку!");
    }
}
