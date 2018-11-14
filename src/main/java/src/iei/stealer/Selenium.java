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
            BookBean newBook = new BookBean("Fnac");
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

    /*public static ArrayList<BookBean> trackCorteIngles(String title, String autor){
        driver.get("https://www.elcorteingles.es/");
        
        WebElement cookiesWindow = driver.findElement(By.id("cookies-agree"));
        if(cookiesWindow != null){
            cookiesWindow.click();
        }

        // Open select category
        driver.findElement(By.id("drilldown")).click();
        // Select category
        WebDriverWait waiting;
        waiting = new WebDriverWait(driver, 1);
        waiting.until(ExpectedConditions
                .presenceOfElementLocated(By.id("md-10")));
        
        driver.findElement(By.id("md-10")).click();
        driver.findElement(By.xpath("//*[@id=\"megadrop-list\"]/li[11]/div/div/div[1]/ul[2]/li[9]/a")).click();
        
        ArrayList<BookBean> listBook = new ArrayList<BookBean>();
        return listBook;
    }*/
    public static ArrayList<BookBean> trackAmazon(String title, String autor) {
        driver.get("https://www.amazon.es/");

        // Open select category
        driver.findElement(By.id("nav-link-shopall")).click();

        // Select category
        WebDriverWait waiting;
        waiting = new WebDriverWait(driver, 1);
        waiting.until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//*[@id=\"shopAllLinks\"]/tbody/tr/td[2]/div[3]/ul/li[1]/a")));
        driver.findElement(By.xpath("//*[@id=\"shopAllLinks\"]/tbody/tr/td[2]/div[3]/ul/li[1]/a")).click();

        // Search in text field
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys(title + " " + autor);
        searchBox.submit();

        boolean existNext = true;
        ArrayList<BookBean> listBook = new ArrayList<BookBean>();
        while (existNext) {
            // List Elements
            WebDriverWait waiting2;
            waiting2 = new WebDriverWait(driver, 10);
            waiting2.until(ExpectedConditions
                    .presenceOfElementLocated(By.id("s-results-list-atf")));

            List<WebElement> elementList
                    = driver.findElements(By.cssSelector(".s-result-item"));

            System.out.println("Número de elementos de la lista: " + elementList.size());

            //Process Data
            for (WebElement elementoActual : elementList) {
                BookBean newBook = new BookBean("Amazon");
                try {
                    newBook.setTitle(elementoActual
                            .findElement(By.cssSelector(".s-access-title"))
                            .getText());
                } catch (Exception e) {
                }

                try {
                    newBook.setAuthor(elementoActual
                            .findElement(By.cssSelector(".s-item-container > div > div > div:last-child > div:first-child > div:last-child span:last-child"))
                            .getText());
                } catch (Exception e) {
                }
                try {
                    newBook.setPrice(elementoActual
                            .findElement(By.cssSelector(".s-price"))
                            .getText());
                } catch (Exception e) {
                }
                listBook.add(newBook);
            }
            try {
               driver.findElement(By.cssSelector("a > #pagnNextString")).click();
            } catch (Exception e) {
                existNext = false;
            }
        }
        return listBook;
    }
}
