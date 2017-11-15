package pl.edu.pw.elka.sagwedt.analytic;

import java.util.ArrayList;
import java.util.List;

import morfologik.stemming.WordData;
import morfologik.stemming.polish.PolishStemmer;

/*
 * Class which, using the morfologik library, obtains the basic form of words.
 * Stores the original input word
 * And a list of it's potential meanings 
 */
class Word {
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