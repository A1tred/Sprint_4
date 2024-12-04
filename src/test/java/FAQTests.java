import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FAQTests {
    private WebDriver driver;
    private MainPage mainPage;

    private final int questionIndex;
    private final String expectedAnswer;

    public FAQTests(int questionIndex, String expectedAnswer) {
        this.questionIndex = questionIndex;
        this.expectedAnswer = expectedAnswer;
    }

    // Параметры для теста: индекс вопроса и ожидаемый ответ
    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {7, "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
        });
    }

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
        // Инициализация WebDriver с использованием WebDriverManager
        driver = new ChromeDriver(options);
        // Неявное ожидание
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        // Открытие страницы
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
    }

    @Test
    public void testFAQAnswer() {
        // Скролл до секции FAQ
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", mainPage.getFAQSection());
        // Клик по вопросу
        mainPage.clickQuestion(questionIndex);
        // Ожидание появления ответа
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(mainPage.getAnswerElement(questionIndex)));
        // Получение текста ответа
        String actualAnswer = mainPage.getAnswerText(questionIndex);
        // Сравнение с ожидаемым ответом
        Assert.assertEquals("Ответ на вопрос не совпадает с ожидаемым", expectedAnswer, actualAnswer);
    }

    @After
    public void tearDown() {
        // Закрытие браузера
        driver.quit();
    }
}