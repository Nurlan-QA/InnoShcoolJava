package api.api_methods;

import api.api_config.ReqSpec;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ui.SelenideTest.config.ConfigProvider;

import static io.restassured.RestAssured.given;

public class GoodsApi {

    // Выносим креды в константы, чтобы не дергать ConfigProvider много раз
    private static final String ADMIN_LOGIN = ConfigProvider.getAdminLogin();
    private static final String ADMIN_PASSWORD = ConfigProvider.getAdminPassword();

    @Step("Получить список всех товаров (GET /goods/list)")
    public Response getList() {
        return given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Создать товар (POST /goods/add): name={good.name}, price={good.price}")
    public Response addGoods(Good good) {
        return given()
                .spec(ReqSpec.requestSpec)
                .auth()
                .basic(ADMIN_LOGIN, ADMIN_PASSWORD)
                .body(good)
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Удалить товар по ID (DELETE /goods/{id})")
    public Response deleteGoods(Long id) {
        return given()
                .spec(ReqSpec.requestSpec)
                .auth()
                .basic(ADMIN_LOGIN, ADMIN_PASSWORD)
                .when()
                .delete("/goods/" + id)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Обновить товар по ID (PATCH /goods/{id})")
    public Response updateGoods(Long id, Good updatedGood) {
        return given()
                .spec(ReqSpec.requestSpec)
                .auth()
                .basic(ADMIN_LOGIN, ADMIN_PASSWORD)
                .body(updatedGood)
                .when()
                .patch("/goods/" + id)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Получить товар по ID (GET /goods/{id})")
    public Response getGoodById(Long id) {
        return given()
                .spec(ReqSpec.requestSpec)
                .auth()
                .basic(ADMIN_LOGIN, ADMIN_PASSWORD)
                .when()
                .get("/goods/" + id)
                .then()
                .log().all()
                .extract().response();
    }
}
