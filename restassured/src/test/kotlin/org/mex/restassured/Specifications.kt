package org.mex.restassured

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification


object Specifications {
    fun requestSpec(url: String): RequestSpecification {
        return RequestSpecBuilder()
            .addHeader("Accept", "application/json")
            .setContentType(ContentType.JSON)
            .build()
    }


    fun responseSpecOK200(): ResponseSpecification =
        ResponseSpecBuilder()
            .expectStatusCode(200)
            .build()

    fun responseSpecError400(): ResponseSpecification =
        ResponseSpecBuilder()
            .expectStatusCode(400)
            .build()

    fun responseSpec(status: Int): ResponseSpecification =
        ResponseSpecBuilder()
            .expectStatusCode(status)
            .build()

    fun installSpecification(
        requestSpec: RequestSpecification,
        responseSpec: ResponseSpecification
    ) {
        RestAssured.requestSpecification = requestSpec
        RestAssured.responseSpecification = responseSpec
    }

    fun installSpecification(
        requestSpec: RequestSpecification
    ) {
        RestAssured.requestSpecification = requestSpec
    }

    fun installSpecification(
        responseSpec: ResponseSpecification
    ) {
        RestAssured.responseSpecification = responseSpec
    }
}