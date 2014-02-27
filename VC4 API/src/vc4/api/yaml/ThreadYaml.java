/**
 * 
 */
package vc4.api.yaml;

import java.util.HashMap;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @author paul
 * 
 */
public class ThreadYaml {

	private static HashMap<Thread, Yaml> map = new HashMap<Thread, Yaml>();

	public static Yaml getYamlForThread() {
		if (map.containsKey(Thread.currentThread())) return map.get(Thread.currentThread());

		DumperOptions yamlOptions = new DumperOptions();
		yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Representer yamlRep = new Representer();
		yamlRep.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(yamlRep, yamlOptions);
		map.put(Thread.currentThread(), yaml);
		return yaml;
	}

}
