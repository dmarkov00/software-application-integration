package models.client;
/**
 * Each travel is from origin Address to destination Address.
 * @author mpesic
 */
public class Address {
	private String street;
	private int number;
	private String city;

	public Address() {
		setStreet(null);
		setNumber(0);
		setCity(null);
	}
	
	public Address(String street, int number, String city) {
		setStreet(street);
		setNumber(number);
		setCity(city);
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return street + " " + number + ", " + city;
	}
	
}
