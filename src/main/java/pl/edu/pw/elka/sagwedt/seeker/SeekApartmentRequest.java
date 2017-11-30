package pl.edu.pw.elka.sagwedt.seeker;

import pl.edu.pw.elka.sagwedt.infrastructure.AppRequest;

/**
 * Request for {@link Seeker} to seek apartment.
 */
public class SeekApartmentRequest extends AppRequest
{
	private Integer minArea = 0;
	private Integer maxArea = Integer.MAX_VALUE;
	private Integer minPrice = 0;
	private Integer maxPrice = Integer.MAX_VALUE;
	
    public SeekApartmentRequest()
    {
    	
    }
    
    public SeekApartmentRequest(Integer mainArea_, Integer maxArea_, Integer mainPrice_, Integer maxPrice_)
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
