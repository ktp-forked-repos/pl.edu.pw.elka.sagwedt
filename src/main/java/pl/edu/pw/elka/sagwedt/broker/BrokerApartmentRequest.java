package pl.edu.pw.elka.sagwedt.broker;

import pl.edu.pw.elka.sagwedt.infrastructure.AppRequest;
import pl.edu.pw.elka.sagwedt.seeker.SeekApartmentRequest;

/**
 * A request to select an apartment.
 */
public class BrokerApartmentRequest extends AppRequest
{
	private Integer minArea = 0;
	private Integer maxArea = Integer.MAX_VALUE;
	private Integer minPrice = 0;
	private Integer maxPrice = Integer.MAX_VALUE;

	
    public BrokerApartmentRequest(SeekApartmentRequest prevRequest)
    {
        minArea = prevRequest.getMinArea();
        maxArea = prevRequest.getMaxArea();
    	minPrice = prevRequest.getMinPrice();
    	maxPrice = prevRequest.getMaxPrice();
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
