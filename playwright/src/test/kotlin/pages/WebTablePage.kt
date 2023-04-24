package pages

import com.microsoft.playwright.ElementHandle
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.WaitForSelectorState
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors

class WebTablePage(private val page: Page) {
    private val searchBoxLocator = "#searchBox"
    private val allRowsLocator = ".rt-tr-group"

    /**
     * Ищет в таблице данные
     * @param query запрос для поиска
     */
    fun search(query: String?) {
        // заполнение текстового поля
        page.fill(searchBoxLocator, query)
        val expectedSelector = "//div[@class='rt-td' and text()='$query']"
        // намеренное ожидание элемента на странице с использованием state.
        page.waitForSelector(expectedSelector, Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE))
    }

    val visibleNames: List<String>
        /**
         * Получает из таблицы не пустые строчки
         * @return список с строчками
         */
        get() = page.querySelectorAll(allRowsLocator) // находит коллекцию элементов
            .stream()
            // достает текст из каждого элемента
            .map(Function { x: ElementHandle -> x.innerText() })
            // условие если текст из строчки не начинается на пробел
            .filter(Predicate { x: String -> !x.startsWith(" ") })
            // собирает результаты в список
            .collect(Collectors.toList())
}
