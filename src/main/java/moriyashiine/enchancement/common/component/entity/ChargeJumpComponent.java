/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.ChargeJumpEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ChargeJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	public int strength = 0;

	private float chargeStrength = 0;
	private int chargeTime = 0;
	private boolean hasChargeJump = false;

	public ChargeJumpComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		strength = tag.getInt("Strength", 0);
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("Strength", strength);
	}

	@Override
	public void tick() {
		chargeTime = ChargeJumpEffect.getChargeTime(obj);
		chargeStrength = ChargeJumpEffect.getStrength(obj, 0);
		hasChargeJump = chargeStrength > 0;
		if (hasChargeJump) {
			if (obj.isOnGround() && obj.isSneaking()) {
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
		return MathHelper.lerp((strength - 2) / (Math.max(1, chargeTime - 2F)), 0F, 1F);
	}

	public float getBoost() {
		return getChargeProgress() * chargeStrength;
	}

	public boolean hasChargeJump() {
		return hasChargeJump;
	}
}
