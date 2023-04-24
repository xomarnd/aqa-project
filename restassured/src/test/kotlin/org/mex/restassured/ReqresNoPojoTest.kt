package org.mex.restassured

import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.function.Predicate

class ReqresNoPojoTest {

    private val URL = "https://reqres.in"

    @Test
    fun checkAvatarsNoPojoTest() {
        Specifications.installSpecification(
            Specifications.requestSpec(URL),
            Specifications.responseSpecOK200()
        )
        val response = RestAssured.given()
            .`when`()["api/users?page=2"]
            .then().log().all()
            .body("page", Matchers.equalTo(2))
            .body("data.id", Matchers.notNullValue())
            .body("data.email", Matchers.notNullValue())
            .body("data.first_name", Matchers.notNullValue())
            .body("data.last_name", Matchers.notNullValue())
            .body("data.avatar", Matchers.notNullValue())
            .extract().response()
        val jsonPath = response.jsonPath()
        val emails = jsonPath.get<List<String>>("data.email")
        val ids = jsonPath.get<List<Int>>("data.id")
        val avatars = jsonPath.get<List<String>>("data.avatar")
        for (i in avatars.indices) {
            Assertions.assertTrue(avatars[i].contains(ids[i].toString()))
        }
        Assertions.assertTrue(emails.stream().allMatch(Predicate { x: String -> x.endsWith("@reqres.in") }))
    }

    @Test
    fun successUserRegTestNoPojo() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200())
        val user: MutableMap<String, String> = HashMap()
        user["email"] = "eve.holt@reqres.in"
        user["password"] = "pistol"
        val response = RestAssured.given()
            .body(user)
            .`when`()
            .post("api/register")
            .then().log().all()
            .extract().response()
        val jsonPath = response.jsonPath()
        val id = jsonPath.get<Int>("id")
        val token = jsonPath.get<String>("token")
        Assertions.assertEquals(4, id)
        Assertions.assertEquals("QpwL5tke4Pnpja7X4", token)
    }

    @Test
    fun unsuccessUserRegNoPojo() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400())
        val user: MutableMap<String, String> = HashMap()
        user["email"] = "sydney@fife"
        val response = RestAssured.given()
            .body(user)
            .`when`()
            .post("api/register")
            .then().log().all()
            .extract().response()
        val jsonPath = response.jsonPath()
        val error = jsonPath.get<String>("error")
        Assertions.assertEquals("Missing password", error)
    }
}