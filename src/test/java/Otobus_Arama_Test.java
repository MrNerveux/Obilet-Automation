import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.Main_Page;
import pages.Seferler_Page;
import utilities.ConfigurationReader;
import utilities.Driver;

public class Otobus_Arama_Test {
    /**
         * Senaryo:
         * Kullanıcı Antalya'dan İzmir'e otobüs araması yapıyor.
         * İlk olarak kullanıcı Nereden kısmına Antalya seçeneğini seçer.
         * Sonrasında Nereye kısmında ise İzmir seçeneğini seçer.
         * Gidiş tarihi olarak Yarın seçeneğini seçer.
         * Otobüs Ara butonuna tıklanır.
         * Doğrulama işlemi olarak Seferler sayfasına yönlendirildiği kontrol edilir.
         * En baştaki test URL'si ile Otobüs Ara sonrası URL karşılaştırılır ve yönlendirme doğrulanır.
         * Gelen sefer listesi boş mu kontrolü yapılır.
         * Listelenen seferlerin ilk başta seçilen Nereden-Nereye rotasıyla aynı olduğu kontrol edilir.
         * Her sefere karşılık gelen bir fiyat kontrolü yapılır.
         * Seferler listelendiğinde 00:00 - 06:00 saat filtresi seçilir.
         * Sonuçların bu filtre ile uyumlu olup olmadığı kontrol edilir.
         * Test tamamlanır.
         */

    Main_Page mainPage;
    Seferler_Page seferlerPage;

    @BeforeClass
    public void setUp() {
        // Tarayıcı açılıyor ve obilet.com'a yönlendiriliyor.
        String url = ConfigurationReader.getProperty("url");
        Driver.getDriver().get(url);
        mainPage = new Main_Page();
    }

    @Test
    public void otobusAramaTesti() {

        // Otobüs Arama öncesi Url bilgimizi alalım.
        String url = Driver.getDriver().getCurrentUrl();
        System.out.println("Yönlendirme öncesi URL: " + url);
        
        // Nereden, Nereye ve tarih seçilerek arama yapılır.
        mainPage.otobusAramaYap("Antalya", "İzmir");

        seferlerPage = new Seferler_Page();

        // Otobüs araması sonrası seferler sayfasına yönlendirildiği doğrulanır.
        String currentUrl = Driver.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("seferler"), "Seferler sayfasına geçilemedi.");
        System.out.println("Yönlendirme sonrası URL: " + Driver.getDriver().getCurrentUrl()
                + "\nSeferler sayfasına yönlendirme başarılı.");

        // Sefer listesinin boş olmadığı kontrol edilir.
        Assert.assertTrue(seferlerPage.seferListesiKontrol(), "Sefer listesi boş.");

        // Listelenen seferlerin doğru rotada olduğu doğrulanır. (Antalya, İzmir)
        Assert.assertTrue(seferlerPage.rotaDogrula("Antalya", "İzmir"), "Rota yanlış!");

        // Her seferin karşısında bir fiyat bilgisinin gösterildiği kontrol edilir.
        Assert.assertTrue(seferlerPage.fiyatKontrol(), "Fiyat bulunamadı!");

        // 00:00 - 06:00 saat filtresi seçilir.
        seferlerPage.saat0006Tikla();

        // Sefer saatlerinin filtre aralığında olduğu kontrol edilir.
        Assert.assertTrue(seferlerPage.saatFiltreKontrol(), "Filtre dışı saat bulundu!");
    }

    @AfterClass
    public void tearDown() {
        // Test tamamlandıktan sonra tarayıcı kapatılır.
        Driver.closeDriver();
    }
}
