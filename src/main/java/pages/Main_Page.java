package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Driver;

import java.time.Duration;
import java.util.List;

public class Main_Page {

    WebDriverWait wait;
    public Main_Page(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "/html/body/main/div[1]/div[2]/form/div[1]/div[1]/div/ob-select/input")
    WebElement neredenButton;

    @FindBy(xpath = "//*[@id=\"destination-input\"]")
    WebElement nereyeButton;

    @FindBy(xpath = "(//input[@type='radio'])[2]")
    WebElement yarınButton;

    @FindBy(xpath = "(//button[@id='search-button'])[1]")
    WebElement otobusAraButton;

    @FindBy(xpath = "//div[@id='ins-responsive-banner-15318405239990']")
    WebElement closeAds;

    @FindBy(xpath = "/html/body/main/ul/li[1]/div[1]/div[5]/button[1]")
    WebElement koltukSec;

    @FindBy(xpath = "(//button[@data-filter='departure'])[1]")
    WebElement saat0006Filtre;

    public boolean saatFiltreKontrol() {
        System.out.println("Saat filtresi uygulandı.");
        try {
            Thread.sleep(3000);
            List<WebElement> saatler = Driver.getDriver()
                    .findElements(By.xpath("//div[@class='departure']"));

            for (WebElement saatElement : saatler) {
                String saat = saatElement.getText().trim();
                System.out.println("Sefer saati: " + saat);

                if (!saat.startsWith("00") && !saat.startsWith("01")
                        && !saat.startsWith("02") && !saat.startsWith("03")
                        && !saat.startsWith("04") && !saat.startsWith("05")) {
                    System.out.println("Filtre dışı saat bulundu: " + saat);
                    return false;
                }
            }
            System.out.println("Filtreleme sonrası " + saatler.size() + " adet sefer listelendi.");
            return true;
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }

    public void clickSaat0006Filtre(){
        saat0006Filtre.click();
    }

    public void clickKoltukSec(){
        koltukSec.click();
    }

    public void neredenSec(String sehir) {
        neredenButton.click();
        try {
            Thread.sleep(2000);
            Driver.getDriver()
                    .findElement(By.xpath("//ul/li[.//span[contains(text(),'" + sehir + "')]]"))
                    .click();
        } catch (Exception e) {
            System.out.println("Nereden seçimi başarısız: " + e.getMessage());
        }
    }

    public void nereyeSec(String sehir2) {
        nereyeButton.click();
        try {
            Thread.sleep(2000);
            Driver.getDriver()
                    .findElement(By.xpath("(//div[@class='results'])[2]//li[@class='item']//span[contains(text(),'" + sehir2 + "')]"))
                    .click();
        } catch (Exception e) {
            System.out.println("Nereye seçimi başarısız: " + e.getMessage());
        }
    }

    public void tarihOlarakYarınSec() {
        yarınButton.click();
    }

    public void otobusAra() {
        otobusAraButton.click();
    }

    public void otobusAramaYap(String neredenSec, String nereyeSec){
        neredenSec(neredenSec);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        nereyeSec(nereyeSec);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        tarihOlarakYarınSec();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        otobusAra();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean seferListesiKontrol() {
        try {
            Thread.sleep(5000); // 3'ten 5'e çıkar
            List<WebElement> seferler = Driver.getDriver()
                    .findElements(By.xpath("//ul[@id='list']/li[contains(@id,'journey-')]"));
            System.out.println("Yarın için toplam " + seferler.size() + " sefer bulundu.");
            return seferler.size() > 0;
        } catch (Exception e) {
            System.out.println("Sefer listesi bulunamadı: " + e.getMessage());
            return false;
        }
    }

    public void reklamKapat(){
        wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(closeAds));
        closeAds.click();
    }

    public boolean rotaDogrulama(String nereden, String nereye) {
        try {
            Thread.sleep(3000);
            List<WebElement> kalkislar = Driver.getDriver().findElements(By.xpath("//span[@class='origin location']"));
            List<WebElement> varislar = Driver.getDriver().findElements(By.xpath("//span[@class='destination location']"));

            for (int i = 0; i < kalkislar.size(); i++) {
                String kalkis = kalkislar.get(i).getAttribute("title");
                String varis = varislar.get(i).getAttribute("title");

                if (!kalkis.contains(nereden) || !varis.contains(nereye)) {
                    System.out.println("Yanlış rota bulundu: " + kalkis + " rotasından " + varis + " rotasına sefer bulunuyor.");
                    return false;
                }
            }
            System.out.println(kalkislar.size() + " adet sefer rota doğrulandı.");
            return true;
        } catch (Exception e) {
            System.out.println("Rota doğrulaması başarısız: " + e.getMessage());
            return false;
        }
    }

    public boolean fiyatKontrol() {
        try {
            Thread.sleep(3000);
            List<WebElement> seferler = Driver.getDriver()
                    .findElements(By.xpath("//li[contains(@id,'journey-')]"));
            List<WebElement> fiyatlar = Driver.getDriver()
                    .findElements(By.xpath("//span[@class='amount-integer']"));

            System.out.println("Sefer sayısı: " + seferler.size());
            System.out.println("Fiyat sayısı: " + fiyatlar.size());

            for (WebElement fiyat : fiyatlar) {
                if (fiyat.getText().isEmpty()) {
                    System.out.println("Boş fiyat bulundu!");
                    return false;
                }
            }
            return fiyatlar.size() > 0;
        } catch (Exception e) {
            System.out.println("Fiyat kontrolü başarısız: " + e.getMessage());
            return false;
        }
    }

    public void koltukSec(String koltukNo) {
        try {
            WebElement koltuk = Driver.getDriver()
                    .findElement(By.xpath("//text[@class='s-seat-n' and text()='" + koltukNo + "']/../.."));
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].click();", koltuk);
        } catch (Exception e) {
            System.out.println("Koltuk seçimi başarısız: " + e.getMessage());
        }
    }
}