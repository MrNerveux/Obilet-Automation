import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.Main_Page;
import utilities.ConfigurationReader;
import utilities.Driver;

public class Otobus_Arama_Test{

    Main_Page mainPage;

    @BeforeClass
    public void setUp() {
        // Tarayıcı açılıyor ve obilet.com'a yönlendiriliyor.
        String url = ConfigurationReader.getProperty("url");
        Driver.getDriver().get(url);
        mainPage = new Main_Page();
    }

    @Test
    public void otobusAramaTesti() {

        /**
         * Senaryo:
         * Kullanıcı Antalya'dan İzmir'e otobüs araması yapıyor.
         * İlk olarak kullanıcı Nereden kısmına Antalya sseçeneğini seçer.
         * Sonrasında Nereye kısmında ise İzmir seçeneğini seçer.
         * Giidş tarihi olarak Yarın seçeneğini seçer.
         * Otobüs Arama butonuna tıklanır
         * Doğrulama işlemi olarak Seferler sayfasına yönlendirildiği kontrol edilir.
         * En baştaki test Url'si ve Otobüs Ara sonrası Url karşılaştırılır ve yönlenirme doğrulanır.
         * Gelen sefer listesi boş mu kontrolü yapılır.
         * Listelenen seferlerin ilk başta seçilen Nereden-Nereye rotasıyl aynı olduğu kontrol edilir.
         * Her sefere karşılık gelen bir fiyat kontrolü yapılır.
         * Seferler listelendiğinde 00:00 ve 06:00 saat filtresi seçilir.
         * Sonuçların bu filtre ile aynı olup olmadığı kontrol edilir.
         * Test tamamlanır.
         */

        // Nereden, Nereye ve tarih seçilerek arama yapılır.
        mainPage.otobusAramaYap("Antalya", "İzmir");

        // Otobü arama sonrası seferler sayfasına yönlendirildiği doğrulanır.
        String url = Driver.getDriver().getCurrentUrl();
        System.out.println("Yönlendirme öncesi URL: " + url);
        Assert.assertTrue(url.contains("seferler"), "Seferler sayfasına geçilemedi.");
        System.out.println("Yönlendirme sonrası URL: " + Driver.getDriver().getCurrentUrl()
                + "\nSeferler sayfasına yönlendirme başarılı.");

        // Sefer listesinin boş olmadığı kontrol edilir.
        Assert.assertTrue(mainPage.seferListesiKontrol(), "Sefer listesi boş.");

        // Listelenen seferlerin doğru rotada olduğu doğrulanır. (Antalya -> İzmir)
        mainPage.rotaDogrulama("Antalya", "İzmir");

        // Her seferin karşısında bir fiyat bilgisinin gösterildiği kontrol edilir.
        mainPage.fiyatKontrol();

        // 00:00 06:00 Filtresi seçilir.
        mainPage.clickSaat0006Filtre();

        // Sefer saatlerinin filtrede belirlenen aralıkta olduğu kontrol edilir.
        mainPage.saatFiltreKontrol();

    }

    @AfterClass
    public void tearDown() {
        // Test tamamlandıktan sonra tarayıcı kapatılır.
        Driver.closeDriver();
    }
}