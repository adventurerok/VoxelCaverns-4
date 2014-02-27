package vc4.editor;

import java.util.Arrays;

import vc4.editor.nbt.NBTEditor;

public class EditorMain {

	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equals("-vbt")) {
				NBTEditor.main(Arrays.copyOfRange(args, 1, args.length));
			}
		} else {
			NBTEditor.main(new String[0]);
		}
	}
}
