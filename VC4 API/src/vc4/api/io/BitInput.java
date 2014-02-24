package vc4.api.io;

import java.io.IOException;

import org.jnbt.Tag;

public interface BitInput {

	public int readBits(int num) throws IOException;
	public long readLongBits(int num) throws IOException;
	public int readBit() throws IOException;
	public long readLongBit() throws IOException;
	public byte readByte() throws IOException;
	public short readShort() throws IOException;
	public int readInt() throws IOException;
	public long readLong() throws IOException;
	public double readDouble() throws IOException;
	public float readFloat() throws IOException;
	public String readString() throws IOException;
	public void readBytes(byte[] bytes) throws IOException;
	public Tag readVBT() throws IOException;
	public boolean readBoolean() throws IOException;
	public void finish() throws IOException;
	public void close() throws IOException;
	
}
