/**
 * 
 */
package vc4.client.font;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import vc4.api.Resources;
import vc4.api.client.ClientWindow;
import vc4.api.font.*;
import vc4.api.graphics.*;
import vc4.api.graphics.shader.ShaderManager;
import vc4.api.math.MathUtils;
import vc4.api.render.DataRenderer;
import vc4.api.text.Localization;
import vc4.api.vector.Vector2f;
import vc4.api.vector.Vector3f;

/**
 * @author paul
 * 
 */
public class ClientFontRenderer extends FontRenderer {

	private static class ClientRenderedText implements RenderedText{
		int backs, vertexes, lines, texture;
		float x, y, width, height, size;
		String text;

		@Override
		public void draw() {
			sm.bindShader("fontback");
			gl.callList(backs);

			sm.bindShader("font");
			gl.bindTexture(GLTexture.TEX_2D_ARRAY, texture);
			gl.callList(vertexes);

			gl.bindShader("shapes");
			gl.callList(lines);
		}

		@Override
		public float getX() {
			return x;
		}

		@Override
		public float getY() {
			return y;
		}

		@Override
		public float getWidth() {
			return width;
		}

		@Override
		public float getHeight() {
			return height;
		}

		@Override
		public String getText() {
			return text;
		}

		@Override
		public float getSize() {
			return size;
		}
	}

	private static OpenGL gl;
	private static ShaderManager sm;
	private static Random random = new Random();

	private static final float italicMax = 5, italicMin = -2;
	private static final float italicDiff = italicMax - italicMin;
	private static final boolean italicsAdvance = false;

	private static String colorChart = "0123456789abcdefgnt";

	Font font;
	float size;

	private Format<Color> color = new Format<Color>(Color.white);
	private Format<Color> uColor = new Format<Color>(null);
	private Format<Color> sColor = new Format<Color>(null);
	private Format<Color> bColor = new Format<Color>(new Color(1F, 1F, 1F, 0F));
	private boolean bold = false;
	private boolean italic;
	private boolean rendering;
	private Format<Boolean> underline = new Format<Boolean>(false);
	private Format<Boolean> strike = new Format<Boolean>(false);
	private Format<Boolean> randColor = new Format<Boolean>(false);
	private Format<Boolean> randLetter = new Format<Boolean>(false);
	private float globalY;

	private boolean disableFormat = false;

	private ConcurrentLinkedQueue<Line> lines = new ConcurrentLinkedQueue<Line>();
	private ConcurrentLinkedQueue<Background> backgrounds = new ConcurrentLinkedQueue<Background>();
	private ConcurrentLinkedQueue<Vertex> vertexes = new ConcurrentLinkedQueue<Vertex>();

	private char lastChar;

	private Color changedColor = null;

	private Line uLine = null;
	private Line sLine = null;
	private Background back = null;

	private boolean mBold, mItalics;
	private float alpha = 1F;

