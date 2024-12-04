package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {
    private WebDriver driver;

    // Конструктор
    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    // Локаторы
    // Кнопка «Заказать» в верхней части страницы
    private By orderButtonTop = By.className("Button_Button__ra12g");

    // Кнопка «Заказать» в нижней части страницы
    //private By orderButtonBottom = By.xpath(".//button[contains(@class,'Button_UltraBig__UU3Lp')]");
    private By orderButtonBottom = By.xpath(".//div[@class='Home_FinishButton__1_cWm']/button[text()='Заказать']");

    // Вопросы в секции «Вопросы о важном»
    private By questions = By.xpath(".//div[@class='accordion']/div");

    // Метод для нажатия на кнопку «Заказать» вверху страницы
    public void clickOrderButtonTop() {
        driver.findElement(orderButtonTop).click();
    }

    // Метод для нажатия на кнопку «Заказать» внизу страницы
    public void clickOrderButtonBottom() {
        // Находим кнопку «Заказать» внизу страницы
        WebElement orderButton = driver.findElement(orderButtonBottom);
        // Скроллим к этой кнопке с помощью JavascriptExecutor
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderButton);
        // Ожидаем, пока кнопка станет кликабельной
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        // Кликаем по кнопке
        orderButton.click();
    }

    // Метод для получения веб-элемента секции FAQ (может понадобиться для скролла)
    public WebElement getFAQSection() {
        // Можно использовать ID первого вопроса для доступа к секции
        return driver.findElement(By.id("accordion__heading-0"));
    }

    // Получение элемента ответа по индексу
    public WebElement getAnswerElement(int index) {
        String answerId = "accordion__panel-" + index;
        return driver.findElement(By.id(answerId));
    }

    // Метод для нажатия на вопрос по его порядковому номеру (0 - базовый индекс)
    public void clickQuestion(int index) {
        // Находим список вопросов
        List<WebElement> questionElements = driver.findElements(questions);
        // Проверяем, что индекс находится в пределах списка
        if (index >= 0 && index < questionElements.size()) {
            WebElement questionElement = questionElements.get(index);
            // Скроллим к вопросу
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", questionElement);
            // Ожидаем, пока элемент станет кликабельным
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(questionElement));
            // Кликаем по вопросу
            questionElement.click();
        } else {
            throw new IllegalArgumentException("Индекс вопроса вне диапазона: " + index);
        }
    }

    // Метод для получения текста ответа на вопрос по его порядковому номеру
    public String getAnswerText(int index) {
        String answerId = "accordion__panel-" + index;
        By answerLocator = By.id(answerId);
        // Ожидаем, пока ответ станет видимым
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator));
        // Получаем текст ответа
        return driver.findElement(answerLocator).getText();
    }
}