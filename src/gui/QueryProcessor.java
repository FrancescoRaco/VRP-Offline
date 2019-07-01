package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import core.GeographicMap;
import core.NoInstructionsFoundException;
import core.NotExistingCoordinatesException;
import core.PathNotFoundException;
import core.UncorrectQueryException;
import test.Bus;
import test.Italy;
import test.NoStopsFoundException;
import test.Test;
import utils.CharSetOperator;

/**
 * Query Processor
 * @author Francesco Raco
 *
 */
public class QueryProcessor
{
	/**
	 * QueryProcessor unique allowed instance
	 */
	protected static QueryProcessor instance;
	
	/**
	 * Geographic map
	 */
	protected GeographicMap map;
	
	/**
	 * Constructor with geographic map
	 * 
	 */
	protected QueryProcessor(GeographicMap map)
	{
		this.map = map;
	}
	
	/**
	 * Get the unique allowed instance; if it does not exist,
	 * then create it by a geographic map as argument
	 * @param map Geographic map
	 * @return Unique allowed instance
	 */
	public static QueryProcessor getInstance(GeographicMap map)
	{
		if (instance == null) instance = new QueryProcessor(map);
		
		return instance;
	}
	
	/**
	 * Get the unique allowed instance; if it does not exist,
	 * then create it by Italy as geographic map
	 * @return Unique allowed instance
	 */
	public static QueryProcessor getInstance()
	{
		return getInstance(new Italy());
	}
	
	/**
	 * Get GeographicMap
	 * @return Geographic map
	 */
	public GeographicMap getGeographicMap()
	{
		return map;
	}
	
	/**
	 * Get Bus object by String query
	 * @param query String representing bus
	 * @return Bus by String query
	 * @throws NoSpecifiedJobsException No SpecifiedJobs Exception
	 */
	protected Bus parse(String query) throws NoSpecifiedJobsException
	{
		//String array representing bus stops
		String[] stops = query.split("\n");
		
		//If (stops <= 3) then there are not enough specified stops
		int size = stops.length;
		if (size <= 3) throw new NoSpecifiedJobsException();
		
		//Get start and end point
		String startPoint = stops[0];
		String endPoint = stops[size - 1];
		
		//Get target locations
		String[] targetLocations = new String[size - 2];
		
		//Assign every intermediate stop to targetLocations string array
		for (int i = 1; i < size - 1; i++) targetLocations[i - 1] = stops[i];
		
		//Return Bus by query
		return new Bus(startPoint, endPoint, targetLocations);
	}
	
	/**
	 * Get bus by file
	 * @param file Name of UTF-8 text file containing 1 bus stop per line (every line ends with '\n')
	 * @return Bus
	 * @throws NotCompatibleFileException Not Compatible File Exception
	 * @throws NoSpecifiedJobsException No Specified Jobs Exception
	 */
	protected Bus getBus(String file) throws NotCompatibleFileException, NoSpecifiedJobsException 
	{
		//Initialize buffered reader
		BufferedReader fileInput = null;
		
		//Initialize string builder which will store the bus stops
		StringBuilder busSB = new StringBuilder();
		
		//Try execute and handle eventual exceptions
		try
		{
			//InputStream object created by the file inputStream
			InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(file);
			
			//Throw an appropriate exception if inputStream is null
			if (inputStream == null) throw new NotCompatibleFileException();
			
			//Assign to fileInput the BufferedReader by InputStreamReader object
			//created with inputStream and "UTF-8" as arguments
			fileInput = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			
			//Do UTF-8 operations
			CharSetOperator.doUTF8Operations(fileInput);
		
			//Add every bus stop (without initial and ending white spaces) to the corresponding
			//string builder
			String stop;
			while ((stop = fileInput.readLine()) != null) busSB.append(stop.trim() + '\n');
		}
		catch(IOException e)
		{
			//Throw an appropriate exception if problems occurred during file reading
			throw new NoSpecifiedJobsException();
		}
		finally
		{
			//Close Buffered Reader resource
			try
			{
				fileInput.close();
			}
			catch (IOException e) {}
			catch(Exception e) {}
		}
		
		//Return bus by its string representation
		return parse(busSB.toString());
	}
	
	/**
	 * Process query in order to calculate TSP Solution, TSP Testing or Single Source Best Path
	 * @param oc Output choice
	 * @param file Bus file
	 * @param from Start point for Single Source Best Path
	 * @param to End point for Single Source Best Path
	 * @return String representing the desired output among 3 possibilities: TSP Solution, TSP Testing or Single Source Best Path
	 */
	public String process(OutputChoice oc, String file, String from, String to)
	{
		//Initialize bus to null
		Bus bus = null;
				
		//Initialize output string
		String output = "";
				
		//Try to execute and handle eventual exceptions
		try
		{
			//If not asking for a Single Source Best Path solution,
			//then get bus by file (path + name)
			if (!oc.equals(OutputChoice.SINGLESOURCEBESTPATH)) bus = getBus(file);
		
			//Get appropriate output (Solution, Test or Single Source Best Path)
			switch(oc)
			{
				case SOLUTION: output = Test.getJspritAlgorithmSolutionInfo(map, bus); break;
				case TEST: output = Test.getJspritAlgorithmTestingInfo(map, bus); break;
				case SINGLESOURCEBESTPATH: output = Test.getGraphHopperAlgorithmSolutionInfo(map, from, to); break;
				default: output = "Tipologia di richiesta non prevista!";
			}
		}
		//Handle Exception and close I/O stream and socket
		catch (NotCompatibleFileException e)
		{
			output = "Nessun file compatibile trovato!";
		}
		catch (NoSpecifiedJobsException e)
		{
			output = "Specificare almeno 3 fermate!";
		}
		catch (NoStopsFoundException e)
		{
			output = "Non ho trovato fermate!";
		}
		catch (UncorrectQueryException e)
		{
			output = "Richiesta non formulata correttamente!";
		}
		catch(NotExistingCoordinatesException e)
		{
			output = "Attenzione: 1 o piu' fermate richieste non sono presenti nel database!";
		}
		catch (PathNotFoundException e)
		{
			output = "Non ho trovato alcun percorso!";
		}
		catch (NoInstructionsFoundException e)
		{
			output = "Non ho trovato istruzioni!";
		}
				
		//Return output string
		return output;
	}
	
	/**
	 * Process query in order to calculate Single Source Best Path
	 * @param from Start point for Single Source Best Path
	 * @param to End point for Single Source Best Path
	 * @return Single Source Best Path solution
	 */
	public String process(String from, String to)
	{
		return process(OutputChoice.SINGLESOURCEBESTPATH, null, from, to);
	}
	
	/**
	 * Process query in order to calculate TSP solution or testing info
	 * @param oc Output desired (TSP solution or testing info)
	 * @param file File string representing bus stops
	 * @return TSP solution or Testing info (depending on OutputChoice specified)
	 */
	public String process(OutputChoice oc, String file)
	{
		return process(oc, file, null, null);
	}
}
