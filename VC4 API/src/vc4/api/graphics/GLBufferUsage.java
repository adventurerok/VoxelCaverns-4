package vc4.api.graphics;

/**
 * Created by paul on 14/12/14.
 */
public enum GLBufferUsage {

    STREAM_DRAW(35040),
    STREAM_COPY(35042),
    STREAM_READ(35041),
    STATIC_DRAW(35044),
    STATIC_READ(35045),
    STATIC_COPY(35046),
    DYNAMIC_DRAW(35048),
    DYNAMIC_READ(35049),
    DYNAMIC_COPY(35050);

    private int gli;

    GLBufferUsage(int glint) {
        gli = glint;
    }

    public int getGlInt() {
        return gli;
    }
}
