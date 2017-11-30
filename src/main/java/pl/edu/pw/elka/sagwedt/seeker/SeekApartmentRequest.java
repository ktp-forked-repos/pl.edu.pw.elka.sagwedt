package pl.edu.pw.elka.sagwedt.seeker;

/**
 * Request for {@link Seeker} to seek apartment.
 */
public class SeekApartmentRequest
{
	private Integer minArea = 0;
	private Integer maxArea = Integer.MAX_VALUE;
	private Integer minPrice = 0;
	private Integer maxPrice = Integer.MAX_VALUE;

    public SeekApartmentRequest()
    {

    }

    public SeekApartmentRequest(final Integer mainArea_, final Integer maxArea_, final Integer mainPrice_, final Integer maxPrice_)
    {
    	minArea = mainArea_;
    	maxArea = maxArea_;
    	minPrice = mainPrice_;
    	maxPrice = maxPrice_;
    }

    public Integer getMinArea() {
    	return minArea;
    }

    public Integer getMaxArea() {
    	return maxArea;
    }

    public Integer getMinPrice() {
    	return minPrice;
    }

    public Integer getMaxPrice() {
    	return maxPrice;
    }
}
