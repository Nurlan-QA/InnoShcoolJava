package api.api_methods;

import api.api_config.ReqSpec;
import io.restassured.response.Response;
import ui.SelenideTest.config.ConfigProvider;

import static io.restassured.RestAssured.given;

public class GoodsApi {

    // Выносим креды в константы, чтобы не дергать ConfigProvider много раз
    private static final String ADMIN_LOGIN = ConfigProvider.getAdminLogin();
    private static final String ADMIN_PASSWORD = ConfigProvider.getAdminPassword();

    public Response getList() {
        return given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .extract().response();
    }

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
