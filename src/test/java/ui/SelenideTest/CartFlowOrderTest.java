package ui.SelenideTest;

import api.api_config.ReqSpec;
import api.api_methods.Good;
import api.api_methods.GoodsApi;
import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ui.SelenideTest.config.ConfigProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CartFlowOrderTest extends BaseTestSelenide {

    private final GoodsApi goodsApi = new GoodsApi();
    private static final int COUNT = 3;

    @AfterEach
    void quitTests() {
        for (int i = 1; i <= COUNT; i++) {
            String productName = ConfigProvider.getProductName() + "_" + i;
            String selector = "input[value*='" + productName + "']";
            $$(selector).shouldHave(sizeGreaterThan(0));

            SelenideElement inputField = $$(selector).first();
            SelenideElement row = inputField.closest("tr");

            row.$("button[data-action='delete']")
                    .shouldBe(enabled)
                    .click();
            switchTo().alert().accept();

            $$(selector).shouldHave(size(0));
            System.out.println("Товар '" + productName + "' удалён!");
        }
        // closeWebDriver() вызовется из BaseTestSelenide.quitTests()
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

        open("/");

        // Добавляем товары в корзину через UI
        for (int i = 1; i <= COUNT; i++) {
            $("button[data-name='" + ConfigProvider.getProductName() + "_" + i + "']")
                    .shouldBe(visible)
                    .click();
        }

        // Проверяем и оформляем заказ
        $("#open-cart-btn")
                .shouldBe(visible)
                .click();

        $$(".cart-item")
                .shouldHave(size(COUNT))
                .filterBy(text(ConfigProvider.getProductName() + "_"))
                .shouldBe();

        int totalPrice = Integer.parseInt($("#total-price").getText());

        if (totalPrice >= 300) {
            throw new AssertionError("Сумма корзины (" + totalPrice + ") больше 300!");
        }

        $("#makeOrder").shouldBe(visible, Duration.ofSeconds(5)).click();

        $$(".toast")
                .filterBy(text("Заказ принят"))
                .shouldHave(sizeGreaterThan(0))
                .first()
                .shouldBe(visible);
        System.out.println("Есть уведомление: Заказ принят в обработку!");

        // Логин через метод из BaseTestSelenide
        $("[href='/admin']").click();
        $("#username").setValue(ConfigProvider.getAdminLogin());
        $("#password").setValue(ConfigProvider.getAdminPassword());
        $(".primary").click();
    }
}
