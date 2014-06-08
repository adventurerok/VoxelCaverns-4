package vc4.api.block;

public enum ExtendedSize {

	SIX(0),
	FOURTEEN(1),
	TWENTYTWO(2),
	THIRTY(3);
	
	private int s;
	
	private ExtendedSize(int s) {
		this.s = s;
	}
	
	public int getBits() {
		return s;
	}
	
}
