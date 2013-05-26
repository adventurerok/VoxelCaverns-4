package vc4.api.stats;

import vc4.api.text.Localization;

public class StatHealing implements Stat {

	boolean negative;
	int amount;
	int max;
	
	@Override
	public String displayStat(String name) {
		if(negative) return Localization.getLocalization("stat." + name + "-", amount, max);
		else return Localization.getLocalization("stat." + name + "+", amount, max);
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}

	public StatHealing(boolean negative, int amount, int max) {
		super();
		this.negative = negative;
		this.amount = amount;
		this.max = max;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public Integer getValue() {
		return amount;
	}
	
	

	public boolean isNegative() {
		return negative;
	}

	public int getAmount() {
		return amount;
	}

	public int getMax() {
		return max;
	}

	@Override
	public Object[] getValues() {
		return new Object[]{amount, negative, max};
	}

}
