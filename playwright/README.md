

## Playwright Trace Module


Данный модуль предназначен для демонстрации работы фреймворка Playwright. Он также предоставляет возможность автоматической загрузки trace-файлов в MinIO (совместимый с Amazon S3 хранилище), который настроен с помощью Docker Compose. С использованием CORS, trace-файлы могут быть открыты на сайте https://trace.playwright.dev/.

**Требования**

1. Docker
2. Docker Compose


Использование  
После успешной настройки проекта, вы можете использовать Playwright для выполнения ваших автоматизированных тестов. Trace-файлы будут автоматически загружены в ваше MinIO хранилище и могут быть открыты на сайте https://trace.playwright.dev/ с помощью ссылки на объект из хранилища в отчете Allur.

При создании автоматического теста с использованием Playwright, убедитесь, что включена опция трассировки:

    open class BaseTest {  
	    ...
	    private val isTraceEnabled = true
	    ...

## Дополнительная информация

-   [Playwright документация](https://playwright.dev/docs/intro)
-   [MinIO документация](https://docs.min.io/)