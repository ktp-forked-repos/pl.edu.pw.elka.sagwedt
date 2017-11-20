package pl.edu.pw.elka.sagwedt.analytic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DataExtractor {

	private static final ArrayList <String> typeKeywords = new ArrayList<String>(
			Arrays.asList("mieszkanie", "dom"));
	private static final String EMAIL_REGEX = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
	private static final ArrayList <String> priceKeywords = new ArrayList<String>(
			Arrays.asList("zł","złotych","cena"));
	private static final ArrayList <String> areaKeywords = new ArrayList<String>(
			Arrays.asList("m2","m²","mkw","metrów"));
	private static final ArrayList <String> districts = new ArrayList<String>(
			Arrays.asList("bemowo", "białołęka", "bielany", "mokotów", "ochota", "praga-południe",
					"praga-północ", "rembertów", "śródmieście", "targówek", "ursus", "ursynów",
					"wawer", "wesoła", "wilanów", "włochy", "wola", "żoliborz"));
	private static final ArrayList <String> numberOfRoomsKeywords = new ArrayList<String>(
			Arrays.asList("pokojowe", "pokoi","pokojów","pok.","pok", "pokoje"));
	private static final ArrayList <String> buildYearKeywords = new ArrayList<String>(
			Arrays.asList("r", "rok","roku"));
	private static final ArrayList <String> telephoneKeywords = new ArrayList<String>(
			Arrays.asList("tel.", "tel", "telefon","telefonu"));

	public static String extractType(final List<Word> words){
		final int index = getKeywordIndexInList(words,typeKeywords);
		if (index >= 0)
			return findMatchingMeaning(words.get(index), typeKeywords);
		return null;
	}

	public static Integer extractPrice(final List<Word> words){
		final int index = getKeywordIndexInList(words,priceKeywords);
		if (index >= 0){
			String numberString = extractNumberClosestToIndex(words, index);
			if (numberString != ""){
				return Integer.valueOf(numberString);
			}
		}
		return null;
	}

	public static Integer extractArea(final List<Word> words){
		final int index = getKeywordIndexInList(words,areaKeywords);
		if (index >= 0){
			String numberString = extractNumberClosestToIndex(words, index);
			if (numberString != ""){
				return Integer.valueOf(numberString);
			}
		}
		return null;
	}

	public static String extractDistrict(final List<Word> words){
		final int index = getKeywordIndexInList(words, districts);

		if(index >= 0)
			return findMatchingMeaning(words.get(index), districts);
		return null;
	}

	public static Integer extractNumberOfRooms(final List<Word> words){
		final int index = getKeywordIndexInList(words,numberOfRoomsKeywords);
		if (index >= 0){
			String numberString = extractNumberClosestToIndex(words, index);
			if (numberString != ""){
				return Integer.valueOf(numberString);
			}
		}
			
		return null;
	}

	public static Integer extractBuildYear(final List<Word> words){
		final int index = getKeywordIndexInList(words,buildYearKeywords);
		if (index >= 0){
			String numberString = extractNumberClosestToIndex(words, index);
			if (numberString != ""){
				return Integer.valueOf(numberString);
			}
		}	
		return null;
	}

	public static String extractEmail(final List<Word> words){
		return extractRegex(words,EMAIL_REGEX);
	}

	public static String extractTelephone(final List<Word> words){
		final int index = getKeywordIndexInList(words,telephoneKeywords);
		if (index >= 0)
			return extractNumberClosestToIndex(words, index);
		return null;
	}

	private static String extractRegex(final List<Word> words, final String regex){
		for(final Word w : words){
			if(w.original.matches(regex))
				return w.original;

			for(final String meaning : w.meanings){
				if(meaning.matches(regex))
						return meaning;
			}
		}
		return null;
	}

	private static int getKeywordIndexInList(final List<Word> words, final List<String> keywords){
		for(final Word word : words){
			for(final String keyword : keywords){
				if(word.original.equalsIgnoreCase(keyword))
					return words.indexOf(word);
				for(final String meaning : word.meanings){
					if(meaning.equalsIgnoreCase(keyword))
						return words.indexOf(word);
				}
			}
		}
		return -1;
	}

	private static String findMatchingMeaning(final Word word, final List<String> keywords){
		for(final String keyword : keywords){
			if(word.original.equalsIgnoreCase(keyword))
					return keyword;
			for(final String meaning : word.meanings){
				if(meaning.equalsIgnoreCase(keyword))
					return keyword;
			}
		}
		return null;
	}

/**
 * Starting at index (position of a keyword) find the nearest number in the list of words
 */
	private static String extractNumberClosestToIndex(final List<Word> words, final int index){
		int offset = 1;
		String originalWord;

		//expand to both directions symmetrically
		while ((index - offset) >= 0 && (index + offset) < words.size()){
			originalWord = words.get(index - offset).original;

			if(isInteger(originalWord)){
				return concatNumberToLeft(words, index-offset);
			} else{
				originalWord = words.get(index + offset).original;
				if(isInteger(originalWord)){
					return concatNumberToRight(words, index+offset);
				}
			}
			offset++;
		}

		//continue to the left
		while ((index - offset) >= 0){
			originalWord = words.get(index - offset).original;

			if(isInteger(originalWord)){
				return concatNumberToLeft(words, index-offset);
			}
				offset++;
		}

		//continue to the right
		while((index + offset) < words.size()){
			originalWord = words.get(index + offset).original;

			if(isInteger(originalWord)){
				return concatNumberToRight(words, index+offset);
			}
				offset++;
		}
		return null;
	}
	/**
	 * Knowing that word under index is a number (least significant part),
	 * check whether the number was not split into multiple words
	 * (search to the left)
	 */
	private static String concatNumberToLeft(final List<Word> words, final int index){
		String numberString = words.get(index).original;
		for(int i = index-1; i>=0; i--){
			if(isInteger(words.get(i).original)){
				numberString = words.get(i).original + numberString;
			} else{
				break;
			}
		}
		return numberString;
	}

	/**
	 * Knowing that word under index is a number (most significant part),
	 * check whether the number was not split into multiple words
	 * (search to the right)
	 */
	private static String concatNumberToRight(final List<Word> words, final int index){
		String numberString = words.get(index).original;
		for(int i = index+1; i < words.size(); i++){
			if(isInteger(words.get(i).original)){
				numberString = numberString +  words.get(i).original;
			} else
				break;
		}
		return numberString;
	}

	/**
	 *
	 * <Moved from Analytic>
	 */

	public static Integer getM2(final List<Word> words) {
		final List<Word> wordsCloseToMeters = getWordsCloseToMetersInfo(words);
		for(final Word w : wordsCloseToMeters) {
			if( isInteger(w.original))
				return Integer.parseInt(w.original);
		}
		return null;
	}

	private static List<Word> getWordsCloseToMetersInfo(final List<Word> input_list) {
		final List<Word> ret = new ArrayList<>();
		Word before = null;
		boolean gettingLast = false;
		for( final Word w : input_list) {
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

	private static boolean isInteger(final String str) {
	    final int size = str.length();

	    for (int i = 0; i < size; i++) {
	        if (!Character.isDigit(str.charAt(i))) {
	            return false;
	        }
	    }

	    return size > 0;
	}

}
