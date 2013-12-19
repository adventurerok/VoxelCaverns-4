package vc4.api.io;

import java.io.IOException;
import java.io.OutputStream;

import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;
 
/**
 * The BitOutputStream allows writing individual bits to a
 * general Java OutputStream.
 * Like the various Stream-classes from Java, the BitOutputStream
 * has to be created based on another OutputStream. This class is able
 * to write a single bit to a stream (even though a byte has to be 
 * filled until the data is flushed to the underlying output stream).
 * It is also able to write an integer value to the stream using
 * the specified number of bits.
 *
 * @author Andreas Jakl
 */
public class BitOutputStream implements AutoCloseable{
	/**
	 * The Java OutputStream that is used to write completed
	 * bytes.
	 */
	private OutputStream iOs;
 
	/**
	 * The temponary buffer containing the individual bits
	 * until a byte has been completed and can be commited
	 * to the output stream.
	 */
	private int iBuffer;
 
	/**
	 * Counts how many bits have been cached up to now.
	 */
	private int iBitCount;
	
	private NBTOutputStream not;
 
	/**
	 * Create a new bit output stream based on an
	 * existing Java OutputSTream.
	 * @param aOs the output stream this class should use.
	 */
	public BitOutputStream(OutputStream aOs)
	{
		iOs = aOs;
	}
 
	/**
	 * Write a single bit to the stream. It will only be flushed
	 * to the underlying OutputStream when a byte has been 
	 * completed or when flush() manually.
	 * @param aBit 1 if the bit should be set, 0 if not
	 * @throws IOException
	 */
	synchronized public void writeBit(int aBit) throws IOException
	{
		if (iOs == null)
			throw new IOException("Already closed");
 
		if (aBit != 0 && aBit != 1)
		{
			throw new IOException(aBit + " is not a bit");
		}
 
		iBuffer |= aBit << iBitCount;
		iBitCount++;
 
		if (iBitCount == 8)
		{
			flush();
		}
	}
	
	/**
	 * Write a single bit to the stream. It will only be flushed
	 * to the underlying OutputStream when a byte has been 
	 * completed or when flush() manually.
	 * @param aBit 1 if the bit should be set, 0 if not
	 * @throws IOException
	 */
	synchronized public void writeBit(long aBit) throws IOException
	{
		if (iOs == null)
			throw new IOException("Already closed");
 
		if (aBit != 0 && aBit != 1)
		{
			throw new IOException(aBit + " is not a bit");
		}
 
		iBuffer |= aBit << iBitCount;
		iBitCount++;
 
		if (iBitCount == 8)
		{
			flush();
		}
	}
 
	/**
	 * Write the current cache to the stream and reset
	 * the buffer.
	 * @throws IOException
	 */
	public void flush() throws IOException
	{
		if (iBitCount > 0)
		{
			iOs.write((byte) iBuffer);
			iBitCount = 0;
			iBuffer = 0;
		}
	}
 
	/**
	 * Flush the data and close the underlying output stream.
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException
	{
		flush();
		iOs.close();
		iOs = null;
	}
 
	/**
	 * Write the specified number of bits from the int value
	 * to the stream. Correspondig to the InputStream,
	 * the bits are written starting at the highest bit 
	 * ( >> aNumberOfBits ), going down to the lowest bit ( >> 0 ).
	 * @param aValue the int containing the bits that should
	 * be written to the stream.
	 * @param aNumBits how many bits of the integer should
	 * be written to the stream.
	 * @throws IOException
	 */
	synchronized public void writeBits(final int aValue, final int aNumBits) 
            throws IOException
	{
		for (int i = aNumBits - 1; i >= 0; i--)
		{
			writeBit((aValue >> i) & 0x01);
		}
	}
	
	/**
	 * Write the specified number of bits from the int value
	 * to the stream. Correspondig to the InputStream,
	 * the bits are written starting at the highest bit 
	 * ( >> aNumberOfBits ), going down to the lowest bit ( >> 0 ).
	 * @param aValue the long containing the bits that should
	 * be written to the stream.
	 * @param aNumBits how many bits of the integer should
	 * be written to the stream.
	 * @throws IOException
	 */
	synchronized public void writeBits(final long aValue, final int aNumBits) 
            throws IOException
	{
		for (int i = aNumBits - 1; i >= 0; i--)
		{
			writeBit(((aValue >> i) & 0x01));
		}
	}
	
	
 
	/**
	 * Calculate how many bits are needed to store the specified
	 * value. Can be used to optimize data transfer.
	 * @param aMaxValue the value that you want to test.
	 * @return how many bits the specified value needs to be
	 * stored.
	 */
	public static int getRequiredNumOfBits(final int aMaxValue)
	{
		// 0 still requires 1 bit to write it.
		if (aMaxValue == 0)
			return 1;
 
		// Go from left to right and search for the first 1
		// 00011010
		//    |---- First 1
		int curBit;
		for (curBit = 31; curBit >= 0; curBit--)
		{
			if ((aMaxValue & (0x01 << curBit)) > 0)
			{
				// Found first bit that is not null - max. value
				break;
			}
		}
		return curBit + 1;
 
		// Real maximum value is everything filled with 1 from this point on.
		// 00011111
		//int maxVal = powOf2(curBit + 1) - 1;
	}
	
	public void writeString(String value) throws IOException{
		int l = value.length();
		writeBits(l, (short) 32);
		for(int dofor = 0; dofor < l; ++dofor){
			char c = value.charAt(dofor);
			writeBits(c, (short) BitUtils.CHARACTER_BITS);
		}
		
	}
	public void writeString(char[] value) throws IOException{
		writeString(new String(value));
		
	}
	public void writeStringArray(String value[]) throws IOException{
		if(value == null) value = new String[0];
		int l = value.length;
		writeBits(l, (short) 32);
		for(int dofor = 0; dofor < value.length; ++dofor){
			writeString(value[dofor]);
		}
		
	}
	
	public void writeBoolean(boolean bool) throws IOException{
		writeBit(bool ? 1 : 0);
	}
	
	public void writeShort(short val) throws IOException{
		writeBits(val, (short) 16);
	}
	public void writeInt(int val) throws IOException{
		writeBits(val, (short) 32);
	}
	public void writeLong(long val) throws IOException{
		writeBits(val, (short)64);
	}
	public void writeFloat(float val) throws IOException{
		writeInt(Float.floatToRawIntBits(val));
	}
	public void writeDouble(double val) throws IOException{
		writeLong(Double.doubleToLongBits(val));
	}

	public void writeByte(byte b) throws IOException {
		writeBits(b, (short) 8);
	}
	public void writeBytes(byte[] b) throws IOException{
		for(int dofor = 0; dofor < b.length; ++dofor) writeByte(b[dofor]);
	}
	
	public void writeNbt(Tag in) throws IOException{
		if(not == null) not = new NBTOutputStream(this);
		not.writeTag(in);
	}
 
}
