/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.world.item.effects.ChargeJumpEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ChargeJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Player obj;
	public int strength = 0;

	private float chargeStrength = 0;
	private int chargeTime = 0;
	private boolean hasChargeJump = false;

	public ChargeJumpComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		strength = input.getIntOr("Strength", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("Strength", strength);
	}

	@Override
	public void tick() {
		chargeTime = ChargeJumpEffect.getChargeTime(obj);
		chargeStrength = ChargeJumpEffect.getStrength(obj, 0);
		hasChargeJump = chargeStrength > 0;
		if (hasChargeJump) {
			if (obj.onGround() && obj.isShiftKeyDown()) {
				if (strength < chargeTime) {
					strength++;
				}
			} else {
				strength = 0;
			}
		} else {
			strength = 0;
		}
	}

	public float getChargeProgress() {
		return Mth.lerp((strength - 2) / (Math.max(1, chargeTime - 2F)), 0, 1);
	}

	public float getBoost() {
		return getChargeProgress() * chargeStrength;
	}

	public boolean hasChargeJump() {
		return hasChargeJump;
	}
}
