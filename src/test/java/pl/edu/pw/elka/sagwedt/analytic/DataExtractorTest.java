package pl.edu.pw.elka.sagwedt.analytic;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataExtractorTest {
    
	private static final String OFFER1 = "klimatyczne mieszkanie na Bielanach 2 900 zł Dodaj takie ogłoszenie! Data dodania 03/11/2017 Lokalizacja Bielany, Warszawa Do wynajęcia przez Właściciel Dostępny 01/12/2017 Rodzaj nieruchomości Mieszkanie Liczba pokoi 3 pokoje Liczba łazienek 2 łazienki Wielkość (m2) 75 Parking Kryty Palący Tak Przyjazne zwierzakom Nie Mieszkanie usytuowane jest na pierwszym piętrze w 4-piętrowym, jednoklatkowym budynku, w środku chronionego osiedla. W zielonej i spokojnej okolicy, przy ul. Kochanowskiego. Składa się z 3 pokoi (salon połączony z kuchnią, 2 sypialnie; przy salonie i 1 sypialni są balkony), widnej łazienki z wanną, wc, garderoby. Jest w pełni umeblowane (TV, meble, szafy wnękowe, kuchenka z piekarnikiem, lodówka z zamrażarką, akcesoria kuchenne, pralka). Miejsce jest bardzo dobrze skomunikowane z centrum miasta- autobusy wzdłuż ul. Kochanowskiego, tramwaje przy ul. Broniewskiego, blisko stacji Metro Stare Bielany. Miejsce parkingowe w garażu podziemnym. Kaucja w wysokości 1-miesięcznego czynszu.";
	private static final String OFFER2 = "Obserwuj Galeria Wynajmę mieszkanie Warszawa, Mazowieckie, Mokotów Dodane o 18:09, 14 listopada 2017, ID ogłoszenia: 386120665 Wyróżnij to ogłoszenie Odśwież to ogłoszenie Oferta od Osoby prywatnej Poziom 2 Umeblowane Tak Rodzaj zabudowy Blok Powierzchnia 36 m² Liczba pokoi Kawalerka Czynsz (dodatkowo) 1 zł Mieszkanie 1-pokojowe na 2-gim piętrze, w bloku 4 piętrowym z 2003 roku, urządzone:lodówka,pralka,zmywarka.W budynku 2 windy i ochrona 24h.Do mieszkania przynależy podziemny garaż.Okolica cicha,pełna zieleni i terenów rekreacyjnych.Blisko sklepy i przystanek. Email: abc@gmail.com Następne ogłoszenie Wróć Wyświetleń:558";

	private static List<Word> words1;
	private static List<Word> words2;
	
	@BeforeClass
	public static void setUp(){
		words1 = Analytic.getWordText(OFFER1);
		words2 = Analytic.getWordText(OFFER2);		
	}
	
	@Test
    public void testExtractEmail(){
        Assert.assertEquals("No e-mail returns null.", null,DataExtractor.extractEmail(words1));
        Assert.assertEquals("Email extracted.", "abc@gmail.com", DataExtractor.extractEmail(words2));
    }
}
