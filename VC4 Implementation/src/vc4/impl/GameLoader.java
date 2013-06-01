/**
 * 
 */
package vc4.impl;

import java.util.Date;

import vc4.api.Version;
import vc4.api.io.SaveFormats;
import vc4.api.logging.Handler;
import vc4.api.logging.Logger;
import vc4.api.text.Localization;
import vc4.impl.io.VBTSaveFormat;
import vc4.impl.io.VCH4SaveFormat;
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
//		Locale locale = Locale.getDefault();
//		System.out.println(locale.getDisplayCountry());
//		Localization.loadLocalization(locale.toString());
		Localization.loadLocalization("en_GB");
		loadSaveFormats();
	}
	
	private static void loadSaveFormats(){
		SaveFormats.registerSaveFormat("VBT", new VBTSaveFormat());
		SaveFormats.registerSaveFormat("VCH4", new VCH4SaveFormat());
	}

}
