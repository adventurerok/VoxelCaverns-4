/**
 * 
 */
package vc4.api.logging;

/**
 * @author paul
 *
 */
public enum Level {

	FATAL(1000),
	SEVERE(900),
	WARNING(800),
	INFO(700),
	DEBUG(500),
	FINE(400),
	FINER(300),
	FINEST(200);
	
	
	int level;
	
	
	private Level(int level) {
		this.level = level;
	}
}
