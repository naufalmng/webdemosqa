package com.belajar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebDemoSqaTest {
    WebDriver driver;
    Actions actions;
    String url;

    @BeforeTest
    public void setup() {
        url = "http://localhost:8000/admin";
        driver = new ChromeDriver();
        driver.get(url);
        driver.manage().window().maximize();
        actions = new Actions(driver);
    }

    @Test()
    public void loginTestNegative() {
        WebElement usernameField = driver.findElement(By.id("id_username"));
        WebElement passwordField = driver.findElement(By.id("id_password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[3]/input"));

        Action action = actions.sendKeys(usernameField, "naufall")
                .pause(Duration.ofSeconds(1))
                .sendKeys(passwordField, "naufalmng")
                .pause(Duration.ofSeconds(1))
                .moveToElement(loginButton)
                .click()
                .build();

        action.perform();

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement errorNoteElem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[@class='errornote']"))
            );
            String expected = "Please enter the correct username and password for a staff account. Note that both " +
                    "fields may be case-sensitive.";
            String actual = errorNoteElem.getText().trim();
            Assert.assertEquals(actual, expected);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test(priority = 2)
    public void loginTestPositive() {
        sleep(2000);
        driver.get(url);
        WebElement usernameField = driver.findElement(By.id("id_username"));
        WebElement passwordField = driver.findElement(By.id("id_password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[3]/input"));

        Action action = actions.sendKeys(usernameField, "naufal")
                .pause(Duration.ofSeconds(1))
                .sendKeys(passwordField, "naufalmng")
                .pause(Duration.ofSeconds(1))
                .moveToElement(loginButton)
                .click()
                .build();

        action.perform();

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement welcomeTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"content\"]/h1"))
            );
            String expected = "Welcome to Demo SQA Testing Portal";
            String actual = welcomeTextElement.getText();
            Assert.assertEquals(actual, expected);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test(priority = 3)
    public void addCategory() {
        sleep(2000);
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addCategoryBtnElem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//a[@href='/admin/products/category/add/']"))
        );

        addCategoryBtnElem.click();

        WebElement categoryNameFieldElem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                "//*[@id=\"id_name\"]"))
        );
        WebElement saveAndAddAnotherBtnElem = driver.findElement(
                By.xpath("//*[@id=\"category_form\"]/div/div/input[2]")
        );

        Action action = actions.pause(Duration.ofSeconds(2))
                .moveToElement(categoryNameFieldElem)
                .click()
                .sendKeys("Makanan")
                .pause(Duration.ofSeconds(2))
                .moveToElement(saveAndAddAnotherBtnElem)
                .click()
                .pause(Duration.ofSeconds(2))
                .sendKeys("Minuman")
                .pause(Duration.ofSeconds(2))
                .build();

        action.perform();

        WebElement saveBtnElem = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#category_form > div > div > input.default"))
        );

        saveBtnElem.click();
        sleep(2000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"changelist-form\"]/div[2]"))
        );

        List<String> categoryNameList = driver.findElements(By.cssSelector(
                "#result_list > tbody > tr > th > a")
        ).stream().map(WebElement::getText).collect(Collectors.toList());

        Assert.assertTrue(categoryNameList.containsAll(Arrays.asList("Makanan","Minuman")));

        driver.close();
    }
    private void sleep(long milliseconds) {
        try{
            Thread.sleep(milliseconds);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}