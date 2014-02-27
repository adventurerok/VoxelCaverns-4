package vc4.api.tool;

import vc4.api.item.ItemStack;

public class MiningData {

	private double fistDestroyTime = 0.5;
	private double maxDestroyTime = 0.35;
	private double minDestroyTime = 0.01;
	private double dropToolPower = 0;
	private double minToolPower = 5;
	private double maxToolPower = 100;
	private int correctDurabilityLoss = 1;
	private int incorrectDurabilityLoss = 2;
	private ToolType required;

	public MiningData setFistDestroyTime(double fistDestroyTime) {
		this.fistDestroyTime = fistDestroyTime;
		return this;
	}

	public MiningData setMaxDestroyTime(double maxDestroyTime) {
		this.maxDestroyTime = maxDestroyTime;
		return this;
	}

	public MiningData setMinDestroyTime(double minDestroyTime) {
		this.minDestroyTime = minDestroyTime;
		return this;
	}

	public MiningData setMinToolPower(double minToolPower) {
		this.minToolPower = minToolPower;
		return this;
	}

	public MiningData setMaxToolPower(double maxToolPower) {
		this.maxToolPower = maxToolPower;
		return this;
	}

	public double getDropToolPower() {
		return dropToolPower;
	}

	public MiningData setDropToolPower(double dropToolPower) {
		this.dropToolPower = dropToolPower;
		return this;
	}

	public MiningData setPowers(double drop, double min, double max) {
		dropToolPower = drop;
		minToolPower = min;
		maxToolPower = max;
		return this;
	}

	public MiningData setTimes(double fists, double min, double max) {
		fistDestroyTime = fists;
		minDestroyTime = min;
		maxDestroyTime = max;
		return this;
	}

	public MiningData setCorrectDurabilityLoss(int correctDurabilityLoss) {
		this.correctDurabilityLoss = correctDurabilityLoss;
		return this;
	}

	public MiningData setIncorrectDurabilityLoss(int incorrectDurabilityLoss) {
		this.incorrectDurabilityLoss = incorrectDurabilityLoss;
		return this;
	}

	public MiningData setRequired(ToolType required) {
		this.required = required;
		return this;
	}

	public double getMaxDestroyTime() {
		return maxDestroyTime;
	}

	public double getMinDestroyTime() {
		return minDestroyTime;
	}

	public ToolType getRequired() {
		return required;
	}

	public double getMinToolPower() {
		return minToolPower;
	}

	public double getMaxToolPower() {
		return maxToolPower;
	}

	public double getFistDestroyTime() {
		return fistDestroyTime;
	}

	public int getCorrectDurabilityLoss() {
		return correctDurabilityLoss;
	}

	public int getIncorrectDurabilityLoss() {
		return incorrectDurabilityLoss;
	}

	public MiningData setDurabilityLoss(int correct, int incorrect) {
		incorrectDurabilityLoss = incorrect;
		correctDurabilityLoss = correct;
		return this;
	}

	public double getTimeToMine(Tool tool) {
		if (required == null || minToolPower < 0.1 || tool == null || tool.getPower() < minToolPower || tool.getPower() < 0.1 || !tool.getType().equals(required)) return fistDestroyTime;
		double diff = (tool.getPower() - minToolPower) / (maxToolPower - minToolPower);
		if (diff > 1) diff = 1;
		return minDestroyTime + (1 - diff) * (maxDestroyTime - minDestroyTime);
	}

	public double getMineSpeed(Tool tool) {
		double time = getTimeToMine(tool);
		if (time < 0.05) return 1;
		return 0.05 / time * 1;

	}

	public double getMiningDone(Tool tool, double deltaMs) {
		double delta = deltaMs / 1000;
		double time = getTimeToMine(tool);
		if (time < delta) return 1;
		return delta / time * 1;
	}

	public boolean canDrop(Tool tool) {
		if (required == null) return true;
		if (dropToolPower < 0.1) return true;
		if (tool == null) return false;
		else if (!required.equals(tool.getType())) return false;
		return tool.getPower() >= dropToolPower;
	}

	public boolean onMine(ItemStack item) {
		Tool tool = item != null ? item.getTool() : null;
		boolean drp;
		boolean crt;
		if (required == null) {
			drp = true;
			crt = false;
		} else if (dropToolPower < 0.1) {
			drp = true;
			crt = false;
		} else if (tool == null) drp = crt = false;
		else if (!required.equals(tool.getType())) drp = crt = false;
		else drp = crt = tool.getPower() >= dropToolPower;
		if (crt) item.damage(correctDurabilityLoss);
		else if (item != null) item.damage(incorrectDurabilityLoss);
		return drp;
	}

}
