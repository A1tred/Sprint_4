package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {
    private WebDriver driver;

    // Конструктор
    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // Локаторы для первой страницы заказа
    // Поле «Имя»
    private By firstNameField = By.xpath(".//input[@placeholder='* Имя']");

    // Поле «Фамилия»
    private By lastNameField = By.xpath(".//input[@placeholder='* Фамилия']");

    // Поле «Адрес»
    private By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");

    // Поле «Станция метро»
    private By metroStationField = By.xpath(".//input[@placeholder='* Станция метро']");

    // Выпадающий список станций метро
    private By metroStationDropdown = By.className("select-search__option");

    // Поле «Телефон»
    private By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка «Далее»
    private By nextButton = By.xpath(".//button[text()='Далее']");

    // Локаторы для второй страницы заказа
    // Поле «Дата доставки»
    private By deliveryDateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");

    // Поле «Срок аренды»
    private By rentalPeriodField = By.className("Dropdown-control");

    // Опция срока аренды
    private By rentalPeriodOption(String period) {
        return By.xpath(".//div[@class='Dropdown-menu']/div[text()='" + period + "']");
    }

    // Чекбокс «Цвет самоката»
    private By scooterColor(String color) {
        return By.id(color);
    }

    // Поле «Комментарий для курьера»
    private By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");

    // Кнопка «Заказать»
    private By orderButton = By.xpath(".//div[@class='Order_Buttons__1xGrp']/button[text()='Заказать']");
    //private By orderButton = By.xpath(".//button[text()='Заказать']");

    // Кнопка подтверждения заказа «Да»
    private By confirmOrderButton = By.xpath(".//button[text()='Да']");

    // Всплывающее окно об успешном заказе
    private By successModal = By.xpath(".//div[text()='Заказ оформлен']");

    public void waitForLoadOrderPage() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(firstNameField));}

    // Методы для заполнения первой страницы заказа
    // Заполнение поля "Имя"
    public void enterFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    // Заполнение поля "Фамилия"
    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    // Заполнение поля "Адрес"
    public void enterAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    // Заполнение поля "Станция метро"
    public void selectMetroStation(String station) {
        try {
            driver.findElement(metroStationField).click();
            driver.findElement(metroStationField).sendKeys(station);
            // Используем явное ожидание появления списка станций
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(metroStationDropdown));
            driver.findElement(metroStationDropdown).click();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Станция метро не найдена: " + station);
        }}

    // Заполнение поля "Телефон"
    public void enterPhoneNumber(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    // Нажатие кнопки "Далее"
    public void clickNextButton() {
        driver.findElement(nextButton).click();
    }

    // Методы для заполнения второй страницы заказа
    // Заполнение поля "Когда привезти самокат"
    public void enterDeliveryDate(String date) {
        WebElement dateField = driver.findElement(deliveryDateField);
        dateField.click();
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.ENTER);
    }

    // Заполнение поля "Срок аренды"
    public void selectRentalPeriod(String period) {
        driver.findElement(rentalPeriodField).click();
        driver.findElement(rentalPeriodOption(period)).click();
    }

    // Выбор цвета самоката
    public void selectScooterColor(String color) {
        driver.findElement(scooterColor(color)).click();
    }

    // Заполнение поля "Комментарий для курьера"
    public void enterComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
    }

    // Нажатие кнопки "Заказать"
    public void clickOrderButton() {
        driver.findElement(orderButton).click();
    }

    // Подтверждение заказа по кнопке "Да"
    public void confirmOrder() {
        driver.findElement(confirmOrderButton).click();
    }

    // Метод для проверки успешного заказа
    public boolean isOrderConfirmed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
            return driver.findElement(successModal).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}