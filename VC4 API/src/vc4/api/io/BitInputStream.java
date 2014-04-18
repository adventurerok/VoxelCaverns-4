package vc4.api.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import vc4.api.vbt.NBTInputStream;
import vc4.api.vbt.Tag;

/**
 * The BitInputStream allows reading individual bits from a general Java InputStream. Like the various Stream-classes from Java, the BitInputStream has to be created based on another Input stream. It provides a function to read the next bit
 * from the sream, as well as to read multiple bits at once and write the resulting data into an integer value.
 * 
 * @author Andreas Jakl
 */
public class BitInputStream implements Closeable, BitInput {
	/**
	 * The Java InputStream this class is working on.
	 */
	private InputStream iIs;

	/**
	 * The buffer containing the currently processed byte of the input stream.
	 */
	private int iBuffer;

	/**
	 * Next bit of the current byte value that the user will get. If it's 8, the next bit will be read from the next byte of the InputStream.
	 */
	private int iNextBit = 8;

	private int iMarkedBit = 8;
	protected int markState = 0;
	protected List<Integer> markData = new ArrayList<Integer>();
	protected int markRead = 1;
	private NBTInputStream nit;

	/**
	 * Create a new bit input stream based on an existing Java InputStream.
	 * 
	 * @param aIs
	 *            the input stream this class should read the bits from.
	 */
	public BitInputStream(InputStream aIs) {
		iIs = aIs;
	}

	/**
	 * Read a specified number of bits and return them combined as an integer value. The bits are written to the integer starting at the highest bit ( << aNumberOfBits ), going down to the lowest bit ( << 0 )
	 * 
	 * @param aNumberOfBits
	 *            defines how many bits to read from the stream.
	 * @return integer value containing the bits read from the stream.
	 * @throws IOException
	 */
	@Override
	synchronized public int readBits(final int aNumberOfBits) throws IOException {
		int value = 0;
		for (int i = aNumberOfBits - 1; i >= 0; i--)
		// for (int i = 0; i < aNumberOfBits; i--)
		{
			value |= (readBit() << i);
		}
		return value;
	}

	/**
	 * Read a specified number of bits and return them combined as an integer value. The bits are written to the integer starting at the highest bit ( << aNumberOfBits ), going down to the lowest bit ( << 0 )
	 * 
	 * @param aNumberOfBits
	 *            defines how many bits to read from the stream.
	 * @return integer value containing the bits read from the stream.
	 * @throws IOException
	 */
	@Override
	synchronized public long readLongBits(final int aNumberOfBits) throws IOException {
		long value = 0;
		for (int i = aNumberOfBits - 1; i >= 0; i--) {
			value |= (readLongBit() << i);
		}
		return value;
	}

	/**
	 * Read the next bit from the stream.
	 * 
	 * @return 0 if the bit is 0, 1 if the bit is 1.
	 * @throws IOException
	 */
	@Override
	synchronized public int readBit() throws IOException {
		if (iIs == null) throw new IOException("Already closed");

		if (iNextBit == 8) {
			if (markState == 2) {
				if (markRead < markData.size()) iBuffer = markData.get(markRead++);
				else markState = 0;
			} else if (markState == 0) iBuffer = iIs.read();
			else if (markState == 1) {
				iBuffer = iIs.read();
				markData.add(iBuffer);
			}

			if (iBuffer == -1) throw new EOFException();

			iNextBit = 0;
		}

		int bit = iBuffer & (1 << iNextBit);
		iNextBit++;

		bit = (bit == 0) ? 0 : 1;

		return bit;
	}

	public InputStream getInputStream() {
		return iIs;
	}

	public void mark(int readLimit) {
		iMarkedBit = iNextBit;
		markData.clear();
		markData.add(iBuffer);
		markRead = 0;
		markState = 1;
	}

	public Tag readNbt() throws IOException {
		if (nit == null) nit = new NBTInputStream(this);
		return nit.readTag();
	}

	public void reset() throws IOException {
		iBuffer = markData.get(0);
		iNextBit = iMarkedBit;
		markRead = 1;
		markState = 2;
	}

	/**
	 * Read the next bit from the stream.
	 * 
	 * @return 0 if the bit is 0, 1 if the bit is 1.
	 * @throws IOException
	 */
	@Override
	synchronized public long readLongBit() throws IOException {
		if (iIs == null) throw new IOException("Already closed");

		if (iNextBit == 8) {
			iBuffer = iIs.read();

			if (iBuffer == -1) throw new EOFException();

			iNextBit = 0;
		}

		int bit = iBuffer & (1 << iNextBit);
		iNextBit++;

		bit = (bit == 0) ? 0 : 1;

		return bit;
	}

	@Override
	public String readString() throws IOException {
		StringBuilder builder = new StringBuilder();
		int i = readBits((short) 32);
		for (int dofor = 0; dofor < i; ++dofor) {
			int ca = readBits((short) BitUtils.CHARACTER_BITS);
			char c = (char) ca;
			builder.append(c);
		}
		return builder.toString();

	}

	public String[] readStringArray() throws IOException {
		int i = readBits((short) 32);
		String result[] = new String[i];
		for (int dofor = 0; dofor < i; ++dofor) {
			result[dofor] = readString();
		}
		return result;
	}

	@Override
	public boolean readBoolean() throws IOException {
		return readBit() == 1;
	}

	@Override
	public int readInt() throws IOException {
		return readBits((short) 32);
	}

	@Override
	public long readLong() throws IOException {
		return readLongBits((short) 64);
	}

	@Override
	public short readShort() throws IOException {
		return (short) readBits((short) 16);
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Close the underlying input stream.
	 * 
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		iIs.close();
		iIs = null;
	}

	@Override
	public byte readByte() throws IOException {
		return (byte) readBits((short) 8);
	}

	@Override
	public void readBytes(byte[] bytes) throws IOException {
		for (int dofor = 0; dofor < bytes.length; ++dofor) {
			bytes[dofor] = readByte();
		}
	}

	public void skip(long bytes) throws IOException {
		iIs.skip(bytes);
	}

	@Override
	public void finish() throws IOException {

	}

	@Override
	public Tag readVBT() throws IOException {
		return readNbt();
	}
}
