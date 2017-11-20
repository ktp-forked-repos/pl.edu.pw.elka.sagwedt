package pl.edu.pw.elka.sagwedt.crawler;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;

public class OtodomCrawler extends AbstractCrawler{
    private static final String DOMAIN = "https://www.otodom.pl";
    private static final String FIRST_PAGE_HREF = DOMAIN + "/wynajem/mieszkanie/warszawa/";
    private static final String NEXT_PAGE_SELECTOR = ".listing .pager .pager-next a";
    private static final String OFFER_LIST_SELECTOR = ".listing .offer-item";
    private static final String OFFER_CONTENT_SELECTOR = ".article-offer .section-offer-text";
    private static final String HREF = "href";
    private static final String DATA_URL = "data-url";

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
            .map(e -> e.attr(DATA_URL))
            .collect(toList());
    }

    @Override
    protected List<String> getOfferContentSelectors()
    {
        return Collections.singletonList(OFFER_CONTENT_SELECTOR);
    }
}
