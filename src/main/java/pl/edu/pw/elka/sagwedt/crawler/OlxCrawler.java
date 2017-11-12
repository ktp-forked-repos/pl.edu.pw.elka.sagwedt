package pl.edu.pw.elka.sagwedt.crawler;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.jsoup.nodes.Document;

/**
 * Web crawler for apartment offers on www.olx.pl service.
 */
public class OlxCrawler extends AbstractCrawler
{
    private static final String DOMAIN = "https://www.olx.pl";
    private static final String FIRST_PAGE_HREF = DOMAIN + "/nieruchomosci/mieszkania/wynajem/warszawa/";
    private static final String NEXT_PAGE_SELECTOR = ".link.pageNextPrev";
    private static final String OFFER_LIST_SELECTOR = ".link.linkWithHash";
    private static final String OFFER_CONTENT_SELECTOR = ".offerdescription.clr";
    private static final String HREF = "href";

    @Override
    protected String getFirstPageHref()
    {
        return FIRST_PAGE_HREF;
    }
    @Override
    protected String getNextPageHref(final Document currentPage)
    {
        return currentPage.selectFirst(NEXT_PAGE_SELECTOR).attr(HREF);
    }
    @Override
    protected List<String> getOfferLinkList(final Document currentPage)
    {
        return currentPage.select(OFFER_LIST_SELECTOR).stream()
            .map(e -> e.attr(HREF))
            .collect(toList());
    }
    @Override
    protected String getOfferContentSelector()
    {
        return OFFER_CONTENT_SELECTOR;
    }

}