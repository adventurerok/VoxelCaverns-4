package vc4.api.stats;

import java.util.HashMap;
import java.util.Map.Entry;

public class Stats {

	private HashMap<String, Stat> stats = new HashMap<>();

	public double getStatDouble(String name) {
		return ((StatDouble) stats.get(name)).value;
	}

	public int getStatInt(String name) {
		return ((StatInt) stats.get(name)).value;
	}

	public boolean hasStat(String name) {
		return stats.get(name) != null;
	}

	public int getStatInt(String name, int def) {
		try {
			return ((StatInt) stats.get(name)).value;
		} catch (Exception e) {
			return def;
		}
	}

	public String getStatString(String name) {
		return ((StatString) stats.get(name)).value;
	}

	public Stat getStat(String name) {
		return stats.get(name);
	}

	public void setStat(String name, Stat stat) {
		stats.put(name, stat);
	}

	public StatHealing getStatHealing(String name) {
		return ((StatHealing) stats.get(name));
	}

	public String getStatsText() {
		StringBuilder result = new StringBuilder();
		for (Entry<String, Stat> v : stats.entrySet()) {
			result.append(v.getValue().displayStat(v.getKey()));
			result.append("\n");
		}
		if (result.length() > 1) return result.substring(0, result.length() - 1);
		return result.toString();
	}
}
