package org.mex.restassured

import io.qameta.allure.Feature
import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mex.restassured.colors.Data
import org.mex.restassured.registration.Register
import org.mex.restassured.registration.SuccessUserReg
import org.mex.restassured.registration.UnsuccessUserReg
import org.mex.restassured.users.UserData
import org.mex.restassured.users.UserTime
import org.mex.restassured.users.UserTimeResponse
import java.time.Clock
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors

@DisplayName("Апи тесты с Pojo классами")
@Feature("Api Regres Pojo")
class ReqresPojoTest {

    private val URL = "https://reqres.in"

    /**
     * 1. Получить список пользователей со второй страница на сайте https://reqres.in/
     * 2. Убедиться что id пользователей содержаться в их avatar;
     * 3. Убедиться, что email пользователей имеет окончание reqres.in;
     */
    @Test
    @DisplayName("Аватары содержат айди пользователей")
    fun checkAvatarContainsIdTest() {
        Specifications.installSpecification(
            Specifications.requestSpec(URL),
            Specifications.responseSpecOK200()
        )
        //1 способ сравнивать значения напрямую из экземпляров класса
        val users = RestAssured.given()
            .`when`()["api/users?page=2"]
            .then()
            .log().all()
            .extract().body().jsonPath().getList("data", UserData::class.java)

        //проверка аватар содержит айди
        users.forEach(Consumer { x: UserData -> Assertions.assertTrue(x.avatar!!.contains(x.id.toString())) })
        //проверка почты оканчиваются на reqres.in
        Assertions.assertTrue(users.stream().allMatch(Predicate { x: UserData -> x.email!!.endsWith("@reqres.in") }))

        //2 способ сравнивать значения через получения списков
        //список с аватарками
        val realPeopleAvatars = users.stream()
            .map(Function { obj: UserData -> obj.avatar })
            .collect(Collectors.toList())
        //список с айди
        val realPeopleIds = users.stream()
            .map(Function { x: UserData -> x.id.toString() })
            .collect(Collectors.toList())
        //проверка через сравнение двух списков
        for (i in realPeopleAvatars.indices) {
            Assertions.assertTrue(realPeopleAvatars[i]!!.contains(realPeopleIds[i]))
        }
    }

    /**
     * 1. Используя сервис https://reqres.in/ протестировать регистрацию пользователя в системе
     * 2. Тест для успешной регистрации
     */
    @Test
    @DisplayName("Успешная регистрация")
    fun successUserRegTest() {
        val UserId = 4
        val UserPassword = "QpwL5tke4Pnpja7X4"
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200())
        val user = Register("eve.holt@reqres.in", "pistol")
        val successUserReg = RestAssured.given()
            .body(user)
            .`when`()
            .post("api/register")
            .then()
            .log().all()
            .extract().`as`(SuccessUserReg::class.java)
        Assertions.assertNotNull(successUserReg.id)
        Assertions.assertNotNull(successUserReg.token)
        Assertions.assertEquals(UserId, successUserReg.id)
        Assertions.assertEquals(UserPassword, successUserReg.token)
    }

    /**
     * 1. Используя сервис https://reqres.in/ протестировать регистрацию пользователя в системе
     * 2. Тест для неуспешной регистрации (не введен пароль)
     */
    @Test
    @DisplayName("Не успешная регистрация")
    fun unSuccessUserRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400())
        val peopleSecond = Register("sydney@fife", "")
        val unSuccessUserReg = RestAssured.given()
            .body(peopleSecond)
            .`when`()
            .post("/api/register")
            .then() //.assertThat().statusCode(400) проверить статус ошибки, если не указана спецификация
            .log().body()
            .extract().`as`(UnsuccessUserReg::class.java)
        Assertions.assertNotNull(unSuccessUserReg.error)
        Assertions.assertEquals("Missing password", unSuccessUserReg.error)
    }

    /**
     * Используя сервис https://reqres.in/ убедиться, что операция LIST<RESOURCE> возвращает данные,
     * отсортированные по годам.
    </RESOURCE> */
    @Test
    @DisplayName("Года правильно отсортированы")
    fun checkSortedYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200())
        val data = RestAssured.given()
            .`when`()["/api/unknown"]
            .then()
            .log().all()
            .extract().body().jsonPath().getList("data", Data::class.java)
        val dataYears = data.stream().map(Function { obj: Data -> obj.year }).collect(Collectors.toList())
        val sortedDataYears = dataYears.stream().sorted().collect(Collectors.toList())
        Assertions.assertEquals(dataYears, sortedDataYears)
        println(dataYears)
        println(sortedDataYears)
    }

    /**
     * Тест 4.1
     * Используя сервис https://reqres.in/ попробовать удалить второго пользователя и сравнить статус-код
     */
    @Test
    @DisplayName("Удаление пользователя")
    fun deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(204))
        RestAssured.given().`when`().delete("/api/users/2")
            .then()
            .log().all()
    }

    /**
     * Используя сервис https://reqres.in/ обновить информацию о пользователе и сравнить дату обновления с текущей датой на машине
     */
    @Test
    @DisplayName("Время сервера и компьютера совпадают")
    fun checkServerAndPcDateTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200())
        val user = UserTime("morpheus", "zion resident")
        val response = RestAssured.given()
            .body(user)
            .`when`()
            .put("/api/users/2")
            .then().log().all()
            .extract().`as`(UserTimeResponse::class.java)

        //так как время считается в плоть до миллисекунд, необходимо убрать последние 5 символов, чтобы время было одинаковое
        val regex = "(.{5})$"
        val currentTime = Clock.systemUTC().instant().toString().replace(regex.toRegex(), "")
        Assertions.assertEquals(response.updatedAt.replace(regex.toRegex(), ""), currentTime)
    }
}