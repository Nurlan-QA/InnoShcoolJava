package api.api_config;

import io.restassured.RestAssured;

public class ApiConfig {
    // Замени на реальный URL твоего учебного API
    public static final String BASE_URL = "http://localhost:8080";

    static {
        RestAssured.baseURI = BASE_URL;
    }
}