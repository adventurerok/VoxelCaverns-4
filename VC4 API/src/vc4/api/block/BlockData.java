package vc4.api.block;

public class BlockData {

	short id;
	byte data;

	public BlockData(short id, byte data) {
		super();
		this.id = id;
		this.data = data;
	}

	public short getId() {
		return id;
	}

	public byte getData() {
		return data;
	}

	public void setData(byte data) {
		this.data = data;
	}

	public void setId(short id) {
		this.id = id;
	}

}
