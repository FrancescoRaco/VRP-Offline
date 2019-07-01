package utils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * CharSet Operator
 * @author Francesco Raco
 *
 */
public class CharSetOperator
{
	/**
	 * Do UTF-8 Operations
	 * @param br Buffered reader
	 * @throws IOException IO Exception
	 */
	public static void doUTF8Operations(BufferedReader br) throws IOException
	{
		//Mark the present position in the stream
		br.mark(1);
	
		//If first char is not 0xFEFF (UTF-8 Byte Order Mark), then reset the stream to the previous mark
		if (br.read() != 0xFEFF) br.reset();
	}
}
