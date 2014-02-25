package vc4.api.io;

import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.jnbt.NBTInputStream;
import org.jnbt.Tag;

public class SwitchInputStream extends InputStream implements DataInput {
	
	InputStream in;
	boolean inInflateMode;
	byte data[];
	int index = 0;
	int length;
	
	int ch1, ch2, ch3, ch4;

	public SwitchInputStream(InputStream in) {
		this.in = in;
	}
	
	public boolean inInflateMode(){
		if(!inInflateMode) return false;
		if(index >= data.length) return false;
		return true;
	}
	
	public void readDeflated() throws IOException{
		int amt = readInt();
		int uncompressed = readInt();
		byte comp[] = new byte[amt];
		readFully(comp, 0, amt);
		Inflater inflate = new Inflater();
		data = new byte[uncompressed];
		inflate.setInput(comp, 0, amt);
		try {
			length = inflate.inflate(data);
		} catch (DataFormatException e) {
			throw new IOException("Bad compression format", e);
		} finally {
			inflate.end();
		}
		inInflateMode = true;
	}
	
	public byte[] readDeflatedBytes() throws IOException {
		int amt = readInt();
		int uncompressed = readInt();
		byte comp[] = new byte[amt];
		readFully(comp, 0, amt);
		Inflater inflate = new Inflater();
		byte[] data = new byte[uncompressed];
		inflate.setInput(comp, 0, amt);
		try {
			length = inflate.inflate(data);
		} catch (DataFormatException e) {
			throw new IOException("Bad compression format", e);
		} finally {
			inflate.end();
		}
		return data;
	}
	
	@Override
	public int read() throws IOException {
		if(!inInflateMode) return in.read();
		else {
			if(index == data.length){
				inInflateMode = false;
				return in.read();
			}
			return data[index++];
		}
	}
	
	@SuppressWarnings("resource")
	public Tag readVBT() throws IOException {
		return new NBTInputStream(getBitInput()).readTag();
	}
	
	public BitInput getBitInput(){
		return new SwitchBitInput(this);
	}
	
	@Override
	public void close() throws IOException {
		in.close();
		data = null;
	}
	
	private static class SwitchBitInput implements BitInput{
		
		SwitchInputStream in;
		private int iBuffer;
		private int iNextBit = 8;
		
		public SwitchBitInput(SwitchInputStream in) {
			this.in = in;
		}

		@Override
		public int readBits(int num) throws IOException {
			int value = 0;
			for (int i = num - 1; i >= 0; i--)
			//for (int i = 0; i < aNumberOfBits; i--)
			{
				value |= (readBit() << i);
			}
			return value;
		}

		@Override
		public long readLongBits(int num) throws IOException {
			long value = 0;
			for (int i = num - 1; i >= 0; i--)
			//for (int i = 0; i < aNumberOfBits; i--)
			{
				value |= (readLongBit() << i);
			}
			return value;
		}

		@Override
		public int readBit() throws IOException {
			if (iNextBit == 8)
			{
				iBuffer = in.read();
				if (iBuffer == -1)
					throw new EOFException();
	 
				iNextBit = 0;
			}
			
			int bit = iBuffer & (1 << iNextBit);
			iNextBit++;
	 
			bit = (bit == 0) ? 0 : 1;
	 
			return bit;
		}
		
		@Override
		@SuppressWarnings("resource")
		public Tag readVBT() throws IOException {
			return new NBTInputStream(this).readTag();
		}

		@Override
		public long readLongBit() throws IOException {
			if (iNextBit == 8)
			{
				iBuffer = in.read();
	 
				if (iBuffer == -1)
					throw new EOFException();
	 
				iNextBit = 0;
			}
	 
			int bit = iBuffer & (1 << iNextBit);
			iNextBit++;
	 
			bit = (bit == 0) ? 0 : 1;
	 
			return bit;
		}

		@Override
		public byte readByte() throws IOException {
			return (byte) readBits(8);
		}
		

		@Override
		public short readShort() throws IOException {
			return (short) readBits(16);
		}

		@Override
		public int readInt() throws IOException {
			return readBits(32);
		}

		@Override
		public long readLong() throws IOException {
			return readLongBits(64);
		}

		@Override
		public double readDouble() throws IOException {
			return Double.longBitsToDouble(readLongBits(64));
		}

		@Override
		public float readFloat() throws IOException {
			return Float.intBitsToFloat(readBits(32));
		}

		@Override
		public String readString() throws IOException {
			StringBuilder builder = new StringBuilder();
			int i = readBits((short) 32);
			for(int dofor = 0; dofor < i; ++dofor){
				int ca = readBits((short) BitUtils.CHARACTER_BITS);
				char c = (char)ca;
				builder.append(c);
			}
			return builder.toString();
		}

		@Override
		public void readBytes(byte[] bytes) throws IOException {
			for(int d = 0; d < bytes.length; ++d) bytes[d] = (byte) readBits(8);
		}

		@Override
		public boolean readBoolean() throws IOException {
			return readBit() == 1;
		}

		@Override
		public void finish() throws IOException {
		}
		
		@Override
		public void close() throws IOException {
			in.close();
		}
		
		
	}
	
	public String readString() throws IOException {
		int len = readInt();
		StringBuilder b = new StringBuilder();
		for(int d = 0; d < len; ++d){
			b.append((char)readShort());
		}
		return b.toString();
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		readFully(b, 0, b.length);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		 if (len < 0)
	            throw new IndexOutOfBoundsException();
	        int n = 0;
	        while (n < len) {
	            int count = read(b, off + n, len - n);
	            if (count < 0)
	                throw new EOFException();
	            n += count;
	        }
	}

	@Override
	public int skipBytes(int n) throws IOException {
		int skipped = 0;
		if(inInflateMode){
			int oldIndex = index;
			index += n;
			n -= data.length - oldIndex;
			skipped += data.length - oldIndex;
			if(index < data.length) return n;
		}
		return (int) (skipped + in.skip(n));
	}

	@Override
	public boolean readBoolean() throws IOException {
		int ch = in.read();
        if (ch < 0)
            throw new EOFException();
        return (ch != 0);
	}

	@Override
	public byte readByte() throws IOException {
		return (byte)read();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return read() & 0xFF;
	}

	@Override
	public short readShort() throws IOException {
		return (read() << 8) | read();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return (read() << 8) | read();
	}

	@Override
	public char readChar() throws IOException {
		return (char)readShort();
	}

	@Override
	public int readInt() throws IOException {
		ch1 = read();
        ch2 = read();
        ch3 = read();
        ch4 = read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
	private byte readBuffer[] = new byte[8];

	@Override
	public long readLong() throws IOException {
		readFully(readBuffer, 0, 8);
        return (((long)readBuffer[0] << 56) +
                ((long)(readBuffer[1] & 255) << 48) +
                ((long)(readBuffer[2] & 255) << 40) +
                ((long)(readBuffer[3] & 255) << 32) +
                ((long)(readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) <<  8) +
                ((readBuffer[7] & 255) <<  0));
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public String readLine() throws IOException {
		throw new UnsupportedOperationException("You want to read a line? USE A BUFFERED READER");
	}

	@Override
	public String readUTF() throws IOException {
		throw new UnsupportedOperationException("Make a request on github or somethin");
	}
	

}
