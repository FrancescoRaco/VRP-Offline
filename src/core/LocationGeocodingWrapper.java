package core;

/**
 * Location Geocoding Wrapper
 * @author Francesco Raco
 *
 */
public class LocationGeocodingWrapper
{
	/**
	 * Address
	 */
	private String address;
	
	/**
	 * Latitude
	 */
	private double lat;
	
	/**
	 * Longitude
	 */
	private double lon;
	
	/**
	 * Create object by string address
	 * @param address String address
	 */
	public LocationGeocodingWrapper(String address)
	{
		this.address = address;
	}
	
	/**
	 * Create object by latitude and longitude double values
	 * @param lat Latitude double value
	 * @param lon Longitude double value
	 */
	public LocationGeocodingWrapper(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * Get string address
	 * @return String address
	 */
	public String getAddress()
	{
		return address;
	}
	
	/**
	 * Get latitude double value
	 * @return Latitude double value
	 */
	public double getLat()
	{
		return lat;
	}
	
	/**
	 * Get longitude double value
	 * @return Longitude double value
	 */
	public double getLon()
	{
		return lon;
	}
}