package moriyashiine.enchancement.common.util;

public enum SubmersionGate {
	ALL(true, true, true),
	WATER_ONLY(true, false, false),
	LAVA_ONLY(false, true, false),
	POWDER_SNOW_ONLY(false, false, true);

	private final boolean allowsWater, allowsLava, allowsPowderSnow;

	SubmersionGate(boolean allowsWater, boolean allowsLava, boolean allowsPowderSnow) {
		this.allowsWater = allowsWater;
		this.allowsLava = allowsLava;
		this.allowsPowderSnow = allowsPowderSnow;
	}

	public boolean allowsWater() {
		return allowsWater;
	}

	public boolean allowsLava() {
		return allowsLava;
	}

	public boolean allowsPowderSnow() {
		return allowsPowderSnow;
	}
}
