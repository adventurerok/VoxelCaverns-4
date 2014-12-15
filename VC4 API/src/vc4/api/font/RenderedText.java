package vc4.api.font;

/**
 * Created by paul on 13/12/14.
 *
 * Represents text that has already been rendered, and can be rerendered when required
 */
public interface RenderedText {

    public void draw();
    public float getX();
    public float getY();
    public float getWidth();
    public float getHeight();
    public String getText();
    public float getSize();
    public void release();
}
