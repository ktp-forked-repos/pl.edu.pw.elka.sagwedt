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

		PolishStemmer stemmer = new PolishStemmer();

		String in = "Mieszkanie 20mkw wynajmę za 500zł miesięcznie plus media. Starość nie radość, śmierć nie wesele. Lubie placki z jagodami. Kocham Piotrusia.";

		printInfo(in);

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
