package pl.edu.pw.elka.sagwedt.finder;

/**
 * Representation of a single apartment offer data.
 */
public class Apartment
{
	private String type;
	private Integer price;
	private Integer area;
	private String district;
	private Integer numberOfRooms;
	private Integer buildYear;
	private String email;
	private String telephone;
	private String URL;
	private Integer size;

    /**
     * Default constructor.
     */
    public Apartment(){}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(final Integer price) {
		this.price = price;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(final Integer area) {
		this.area = area;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(final String district) {
		this.district = district;
	}

	public Integer getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(final Integer numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public Integer getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(final Integer buildYear) {
		this.buildYear = buildYear;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(final String uRL) {
		URL = uRL;
	}
	
	
	public Integer getSize() {
		return size;
	}

	public void setSize(final Integer s) {
		this.size = s;
	}
	
	
}