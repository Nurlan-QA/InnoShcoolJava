package api.api_tests;

import api.api_config.ApiConfig;
import api.api_config.ReqSpec;
import api.api_methods.GoodsApi;
import api.api_methods.Good;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GoodTest {
    @BeforeEach
    void clearDatabaseBeforeEachTest() {
        System.out.println("Очистка базы перед тестом...");

        // 1. Получаем ответ, но НЕ пытаемся сразу сделать List<Good>
        Response response = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        // 2. Используем JsonPath, чтобы достать именно массив из поля "goods"
        // Это вернет нам List<Map<String, Object>> или сразу List<Good>, если настроить
        List<Good> allGoods = response.jsonPath().getList("goods", Good.class);

        System.out.println("Найдено товаров для удаления: " + allGoods.size());

        // 3. Удаляем каждый
        for (Good good : allGoods) {
            given()
                    .spec(ReqSpec.requestSpec)
                    .auth()
                    .basic("admin", "secret123")
                    .when()
                    .delete("/goods/" + good.getId())
                    .then()
                    .log().all()
                    .statusCode(200); // Или 204, проверь свой API
            System.out.println("Удален товар с ID: " + good.getId());
        }
        System.out.println("База очищена!");
    }
    private final GoodsApi goodsApi = new GoodsApi();

    // ==========================================
    // ЗАДАЧА 1.1: given/when/then + проверка пустого списка
    // ==========================================
    @Test
    void testGetListWithGivenWhenThen() {
        given()
                .baseUri(ApiConfig.BASE_URL)
                .queryParam("page", 0)
                .queryParam("size", "100")
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods", hasSize(0));
    }

    // ==========================================
    // ЗАДАЧА 1.2: RequestSpecification + проверка пустого списка
    // ==========================================
    @Test
    void testGetListWithRequestSpec() {
        given()
                .spec(ReqSpec.requestSpec) // Используем наш спецификатор (baseUri, auth)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods", hasSize(0));
    }

    // ==========================================
    // ЗАДАЧА 1.3: POST + проверка через Hamcrest
    // ==========================================
    @Test
    void testAddGoodsRestAssured() {
        String uniqueName = "Hamcrest_Good_" + System.currentTimeMillis();
        Good newGood = new Good(uniqueName, 99.99);

        // 1. Создаем товар
        Response createResponse = goodsApi.addGoods(newGood);
        createResponse.then().statusCode(200);

        // 2. Проверяем наличие через встроенные матчеры REST Assured
        given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                // Ищем в списке goods элемент, у которого name совпадает
                .body("goods", hasItem(hasEntry("name", uniqueName)));

        System.out.println("********* Тест на добавление товара с RestAssured пройден! *********");
    }

    // ==========================================
    // ЗАДАЧА 1.4: POST + проверка через AssertJ
    // ==========================================
    @Test
    void testAddGoodsWithAssertJ() {
        String uniqueName = "AssertJ_Good_" + System.currentTimeMillis();
        Good newGood = new Good(uniqueName, 88.88);

        // 1. Создаем товар
        goodsApi.addGoods(newGood).then().statusCode(200);

        // 2. Получаем ответ на GET /goods/list
        Response response = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .extract().response();

        List<Good> goodsList = response.jsonPath().getList("goods", Good.class);

        // 3. Проверки
        Assertions.assertThat(goodsList)
                .as("Список не должен быть пустым после добавления")
                .isNotEmpty();

        Assertions.assertThat(goodsList)
                .as("Товар с именем " + uniqueName + " должен быть в списке")
                .extracting(Good::getName)
                .contains(uniqueName);

        System.out.println("********* Тест на добавление товара с AssertJ пройден! *********");
    }

    // ==========================================
    // ЗАДАЧА 2: Покрытие остальных эндпоинтов (Delete, Update)
    // Эти тесты создают -> получают ID -> удаляют/обновляют -> проверяют по id что товар удален/изменен.
    // ==========================================

    @Test
    void testDeleteGoods() {
        String uniqueName = "Delete_Good_" + System.currentTimeMillis();
        Good newGood = new Good(uniqueName, 77.77);

        // 1. Создаем товар
        Response createResp = goodsApi.addGoods(newGood);
        createResp.then().statusCode(200);

        // ВАЖНО: Парсим ответ создания правильно!
        // Если там тоже обертка {"data": {"id": ...}}, то этот код тоже упадет.
        // Предполагаем, что в ответе создания сразу объект Good или структура простая.

        Long createdId = createResp.jsonPath().getLong("data.id");

        System.out.println("Создан товар с ID: " + createdId);

        // 2. Удаляем по этому ID
        Response deleteResp = goodsApi.deleteGoods(createdId);

        Assertions.assertThat(deleteResp.getStatusCode())
                .as("Статус код при удалении должен быть 200")
                .isEqualTo(200);
        System.out.println("***** Товар удален *****");

        // 3. ПРОВЕРКА ЧЕРЕЗ GET /goods/{id}
        Response getByIdResp = goodsApi.getGoodById(createdId);

        Assertions.assertThat(getByIdResp.getStatusCode())
                .as("После удаления товар с ID %d не должен существовать (ожидаем 404)", createdId)
                .isEqualTo(404);

        System.out.println("********* Тест удаления пройден! **********");
    }


    @Test
    void testUpdateGoods() {
        String uniqueName = "Update_Good_" + System.currentTimeMillis();
        Good newGood = new Good(uniqueName, 55.55);

        // 1. Создаем товар
        Response createResp = goodsApi.addGoods(newGood);
        createResp.then().statusCode(200);

        Long createdId = createResp.jsonPath().getLong("data.id");
        System.out.println("**** Создан товар с ID: " + createdId);

        // 2. Обновляем цену
        double newPrice = 123.45;
        Good updatedGood = new Good(uniqueName, newPrice);

        Response updateResp = goodsApi.updateGoods(createdId, updatedGood);

        Assertions.assertThat(updateResp.getStatusCode())
                .as("Статус обновления товара с ID %d должен быть 200", createdId)
                .isEqualTo(200);

        System.out.println("**** Товар обновлен *****");

        // 3. ПОЛУЧЕНИЕ ЧЕРЕЗ GET /goods/{id} для проверки
        Response getByIdResp = goodsApi.getGoodById(createdId);

        Assertions.assertThat(getByIdResp.getStatusCode())
                .as("Статус получения товара по ID %d должен быть 200", createdId)
                .isEqualTo(200);

        Good foundGood = getByIdResp.jsonPath().getObject("$", Good.class);
        Assertions.assertThat(foundGood)
                .as("Товар с ID %d должен существовать в ответе", createdId)
                .isNotNull();
        Assertions.assertThat(foundGood.getPrice())
                .as("Цена товара с ID %d должна быть обновлена до %f", createdId, newPrice)
                .isEqualTo(newPrice);

        System.out.println("********* Тест обновления товара пройден! *********");
    }

}
