package vc4.api.io;

import java.io.*;
import java.util.zip.Deflater;

import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;

public class SwitchOutputStream extends OutputStream implements DataOutput{

	OutputStream out;
	byte[] temp;
	int index;
	boolean inDeflateMode;
	private byte writeBuffer[] = new byte[8];
	
	public SwitchOutputStream(OutputStream out) {
		this.out = out;
	}

	public void startDeflateMode(){
		inDeflateMode = true;
		index = 0;
		if(temp == null) temp = new byte[102400];
	}
	
	@Override
	public synchronized void write(byte[] b, int off, int len) throws IOException {
		if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                   ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
		if(!inDeflateMode){
	        for (int i = 0 ; i < len ; i++) {
	            out.write(b[off + i]);
	        }
		} else {
			System.arraycopy(b, off, temp, index, len);
			index += len;
		}
	}
	
	@Override
	public synchronized void write(int b) throws IOException {
		if(!inDeflateMode) out.write(b);
		else temp[index++] = (byte) b;
	}
	
	@SuppressWarnings("resource")
	public void writeVBT(Tag tag) throws IOException{
		new NBTOutputStream(getBitOutput()).writeTag(tag);
	}
	
	public void endDeflateMode() throws IOException{
		inDeflateMode = false;
		if(index == 0) return;
		Deflater deflate = new Deflater(Deflater.DEFAULT_COMPRESSION);
		try {
			deflate.setInput(temp, 0, index);
			byte[] output = new byte[index];
			int len = deflate.deflate(output);
			writeInt(len);
			writeInt(index);
			for (int i = 0 ; i < len ; i++) {
	            out.write(output[i]);
	        }
		} finally {
			deflate.end();
		}
	}
	
	@Override
	public void close() throws IOException {
		out.close();
		temp = null;
	}
	
	public BitOutput getBitOutput(){
		return new SwitchBitOutput(this);
	}
	
	private static class SwitchBitOutput implements BitOutput {
		
		SwitchOutputStream out;
		
		private int iBuffer;
		private int iBitCount;
		
		public SwitchBitOutput(SwitchOutputStream out) {
			this.out = out;
		}

		@Override
		public void writeBits(int bits, int num) throws IOException {
			for (int i = num - 1; i >= 0; i--) {
				writeBit((bits >> i) & 0x01);
			}
		}

		@Override
		public void writeBits(long bits, int num) throws IOException {
			for (int i = num - 1; i >= 0; i--) {
				writeBit((bits >> i) & 0x01);
			}
		}

		@Override
		public void writeBit(int bit) throws IOException {
			iBuffer |= bit << iBitCount;
			iBitCount++;

			if (iBitCount == 8) {
				flush();
			}
		}

		@Override
		public void writeBit(long bit) throws IOException {
			iBuffer |= bit << iBitCount;
			iBitCount++;

			if (iBitCount == 8) {
				flush();
			}
		}

		@Override
		public void writeByte(int b) throws IOException {
			writeBits(b, 8);
		}

		@Override
		public void writeShort(short s) throws IOException {
			writeBits(s, 16);
		}

		@Override
		public void writeInt(int i) throws IOException {
			writeBits(i, 32);
		}

		@Override
		public void writeLong(long l) throws IOException {
			writeBits(l, 64);
		}

		@Override
		public void writeDouble(double d) throws IOException {
			writeBits(Double.doubleToRawLongBits(d), 64);
		}

		@Override
		public void writeFloat(float f) throws IOException {
			writeBits(Float.floatToRawIntBits(f), 32);
		}

		@Override
		public void writeString(String s) throws IOException {
			int l = s.length();
			writeBits(l, (short) 32);
			for (int dofor = 0; dofor < l; ++dofor) {
				char c = s.charAt(dofor);
				writeBits(c, (short) BitUtils.CHARACTER_BITS);
			}
		}

		@Override
		public void writeBytes(byte[] bytes) throws IOException {
			for(int d = 0; d < bytes.length; ++d) writeByte(bytes[d]);
		}
		
		public void flush() throws IOException {
			if (iBitCount > 0) {
				out.write(iBuffer);
				iBitCount = 0;
				iBuffer = 0;
			}
		}

		@Override
		public void writeBoolean(boolean b) throws IOException {
			writeBit(b ? 1 : 0);
		}

		@Override
		public void finish() throws IOException {
			flush();
		}
		
		@Override
		public void close() throws IOException {
			out.close();
		}
		
		@SuppressWarnings("resource")
		@Override
		public void writeVBT(Tag tag) throws IOException {
			new NBTOutputStream(this).writeTag(tag);
		}
		
	}

	public void writeString(String s) throws IOException{
		int l = s.length();
		writeInt(l);
		for (int dofor = 0; dofor < l; ++dofor) {
			char c = s.charAt(dofor);
			writeShort(c);
		}
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		write(v ? 1 : 0);
	}

	@Override
	public void writeByte(int v) throws IOException {
		write(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		if(!inDeflateMode){
			out.write((v >>> 8) & 0xFF);
        	out.write((v >>> 0) & 0xFF);
		} else {
			temp[index++] = (byte) ((v >>> 8) & 0xFF);
			temp[index++] = (byte) ((v >>> 0) & 0xFF);
		}
	}

	@Override
	public void writeChar(int v) throws IOException {
		write((v >>> 8) & 0xFF);
        write((v >>> 0) & 0xFF);
	}

	@Override
	public void writeInt(int v) throws IOException {
		if(!inDeflateMode){
			out.write((v >>> 24) & 0xFF);
        	out.write((v >>> 16) & 0xFF);
        	out.write((v >>>  8) & 0xFF);
    		out.write((v >>>  0) & 0xFF);
		} else {
			temp[index++] = (byte) ((v >>> 24) & 0xFF);
			temp[index++] = (byte) ((v >>> 16) & 0xFF);
			temp[index++] = (byte) ((v >>>  8) & 0xFF);
			temp[index++] = (byte) ((v >>>  0) & 0xFF);
		}
	}

	@Override
	public void writeLong(long v) throws IOException {
		writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
        write(writeBuffer);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		writeInt(Float.floatToRawIntBits(v));
	}

	@Override
	public void writeDouble(double v) throws IOException {
		writeLong(Double.doubleToRawLongBits(v));
	}

	@Override
	public void writeBytes(String s) throws IOException {
		int len = s.length();
        for (int i = 0 ; i < len ; i++) {
            write((byte)s.charAt(i));
        }
	}

	@Override
	public void writeChars(String s) throws IOException {
		writeString(s);
	}

	@Override
	public void writeUTF(String str) throws IOException {
		throw new UnsupportedOperationException("Now go pester me all day to add this, or just simply use writeString(), after all, this is intended for game save files only");
	}
	
	
	
}
