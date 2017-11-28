package pl.edu.pw.elka.sagwedt.broker;

import pl.edu.pw.elka.sagwedt.infrastructure.AppRequest;
import pl.edu.pw.elka.sagwedt.seeker.SeekApartmentRequest;

/**
 * A request to select an apartment.
 */
public class BrokerApartmentRequest extends AppRequest
{
	private Integer min_area = 0;
	
    public BrokerApartmentRequest(SeekApartmentRequest prevRequest)
    {
        min_area = prevRequest.getMinArea();
    }
    
    public Integer getMinArea() {
    	return min_area;
    }
    
    
}
