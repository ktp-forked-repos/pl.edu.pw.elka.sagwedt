package pl.edu.pw.elka.sagwedt.analytic;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import morfologik.stemming.WordData;
import morfologik.stemming.polish.*;

/*
 * Class which is supposed to allow analysis on the text 
 * 
 */
public class Analytic {

	public static void main(String[] args) {

		String in = "klimatyczne mieszkanie na Bielanach 2 900 zł Dodaj takie ogłoszenie! Data dodania 03/11/2017 Lokalizacja Bielany, Warszawa Do wynajęcia przez Właściciel Dostępny 01/12/2017 Rodzaj nieruchomości Mieszkanie Liczba pokoi 3 pokoje Liczba łazienek 2 łazienki Wielkość (m2) 75 Parking Kryty Palący Tak Przyjazne zwierzakom Nie Mieszkanie usytuowane jest na pierwszym piętrze w 4-piętrowym, jednoklatkowym budynku, w środku chronionego osiedla. W zielonej i spokojnej okolicy, przy ul. Kochanowskiego. Składa się z 3 pokoi (salon połączony z kuchnią, 2 sypialnie; przy salonie i 1 sypialni są balkony), widnej łazienki z wanną, wc, garderoby. Jest w pełni umeblowane (TV, meble, szafy wnękowe, kuchenka z piekarnikiem, lodówka z zamrażarką, akcesoria kuchenne, pralka). Miejsce jest bardzo dobrze skomunikowane z centrum miasta- autobusy wzdłuż ul. Kochanowskiego, tramwaje przy ul. Broniewskiego, blisko stacji Metro Stare Bielany. Miejsce parkingowe w garażu podziemnym. Kaucja w wysokości 1-miesięcznego czynszu.";
		String in2 = "Obserwuj Galeria Wynajmę mieszkanie Warszawa, Mazowieckie, Mokotów Dodane o 18:09, 14 listopada 2017, ID ogłoszenia: 386120665 Wyróżnij to ogłoszenie Odśwież to ogłoszenie Oferta od Osoby prywatnej Poziom 2 Umeblowane Tak Rodzaj zabudowy Blok Powierzchnia 36 m² Liczba pokoi Kawalerka Czynsz (dodatkowo) 1 zł Mieszkanie 1-pokojowe na 2-gim piętrze, w bloku 4 piętrowym z 2003 roku, urządzone:lodówka,pralka,zmywarka.W budynku 2 windy i ochrona 24h.Do mieszkania przynależy podziemny garaż.Okolica cicha,pełna zieleni i terenów rekreacyjnych.Blisko sklepy i przystanek. Następne ogłoszenie Wróć Wyświetleń:558";
		printInfo(in);
		printInfo(in2);
		
		System.out.println("Metry1: " + getM2(in));
		System.out.println("Metry2: " + getM2(in2));

	}
	
	private static void printInfo(String in) {
		List<Word> list = getWordText(in);
		
		for(Word w : list) {
			System.out.println("$: " + w.original);
			System.out.print("> ");
			for(String meaning : w.meanings) {
				System.out.print(meaning + ",");
			}
			System.out.println();
		}
	}

	private static List<String> getStringWords(String sentence) {
		List<String> words = new ArrayList<>();
		for (String word : sentence.toLowerCase(new Locale("pl")).split("[\\s\\.\\,\\?\\!]+")) {
			words.add(word);
		}
		return words;
	}

	private static List<Word> getWordText(List<String> stringWords) {
		List<Word> ret = new ArrayList<>();
		for (String word : stringWords) {
			ret.add(new Word(word));
		}
		return ret;
	}

	private static List<Word> getWordText(String original_text) {
		return getWordText(getStringWords(original_text));
	}
	
	private static List<Word> getWordsCloseToMetersInfo(List<Word> input_list) {
		List<Word> ret = new ArrayList<>();
		Word before = null;
		boolean gettingLast = false;
		for( Word w : input_list) {
			if(gettingLast) {
				ret.add(w);
				return ret;
			}
			if((w.original.contains("mkw") || w.original.contains("m2") || w.original.contains("m²"))&& !gettingLast ) {
				ret.add(before);
				ret.add(w);
				gettingLast = true;
			}
			before = w;
		}
		return null;
	}
	
	private static boolean isInteger(String str) {
	    int size = str.length();

	    for (int i = 0; i < size; i++) {
	        if (!Character.isDigit(str.charAt(i))) {
	            return false;
	        }
	    }

	    return size > 0;
	}
	
	private static Integer getM2(String full_input) {
		List<Word> words = getWordsCloseToMetersInfo(getWordText(full_input));
		for(Word w : words) {
			if( isInteger(w.original))
				return Integer.parseInt(w.original);
		}
		return null;
	}

	/*
	 * Class which, using the morfologik library, obtains the basic form of words.
	 * Stores the original input word
	 * And a list of it's potential meanings 
	 */
		private static class Word {
			String original;
			List<String> meanings = new ArrayList<>();

			Word(String org_) {
				original = org_;
				getMeanings(lookup(org_));
			}

			private List<WordData> lookup(String one_word) {
				PolishStemmer stemmer = new PolishStemmer();
				return stemmer.lookup(one_word);
			}

			private void getMeanings(List<WordData> worddata_list) {
				for (WordData wd : worddata_list) {
					meanings.add(wd.getStem().toString());
				}

			}

		}

}
