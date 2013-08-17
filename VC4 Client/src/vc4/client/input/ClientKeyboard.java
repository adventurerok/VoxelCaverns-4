/**
 * 
 */
package vc4.client.input;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import vc4.api.input.*;

/**
 * @author paul
 *
 */
public class ClientKeyboard implements Keyboard{

	
	public static boolean disabled = false;
	
	public Set<Integer> currentPressedKeys = new HashSet<Integer>();
	public Set<Integer> oldPressedKeys = new HashSet<Integer>();
	public Set<Integer> vOldPressedKeys = new HashSet<Integer>();
	
	public ConcurrentLinkedQueue<KeyEventData> events = new ConcurrentLinkedQueue<KeyEventData>();
	private KeyEventData current;
	
	/**
	 * 
	 */
	public ClientKeyboard() {
		Input.setKeyboard(this);
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#keyPressed(vc4.api.input.Key)
	 */
	@Override
	public boolean keyPressed(Key key) {
		if(disabled) return false;
		return isKeyDown(key) && !wasKeyDown(key);
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#isKeyDown(vc4.api.input.Key)
	 */
	@Override
	public boolean isKeyDown(Key key) {
		try{
			if(!org.lwjgl.input.Keyboard.isCreated()) return false;
			if(disabled) return false;
			if(key.getKey() < 1024){
				boolean b = org.lwjgl.input.Keyboard.isKeyDown(key.getKey());
				if(b) currentPressedKeys.add(key.getKey());
				return b;
			} else {
				boolean b = org.lwjgl.input.Keyboard.isKeyDown(key.getFirst());
				boolean b1 = org.lwjgl.input.Keyboard.isKeyDown(key.getSecond());
				if(b) currentPressedKeys.add(key.getFirst());
				if(b1) currentPressedKeys.add(key.getSecond());
				return b || b1;
			}
		} catch(Exception e){ return false;}
		
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#wasKeyDown(vc4.api.input.Key)
	 */
	@Override
	public boolean wasKeyDown(Key key) {
		if(disabled) return false;
		if(key.getKey() < 1024) return oldPressedKeys.contains(key.getKey());
		
		boolean b = oldPressedKeys.contains(key.getFirst());
		boolean b1 = oldPressedKeys.contains(key.getSecond());
		return b || b1;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#update()
	 */
	@Override
	public void update() {
		vOldPressedKeys = oldPressedKeys;
		oldPressedKeys = currentPressedKeys;
		currentPressedKeys = new HashSet<Integer>();
		boolean ctrl = isKeyDown(Key.CONTROL);
		boolean shift = isKeyDown(Key.SHIFT);
		while(org.lwjgl.input.Keyboard.next()){
			int key = org.lwjgl.input.Keyboard.getEventKey();
			boolean pressed = org.lwjgl.input.Keyboard.getEventKeyState();
			events.add(new KeyEventData(key, org.lwjgl.input.Keyboard.getEventCharacter(), pressed, ctrl, shift));
		}
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#keyReleased(vc4.api.input.Key)
	 */
	@Override
	public boolean keyReleased(Key key) {
		if(disabled) return false;
		return !isKeyDown(key) && wasKeyDown(key);
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#next()
	 */
	@Override
	public boolean next() {
		current = events.poll();
		return current != null;
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#getEventKey()
	 */
	@Override
	public Key getEventKey() {
		return current.getKey();
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#getEventChar()
	 */
	@Override
	public char getEventChar() {
		return current.getChar();
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#getEventKeyPressed()
	 */
	@Override
	public boolean getEventKeyPressed() {
		return current.wasKeyPressed();
	}

	/* (non-Javadoc)
	 * @see vc4.api.input.Keyboard#clearEvents()
	 */
	@Override
	public void clearEvents() {
		events.clear();
	}
	
	protected static class KeyEventData {

		private Key _key;
		private char _char;
		
		private boolean _ctrl;
		private boolean _shift;
		private boolean _pressed;
		public KeyEventData(int key, char c, boolean pressed, boolean ctrl, boolean shift) {
			super();
			this._key = Key.getKey(key);
			this._char = c;
			this._ctrl = ctrl;
			this._shift = shift;
			this._pressed = pressed;
		}
		
		public Key getKey(){
			return _key;
		}
		public char getChar(){
			return _char;
		}
		public boolean wasControlPressed(){
			return _ctrl;
		}
		public boolean wasShiftPressed(){
			return _shift;
		}
		public boolean wasKeyPressed(){
			return _pressed;
		}
		
		
	}

}
