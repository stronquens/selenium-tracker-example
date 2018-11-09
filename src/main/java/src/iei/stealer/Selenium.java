/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.iei.stealer;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Armado
 */
public class Selenium {

    private static WebDriver driver = null;

    public static void connectChrome() {
        String exePath = ".\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
    }

    public static void disconnectChrome() {
        driver.quit();
    }

    public static ArrayList<BookBean> trackFnac(String title, String autor) {
        driver.get("https://www.fnac.es/");
        // Open select category
        driver.findElement(By.cssSelector(".Header__aisle a")).click();
        // Select category
        WebDriverWait waiting;
        waiting = new WebDriverWait(driver, 1);
        waiting.until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector("li.select-option:nth-child(2)")));
        driver.findElement(By.cssSelector("li.select-option:nth-child(2)")).click();
        
        // Search in text field
        WebElement searchBox = driver.findElement(By.id("Fnac_Search"));
        searchBox.sendKeys(title + " " + autor);
        searchBox.submit();

        WebDriverWait waiting2;
        waiting2 = new WebDriverWait(driver, 10);
        waiting2.until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector(".Article-list")));

        List<WebElement> elementList
                = driver.findElements(By.cssSelector(".Article-item"));

        System.out.println("Número de elementos de la lista: " + elementList.size());
        // Process data
        ArrayList<BookBean> listBook = new ArrayList<BookBean>();
        for (WebElement elementoActual : elementList) {
            BookBean newBook = new BookBean("Fanc");
            try {
                newBook.setTitle(elementoActual
                        .findElement(By.cssSelector(".Article-desc a:first-child"))
                        .getText());
            } catch (Exception e) {
            }
            try {
                newBook.setAuthor(elementoActual
                        .findElement(By.cssSelector(".Article-descSub a:first-child"))
                        .getText());
            } catch (Exception e) {
            }
            try {
                newBook.setPrice(elementoActual
                        .findElement(By.cssSelector(".userPrice"))
                        .getText());
            } catch (Exception e) {
            }
            listBook.add(newBook);
        }
        return listBook;
    }
}
