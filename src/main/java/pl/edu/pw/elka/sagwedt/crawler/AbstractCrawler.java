package pl.edu.pw.elka.sagwedt.crawler;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

import pl.edu.pw.elka.sagwedt.finder.Offer;

/**
 * Base class for crawlers.
 */
public abstract class AbstractCrawler
{
    /**
     * Method for getting specified quantity of newest offers.
     *
     * @param count how many offers should be fetched.
     * @return list of offers content
     */
    public List<Offer> getOfferList(final int count)
    {
        final List<Offer> results = Lists.newArrayList();
        String pageHref = getFirstPageHref();
        while(results.size() < count)
        {
            final Document currentPage = getPageSafe(pageHref);
            final List<Offer> newOffers = getOfferList(currentPage, count - results.size());
            results.addAll(newOffers);
            pageHref = getNextPageHref(currentPage);
        }
        return results;
    }

    /**
     * Returns url to first page.
     */
    protected abstract String getFirstPageHref();

    /**
     * Returns href to next page.
     */
    protected abstract String getNextPageHref(Document currentPage);

    /**
     * Returns selector for extracting offer links from page.
     */
    protected abstract List<String> getOfferLinkList(Document currentPage);

    /**
     * Returns offer content selector from which text is extracted.
     */
    protected abstract List<String> getOfferContentSelectors();


    /**
     * Method for getting offers of specified page.
     */
    private List<Offer> getOfferList(final Document page, final int count)
    {
    	List<Document> offerDocuments = getOfferLinkList(page).stream().limit(count).map(this::getPageSafe).collect(toList());
    	List<Offer> offerList = new ArrayList<Offer>();
    	for(Document d : offerDocuments) {
    		offerList.add(new Offer(getPageOfferAsString(d), d.baseUri()));
    	}
    	return offerList;
    }

    /**
     * Returns string of an offer from specified offer page.
     */
    private String getPageOfferAsString(final Document page)
    {
        return getOfferContentSelectors().stream()
            .map(page::select)
            .map(Elements::text)
            .collect(joining(". "));
    }

    /**
     * Safe get page for href;
     */
    private Document getPageSafe(final String href)
    {
        try
        {
            return Jsoup.connect(href).get();
        }
        catch(final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
