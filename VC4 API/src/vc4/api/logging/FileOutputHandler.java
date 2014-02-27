/**
 * 
 */
package vc4.api.logging;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ConcurrentLinkedQueue;

import vc4.api.util.DirectoryLocator;

/**
 * @author paul
 * 
 */
public class FileOutputHandler extends OutputHandler {

	PrintWriter output;
	int lines = 0;
	volatile boolean open;
	ConcurrentLinkedQueue<String> closedTextToAdd = new ConcurrentLinkedQueue<>();

	/**
	 * 
	 */
	public FileOutputHandler() {
		try {
			File f = new File(DirectoryLocator.getPath() + "/logs/log.log");
			if (!f.exists() && !f.getParentFile().mkdirs() && !f.createNewFile()) return;
			lines = count(f.getAbsolutePath());
			output = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			open = true;
		} catch (IOException e) {
			Logger.getLogger(FileOutputHandler.class).warning("Failed to open logging file out", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.vc3d.logging.OutputHandler#printText(game.vc3d.logging.Level, java.lang.String)
	 */
	@Override
	protected void printText(Level level, String text) {
		synchronized (output) {
			if (open) {
				output.println(text);
				++lines;
				if (lines > 1000) try {
					shiftFiles();
				} catch (IOException | UnsupportedOperationException e) {
					Logger.getLogger(FileOutputHandler.class).warning("Failed to shift files", e);
				}
			} else {
				closedTextToAdd.add(text);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.vc3d.logging.OutputHandler#close()
	 */
	@Override
	public void close() {
		open = false;
		output.close();
	}

	public void shiftFiles() throws IOException, UnsupportedOperationException {
		open = false;
		output.close();
		File f = new File(DirectoryLocator.getPath() + "/logs/log4.log");
		File f1 = null;
		if (f.exists()) f.delete();
		for (int d = 3; d > 0; --d) {
			f = new File(DirectoryLocator.getPath() + "/logs/log" + d + ".log");
			if (!f.exists()) continue;
			f1 = new File(DirectoryLocator.getPath() + "/logs/log" + (d + 1) + ".log");
			Files.move(f.toPath(), f1.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		f = new File(DirectoryLocator.getPath() + "/logs/log.log");
		f1 = new File(DirectoryLocator.getPath() + "/logs/log1.log");
		Files.move(f.toPath(), f1.toPath(), StandardCopyOption.REPLACE_EXISTING);
		f.delete();
		f.createNewFile();
		output = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
		lines = 0;
		open = true;
		String s = null;
		while ((s = closedTextToAdd.poll()) != null) {
			output.println(s);
			++lines;
		}
	}

	public int count(String filename) throws IOException {
		if (!new File(filename).exists()) return 0;
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(filename));
			try {
				byte[] c = new byte[1024];
				int count = 0;
				int readChars = 0;
				boolean empty = true;
				while ((readChars = is.read(c)) != -1) {
					empty = false;
					for (int i = 0; i < readChars; ++i) {
						if (c[i] == '\n') ++count;
					}
				}
				return (count == 0 && !empty) ? 1 : count;
			} finally {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
