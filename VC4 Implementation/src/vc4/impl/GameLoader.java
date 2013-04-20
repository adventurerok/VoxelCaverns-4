/**
 * 
 */
package vc4.impl;

import java.util.Date;
import java.util.Locale;

import vc4.api.Version;
import vc4.api.logging.Handler;
import vc4.api.logging.Logger;
import vc4.api.text.Localization;
import vc4.impl.logging.ImplLoggerFactory;

/**
 * @author paul
 *
 */
public class GameLoader {

	public static void load(Handler...startupHandlers){
		new ImplLoggerFactory();
		for(Handler h: startupHandlers){
			Logger.addDefaultHandler(h);
		}
		Logger.getLogger("VC4").info(new Date());
		Version.loadVersion();
		Locale locale = Locale.getDefault();
		System.out.println(locale.getDisplayCountry());
		Localization.loadLocalization(locale.toString());
	}

}
