package vc4.api.biome;

public class ZoomGenStatic extends ZoomGenerator {

	int[] data;

	public void setData(int[] data) {
		this.data = data;
	}

	public ZoomGenStatic(int[] data) {
		super(null);
		this.data = data;
	}

	@Override
	public int[] generate(long x, long z, int size) {
		return data;
	}

}
