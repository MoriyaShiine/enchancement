/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffecttype;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FrozenGuardianComponent implements AutoSyncedComponent {
	private final Guardian obj;
	private float forcedTailAnimation = 0, forcedSpikesAnimation = 0;

	public FrozenGuardianComponent(Guardian obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		forcedTailAnimation = input.getFloatOr("ForcedTailAnimation", 0);
		forcedSpikesAnimation = input.getFloatOr("ForcedSpikesAnimation", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putFloat("ForcedTailAnimation", forcedTailAnimation);
		output.putFloat("ForcedSpikesAnimation", forcedSpikesAnimation);
	}

	public void sync() {
		ModEntityComponents.FROZEN_GUARDIAN.sync(obj);
	}

	public float getForcedTailAnimation() {
		return forcedTailAnimation;
	}

	public void setForcedTailAnimation(float forcedTailAnimation) {
		this.forcedTailAnimation = forcedTailAnimation;
	}

	public float getForcedSpikesAnimation() {
		return forcedSpikesAnimation;
	}

	public void setForcedSpikesAnimation(float forcedSpikesAnimation) {
		this.forcedSpikesAnimation = forcedSpikesAnimation;
	}
}
