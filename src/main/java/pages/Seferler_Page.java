package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Driver;

import java.time.Duration;
import java.util.List;

public class Seferler_Page {

    WebDriverWait wait;

    public Seferler_Page() {
        PageFactory.initElements(Driver.getDriver(), this);
        wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
    }

    @FindBy(xpath = "//li[contains(@id,'journey-')]")
    List<WebElement> seferListesi;

    @FindBy(xpath = "//div[@class='departure']")
    List<WebElement> seferSaatleri;

    @FindBy(xpath = "//span[@class='origin location']")
    List<WebElement> kalkislar;

    @FindBy(xpath = "//span[@class='destination location']")
    List<WebElement> varislar;

    @FindBy(xpath = "//span[@class='amount-integer']")
    List<WebElement> fiyatlar;

    @FindBy(xpath = "(//button[@data-filter='departure'])[1]")
    WebElement saat0006Filtre;

    public void saat0006Tikla() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(saat0006Filtre));
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].click();", saat0006Filtre);
        } catch (Exception e) {
            System.out.println("Filtre tıklanamadı: " + e.getMessage());
        }
    }

    public boolean seferListesiKontrol() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(seferListesi));
            System.out.println("Toplam " + seferListesi.size() + " sefer bulundu.");
            return seferListesi.size() > 0;
        } catch (Exception e) {
            System.out.println("Sefer listesi boş: " + e.getMessage());
            return false;
        }
    }

    public boolean rotaDogrula(String nereden, String nereye) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(kalkislar));
            for (int i = 0; i < kalkislar.size(); i++) {
                String kalkis = kalkislar.get(i).getAttribute("title");
                String varis = varislar.get(i).getAttribute("title");
                if (!kalkis.contains(nereden) || !varis.contains(nereye)) {
                    System.out.println("Yanlış rota: " + kalkis + " -> " + varis);
                    return false;
                }
            }
            System.out.println("Tüm rotalar doğrulandı.");
            return true;
        } catch (Exception e) {
            System.out.println("Rota doğrulaması başarısız: " + e.getMessage());
            return false;
        }
    }

    public boolean fiyatKontrol() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(fiyatlar));
            for (WebElement fiyat : fiyatlar) {
                if (fiyat.getText().isEmpty()) {
                    System.out.println("Boş fiyat bulundu.");
                    return false;
                }
            }
            System.out.println("Toplam " + fiyatlar.size() + " fiyat doğrulandı.");
            return true;
        } catch (Exception e) {
            System.out.println("Fiyat kontrolü başarısız: " + e.getMessage());
            return false;
        }
    }

    public boolean saatFiltreKontrol() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(seferSaatleri));
            for (WebElement saatElement : seferSaatleri) {
                String saat = saatElement.getText().trim();
                System.out.println("Sefer saati: " + saat);
                if (!saat.startsWith("00") && !saat.startsWith("01") && !saat.startsWith("02") && !saat.startsWith("03")
                        && !saat.startsWith("04") && !saat.startsWith("05")) {
                    System.out.println("Filtre dışı saat: " + saat);
                    return false;
                }
            }
            System.out.println("Filtreleme sonrası " + seferSaatleri.size() + " adet sefer listelendi.");
            return true;
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }
}