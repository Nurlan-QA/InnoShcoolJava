package api.api_methods;

import api.api_config.ReqSpec;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GoodsApi extends ReqSpec {

    public Response getList() {
        return given()
                .spec(requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .extract().response();
    }

    public Response addGoods(Good good) {
        // Передаем объект напрямую, RestAssured сам превратит его в JSON
        return given()
                .spec(requestSpec)
                .auth()
                .basic("admin", "secret123")
                .body(good)
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .extract().response();
    }

    // УДАЛЕНИЕ: принимает ID как аргумент
    public Response deleteGoods(Long id) {
        return given()
                .spec(requestSpec)
                .auth()
                .basic("admin", "secret123")
                .when()
                .delete("/goods/" + id) // Динамический URL
                .then()
                .log().all()
                .extract().response();
    }

    // ОБНОВЛЕНИЕ: принимает ID и новый объект
    public Response updateGoods(Long id, Good updatedGood) {
        return given()
                .spec(requestSpec)
                .auth()
                .basic("admin", "secret123")
                .body(updatedGood)
                .when()
                .patch("/goods/" + id) // Используем PATCH для частичного обновления
                .then()
                .log().all()
                .extract().response();
    }

    // НОВЫЙ МЕТОД: Получение товара по ID
    public Response getGoodById(Long id) {
        return given()
                .spec(requestSpec)
                .auth()
                .basic("admin", "secret123")
                .when()
                .get("/goods/" + id) // Эндпоинт http://localhost:8080/goods/{id}
                .then()
                .log().all()
                .extract().response();
    }
}
