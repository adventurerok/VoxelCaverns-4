package vc4.api.stats;

import vc4.api.text.Localization;

public class StatString implements Stat {
	
	String value;

	@Override
	public String displayStat(String name) {
		return Localization.getLocalization("stat." + name, value);
	}

	public StatString(String value) {
		super();
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public Object[] getValues() {
		return new Object[]{value};
	}

}
