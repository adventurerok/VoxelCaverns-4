package vc4.api.io;

import java.io.IOException;

import org.jnbt.Tag;

public interface BitOutput {

	public void writeBits(int bits, int num) throws IOException;
	public void writeBits(long bits, int num) throws IOException;
	public void writeBit(int bit) throws IOException;
	public void writeBit(long bit) throws IOException;
	public void writeByte(int b) throws IOException;
	public void writeShort(short s) throws IOException;
	public void writeInt(int i) throws IOException;
	public void writeLong(long l) throws IOException;
	public void writeDouble(double d) throws IOException;
	public void writeFloat(float f) throws IOException;
	public void writeString(String s) throws IOException;
	public void writeBytes(byte[] bytes) throws IOException;
	public void writeBoolean(boolean b) throws IOException;
	public void writeVBT(Tag tag) throws IOException;
	public void finish() throws IOException;
	public void close() throws IOException;
	
}
