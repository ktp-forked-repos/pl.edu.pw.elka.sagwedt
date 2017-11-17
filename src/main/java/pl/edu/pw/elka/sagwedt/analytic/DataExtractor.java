package pl.edu.pw.elka.sagwedt.analytic;

import java.util.ArrayList;
import java.util.List;

class DataExtractor {

	private static final String EMAIL_REGEX = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
	
	public static String extractType(List<Word> words){
		// TODO Implement logic
		return "";
	}

	public static Integer extractPrice(List<Word> words){
		// TODO Implement logic
		return 0;
	}

	public static Integer extractArea(List<Word> words){
		// TODO Implement logic
		return 0;
	}

	public static String extractDistrict(List<Word> words){
		// TODO Implement logic
		return "";
	}

	public static Integer extractNumberOfRooms(List<Word> words){
		// TODO Implement logic
		return 0;
	}

	public static Integer extractBuildYear(List<Word> words){
		// TODO Implement logic
		return 0;
	}

	public static String extractEmail(List<Word> words){
		return extractRegex(words,EMAIL_REGEX);
	}
	
	public static String extractTelephone(List<Word> words){
		// TODO Implement logic
		return "";
	}
	
	private static String extractRegex(List<Word> words, String regex){
		for(Word w : words){
			if(w.original.matches(regex))
				return w.original;
			
			for(String meaning : w.meanings){
				if(meaning.matches(regex))
						return meaning;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * <Moved from Analytic>
	 */
	
	public static Integer getM2(List<Word> words) {
		List<Word> wordsCloseToMeters = getWordsCloseToMetersInfo(words);
		for(Word w : wordsCloseToMeters) {
			if( isInteger(w.original))
				return Integer.parseInt(w.original);
		}
		return null;
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
	
	/**
	 * 
	 * </Moved from Analytic>
	 */

}
