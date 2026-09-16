package api.api_config;

import io.restassured.RestAssured;
import ui.SelenideTest.config.ConfigProvider;

public class ApiConfig {
    public static final String BASE_URL = ConfigProvider.getApiUrl();

    static {
        RestAssured.baseURI = BASE_URL;
    }
}