	public ClientFontRenderer(String font, float defaultSize) {
		this.font = Resources.getFont(font);
		size = defaultSize;
		if (gl == null) gl = Graphics.getOpenGL();
		if (sm == null) sm = Graphics.getClientShaderManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.font.FontRenderer#renderString(float, float, java.lang.String)
	 */
	@Override
	public RenderedText renderString(float x, float y, String text) {
		return renderString(x, y, text, size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.font.FontRenderer#renderString(float, float, java.lang.String, float)
	 */
	@Override
	public RenderedText renderString(float x, float y, String text, float size) {
		ClientRenderedText output = new ClientRenderedText();
		output.x = x;
		output.y = y;
		output.size = size;
		output.text = text;

		boolean wasRendering = rendering;
		int lineCount = 1;
		float startX = x;
		float maxX = x;
		float sizeMulti = size / font.getSize();
		if (!rendering) {
			gl.lineWidth(2 * (size / 32F));
			color.setChanged(true);
			if (underline.get()) uLine = new Line(x, y + font.getBase() * sizeMulti + 1, getColor(uColor));
			if (strike.get()) sLine = new Line(x, y + font.getBase() * sizeMulti * 0.67F, getColor(sColor));
			if (getColor(bColor).getAlpha() > 3) back = new Background(x, y, font, getColor(bColor), sizeMulti);
		}
		rendering = true;
		for (int d = 0; d < text.length(); ++d) {
			if (randColor.get()) {
				color.set(new Color(random.nextInt(0xFFFFFF)));
			}
			if (color.hasChanged()) {
				if (underline.get() && uColor.get() == null) {
					if (uLine != null) lines.add(uLine);
					uLine = new Line(x, y + font.getBase() * sizeMulti + 1, color.get());
				}
				if (strike.get() && sColor.get() == null) {
					if (sLine != null) lines.add(sLine);
					sLine = new Line(x, y + font.getBase() * sizeMulti * 0.67F, color.get());
				}
				if (bColor.get() == null) {
					if (back != null) backgrounds.add(back);
					back = new Background(x, y, font, color.get(), sizeMulti);
				}
				Color c = color.get();
				gl.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, alpha);
				changedColor = c;
			}
			if (uColor.hasChanged() && underline.get()) {
				if (uLine != null) lines.add(uLine);
				uLine = new Line(x, y + font.getBase() * sizeMulti + 1, getColor(uColor));
			}
			if (sColor.hasChanged() && strike.get()) {
				if (sLine != null) lines.add(sLine);
				sLine = new Line(x, y + font.getBase() * sizeMulti * 0.67F, getColor(sColor));
			}
			if (bColor.hasChanged()) {
				if (back != null) backgrounds.add(back);
				if (getColor(bColor).getAlpha() > 3) back = new Background(x, y, font, getColor(bColor), sizeMulti);
				else back = null;
			}
			if (underline.hasChanged()) {
				if (underline.get()) {
					uLine = new Line(x, y + font.getBase() * sizeMulti + 1, getColor(uColor));
				} else {
					if (uLine != null) lines.add(uLine);
					uLine = null;
				}
			}
			if (strike.hasChanged()) {
				if (strike.get()) {
					sLine = new Line(x, y + font.getBase() * sizeMulti * 0.67F, getColor(sColor));
				} else {
					if (sLine != null) lines.add(sLine);
					sLine = null;
				}
			}
			char c = text.charAt(d);
			if (c == '\n') {
				if (underline.get() && uLine != null) lines.add(uLine);
				if (strike.get() && sLine != null) lines.add(sLine);
				if (back != null) backgrounds.add(back);

				if(x > maxX) maxX = x;
				x = startX;
				y += size;
				lineCount+= 1;
				if (underline.get()) uLine = new Line(x, y + font.getBase() * sizeMulti + 1, getColor(uColor));
				if (strike.get()) sLine = new Line(x, y + font.getBase() * sizeMulti * 0.67F, getColor(sColor));
				if (getColor(bColor).getAlpha() > 3) back = new Background(x, y, font, getColor(bColor), sizeMulti);
				continue;
			}
			if (c == '\t') {
				float l = 64 * sizeMulti;
				x += l;
				if (underline.get()) uLine.addLength(l);
				if (strike.get()) sLine.addLength(l);
				if (bColor.get().getAlpha() > 3 && back != null) back.addLength(l);
				continue;
			}
			if (c == '{' && !disableFormat) {
				int indent = 0;
				StringBuilder format = new StringBuilder();
				for (d += 1; d < text.length(); ++d) {
					char s = text.charAt(d);
					if (s == '}') --indent;
					else if (s == '{') ++indent;
					if (indent < 0) break;
					format.append(s);
				}
				globalY = y;
				x += handleFormat(x, y, size, format.toString());
				if (globalY != y) y = globalY;
				continue;
			}
			float l = renderChar(x, y, c, sizeMulti);
			x += l;
			if (underline.get()) uLine.addLength(l);
			if (strike.get()) sLine.addLength(l);
			if (bColor.get().getAlpha() > 3 && back != null) back.addLength(l);
		}
		if (!wasRendering) {
			if (underline.get() && uLine != null) lines.add(uLine);
			if (strike.get() && sLine != null) lines.add(sLine);
			if (back != null) backgrounds.add(back);

			DataRenderer render = new DataRenderer();
			int count = renderBacks(render);
			render.compile();
			int list = render.createList(0, count, GLPrimitive.LINES);
			output.backs = list;

			render = new DataRenderer();
			count = renderVertexes(render);
			render.compile();
			list = render.createList(0, count, GLPrimitive.TRIANGLES);
			output.vertexes = list;

			render = new DataRenderer();
			count = renderLines(render);
			render.compile();
			list = render.createList(0, count, GLPrimitive.TRIANGLES);
			output.lines = list;

			//sm.unbindShader();
			rendering = false;
		}
		globalY = y;
		output.width = Math.max(x, maxX) - startX;
		output.height = lineCount * size;
		return output;
	}

	private float handleFormat(float x, float y, float size, String format) {
		String parts[] = format.split(";");
		float ret = 0;
		for (String s : parts) {
			float f = subHandleFormat(x, y, size, s);
			ret += f;
			x += f;
		}
		return ret;
	}

	/**
	 * @param format
	 */
	private float subHandleFormat(float x, float y, float size, String format) {
		int firstIndex = format.indexOf(":");
		if (firstIndex == -1) return 0;
		String key = format.substring(0, firstIndex);
		String value = format.substring(firstIndex + 1, format.length());
		char kc = key.length() == 1 ? key.charAt(0) : ' ';
		if (kc == 'c') {
			return colorFormat(this.color, value, false);
		} else if (key.equals("l")) {
			String oa[] = value.split(",");
			String args[] = Arrays.copyOfRange(oa, 1, oa.length);
			String loc = Localization.getLocalization(oa[0], args);
			return renderString(x, y, loc, size).getWidth(); //TODO must fix
		} else if (kc == 'n') {
			disableFormat = true;
			float f = renderString(x, y, value, size).getWidth(); //TODO must fix: text not actually rendered
			disableFormat = false;
			return f;
		} else if (kc == 'f') {
			String val = value;
			if (val.length() != 1) return 0;
			char c = val.charAt(0);
			if (c == 'b') {
				bold = !bold;
				return 0;
			} else if (c == 'i') {
				int ret = 0;
				if (italic) {
					if (italicsAdvance) {
						Gylph g;
						if (bold) g = font.getBoldPair().getGylph(lastChar);
						else g = font.getGylph(lastChar);
						float aLine = (32 - g.yoffset) / 32F;
						float imod = italic ? (italicDiff * aLine + italicMin) : 0;
						imod *= (size / font.getSize());
						ret = MathUtils.floor(imod + 0.5F);
					}
				}
				italic = !italic;
				return ret;
			} else if (c == 'u') {
				underline.set(!underline.get());
				return 0;
			} else if (c == 's') {
				strike.set(!strike.get());
			} else if (c == 'r') {
				bold = false;
				underline.set(false);
				strike.set(false);

				color.set(Color.white);
				uColor.set(null);
				sColor.set(null);
				bColor.set(new Color(1F, 1F, 1F, 0F));

				int ret = 0;
				if (italic) {
					if (italicsAdvance) {
						Gylph g;
						if (bold) g = font.getBoldPair().getGylph(lastChar);
						else g = font.getGylph(lastChar);
						float aLine = (32 - g.yoffset) / 32F;
						float imod = italic ? (italicDiff * aLine + italicMin) : 0;
						imod *= (size / font.getSize());
						ret = MathUtils.floor(imod + 0.5F);
					}
					italic = !italic;
				}
				return ret;
			}
		} else if (kc == 'u') {
			return colorFormat(uColor, value, true);
		} else if (kc == 's') {
			return colorFormat(sColor, value, true);
		} else if (kc == 'b') {
			return colorFormat(bColor, value, true);
		} else if (kc == 'r') {
			char c = value.charAt(0);
			if (c == 'c') randColor.set(!randColor.get());
			else if (c == 'l') randLetter.set(!randLetter.get());
		} else if (kc == 'o') {
			String setting = ClientWindow.getClientWindow().getGame().getSetting(value).getString();
			return renderString(x, y, setting, size).getWidth(); //TODO check if breaks anything
		} else if (key.equals("lo")) {
			String oa[] = value.split(",");
			String args[] = Arrays.copyOfRange(oa, 1, oa.length);
			String setting = ClientWindow.getClientWindow().getGame().getSetting(oa[0]).getString();
			String loc = Localization.getLocalization(setting, args);
			return renderString(x, y, loc, size).getWidth();
		}
		return 0;
	}

	private float renderChar(float x, float y, char c, float sizeMulti) {
		Gylph g;
		if (bold) g = font.getBoldPair().getGylph(c);
		else g = font.getGylph(c);
		x += g.xoffset * sizeMulti;
		if (randLetter.get()) {
			if (bold) g = font.getBoldPair().getRandomGylph(g);
			else g = font.getRandomGylph(g);
		}
		y += g.yoffset * sizeMulti;
		float ex = x + (g.width * sizeMulti);
		float ey = y + (g.height * sizeMulti);

		float aLine = (32 - g.yoffset) / 32F;
		float bLine = (32 - (g.yoffset + g.height)) / 32F;

		float imod = italic ? (italicDiff * aLine + italicMin) : 0;
		float lmod = italic ? (italicDiff * bLine + italicMin) : 0;
		imod *= sizeMulti;
		lmod *= sizeMulti;

		vertexes.add(new Vertex(changedColor, new Vector3f(g.sx, g.sy, g.page), new Vector2f(x + imod, y)));
		changedColor = null;
		vertexes.add(new Vertex(changedColor, new Vector3f(g.ex, g.sy, g.page), new Vector2f(ex + imod, y)));
		vertexes.add(new Vertex(changedColor, new Vector3f(g.ex, g.ey, g.page), new Vector2f(ex + lmod, ey)));
		vertexes.add(new Vertex(changedColor, new Vector3f(g.sx, g.ey, g.page), new Vector2f(x + lmod, ey)));

		lastChar = c;

		return g.xadvance * sizeMulti;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.font.FontRenderer#resetStyles()
	 */
	@Override
	public void resetStyles() {
		bold = false;
		underline.set(false);
		strike.set(false);

		color.set(Color.white);
		uColor.set(null);
		sColor.set(null);
		bColor.set(new Color(1F, 1F, 1F, 0F));

		italic = false;
	}

	private int renderLines(DataRenderer render) {
		if (lines.isEmpty()) return 0;
		//sm.bindShader("shapes");
		int count = lines.size();
		Line line;
		while (!lines.isEmpty()) {
			line = lines.poll();
			if (line == null) continue;
			Color c = line.color;
			render.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, alpha);
			render.addVertex(line.startX, line.y, 0);
			render.addVertex(line.endX, line.y, 0);
		}
		return count;
	}

	private int renderVertexes(DataRenderer render) {
		//sm.bindShader("font");
		//gl.bindTexture(GLTexture.TEX_2D_ARRAY, font.getTexture().getTexture());
		//gl.begin(GLPrimitive.QUADS);
		//render = new DataRenderer();
		render.useQuadInputMode(true);
		Vertex v;

		int count = vertexes.size();
		while ((v = vertexes.poll()) != null) {
			if (v.color != null) {
				Color c = v.color;
				render.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, alpha);
			}
			render.tex(v.tex.x, v.tex.y, v.tex.z);
			render.addVertex(v.pos.x, v.pos.y, 0f);
		}
		render.useQuadInputMode(false);

		return count;

		//render.compile();
		//render.render();
		//gl.bindTexture(GLTexture.TEX_2D_ARRAY, 0);
	}

	private int renderBacks(DataRenderer render) {
		if (backgrounds.isEmpty()) return 0;
		//sm.bindShader("fontback");
		//gl.begin(GLPrimitive.QUADS);
		render.useQuadInputMode(true);

		Background back;

		int count = backgrounds.size();
		while (!backgrounds.isEmpty()) {
			back = backgrounds.poll();
			if (back == null) continue;
			Color c = back.color;
			render.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, alpha);
			render.addVertex(back.startX, back.startY, 1);
			render.addVertex(back.endX, back.startY, 1);
			render.addVertex(back.endX, back.endY, 1);
			render.addVertex(back.startX, back.endY, 1);
		}
		render.useQuadInputMode(false);

		return count;
	}

