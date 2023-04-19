package core

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType.LaunchOptions
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.Tracing.StartOptions
import com.microsoft.playwright.Tracing.StopOptions
import com.mex.s3.S3SetProperties
import io.qameta.allure.Allure
import org.mex.testutils.UUIDGenerator
import org.testng.ITestResult
import org.testng.annotations.AfterClass
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

open class BaseTest {
    private lateinit var browser: Browser
    protected lateinit var page: Page
    private lateinit var context: BrowserContext
    private val isTraceEnabled = true

    /**
     * Инициализация браузера и его настроек перед запуском всех тестов в классе
     */
    @BeforeClass
    fun setUp() {
        // инициализация браузера с настройками
        browser = Playwright
            .create()
            .chromium()
            .launch(LaunchOptions().setHeadless(false).setChannel("chrome"))

        // создаем контекст для браузера
        context = browser.newContext()

        // трейсинг замедляет скорость заполнение полей
        if (isTraceEnabled) {
            context.tracing().start(
                StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false)
            )
        }
        // создаем новую страницу
        page = context.newPage()
    }

    /**
     * Закрывает браузер после выполнения всех тестов в классе
     */
    @AfterClass
    fun tearDown() {
        browser.close()
    }

    /**
     * Добавляет вложения к упавшему тесту. Скриншот, исходный код страницы, трейсинг
     *
     * @param result данные о тесте
     * @throws IOException
     */
    @AfterMethod
    @Throws(IOException::class)
    fun attachFilesToFailedTest(result: ITestResult) {
        if (!result.isSuccess) {
            val uuid = UUIDGenerator().generateAndToString()
            val screenshot = page.screenshot(
                Page.ScreenshotOptions()
                    .setPath(Paths.get("build/allure-results/screenshot_" + uuid + "screenshot.png"))
                    .setFullPage(true)
            )
            Allure.addAttachment(uuid, ByteArrayInputStream(screenshot))
            Allure.addAttachment("source.html", "text/html", page.content())

            if (isTraceEnabled) {
                val fileName = "s3properties.json"
                val fileUrl = javaClass.classLoader.getResource(fileName)
                val filePath = Paths.get(fileUrl!!.toURI()).toString()
                val s3 = S3SetProperties(filePath).s3FileStorage

                val tempTracePath: Path = Files.createTempFile("trace-", ".zip")
                context.tracing().stop(StopOptions().setPath(tempTracePath))

                val outputStream = ByteArrayOutputStream().apply {
                    FileInputStream(tempTracePath.toFile()).use { inputStream ->
                        inputStream.copyTo(this, bufferSize = 1024)
                    }
                }

                val traceFileName = String.format("%s_trace.zip", uuid)
                val traceInputStream: InputStream = ByteArrayInputStream(outputStream.toByteArray())
                s3.putObject(traceFileName, traceInputStream.readBytes())
                val presignedUrl = s3.getPresignedUrl(traceFileName)

                Files.delete(tempTracePath)

                val traceUrl = "https://trace.playwright.dev/?trace="
                Allure.step("Go playwright trace: ${traceUrl + presignedUrl}")
            }
        }
    }
}
