package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Driver;

import java.time.Duration;

public class Main_Page {

    WebDriverWait wait;

    public Main_Page() {
        PageFactory.initElements(Driver.getDriver(), this);
        wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
    }

    @FindBy(xpath = "/html/body/main/div[1]/div[2]/form/div[1]/div[1]/div/ob-select/input")
    WebElement neredenButton;

    @FindBy(xpath = "//*[@id=\"destination-input\"]")
    WebElement nereyeButton;

    @FindBy(xpath = "(//input[@type='radio'])[2]")
    WebElement yarınButton;

    @FindBy(xpath = "(//button[@id='search-button'])[1]")
    WebElement otobusAraButton;

    public void neredenSec(String sehir) {
        neredenButton.click();
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//ul/li[.//span[contains(text(),'" + sehir + "')]]")));
            Driver.getDriver()
                    .findElement(By.xpath("//ul/li[.//span[contains(text(),'" + sehir + "')]]"))
                    .click();
        } catch (Exception e) {
            System.out.println("Nereden seçimi başarısız: " + e.getMessage());
        }
    }

    public void nereyeSec(String sehir) {
        nereyeButton.click();
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[@class='results'])[2]//li[@class='item']//span[contains(text(),'" + sehir + "')]")));
            Driver.getDriver()
                    .findElement(By.xpath("(//div[@class='results'])[2]//li[@class='item']//span[contains(text(),'" + sehir + "')]"))
                    .click();
        } catch (Exception e) {
            System.out.println("Nereye seçimi başarısız: " + e.getMessage());
        }
    }

    public void tarihOlarakYarınSec() {
        wait.until(ExpectedConditions.elementToBeClickable(yarınButton));
        yarınButton.click();
    }

    public void otobusAra() {
        wait.until(ExpectedConditions.elementToBeClickable(otobusAraButton));
        otobusAraButton.click();
    }

    public void otobusAramaYap(String nereden, String nereye) {
        neredenSec(nereden);
        nereyeSec(nereye);
        tarihOlarakYarınSec();
        otobusAra();
    }
}