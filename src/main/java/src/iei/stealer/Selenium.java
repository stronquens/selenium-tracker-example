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
        ArrayList<BookBean> listBook = new ArrayList<BookBean>();
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

        // Check No results page
        try {
            driver.findElement(By.cssSelector(".noResults"));
            BookBean unknown = new BookBean("Fnac");
            unknown.setUnknown();
            listBook.add(unknown);
            return listBook;
        } catch (Exception e) {
        }

        // Get elements from page
        boolean existNext = true;
        while (existNext) {
            WebDriverWait waiting2;
            waiting2 = new WebDriverWait(driver, 10);
            waiting2.until(ExpectedConditions
                    .presenceOfElementLocated(By.cssSelector(".Article-list")));

            List<WebElement> elementList
                    = driver.findElements(By.cssSelector(".Article-item"));

            System.out.println("Número de elementos de la lista: " + elementList.size());
            // Process data
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
                    String oldPrice = "";
                    try{
                        WebElement oldPriceW = elementoActual.findElement(By.cssSelector(".oldPrice"));
                        oldPrice = oldPriceW.getText();
                    } catch (Exception e){
                    }
                    if(!"".equals(oldPrice)){
                        newBook.setPrice(oldPrice);
                        newBook.setDiscPrice(elementoActual
                                .findElement(By.cssSelector(".userPrice"))
                                .getText());
                    } else {
                        newBook.setPrice(elementoActual
                                .findElement(By.cssSelector(".userPrice"))
                                .getText());
                    }
                } catch (Exception e) {
                }
                if (!newBook.getTitle().equalsIgnoreCase("")) {
                    listBook.add(newBook);
                }
            }

            // Change Page
            try {
                driver.findElement(By.cssSelector(".actionNext:last-child > i")).click();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                existNext = false;
            }
        }
        return listBook;
    }

    public static ArrayList<BookBean> trackAmazon(String title, String autor) {
        ArrayList<BookBean> listBook = new ArrayList<BookBean>();
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

        // Check No results page
        try {
            driver.findElement(By.cssSelector("#noResultsTitle"));
            BookBean unknown = new BookBean("Amazon");
            unknown.setUnknown();
            listBook.add(unknown);
            return listBook;
        } catch (Exception e) {
        }

        // Moves pages
        boolean existNext = true;
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
                // Book with useful data
                if (!newBook.getTitle().equalsIgnoreCase("")) {
                    listBook.add(newBook);
                }
            }

            // Check final page
            try {
                driver.findElement(By.cssSelector("a > #pagnNextString")).click();
            } catch (Exception e) {
                existNext = false;
            }
        }
        return listBook;
    }
}
