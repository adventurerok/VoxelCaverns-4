package vc4.api.graphics;

/**
 * Created by paul on 14/12/14.
 */
public enum GLBufferType {

    ARRAY_BUFFER(34962);

    private int gli;

    GLBufferType(int glint) {
        gli = glint;
    }

    public int getGlInt() {
        return gli;
    }
}
