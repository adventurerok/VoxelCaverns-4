/**
 * 
 */
package vc4.api.input;

import java.util.*;

/**
 * @author paul
 *
 */
public enum Key {

	
	ESCAPE(1),
	NUM_1(2),
	NUM_2(3),
	NUM_3(4),
	NUM_4(5),
	NUM_5(6),
	NUM_6(7),
	NUM_7(8),
	NUM_8(9),
	NUM_9(10),
	NUM_0(11),
	MINUS(12),
	EQUALS(13),
	BACK(14),
	TAB(15),
	Q(16),
	W(17),
	E(18),
	R(19),
	T(20),
	Y(21),
	U(22),
	I(23),
	O(24),
	P(25),
	LBRACKET(26),
	RBRACKET(27),
	RETURN(28),
	LCONTROL(29),
	A(30),
	S(31),
	D(32),
	F(33),
	G(34),
	H(35),
	J(36),
	K(37),
	L(38),
	SEMICOLON(39),
	APOSTROPHE(40),
	GRAVE(41),
	LSHIFT(42),
	BACKSLASH(43),
	Z(44),
	X(45),
	C(46),
	V(47),
	B(48),
	N(49),
	M(50),
	COMMA(51),
	PERIOD(52),
	SLASH(53),
	RSHIFT(54),
	MULTIPLY(55),
	LMENU(56),
	SPACE(57),
	CAPITAL(58),
	F1(59),
	F2(60),
	F3(61),
	F4(62),
	F5(63),
	F6(64),
	F7(65),
	F8(66),
	F9(67),
	F10(68),
	NUMLOCK(69),
	SCROLL(70),
	NUMPAD7(71),
	NUMPAD8(72),
	NUMPAD9(73),
	SUBTRACT(74),
	NUMPAD4(75),
	NUMPAD5(76),
	NUMPAD6(77),
	ADD(78),
	NUMPAD1(79),
	NUMPAD2(80),
	NUMPAD3(81),
	NUMPAD0(82),
	DECIMAL(83),
	F11(87),
	F12(88),
	F13(100),
	F14(101),
	F15(102),
	KANA(112),
	CONVERT(121),
	NOCONVERT(123),
	YEN(125),
	NUMPADEQUALS(141),
	CIRCUMFLEX(144),
	AT(145),
	COLON(146),
	UNDERLINE(147),
	KANJI(148),
	STOP(149),
	AX(150),
	UNLABELED(151),
	NUMPADENTER(156),
	RCONTROL(157),
	NUMPADCOMMA(179),
	DIVIDE(181),
	SYSRQ(183),
	RMENU(184),
	PAUSE(197),
	HOME(199),
	UP(200),
	PRIOR(201),
	LEFT(203),
	RIGHT(205),
	END(207),
	DOWN(208),
	NEXT(209),
	INSERT(210),
	DELETE(211),
	LMETA(219),
	RMETA(220),
	APPS(221),
	POWER(222),
	SLEEP(223),
	
	CONTROL(1024, LCONTROL.getKey(), RCONTROL.getKey()),
	SHIFT(1025, LSHIFT.getKey(), RSHIFT.getKey()),
	META(1026, LMETA.getKey(), RMETA.getKey()),
	MENU(1027, LMENU.getKey(), RMENU.getKey()),
	BRACKET(1028, LBRACKET.getKey(), RBRACKET.getKey());
	
	
	private int key;
	
	private int first, second;
	
	private static HashMap<Integer, Key> keyMap = new HashMap<Integer, Key>();
	
	static
    {
        
        for (Key key : EnumSet.allOf(Key.class))
        {
            keyMap.put(key.key, key);
        }
    }
	
	/**
	 * 
	 */
	private Key(int key) {
		this.key = key;
		
	}
	
	/**
	 * 
	 */
	private Key(int key, int first, int second) {
		this.key = key;
		this.first = first;
		this.second = second;
	}
	
	public static Key getKey(int id){
		return keyMap.get(id);
	}
	
	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}
	
	/**
	 * @return the first
	 */
	public int getFirst() {
		return first;
	}
	
	/**
	 * @return the second
	 */
	public int getSecond() {
		return second;
	}
}
