package core

import com.microsoft.playwright.Dialog
import com.microsoft.playwright.ElementHandle
import com.microsoft.playwright.Locator.DragToOptions
import com.microsoft.playwright.Response
import org.testng.Assert
import org.testng.annotations.Test
import pages.WebTablePage
import java.util.function.Consumer
import java.util.function.Predicate

class SearchTests : BaseTest() {
    @Test
    fun fillManyTextBoxes() {
        page.navigate("https://datatables.net/examples/api/form.html")
        // Выбираем из выпадающего списка значение
        page.selectOption("[name=example_length]", "50")
        // Получаем нужын элементы на странице
        val fields = page.querySelectorAll("//tbody//input[@type='text']")
        // Заполняем все элементы текстом
        fields.forEach(Consumer { x: ElementHandle -> x.fill("threadqa playwright") })
    }

    @Test
    fun textBoxFillTest() {
        page.navigate("http://85.192.34.140:8081/")
        page.getByText("Elements").click()
        page.querySelector("//li[@id='item-0']/span[1]").click()
        page.fill("[id=userName]", "ThreadQA Test")
        page.fill("[id=userEmail]", "threadqa@gmail.com")
        page.fill("[id=currentAddress]", "somewhere")
        page.click("[id=submit]")

        // Проверяем, что после заполнения формы, появился другой блок
        Assert.assertTrue(page.isVisible("[id=output]"))
        // Проверяем, что в появившемся блоке, текст содержит предыдущий текст
        Assert.assertTrue(page.locator("[id=name]").textContent().contains("NOT ThreadQA Test"))
    }

    @Test
    fun sliderTest() {
        page.navigate("http://85.192.34.140:8081/")
        page.getByText("Widgets").click()
        page.getByText("Slider").click()
        val slider = page.locator(".range-slider")
        // Двигаем слайдер слева на право на 20 пикселей
        slider.dragTo(slider, DragToOptions().setTargetPosition(slider.boundingBox().x + 20, 0.0))
        // Проверяем, что старое значение слайдера не равно прежнему
        Assert.assertNotEquals("25", page.locator("[id=sliderValue]").getAttribute("value"))
    }

    @Test
    fun alertTest() {
        page.navigate("http://85.192.34.140:8081/")
        page.getByText("Alerts, Frame").first().click()
        page.locator("//*[@id='item-1']//following::span[text()='Alerts']").click()
        page.locator("[id=alertButton]").click()
        // Метод который позволяет переключится на диалоговое окно
        page.onDialog(
            Consumer { dialog: Dialog ->
            Assert.assertEquals("alert", dialog.type())
            // Проверка что сообщение в окне появилось нужное
            Assert.assertEquals(dialog.message(), "You clicked a button")
            // Нажимаем ОК в диалоговом окне
            dialog.accept()
        }
        )
    }

    @Test(priority = 1)
    fun networkTest() {
        // открываем нужную страницу
        page.navigate("http://85.192.34.140:8081/")
        // кликаем по элементу с помощью текста
        page.getByText("Elements").click()
        // клик по первому элементу, подходящий под условие если их найдено много
        page.getByText("Links").first().click()

        // Runnable операция при определенных действий с сетью в браузере
        page.waitForRequest(
            "http://85.192.34.140/api/bad-request",
            Runnable {
            // по нажатию кнопки должен отправизся запрос по адресу
            page.locator("[id=bad-request]").click()
        }
        )

        // Чтение последнего запроса из браузера из вкладки Network
        page.onResponse(
            Consumer { response: Response ->
            Assert.assertEquals(response.status(), 400)
            Assert.assertEquals(response.url(), "http://85.192.34.140/api/bad-request")
            Assert.assertEquals(response.request().method(), "GET")
        }
        )
    }

    @Test
    fun searchForExactTitleTest() {
        page.navigate("http://85.192.34.140:8081/")
        page.getByText("Elements").click()
        page.getByText("Web Tables").click()

        // Реализация PageObject паттерна
        val name = "Kierra"
        val webTablePage = WebTablePage(page)
        webTablePage.search(name)
        val namesInTable = webTablePage.visibleNames

        // Проверяем, что найден всего 1 элемент в таблице
        Assert.assertEquals(namesInTable.size, 1)
        // Проверяем, что в таблице присутсвует искомое название
        Assert.assertTrue(namesInTable.stream().anyMatch(Predicate { x: String -> x.contains(name) }))
    }
}
