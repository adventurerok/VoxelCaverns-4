package vc4.api.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
 
/**
 * The BitInputStream allows reading individual bits from a
 * general Java InputStream.
 * Like the various Stream-classes from Java, the BitInputStream
 * has to be created based on another Input stream. It provides
 * a function to read the next bit from the sream, as well as to read multiple
 * bits at once and write the resulting data into an integer value.
 *
 * @author Andreas Jakl
 */
public class BitInputStream {
	/**
	 * The Java InputStream this class is working on.
	 */
	private InputStream iIs;
 
	/**
	 * The buffer containing the currently processed
	 * byte of the input stream.
	 */
	private int iBuffer;
 
	/**
	 * Next bit of the current byte value that the user will
	 * get. If it's 8, the next bit will be read from the 
	 * next byte of the InputStream.
	 */
	private int iNextBit = 8;
	
	private int iMarkedBit = 8;
	protected int markState = 0;
	protected List<Integer> markData = new ArrayList<Integer>();
	protected int markRead = 1;
 
	/**
	 * Create a new bit input stream based on an existing Java InputStream.
	 * @param aIs the input stream this class should read the bits from.
	 */
	public BitInputStream(InputStream aIs)
	{
		iIs = aIs;
	}
 
	/**
	 * Read a specified number of bits and return them combined as
	 * an integer value. The bits are written to the integer
	 * starting at the highest bit ( << aNumberOfBits ), going down 
	 * to the lowest bit ( << 0 )
	 * @param aNumberOfBits defines how many bits to read from the stream.
	 * @return integer value containing the bits read from the stream.
	 * @throws IOException
	 */
	synchronized public int readBits(final short aNumberOfBits) 
            throws IOException
	{
		int value = 0;
		for (int i = aNumberOfBits - 1; i >= 0; i--)
		//for (int i = 0; i < aNumberOfBits; i--)
		{
			value |= (readBit() << i);
		}
		return value;
	}
	
	/**
	 * Read a specified number of bits and return them combined as
	 * an integer value. The bits are written to the integer
	 * starting at the highest bit ( << aNumberOfBits ), going down 
	 * to the lowest bit ( << 0 )
	 * @param aNumberOfBits defines how many bits to read from the stream.
	 * @return integer value containing the bits read from the stream.
	 * @throws IOException
	 */
	synchronized public long readBitsAsLong(final short aNumberOfBits) 
            throws IOException
	{
		long value = 0;
		for (int i = aNumberOfBits - 1; i >= 0; i--)
		{
			value |= (readBitAsLong() << i);
		}
		return value;
	}
 
	/**
	 * Read the next bit from the stream.
	 * @return 0 if the bit is 0, 1 if the bit is 1.
	 * @throws IOException
	 */
	synchronized public int readBit() throws IOException
	{
		if (iIs == null)
			throw new IOException("Already closed");
 
		if (iNextBit == 8)
		{
			if(markState == 2){
				if(markRead < markData.size()) iBuffer = markData.get(markRead++);
				else markState = 0;
			}
			else if(markState == 0)iBuffer = iIs.read();
			else if(markState == 1){
				iBuffer = iIs.read();
				markData.add(iBuffer);
			}
 
			if (iBuffer == -1)
				throw new EOFException();
 
			iNextBit = 0;
		}
 
		int bit = iBuffer & (1 << iNextBit);
		iNextBit++;
 
		bit = (bit == 0) ? 0 : 1;
 
		return bit;
	}
	
	public void mark(int readLimit){
		iMarkedBit = iNextBit;
		markData.clear();
		markData.add(iBuffer);
		markRead = 0;
		markState = 1;
	}
	
	public void reset() throws IOException{
		iBuffer = markData.get(0);
		iNextBit = iMarkedBit;
		markRead = 1;
		markState = 2;
	}
	
	/**
	 * Read the next bit from the stream.
	 * @return 0 if the bit is 0, 1 if the bit is 1.
	 * @throws IOException
	 */
	synchronized public long readBitAsLong() throws IOException
	{
		if (iIs == null)
			throw new IOException("Already closed");
 
		if (iNextBit == 8)
		{
			iBuffer = iIs.read();
 
			if (iBuffer == -1)
				throw new EOFException();
 
			iNextBit = 0;
		}
 
		int bit = iBuffer & (1 << iNextBit);
		iNextBit++;
 
		bit = (bit == 0) ? 0 : 1;
 
		return bit;
	}
	
	public String readString() throws IOException{
		StringBuilder builder = new StringBuilder();
		int i = readBits((short) 32);
		for(int dofor = 0; dofor < i; ++dofor){
			int ca = readBits((short) BitUtils.CHARACTER_BITS);
			char c = (char)ca;
			builder.append(c);
		}
		return builder.toString();

	}
	
	public String[] readStringArray() throws IOException{
		int i = readBits((short) 32);
		String result[] = new String[i];
		for(int dofor = 0; dofor < i; ++dofor){
			result[dofor] = readString();
		}
		return result;
	}
	
	
	public boolean readBoolean() throws IOException{
		return readBit() == 1;
	}
	
	public int readInt() throws IOException{
		return readBits((short) 32);
	}
	
	public long readLong() throws IOException{
		return readBitsAsLong((short) 64);
	}
	public short readShort() throws IOException{
		return (short) readBits((short) 16);
	}
	public float readFloat() throws IOException{
		return Float.intBitsToFloat(readInt());
	}
	public double readDouble() throws IOException{
		return Double.longBitsToDouble(readLong());
	}
 
	/**
	 * Close the underlying input stream.
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		iIs.close();
		iIs = null;
	}

	public byte readByte() throws IOException {
		return (byte) readBits((short) 8);
	}

	public void readBytes(byte[] bytes) throws IOException {
		for(int dofor = 0; dofor < bytes.length; ++dofor){
			bytes[dofor] = readByte();
		}
	}
	
	public void skip(long bytes) throws IOException{
		iIs.skip(bytes);
	}
}
