package api.api_config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;


public class ReqSpec {

    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(ApiConfig.BASE_URL)
            .addHeader("Content-Type", "application/json")
            .log(LogDetail.ALL)
            .build();

}