	private float colorFormat(Format<Color> color, String text, boolean allowNull) {
		if (text.length() == 1) {
			int pos = colorChart.indexOf(text.charAt(0));
			color.set(ChatColor.getColor(pos));
			if (!allowNull && color.get() == null) color.set(Color.white);
		} else if (text.length() == 6) {
			try {
				color.set(new Color(Integer.parseInt(text, 16)));
			} catch (NumberFormatException e) {
			}
		}
		return 0;
	}

	private Color getColor(Format<Color> color) {
		Color c = color.get();
		if (c == null) c = this.color.get();
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.font.FontRenderer#measureString(java.lang.String, float)
	 */
	@Override
	public Vector2f measureString(String text, float size) {
		mBold = mItalics = false;
		return subMeasureString(text, size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.font.FontRenderer#measureString(java.lang.String)
	 */
	public Vector2f subMeasureString(String text, float size) {
		Vector2f result = new Vector2f(0, 0);
		String[] parts = text.split("\n");
		float cl = 0;
		float sizeMod = size / font.getSize();
		char lastChar = ' ';
		for (int d = 0; d < parts.length; ++d) {
			cl = 0;
			String s = parts[d];
			if (!s.isEmpty() || d != parts.length - 1) result.y += size;
			for (int z = 0; z < s.length(); ++z) {
				char c = s.charAt(z);
				if (c == ' ') {
					cl += 16F * sizeMod;
				} else if (c == '\t') {
					cl += 64F * sizeMod;
				} else if (c == '{' && !disableFormat) {
					int indent = 0;
					StringBuilder format = new StringBuilder();
					for (z += 1; z < s.length(); ++z) {
						char q = s.charAt(z);
						if (q == '}') --indent;
						else if (q == '{') ++indent;
						if (indent < 0) break;
						format.append(s);
					}
					String subs[] = format.toString().split(";");
					for (String ss : subs) {
						int firstIndex = ss.indexOf(":");
						if (firstIndex == -1) continue;
						String key = ss.substring(0, firstIndex);
						String value = ss.substring(firstIndex + 1, ss.length());
						if (key.equals("f")) {
							if (value.equals("b")) mBold = !mBold;
							else if (value.equals("i")) {
								mItalics = !mItalics;
								if (!mItalics) {
									if (italicsAdvance) {
										Gylph g;
										if (mBold) g = font.getBoldPair().getGylph(lastChar);
										else g = font.getGylph(lastChar);
										if (g == null) continue;
										float aLine = (32 - g.yoffset) / 32F;
										float imod = italic ? (italicDiff * aLine + italicMin) : 0;
										imod *= sizeMod;
										cl += MathUtils.floor(imod + 0.5F);
									}
								}
							}
						} else if (key.equals("l")) {
							String oa[] = value.split(",");
							String args[] = Arrays.copyOfRange(oa, 1, oa.length);
							String loc = Localization.getLocalization(oa[0], args);
							cl = subMeasureString(loc, result, cl);
						} else if (key.equals("o")) {
							String setting = ClientWindow.getClientWindow().getGame().getSetting(parts[1]).getString();
							cl = subMeasureString(setting, result, cl);
						} else if (key.equals("lo")) {
							String oa[] = value.split(",");
							String args[] = Arrays.copyOfRange(oa, 1, oa.length);
							String setting = ClientWindow.getClientWindow().getGame().getSetting(oa[0]).getString();
							String loc = Localization.getLocalization(setting, args);
							cl = subMeasureString(loc, result, cl);
						}
					}
					continue;
				} else {
					Gylph g;
					if (mBold) g = font.getBoldPair().getGylph(c);
					else g = font.getGylph(c);

					if (g == null) continue;
					cl += g.xadvance * sizeMod;
					lastChar = c;
				}
			}
			if (cl > result.x) result.x = cl;
		}

		return result;
	}

	private float subMeasureString(String loc, Vector2f result, float cl) {
		int i = loc.indexOf('\n');
		String locF = loc;
		if (i != -1) locF = loc.substring(0, i);
		Vector2f v = subMeasureString(locF, size);
		cl += v.x;

		if (i != -1) {
			int i2 = loc.lastIndexOf('\n');
			cl = 0;
			if (i2 != i) {
				String next = loc.substring(locF.length(), i2);
				Vector2f v2 = subMeasureString(next, size);
				result.y += v2.y;
				if (v2.x > result.x) result.x = v2.x;
			}
			Vector2f v3 = subMeasureString(loc.substring(i2 + 1, loc.length()), size);
			cl = v3.x;
			result.y += v3.y;
		}
		return cl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.font.FontRenderer#setAlpha(float)
	 */
	@Override
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

}
