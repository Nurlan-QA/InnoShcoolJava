package ui.SelenideTest;

import api.api_config.ReqSpec;
import api.api_methods.Good;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import api.api_methods.GoodsApi;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


// 3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей).
// Проверить уведомление об обработке заказа.
// 3.5. Хотя бы в одном кейсе должны быть обращение к API для генерации тестовых данных.
// 3.6. Хотя бы в одном кейсе в блоке @AfterEach тестовые данные должны удаляться.

public class CartFlowOrderTest {
    private final GoodsApi goodsApi = new GoodsApi();

    int count = 3;

    @AfterEach
    void quitTests() {

//    УДАЛЕНИЕ ТЕСТОВЫХ ДАННЫХ В AfterEach

        for (int i = 1; i <= count; i++) {
            // Формируем имя товара
            String productName = "Смартфон_" + i;
            String selector = "input[value*='" + productName + "']";
            $$(selector).shouldHave(sizeGreaterThan(0));

            SelenideElement inputField = $$(selector).first();
            SelenideElement row = inputField.closest("tr");

            // Находим кнопку удаления ВНУТРИ этой строки и кликаем
            row.$("button[data-action='delete']")
                    .shouldBe(enabled) // Ждем, пока кнопка станет активной
                    .click();
            switchTo().alert().accept();

            // Проверяем, что товар удален
            $(selector).shouldNot(exist);
            $$(selector).shouldHave(size(0));
            System.out.println("Тестовые данные успешно удалены!");
        }
        Selenide.closeWebDriver();
    }

    @Test
    void addThreeGoodsWithApi() {

        // СОЗДАНИЕ ТЕСТОВЫХ ДАННЫХ В BeforeEach

        List<Good> createdGoods = new ArrayList<>();
        for (int i = 1; i <= count; i++) {

            long now = System.currentTimeMillis();
            String uniqueName = "Смартфон_" + i;

            Good newGood = new Good(uniqueName, 80.00 + i);

            // 1. Создаем товар
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
        assertThat(goodsList).hasSizeGreaterThanOrEqualTo(count);

        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
//        Configuration.timeout = 10000; // 10 секунд
        Configuration.baseUrl = "http://localhost:8080";
        open("/");

        // Добавляем эти три товара в корзину через UI
        for (int i = 1; i <= count; i++) {
            $("button[data-name='Смартфон_" + i + "']")
                    .shouldBe(visible)
                    .click();
        }


        // Проверяем и оформляем заказ

        $("#open-cart-btn")
                .shouldBe(visible)
                .click();

        $$(".cart-item")
                .shouldHave(size(count))
                .filterBy(text("Смартфон_"))
                .shouldBe();

        int totalPrice = Integer.parseInt($("#total-price").getText());

        if (totalPrice >= 300) {
            throw new AssertionError("Сумма корзины (" + totalPrice + ") больше 300!");
        }
//        Нажимаем на кнопку 'Оформить заказ'
        $("#makeOrder").shouldBe(visible, Duration.ofSeconds(5)).click();
//        Проверяем наличие тостера об успешном оформлении
        $$(".toast")
                .filterBy(text("Заказ принят"))
                .shouldHave(sizeGreaterThan(0))
                .first()
                .shouldBe(visible);
        System.out.println("Есть уведомление: Заказ принят в обработку!");

        // *********** ПЕРЕХОД В АДМИНКУ ДЛЯ УДАЛЕНИЯ ТЕСТОВЫХ ДАННЫХ *************
        $("[href='/admin']").click();
        $("#username").setValue("admin");
        $("#password").setValue("secret123");
        $(".primary").click();

    }
}


