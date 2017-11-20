package pl.edu.pw.elka.sagwedt.analytic;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataExtractorTest {
    
	private static final String OFFER1 = "klimatyczne mieszkanie na Bielanach 2 900 zł Dodaj takie ogłoszenie! Data dodania 03/11/2017 Lokalizacja Bielany, Warszawa Do wynajęcia przez Właściciel Dostępny 01/12/2017 Rodzaj nieruchomości Mieszkanie Liczba pokoi 3 pokoje Liczba łazienek 2 łazienki Wielkość (m2) 75 Parking Kryty Palący Tak Przyjazne zwierzakom Nie Mieszkanie usytuowane jest na pierwszym piętrze w 4-piętrowym, jednoklatkowym budynku, w środku chronionego osiedla. W zielonej i spokojnej okolicy, przy ul. Kochanowskiego. Składa się z 3 pokoi (salon połączony z kuchnią, 2 sypialnie; przy salonie i 1 sypialni są balkony), widnej łazienki z wanną, wc, garderoby. Jest w pełni umeblowane (TV, meble, szafy wnękowe, kuchenka z piekarnikiem, lodówka z zamrażarką, akcesoria kuchenne, pralka). Miejsce jest bardzo dobrze skomunikowane z centrum miasta- autobusy wzdłuż ul. Kochanowskiego, tramwaje przy ul. Broniewskiego, blisko stacji Metro Stare Bielany. Miejsce parkingowe w garażu podziemnym. Kaucja w wysokości 1-miesięcznego czynszu. Rok budowy: 1999 Kontakt tel. 123 456 789 ";
	private static final String OFFER2 = "Obserwuj Galeria Wynajmę mieszkanie Warszawa, Mazowieckie, Mokotów Dodane o 18:09, 14 listopada 2017, ID ogłoszenia: 386120665 Wyróżnij to ogłoszenie Odśwież to ogłoszenie Oferta od Osoby prywatnej Poziom 2 Umeblowane Tak Rodzaj zabudowy Blok Powierzchnia 36 m² Liczba pokoi 1 Kawalerka Czynsz (dodatkowo) 10 000 000 zł Mieszkanie 1-pokojowe na 2-gim piętrze, w bloku 4 piętrowym z 2003 roku, urządzone:lodówka,pralka,zmywarka.W budynku 2 windy i ochrona 24h.Do mieszkania przynależy podziemny garaż.Okolica cicha,pełna zieleni i terenów rekreacyjnych.Blisko sklepy i przystanek. Telefon: 555 555 555 Email: abc@gmail.com Następne ogłoszenie Wróć Wyświetleń:558";
	private static final String OFFER3 = "Data dodania 07/11/2017     Lokalizacja     Ursynów, Warszawa     Do wynajęcia przez Właściciel     Dostępny 01/12/2017     Rodzaj nieruchomości Dom     Liczba pokoi 6 lub więcej pokoi     Liczba łazienek 2 łazienki     Wielkość (m2) 330     Parking Garaż     Palący Nie     Przyjazne zwierzakom Tak Duży dom na Ursynowie, z dogodnym dojazdem do  ul. Puławskiej.  Dom z użytkowym poddaszem, na parterze otwarta przestrzeń z salonem , kuchnią i jadalnią, oraz dwa pokoje i łazienka, wc. Na piętrze olbrzymi pokój telewizyjny, dwa małe pokoje dziecięce , garderoba i łazienka. W zależności od potrzeb może być umeblowany bądź nie. Garaż na jeden samochód, przed domem dwa miejsca postojowe. Cicha uliczka, pięciu mieszkańców. Zalesiony ogród ze starodrzewiem, podlewanie automatyczne , taras z wejściem z  z salonu. Budynek z 2000 roku";
	
	private static List<Word> words1;
	private static List<Word> words2;
	private static List<Word> words3;
	
	@BeforeClass
	public static void setUp(){
		words1 = Analytic.getWordText(OFFER1);
		words2 = Analytic.getWordText(OFFER2);
		words3 = Analytic.getWordText(OFFER3);
	}
	
	@Test
    public void testExtractEmail(){
        Assert.assertEquals("No e-mail returns null.", null,DataExtractor.extractEmail(words1));
        Assert.assertEquals("Email extracted.", "abc@gmail.com", DataExtractor.extractEmail(words2));
    }
	
	@Test
    public void testExtractType(){
        Assert.assertEquals("Extract type: house.", "dom",DataExtractor.extractType(words3));
        Assert.assertEquals("Extract type: apartment.", "mieszkanie", DataExtractor.extractType(words2));
    }
	
	@Test
    public void testExtractDistrict(){
        Assert.assertEquals("Extract district: Bielany.", "bielany",DataExtractor.extractDistrict(words1));
        Assert.assertEquals("Extract district: Mokotów.", "mokotów", DataExtractor.extractDistrict(words2));
    }
	
	@Test
    public void testExtractPrice(){
        Assert.assertEquals("Extract price 1.", 2900, DataExtractor.extractPrice(words1).intValue());
        Assert.assertEquals("Extract price 2.", 10000000, DataExtractor.extractPrice(words2).intValue());
        Assert.assertEquals("Extract price 3.", null, DataExtractor.extractPrice(words3));
	}
	
	@Test
    public void testExtractAreaTest(){
        Assert.assertEquals("Extract area 1.", 75, DataExtractor.extractArea(words1).intValue());
        Assert.assertEquals("Extract area 2.", 36, DataExtractor.extractArea(words2).intValue());
        Assert.assertEquals("Extract area 3.", 330, DataExtractor.extractArea(words3).intValue());
	}
	
	@Test
    public void testExtractnumberOfRoomsTest(){
        Assert.assertEquals("Extract numberOfRooms 1.", 3, DataExtractor.extractNumberOfRooms(words1).intValue());
        Assert.assertEquals("Extract numberOfRooms 2.", 1, DataExtractor.extractNumberOfRooms(words2).intValue());
        Assert.assertEquals("Extract numberOfRooms 3.", 6, DataExtractor.extractNumberOfRooms(words3).intValue());
	}
	
	@Test
    public void testExtractBuildYearTest(){
        Assert.assertEquals("Extract buildYear 1.", 1999, DataExtractor.extractBuildYear(words1).intValue());
        Assert.assertEquals("Extract buildYear 2.", 2003, DataExtractor.extractBuildYear(words2).intValue());
        Assert.assertEquals("Extract buildYear 3.", 2000, DataExtractor.extractBuildYear(words3).intValue());
	}
	
	@Test
    public void testExtractTelephone(){
        Assert.assertEquals("Extract telephone 1.", "123456789", DataExtractor.extractTelephone(words1));
        Assert.assertEquals("Extract telephone 2.", "555555555", DataExtractor.extractTelephone(words2));
        Assert.assertEquals("Extract telephone 3.", null, DataExtractor.extractTelephone(words3));
	}
}
