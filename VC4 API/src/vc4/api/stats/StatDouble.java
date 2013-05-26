package vc4.api.stats;

import vc4.api.text.Localization;

public class StatDouble implements Stat {

	double value;
	int decimals = 2;
	
	
	
	public StatDouble(double value, int decimals) {
		super();
		this.value = value;
		this.decimals = decimals;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String displayStat(String name) {
		return Localization.getLocalization("stat." + name, dps[decimals].format(value));
	}

	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public Object[] getValues() {
		return new Object[]{value};
	}

}
