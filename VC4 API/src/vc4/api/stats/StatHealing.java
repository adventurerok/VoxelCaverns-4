package vc4.api.stats;

import vc4.api.text.Localization;

public class StatHealing implements Stat {

	boolean negative;
	int amount;
	int max;
	
	@Override
	public String displayStat(String name) {
		if(negative) return Localization.getLocalization("stat.heal-", amount, max);
		else return Localization.getLocalization("stat.heal+", amount, max);
	}

	@Override
	public Object getValue() {
		return amount;
	}

	@Override
	public Object[] getValues() {
		return new Object[]{amount, negative, max};
	}

}
