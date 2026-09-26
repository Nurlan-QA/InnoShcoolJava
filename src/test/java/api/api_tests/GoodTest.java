package api.api_tests;

import api.api_config.ApiConfig;
import api.api_config.ReqSpec;
import api.api_methods.GoodsApi;
import api.api_methods.Good;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.config.ConfigProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GoodTest {

    private final GoodsApi goodsApi = new GoodsApi();
    private final List<Long> createdGoodIds = new ArrayList<>();

    private static final Set<Long> PROTECTED_IDS = Set.of(50L, 51L, 52L, 53L);

    @Step("Очистка базы перед тестом (кроме защищённых ID 50-53)")
    @BeforeEach
    void clearDatabaseBeforeEachTest() {
        Response response = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Good> allGoods = response.jsonPath().getList("goods", Good.class);

        for (Good good : allGoods) {
            if (PROTECTED_IDS.contains(good.getId())) {
                continue;
            }
            given()
                    .spec(ReqSpec.requestSpec)
                    .auth()
                    .basic(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword())
                    .when()
                    .delete("/goods/" + good.getId())
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200);
        }
    }

    @Step("Очистка товаров, созданных в тесте")
    @AfterEach
    void clearCreatedGoodsAfterEachTest() {
        for (Long id : createdGoodIds) {
            Response resp = given()
                    .spec(ReqSpec.requestSpec)
                    .auth()
                    .basic(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword())
                    .when()
                    .delete("/goods/" + id);

            int status = resp.getStatusCode();
            if (status != 200 && status != 404) {
                System.err.println("Неожиданный статус " + status + " при удалении ID " + id);
            }
        }
        createdGoodIds.clear();
    }

    // ==========================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ С @Step ДЛЯ ПРОВЕРОК
    // ==========================================

    @Step("Проверка: список товаров содержит только {expectedSize} защищённых элементов")
    public void checkGoodsListHasOnlyProtected(Response response, int expectedSize) {
        response.then()
                .statusCode(200)
                .body("goods", hasSize(expectedSize))
                .body("goods.id", containsInAnyOrder(50, 51, 52, 53));
    }

    @Step("Проверка: товар с именем '{name}' присутствует в списке")
    public void checkGoodInListByName(Response response, String name) {
        response.then()
                .statusCode(200)
                .body("goods", hasItem(hasEntry("name", name)));
    }

    @Step("Проверка: список товаров не пуст")
    public void checkGoodsListNotEmpty(List<Good> goodsList) {
        Assertions.assertThat(goodsList).isNotEmpty();
    }

    @Step("Проверка: товар с именем '{name}' есть в списке")
    public void checkGoodNameInList(List<Good> goodsList, String name) {
        Assertions.assertThat(goodsList)
                .extracting(Good::getName)
                .contains(name);
    }

    @Step("Проверка: статус-код ответа равен {expected}")
    public void checkStatusCode(Response response, int expected) {
        Assertions.assertThat(response.getStatusCode())
                .as("Ожидаем статус-код " + expected)
                .isEqualTo(expected);
    }

    @Step("Проверка: статус-код ответа — 404 или 500")
    public void checkStatusCodeIn(Response response, int... expectedCodes) {
        List<Integer> codes = new ArrayList<>();
        for (int c : expectedCodes) {
            codes.add(c);
        }
        Assertions.assertThat(response.getStatusCode())
                .as("Ожидаем один из статусов: " + codes)
                .isIn(codes.toArray());
    }


    @Step("Проверка: цена товара равна {expectedPrice}")
    public void checkGoodPrice(Good good, double expectedPrice) {
        Assertions.assertThat(good.getPrice())
                .as("Цена должна быть " + expectedPrice)
                .isEqualTo(expectedPrice);
    }

    @Step("Проверка: товар не null")
    public void checkGoodNotNull(Good good) {
        Assertions.assertThat(good).isNotNull();
    }

    @Step("Проверка: товар с ID {createdId} найден в списке с ценой {newPrice}")
    public void checkGoodInListById(List<Good> goodsList, Long createdId, double newPrice) {
        Good foundInList = goodsList.stream()
                .filter(g -> g.getId().equals(createdId))
                .findFirst()
                .orElse(null);

        Assertions.assertThat(foundInList).isNotNull();
        Assertions.assertThat(foundInList.getPrice()).isEqualTo(newPrice);
    }

    // ==========================================
    // ТЕСТЫ
    // ==========================================

    @Test
    void testGetListWithGivenWhenThen() {
        Response response = given()
                .baseUri(ApiConfig.BASE_URL)
                .queryParam("page", 0)
                .queryParam("size", "100")
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        checkGoodsListHasOnlyProtected(response, 4);
    }

    @Test
    void testGetListWithRequestSpec() {
        Response response = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        checkGoodsListHasOnlyProtected(response, 4);
    }

    @Test
    void testAddGoodsRestAssured() {
        long now = System.currentTimeMillis();
        String uniqueName = ConfigProvider.getProductName() + "_RA_" + (now % 1000000);
        double price = Double.parseDouble(ConfigProvider.getProductPrice());

        Good newGood = new Good(uniqueName, price);

        Response createResponse = goodsApi.addGoods(newGood);
        checkStatusCode(createResponse, 200);

        Long createdId = createResponse.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        Response listResponse = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        checkGoodInListByName(listResponse, uniqueName);
    }

    @Test
    void testAddGoodsWithAssertJ() {
        long now = System.currentTimeMillis();
        String uniqueName = "Good_AJ" + (now % 1000000);
        Good newGood = new Good(uniqueName, 88.88);

        Response createResp = goodsApi.addGoods(newGood);
        checkStatusCode(createResp, 200);

        Long createdId = createResp.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        Response response = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().response();

        List<Good> goodsList = response.jsonPath().getList("goods", Good.class);

        checkGoodsListNotEmpty(goodsList);
        checkGoodNameInList(goodsList, uniqueName);
    }

    @Test
    void testDeleteGoods() {
        long now = System.currentTimeMillis();
        String uniqueName = ConfigProvider.getProductName() + "_delete_" + (now % 1000000);
        double price = Double.parseDouble(ConfigProvider.getProductPrice());
        Good newGood = new Good(uniqueName, price);

        Response createResp = goodsApi.addGoods(newGood);
        checkStatusCode(createResp, 200);

        Long createdId = createResp.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        Response deleteResp = goodsApi.deleteGoods(createdId);
        checkStatusCode(deleteResp, 200);

        Response getByIdResp = goodsApi.getGoodById(createdId);
        checkStatusCodeIn(getByIdResp, 404, 500);
    }

    @Test
    void testUpdateGoods() {
        long now = System.currentTimeMillis();
        String uniqueName = ConfigProvider.getProductName() + "_update_" + (now % 1000000);
        double price = Double.parseDouble(ConfigProvider.getProductPrice());
        Good newGood = new Good(uniqueName, price);

        Response createResp = goodsApi.addGoods(newGood);
        checkStatusCode(createResp, 200);

        Long createdId = createResp.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        double newPrice = Double.parseDouble(ConfigProvider.getProductBigPrice());
        Good updatedGood = new Good(uniqueName, newPrice);

        Response updateResp = goodsApi.updateGoods(createdId, updatedGood);
        checkStatusCode(updateResp, 200);

        // Проверка через тело ответа PATCH
        Good patchedGood = updateResp.jsonPath().getObject("$", Good.class);
        checkGoodNotNull(patchedGood);
        checkGoodPrice(patchedGood, newPrice);

        // Доп. проверка через GET /goods/list
        Response listResp = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().response();

        List<Good> goodsList = listResp.jsonPath().getList("goods", Good.class);
        checkGoodInListById(goodsList, createdId, newPrice);
    }
}
