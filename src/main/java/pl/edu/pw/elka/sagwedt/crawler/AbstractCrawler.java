package pl.edu.pw.elka.sagwedt.crawler;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.common.collect.Lists;

/**
 * Base class for crawlers.
 */
abstract class AbstractCrawler
{
    /**
     * Method for getting specified quantity of newest offers.
     *
     * @param count how many offers should be fetched.
     * @return list of offers content
     */
    public List<String> getOfferList(final int count)
    {
        final List<String> results = Lists.newArrayList();
        String pageHref = getFirstPageHref();
        while(results.size() < count)
        {
            final Document currentPage = getPageSafe(pageHref);
            final List<String> newOffers = getOfferList(currentPage, count - results.size());
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
    protected abstract String getOfferContentSelector();


    /**
     * Method for getting offers of specified page.
     */
    private List<String> getOfferList(final Document page, final int count)
    {
        return getOfferLinkList(page).stream()
            .limit(count)
            .map(this::getPageSafe)
            .map(p -> p.selectFirst(getOfferContentSelector()))
            .map(Element::text)
            .collect(toList());
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
