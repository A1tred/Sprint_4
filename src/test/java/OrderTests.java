import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import pages.MainPage;
import pages.OrderPage;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OrderTests {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;

    // Параметры для теста
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String date;
    private final String rentalPeriod;
    private final String color;
    private final String comment;
    private final String orderButton; // "top" или "bottom"

    public OrderTests(String firstName, String lastName, String address,
                      String metroStation, String phone, String date,
                      String rentalPeriod, String color, String comment, String orderButton) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
        this.orderButton = orderButton;
    }

    // Наборы данных для теста
    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"Иван", "Иванов", "Москва, ул. Пушкина, д.1", "Пушкинская", "+79991234567",
                        "10.11.2023", "двое суток", "black", "Позвонить за час", "top"},
                {"Анна", "Петрова", "Санкт-Петербург, Невский пр., д.10", "Невский проспект", "+79997654321",
                        "15.11.2023", "трое суток", "grey", "Не звонить", "bottom"}
        });
    }

    @Before
    public void setUp() {
        // Для указания нужного браузера, можно изменить значение по умолчанию
        // Либо указать нужный браузер через VM options (пример: -Dbrowser=firefox)
        String browser = System.getProperty("browser", "chrome");
        if (browser.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
            driver = new ChromeDriver(options);
        } else if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
            driver = new FirefoxDriver(options);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);}


    @Test
    public void testOrderScooter() {
        // Нажимаем на нужную кнопку заказа
        if (orderButton.equals("top")) {
            mainPage.clickOrderButtonTop();
        } else {
            mainPage.clickOrderButtonBottom();
        }

        orderPage.waitForLoadOrderPage();

        // Заполняем первую страницу заказа
        orderPage.enterFirstName(firstName);
        orderPage.enterLastName(lastName);
        orderPage.enterAddress(address);
        orderPage.selectMetroStation(metroStation);
        orderPage.enterPhoneNumber(phone);
        orderPage.clickNextButton();

        // Заполняем вторую страницу заказа
        orderPage.enterDeliveryDate(date);
        orderPage.selectRentalPeriod(rentalPeriod);
        orderPage.selectScooterColor(color);
        orderPage.enterComment(comment);
        orderPage.clickOrderButton();
        orderPage.confirmOrder();

        // Проверяем, что заказ был успешно оформлен
        Assert.assertTrue("Заказ не был оформлен успешно", orderPage.isOrderConfirmed());
    }

    @After
    public void tearDown() {
        // Закрываем браузер
        driver.quit();
    }
}