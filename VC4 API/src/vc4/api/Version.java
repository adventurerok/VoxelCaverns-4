/**
 * 
 */
package vc4.api;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.yaml.snakeyaml.Yaml;

import vc4.api.logging.Logger;
import vc4.api.util.DirectoryLocator;
import vc4.api.yaml.ThreadYaml;

/**
 * @author paul
 * 
 */
@SuppressWarnings("unchecked")
public class Version {

	public static final int DEVELOPMENT_STAGE, MAJOR, MINOR, BUILD, REVISION;
	public static final String VERSION;

	private static int ds = 0, ma = 0, mi = 0, b = 0, r = 0;

	private static JFrame changelogWriter;

	static {

		InputStream in = Version.class.getClassLoader().getResourceAsStream("vc4/impl/build/build.yml");
		Yaml yaml = ThreadYaml.getYamlForThread();
		LinkedHashMap<String, ?> map = (LinkedHashMap<String, ?>) yaml.load(in);
		try {
			in.close();
		} catch (IOException e1) {
		}

		for (Entry<String, ?> e : map.entrySet()) {
			if (!(e.getValue() instanceof Number)) continue;
			Number v = (Number) e.getValue();
			switch (e.getKey()) {
				case "stage":
					ds = v.intValue();
					break;
				case "major":
					ma = v.intValue();
					break;
				case "minor":
					mi = v.intValue();
					break;
				case "build":
					b = v.intValue();
					break;
				case "revision":
					r = v.intValue();
					break;
			}
		}

		String path = DirectoryLocator.getPath() + "/settings/build.yml";
		File f = new File(path);
		// Build modifier only works in eclipse!
		if (f.exists() && !Version.class.getClassLoader().getResource("vc4/impl/build/build.yml").getFile().contains("!")) {
			try {
				map = (LinkedHashMap<String, ?>) yaml.load(new FileInputStream(f));
				boolean a_ds = false, a_ma = false, a_mi = false, a_b = false, a_r = false;
				for (Entry<String, ?> e : map.entrySet()) {
					if (!(e.getValue() instanceof Boolean)) continue;
					boolean a = (Boolean) e.getValue();
					switch (e.getKey()) {
						case "allowstage":
							a_ds = a;
							break;
						case "allowmajor":
							a_ma = a;
							break;
						case "allowminor":
							a_mi = a;
							break;
						case "allowbuild":
							a_b = a;
							break;
						case "allowrevision":
							a_r = a;
							break;
					}
				}

				ArrayList<Object> nList = new ArrayList<Object>();
				nList.add("None");
				if (a_ds) nList.add("Development Stage");
				if (a_ma) nList.add("Major");
				if (a_mi) nList.add("Minor");
				if (a_r) nList.add("Revision");
				if (a_b) nList.add("Build");

				Object[] objects = nList.toArray();
				int i = JOptionPane.showOptionDialog(null, "Select Version Change", "Build", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, objects, objects[0]);
				if (i == -1)
				; // -1 error fixed
				else if (objects[i].equals("Development Stage")) {
					++ds;
					ma = 1;
					mi = r = 0;
					++b;
				} else if (objects[i].equals("Major")) {
					++ma;
					mi = r = 0;
					++b;
				} else if (objects[i].equals("Minor")) {
					++mi;
					r = 0;
					++b;
				} else if (objects[i].equals("Revision")) {
					++r;
					++b;
				} else if (objects[i].equals("Build")) {
					++b;
				}

				Map<String, Object> dump = new HashMap<String, Object>();
				dump.put("stage", ds);
				dump.put("major", ma);
				dump.put("minor", mi);
				dump.put("revision", r);
				dump.put("build", b);

				URL url = Version.class.getClassLoader().getResource("vc4/impl/build/build.yml");
				String loc = URLDecoder.decode(url.getFile(), "UTF-8");
				FileWriter r = new FileWriter(loc);
				yaml.dump(dump, r);
				r.close();
				loc = loc.replace("/bin/", "/src/");
				r = new FileWriter(loc);
				yaml.dump(dump, r);
				r.close();

				if (i > 0) {
					changelogWriter = (JFrame) Version.class.getClassLoader().loadClass("vc4.impl.version.ChangelogUpdater").newInstance();
					changelogWriter.setVisible(true);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			DEVELOPMENT_STAGE = ds;
			MAJOR = ma;
			MINOR = mi;
			BUILD = b;
			REVISION = r;
		} else {
			DEVELOPMENT_STAGE = ds;
			MAJOR = ma;
			MINOR = mi;
			BUILD = b;
			REVISION = r;
		}

		String devStage = "";
		if (ds == 0) devStage = "Development";
		else if (ds == 1) devStage = "Alpha";
		else if (ds == 2) devStage = "Beta";
		else if (ds == 3) devStage = "Release Canadate";

		VERSION = "VC4-" + devStage + "-" + ma + "." + mi + "." + r + "-" + b;

		Logger.getLogger("VC4").info("Loading " + VERSION);
	}

	public static void loadVersion() {
	}

}
