package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import gui.Main;
import utils.CharSetOperator;


/**
 * Offline Geocoder
 * @author Francesco Raco
 *
 */
public class OfflineGeocoder
{
	/**
	 * Mappings file
	 */
	public static final String MAPPINGSFILE = "geocoding/mappings.txt";
	
	/**
	 * Online geocoding server: it can be used in order to store mappings into the
	 * offline text file
	 */
	public static String server = "http://racomaps.ns0.it/nominatim";
	
	
	/**
	 * Get Offline Geocoding (common method which provides both direct and reverse geocoding)
	 * @param address String address
	 * @param lat Double latitude
	 * @param lon Double longitude
	 * @return LocationGeocodingWrapper object containing the Direct / Reverse Geocoding values (depending on the choice)
	 * @throws NotExistingCoordinatesException Not Existing Coordinates Exception
	 */
	private static LocationGeocodingWrapper getOfflineGeocoding(String address, double lat, double lon) throws NotExistingCoordinatesException
	{
		//Create a file InputStream object by Mappings text file
		InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(MAPPINGSFILE);
		
		//Initialize to null the Location Geocoding Wrapper object
		LocationGeocodingWrapper lgw = null;
		
		//Initialize to null the mappingsReader (BufferedReader)
		BufferedReader mappingsReader = null;
		
		//Try to execute and handle eventual IOException
		try
		{
			//Assign to mappingsReader the BufferedReader created by
			//InputStreamReader with the file inputStream and "UTF-8" as arguments
			mappingsReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		
			//Do UTF-8 operations
			CharSetOperator.doUTF8Operations(mappingsReader);
		
			//Iterate on every non null text line in the mappings file
			String stop;
			while ((stop = mappingsReader.readLine()) != null)
			{
				//If asking for direct geocoding and a hit occurs
				if (address != null && stop.startsWith(address.replaceAll(" ", "-")))
				{
					//Store address, lat & lon values into a String array
					String[] line = stop.split(" ");
					
					//Create a wrapper object containing the mapping results
					lgw = new LocationGeocodingWrapper(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
					
					//End of while loop
					break;
				}
				
				//If asking for reverse geocoding and a hit occurs
				else if ((address == null) && stop.contains(Double.toString(lat)) && stop.contains(Double.toString(lon)))
				{
					//Store address, lat & lon values into a String array
					String[] line = stop.split(" ");
					
					//Create a wrapper object containing the mapping results
					lgw = new LocationGeocodingWrapper(line[0].replaceAll("-", " "));
					
					//End of while loop
					break;
				}
			}
		}
		catch(IOException e)
		{
			//Can't find mapping
			throw new NotExistingCoordinatesException();
		}
		finally
		{
			try
			{
				//Close mappingsReader BufferedReader
				mappingsReader.close();
			}
			catch (IOException e) {}
		}
		
		//Can't find mapping
		if (lgw == null) throw new NotExistingCoordinatesException();
		
		//Return wrapper object containing the mapping results
		return lgw;
	}
	
	/**
	 * Main method which lets to store online mappings into offline mappings text file
	 * @param args args
	 * @throws URISyntaxException URI Syntax Exception
	 * @throws IOException IO Exception
	 * @throws NotExistingCoordinatesException Not Existing Coordinates Exception
	 */
	public static void main(String[] args) throws URISyntaxException, IOException, NotExistingCoordinatesException
	{
		addMappingsFromServer(server);
	}
	
	/**
	 * Store online mappings into offline mappings text file
	 * @param server Geocoding server
	 * @throws URISyntaxException URI Syntax Exception
	 * @throws IOException IO Exception
	 * @throws NotExistingCoordinatesException Not Existing Coordinates Exception
	 */
	public static void addMappingsFromServer(String server) throws URISyntaxException, IOException, NotExistingCoordinatesException
	{
		//Create the mappings file writer
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ClassLoader.getSystemClassLoader().getResource(MAPPINGSFILE).toURI())), "UTF-8"));
		
		//Inserted stops
		Set<String> insertedStops = new TreeSet<String>();
		
		//Buses Path
		URL busesPath = ClassLoader.getSystemClassLoader().getResource(Main.BUSESPATH);
		
		//Buses Folder
		File busesFolder = new File(busesPath.toURI());
		
		//All files (representing the buses) contained in the buses folder
		File[] buses = busesFolder.listFiles();
	    
		//Iterate on every bus file
		for (File bus : buses)
		{
			//Create the bus file reader
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(bus), "UTF-8"));
			
			//Do UTF-8 operations
			CharSetOperator.doUTF8Operations(br);
	        
	        //Iterate on every non null or non BOM (Byte Order Mark) bus file line
			String stop = null;
	        while ((stop = br.readLine()) != null && !stop.endsWith("\\u0003") && !stop.endsWith("\\ufeff"))
	        {
	        	//If insertedStops does not contain this stop
	        	//(removing first all initial and ending white spaces),
	        	//then write this mapping into the mappings text file
	        	if (!insertedStops.contains(stop.trim()))
	        	{
	        		//Map containing the associations between "lat" & "lon" strings
	        		//and corresponding double values
	        		Map<String, Double> coords = OpenStreetMapUtils.getInstance(server).getCoordinates(stop);
	        		
	        		//Add stop to insertedStop set
	        		insertedStops.add(stop);
	        		
	        		//write this mapping into the mappings text file,
	        		//replacing first all the white spaces with "-" character
	        		bw.write(stop.replaceAll(" ", "-") + " " + coords.get("lat") + " " + coords.get("lon") + '\n');
	        	}
	        }
	        
	        //Close buffered reader
	        br.close();
	    }
		
		//Close buffered writer
		bw.close();
	}
	
	/**
	 * Get Direct Offline Geocoding
	 * @param address String address
	 * @return Map which stores mapping between "lat" + "lon" strings and corresponding double values
	 * @throws NotExistingCoordinatesException Not Existing Coordinates Exception
	 */
	public static Map<String, Double> getDirectOfflineGeocoding(String address) throws NotExistingCoordinatesException
	{
		//Get LocationGeocodingWrapper object containing the mapping result
		//(lat & lon double values = 0.0 because we are not asking for them)
		LocationGeocodingWrapper lgw = getOfflineGeocoding(address, 0.0, 0.0);
		
		//Initialize the map which will contain the mapping
		Map<String, Double> coords = new TreeMap<String, Double>();
		
		//Put associations between "lat" & "lon" values and corresponding
		//double values
		coords.put("lat", lgw.getLat());
		coords.put("lon", lgw.getLon());
		
		//Return coords map
		return coords;
	}
	
	/**
	 * Get reverse Offline Geocoding
	 * @param lat Double latitude
	 * @param lon Double longitude
	 * @return String address related to lat + lon double values
	 * @throws NotExistingCoordinatesException Not Existing Coordinates Exception
	 */
	public static String getReverseOfflineGeocoding(double lat, double lon) throws NotExistingCoordinatesException
	{
		//Get LocationGeocodingWrapper object containing the mapping result
		//(string address is null because we are not asking for it)
		LocationGeocodingWrapper lgw = getOfflineGeocoding(null, lat, lon);
		
		//Return string address related to lat & lon double values specified
		return lgw.getAddress();
	}
}
