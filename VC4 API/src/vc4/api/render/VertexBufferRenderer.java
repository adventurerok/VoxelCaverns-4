/**
 *
 */
package vc4.api.render;

import java.awt.Color;
import java.nio.FloatBuffer;

import vc4.api.graphics.*;
import vc4.api.list.FloatList;
import vc4.api.util.BufferUtils;
import vc4.api.vector.*;

/**
 * @author paul
 *
 * The data renderer for vertex buffers
 *
 */
public class VertexBufferRenderer implements Renderer {

    private static OpenGL gl;

    FloatBuffer buffer;
    FloatList data = new FloatList();

    Vector4f color = new Vector4f(1, 1, 1, 1);
    Vector4f tex = new Vector4f(0, 0, 0, 0);

    Vector3d trans = new Vector3d(0, 0, 0);

    Vertex[] quad = new Vertex[4];
    Vertex[] tri = new Vertex[3];
    int qPos = 0;

    boolean quadInput = false;

    int amountOfVertexes = 0;

    boolean createdArray = false;
    int arrayId = 0;

    public int getVertexCount(){
        return amountOfVertexes;
    }

    /**
     *
     */
    public VertexBufferRenderer() {
        if (gl == null) gl = Graphics.getOpenGL();
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.impl.world.rendering.Renderer#vertex(double, double, double)
     */
    @Override
    public void vertex(double x, double y, double z) {
        if (!quadInput) {
            vertex(new Vertex(new Vector3d(x + trans.x, y + trans.y, z + trans.z), color, tex));
            return;
        }
        if (qPos < 4) {
            quad[qPos++] = new Vertex(new Vector3d(x + trans.x, y + trans.y, z + trans.z), color, tex);
        }
        if (qPos == 4) {
            vertex(quad[0]);
            vertex(quad[2]);
            vertex(quad[1]);

            vertex(quad[0]);
            vertex(quad[3]);
            vertex(quad[2]);

            qPos = 0;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.impl.world.rendering.Renderer#vertex(vc4.impl.world.rendering.DataRenderer.Vertex)
     */
    @Override
    public void vertex(Vertex v) {
        inputVertex(v);

    }

    private void inputVertex(Vertex v) {
        data.add((float) (v.position.x));
        data.add((float) (v.position.y));
        data.add((float) (v.position.z));
        data.add(v.color.x);
        data.add(v.color.y);
        data.add(v.color.z);
        data.add(v.color.w);
        data.add(v.tex.x);
        data.add(v.tex.y);
        data.add(v.tex.z);
        data.add(v.tex.w);
        ++amountOfVertexes;
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.impl.world.rendering.Renderer#color(float, float, float, float)
     */
    @Override
    public void color(float r, float g, float b, float a) {
        color = new Vector4f(r, g, b, a);
    }

    @Override
    public void color(double r, double g, double b, double a) {
        color = new Vector4f((float) r, (float) g, (float) b, (float) a);
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.impl.world.rendering.Renderer#tex(float, float, float, float)
     */
    @Override
    public void tex(float s, float t, float r, float q) {
        tex = new Vector4f(s, t, r, q);
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.impl.world.rendering.Renderer#setTranslate(double, double, double)
     */
    @Override
    public void setTranslate(double x, double y, double z) {
        trans = new Vector3d(x, y, z);
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.impl.world.rendering.Renderer#useQuadInputMode(boolean)
     */
    @Override
    public void useQuadInputMode(boolean use) {
        quadInput = use;
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.impl.world.rendering.Renderer#compile()
     */
    @Override
    public void compile() {
        if (amountOfVertexes < 1) return;
        buffer = BufferUtils.createFloatBuffer(data.size());
        buffer.put(data.toArray());
        buffer.flip();

        arrayId = gl.genBuffers();
        gl.bindBuffer(GLBufferType.ARRAY_BUFFER, arrayId);

        gl.bufferData(GLBufferType.ARRAY_BUFFER, buffer, GLBufferUsage.STATIC_DRAW);

        gl.bindBuffer(GLBufferType.ARRAY_BUFFER, 0);

        buffer = null;

        data = null;
        tri = null;
        quad = null;
        color = null;
        tex = null;
        trans = null;
    }

    public int getBufferId() {
        return arrayId;
    }

    /*
         * (non-Javadoc)
         *
         * @see vc4.impl.world.rendering.Renderer#render()
         */
    @Override
    public void render() {
        if (amountOfVertexes < 1) return;
        if(arrayId == 0) throw new RuntimeException("VBO not compiled");

        render(gl, arrayId, GLPrimitive.TRIANGLES, 0, amountOfVertexes);
    }


    public static void render(OpenGL gl, int array, GLPrimitive type, int start, int count){
        gl.bindBuffer(GLBufferType.ARRAY_BUFFER, array);

        gl.vertexAttribPointer(0, 3, GLType.FLOAT, false, 44, 0);

        gl.vertexAttribPointer(3, 4, GLType.FLOAT, false, 44, 12);

        gl.vertexAttribPointer(8, 4, GLType.FLOAT, false, 44, 28);

        gl.drawArrays(type, start, count);

        gl.bindBuffer(GLBufferType.ARRAY_BUFFER, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see vc4.api.graphics.Renderer#color(java.awt.Color)
     */
    @Override
    public void color(Color color) {
        color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    /**
     *
     */
    @Override
    public void destroy() {
        gl.deleteBuffers(arrayId);
        buffer = null;
        data = null;
    }

    @Override
    public void tex(double s, double t, double r, double q) {
        tex((float) s, (float) t, (float) r, (float) q);

    }

    @Override
    public void color(Color color, double multiply) {
        color((color.getRed() / 255f) * multiply, (color.getGreen() / 255f) * multiply, (color.getBlue() / 255f) * multiply, color.getAlpha() / 255f);

    }

    @Override
    public void color(Vector4f color) {
        this.color = color;
    }

    @Override
    public void tex(Vector4f tex) {
        this.tex = tex;
    }

    @Override
    public void addVertex(Vector3d vertex) {
        vertex(vertex.x, vertex.y, vertex.z);
    }

    @Override
    public void tex(Vector3f tex) {
        tex(tex.x, tex.y, tex.z, 0);

    }

    @Override
    public void light(float r, float g, float b, boolean sky) {
        // Not Needed
    }

    @Override
    public void light(float r, float g, float b, float sky) {
    }

    @Override
    public void light(Vector3f light, boolean sky) {
    }

    @Override
    public void light(Vector3f light, float sky) {
    }

    @Override
    public void tex(double s, double t, double r) {
        tex = new Vector4f((float) s, (float) t, (float) r, 0);
    }

}
