/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class BouncyComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	public int bounceStrength = 0, grappleTimer = 0;

	private int bouncyLevel = 0;
	private boolean hasBouncy = false;

	public BouncyComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		bounceStrength = tag.getInt("BounceStrength");
		grappleTimer = tag.getInt("GrappleTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("BounceStrength", bounceStrength);
		tag.putInt("GrappleTimer", grappleTimer);
	}

	@Override
	public void tick() {
		bouncyLevel = EnchantmentHelper.getEquipmentLevel(ModEnchantments.BOUNCY, obj);
		hasBouncy = bouncyLevel > 0;
		if (hasBouncy) {
			if (obj.isOnGround() && obj.isSneaking()) {
				if (bounceStrength < 30) {
					bounceStrength++;
				}
			} else {
				bounceStrength = 0;
			}
		} else {
			bounceStrength = 0;
		}
		if (obj.isOnGround()) {
			grappleTimer = 0;
		} else if (grappleTimer > 0) {
			grappleTimer--;
		}
	}

	public float getBoostProgress() {
		return MathHelper.lerp((bounceStrength - 2) / 28F, 0F, 1F);
	}

	public int getBouncyLevel() {
		return bouncyLevel;
	}

	public boolean hasBouncy() {
		return hasBouncy;
	}
}
