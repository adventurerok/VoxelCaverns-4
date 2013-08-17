/**
 * 
 */
package vc4.api.gui;

import java.util.ArrayList;

import vc4.api.font.FontRenderer;
import vc4.api.gui.listeners.TextListener;
import vc4.api.input.*;
import vc4.api.util.Clipboard;

/**
 * @author paul
 * 
 */
public class TextBox extends Component {
	
	private static FontRenderer font = FontRenderer.createFontRenderer("unispaced_14", 14);

	/** The key repeat interval */
	private static final int INITIAL_KEY_REPEAT_INTERVAL = 400;
	/** The key repeat interval */
	protected static final int KEY_REPEAT_INTERVAL = 50;

	protected String value = "";

	/** The current cursor position */
	protected int cursorPos;

	/** True if the cursor should be visible */
	protected boolean visibleCursor = true;

	/** The last key pressed */
	protected Key lastKey = null;

	/** The last character pressed */
	protected char lastChar = 0;

	/** The time since last key repeat */
	protected long repeatTimer;
	
	protected boolean password = false;

	/** The text before the paste in */
	private String oldText;
	
	protected String notext = "";

	/** The cursor position before the paste */
	private int oldCursorPos;

	private boolean wasInFocus = false;

	private Keyboard keyboard;
	
	private ArrayList<TextListener> listeners = new ArrayList<>();
	
	protected static String hidden;

	static {
		StringBuilder h = new StringBuilder("*");
		for (int dofor = 0; dofor < 250; ++dofor) {
			h.append("*");
		}
		hidden = h.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.gui.Component#update()
	 */
	@Override
	public void update() {
		if (hasFocus()) {
			keyboard = Input.getClientKeyboard();
			if (!wasInFocus) keyboard.clearEvents();
			try {
				while (keyboard.next()) {
					if (keyboard.getEventKeyPressed()) {
						Key key = keyboard.getEventKey();
						char posChar = keyboard.getEventChar();
						keyPressed(key, posChar);
					}
				}
			} catch (Exception e) {
			}
		}
		wasInFocus = hasFocus();
	}
	
	@Override
	public void setFocus(boolean focus) {
		if(!focus) setText("");
		super.setFocus(focus);
	}

	/**
	 * @param key
	 * @param posChar
	 */
	private void keyPressed(Key key, char c) {
		if (!hasFocus()) return;
		if(key == Key.ESCAPE){
			setFocus(false);
		}
		boolean ctrl = keyboard.isKeyDown(Key.CONTROL);
		if (ctrl && key != null) {
			if (key == Key.Z) {
				if (oldText != null) {
					doUndo(oldCursorPos, oldText);
				}
			} else if (key == Key.V) {
				String text = Clipboard.getText();
				if (text != null) {
					doPaste(text);
				}
			}
		}
		if (key != null && ctrl || keyboard.isKeyDown(Key.MENU)) return;
		if (lastKey != key) {
			lastKey = key;
			repeatTimer = System.currentTimeMillis() + INITIAL_KEY_REPEAT_INTERVAL;
		} else {
			repeatTimer = System.currentTimeMillis() + KEY_REPEAT_INTERVAL;
		}
		lastChar = c;
		if (key == Key.LEFT && cursorPos > 0) --cursorPos;
		else if (key == Key.RIGHT && cursorPos < value.length()) ++cursorPos;
		else if (key == Key.BACK) {
			if ((cursorPos > 0) && (value.length() > 0)) {
				if (cursorPos < value.length()) {
					value = value.substring(0, cursorPos - 1) + value.substring(cursorPos);
				} else {
					value = value.substring(0, cursorPos - 1);
				}
				cursorPos--;
			}

		} else if (key == Key.DELETE) {
			if (value.length() > cursorPos) {
				value = value.substring(0, cursorPos) + value.substring(cursorPos + 1);
			}

		} else if ((((c < 256) && (c > 31)) || c == '\t') && (value.length() < 100)) {
			if (cursorPos < value.length()) {
				value = value.substring(0, cursorPos) + c
						+ value.substring(cursorPos);
			} else {
				value = value.substring(0, cursorPos) + c;
			}
			cursorPos++;
		} else if(key == Key.RETURN){
			for(TextListener l : listeners){
				l.textRecieved(this, value);
			}
			setFocus(false);
		}
	}

	/**
	 * Do the paste into the field, overrideable for custom behaviour
	 * 
	 * @param text
	 *            The text to be pasted in
	 */
	protected void doPaste(String text) {
		recordOldPosition();

		for (int i = 0; i < text.length(); i++) {
			keyPressed(null, text.charAt(i));
		}
	}
	
	/**
	 * Do the undo of the paste, overrideable for custom behaviour
	 * 
	 * @param oldCursorPos before the paste
	 * @param oldText The text before the last paste
	 */
	protected void doUndo(int oldCursorPos, String oldText) {
		if (oldText != null) {
			setText(oldText);
			setCursorPos(oldCursorPos);
		}
	}
	
	public void addListener(TextListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Record the old position and content
	 */
	protected void recordOldPosition() {
		oldText = getText();
		oldCursorPos = cursorPos;
	}
	
	public String getText(){
		return value;
	}
	
	/**
	 * Set the value to be displayed in the text field
	 * 
	 * @param value
	 *            The value to be displayed in the text field
	 */
	public void setText(String value) {
		this.value = value;
		if (cursorPos > value.length()) {
			cursorPos = value.length();
		}
	}

	/**
	 * Set the position of the cursor
	 * 
	 * @param pos
	 *            The new position of the cursor
	 */
	public void setCursorPos(int pos) {
		cursorPos = pos;
		if (cursorPos > value.length()) {
			cursorPos = value.length();
		}
	}
	
	@Override
	public void draw() {
//		if(drawBackground){
//			Renderer.drawRectangle(Color.brown, getBounds());
//			Renderer.drawOutline(BasicButtonUI.getBackColor(state), getBounds());
//		}
		if (lastKey != null) {
			if (Input.getClientKeyboard().isKeyDown(lastKey)) {
				if (repeatTimer < System.currentTimeMillis()) {
					repeatTimer = System.currentTimeMillis() + KEY_REPEAT_INTERVAL;
					keyPressed(lastKey, lastChar);
				}
			} else {
				lastKey = null;
			}
		}

		try{
			if((value != null && !value.isEmpty()) || hasFocus()){
				int cpos = (int) font.measureString(value.substring(0, cursorPos), 14).x;
				int tx = 0;
				if (cpos > getWidth()) {
					tx = (int) (getWidth() - cpos - font.measureString("_", 14).x);
				}
				String rendering = value;
				if(value == null) rendering = "";
				else if(password){
					int length = value.length();
					rendering = hidden.substring(0, length);
				}
				if(cursorPos < rendering.length()){
					String start = rendering.substring(0, cursorPos);
					String end = rendering.substring(cursorPos, rendering.length());
					int at = 0;
					font.renderString(at + tx + 2 + getX(), getY(), start);
					at += font.measureString(start, 14).x;
					font.renderString(at + tx + 2 + getX(), getY(), "|");
					at += font.measureString("|", 14).x;
					font.renderString(at + tx + 2 + getX(), getY(), end);
				} else {
					font.renderString(tx + 2 + getX(), getY(), rendering + (hasFocus() && visibleCursor ? "_" : "") );
				}
			} else {
				font.renderString(2 + getX(), getY(), "{f:i}" + notext.toString());
			}
			font.resetStyles();
		} catch(Exception e){
		}

	}
}
