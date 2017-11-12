package pl.edu.pw.elka.sagwedt.crawler;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.jsoup.nodes.Document;

/**
 * Web crawler for apartment offers on www.gumtree.pl service.
 */
public class GumtreeCrawler extends AbstractCrawler
{
    private static final String DOMAIN = "https://www.gumtree.pl";
    private static final String FIRST_PAGE_HREF = DOMAIN + "/s-mieszkania-i-domy-do-wynajecia/warszawa/mieszkanie/v1c9008l3200008a1dwp1";
    private static final String NEXT_PAGE_SELECTOR = ".pagination .next.follows";
    private static final String OFFER_LIST_SELECTOR = ".view li a.href-link";
    private static final String OFFER_CONTENT_SELECTOR = ".vip-header-and-details";
    private static final String HREF = "href";

    @Override
    protected String getFirstPageHref()
    {
        return FIRST_PAGE_HREF;
    }

    @Override
    protected String getNextPageHref(final Document currentPage)
    {
        return DOMAIN + currentPage.selectFirst(NEXT_PAGE_SELECTOR).attr(HREF);
    }

    @Override
    protected List<String> getOfferLinkList(final Document currentPage)
    {
        return currentPage.select(OFFER_LIST_SELECTOR).stream()
            .map(e -> e.attr(HREF))
            .map(href -> DOMAIN + href)
            .collect(toList());
    }

    @Override
    protected String getOfferContentSelector()
    {
        return OFFER_CONTENT_SELECTOR;
    }
}