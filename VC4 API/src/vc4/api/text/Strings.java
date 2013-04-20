/**
 * 
 */
package vc4.api.text;

/**
 * @author paul
 *
 */
public class Strings {

	public static String chatLocalization(String loc, Object[] args){
		StringBuilder ag = new StringBuilder();
		for(Object o : args){
			ag.append(',');
			ag.append(o);
		}
		return "{l:" + loc + ag.toString() + "}";
	}

}
