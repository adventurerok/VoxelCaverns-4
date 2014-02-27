package vc4.api.graphics;

public interface GraphicsInfo {

	public int getGlVersion();

	public boolean hasShaderSupport();

	public boolean hasTextureArraySupport();

	public boolean hasRequiredGraphics();

}
