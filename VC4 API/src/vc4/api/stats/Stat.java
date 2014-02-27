package vc4.api.stats;

import java.text.DecimalFormat;

public interface Stat {

	public static DecimalFormat[] dps = new DecimalFormat[] { new DecimalFormat("#"), new DecimalFormat("#.#"), new DecimalFormat("#.##"), new DecimalFormat("#.###"), new DecimalFormat("#.####"), new DecimalFormat("#.#####"),
			new DecimalFormat("#.######"), new DecimalFormat("#.#######"), new DecimalFormat("#.########") };

	public String displayStat(String name);

	public Object getValue();

	public Object[] getValues();
}
