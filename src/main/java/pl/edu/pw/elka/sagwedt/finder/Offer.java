package pl.edu.pw.elka.sagwedt.finder;

/**
 * Offer in text form, with its url
 */
public class Offer {
	private String text;
	private String url;
	
	/**
	 * Constructor
	 */
	public Offer(String text_, String url_) {
		text = text_;
		url = url_;
	}
	
	public String getOfferText() {
		return text;
	}
	
	public String getOfferURL() {
		return url;
	}
}
