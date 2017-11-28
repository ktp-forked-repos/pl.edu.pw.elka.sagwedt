package pl.edu.pw.elka.sagwedt.seeker;

import pl.edu.pw.elka.sagwedt.infrastructure.AppRequest;

/**
 * Request for {@link Seeker} to seek apartment.
 */
public class SeekApartmentRequest extends AppRequest
{
	private Integer min_area = 0;
	
    public SeekApartmentRequest()
    {
    	
    }
    
    public SeekApartmentRequest(Integer main_area_)
    {
    	min_area = main_area_;
    }
    
    public Integer getMinArea() {
    	return min_area;
    }
}
