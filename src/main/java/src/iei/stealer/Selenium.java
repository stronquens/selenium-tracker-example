/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.iei.stealer;

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

    public static void track() {
        connectChrome();
        trackFnac();
    }

    private static void connectChrome() {
        String exePath = "D:\\Users\\Armando\\Downloads\\chromedriver_win32\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        // driver.get("http://www.fnac.es");
        // driver.quit();
    }

    private static void trackFnac() {
        driver.get("https://www.fnac.es/");
        WebElement cajaBusqueda = driver.findElement(By.id("Fnac_Search"));
        cajaBusqueda.sendKeys("harry potter");
        cajaBusqueda.submit();

        /* Esperar a que se muestre el elemento Cafeteras */
        WebDriverWait waiting;
        waiting = new WebDriverWait(driver, 10);
        waiting.until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector(".Article-list")));

        List<WebElement> listaElementos
                = driver.findElements(By.cssSelector(".Article-item"));

        System.out.println("Número de elementos de la lista: " + listaElementos.size());

        WebElement aux;
        for (WebElement elementoActual : listaElementos) {
            aux = elementoActual.findElement(By.cssSelector(".Article-desc a:first-child"));
            System.out.println("Titulo: " + aux.getText());
        }
    }
}
