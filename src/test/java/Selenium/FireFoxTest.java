package Selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class FireFoxTest {
    public WebDriver driver;
    public static final String HOST_NAME = "http://localhost:7070/";
    @Before
    public void setUp(){
        System.setProperty("webdriver.gecko.driver", "src\\test\\java\\Selenium\\drivers\\geckodriver.exe");
        driver = new FirefoxDriver();
    }
    @Test
    public void checkTitle(){

        driver.navigate().to(HOST_NAME);

        Assert.assertTrue( driver.getTitle().startsWith("Hello"));

    }
    @Test
    public void findAuth(){
        driver.navigate().to(HOST_NAME);
        WebElement enterButton = driver.findElement(By.id("navbardrop"));
        assertNotNull(enterButton);
    }
    @Test
    public void correctAuth() throws InterruptedException {
        driver.navigate().to(HOST_NAME);
        WebElement enterButton = driver.findElement(By.id("navbardrop"));
        enterButton.click();
        Thread.sleep(1000);
        WebElement loginField = driver.findElement(By.id("j_username"));
        loginField.sendKeys("seeker@mail.ru");
        WebElement passwordField = driver.findElement(By.id("j_password"));
        passwordField.sendKeys("seeker");
        Thread.sleep(1000);
        WebElement submitButton = driver.findElement(By.id("submit-button"));
        submitButton.click();
        Thread.sleep(1000);
        WebElement profileButton = driver.findElement(By.id("profile-href"));
        assertNotNull(profileButton);
    }
    @Test(expected = NoSuchElementException.class)
    public void incorrectAuth() throws InterruptedException {
        driver.navigate().to(HOST_NAME);
        WebElement enterButton = driver.findElement(By.id("navbardrop"));
        enterButton.click();
        Thread.sleep(1000);
        WebElement loginField = driver.findElement(By.id("j_username"));
        loginField.sendKeys("derimo@mail.ru");
        WebElement passwordField = driver.findElement(By.id("j_password"));
        passwordField.sendKeys("seeker");
        Thread.sleep(1000);
        WebElement submitButton = driver.findElement(By.id("submit-button"));
        submitButton.click();
        Thread.sleep(1000);
        WebElement profileButton = driver.findElement(By.id("profile-href"));
    }
    //
    @After
    public void clear(){
        if(driver != null) {
            driver.close();
        }
    }
    @Test
    public void searchTagsCorrect() throws InterruptedException {
        correctAuthForAnother();
        WebElement searchBox = driver.findElement(By.id("search_box"));
        searchBox.sendKeys("Gi");
        Thread.sleep(1000);
        WebElement advice = driver.findElement(By.className("advice_variant"));
        advice.click();
        Thread.sleep(100);
        WebElement icon = driver.findElement(By.id("searchButton-3"));
        WebElement searchButton = driver.findElement(By.className("my-sm-0"));
        searchButton.click();
        Thread.sleep(1000);
        WebElement resultElement = driver.findElement(By.className("list-group-item"));
        assertNotNull(resultElement);
    }
    @Test
    public void searchTagsIncorrect() throws InterruptedException {
        final String BAD_QUERY = "qwertyu";
        correctAuthForAnother();
        WebElement searchBox = driver.findElement(By.id("search_box"));
        searchBox.sendKeys(BAD_QUERY);
        Thread.sleep(1000);
        WebElement advice = driver.findElement(By.className("advice_variant"));
        assertEquals(advice.getText(),"По запросу \"" + BAD_QUERY +"\" ничего не найдено");
    }

    private void correctAuthForAnother() throws InterruptedException {
        driver.navigate().to(HOST_NAME);
        WebElement enterButton = driver.findElement(By.id("navbardrop"));
        enterButton.click();
        Thread.sleep(1000);
        WebElement loginField = driver.findElement(By.id("j_username"));
        loginField.sendKeys("seeker@mail.ru");
        WebElement passwordField = driver.findElement(By.id("j_password"));
        passwordField.sendKeys("seeker");
        Thread.sleep(1000);
        WebElement submitButton = driver.findElement(By.id("submit-button"));
        submitButton.click();
        Thread.sleep(1000);
    }
}
