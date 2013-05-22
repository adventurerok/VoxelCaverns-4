package vc4.api.stats;

import vc4.api.text.Localization;

public class StatInt implements Stat {

	int value;
	
	
	
	public StatInt(int value) {
		super();
		this.value = value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String displayStat(String name) {
		return Localization.getLocalization("stat." + name, value);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Object[] getValues() {
		return new Object[]{value};
	}

}
