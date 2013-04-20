package vc4.api.util;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;

public class Clipboard {

	// If a string is on the system clipboard, this method returns it;
	// otherwise it returns null.
	public static String getText() {
	    Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

	    try {
	        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	            String text = (String)t.getTransferData(DataFlavor.stringFlavor);
	            return text;
	        }
	    } catch (UnsupportedFlavorException e) {
	    } catch (IOException e) {
	    }
	    return null;
	}

	// This method writes a string to the system clipboard.
	// otherwise it returns null.
	public static void setText(String str) {
	    StringSelection ss = new StringSelection(str);
	    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}
}
