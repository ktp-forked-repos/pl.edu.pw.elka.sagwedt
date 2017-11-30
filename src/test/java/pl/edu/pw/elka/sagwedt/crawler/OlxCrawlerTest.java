package pl.edu.pw.elka.sagwedt.crawler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import pl.edu.pw.elka.sagwedt.finder.Offer;

public class OlxCrawlerTest
{
    @Test
    public void testGetOfferListInt_connectionSuccess() throws Exception
    {
        final List<Offer> offerList = new OlxCrawler().getOfferList(1);
        assertThat(offerList).hasSize(1);
    }
}