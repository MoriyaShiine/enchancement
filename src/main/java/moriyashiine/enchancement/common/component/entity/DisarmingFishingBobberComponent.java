/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.DisarmingFishingBobberEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.Collections;

public class DisarmingFishingBobberComponent implements Component {
	private boolean enabled = false, stealsFromPlayers = false;
	private int playerCooldown = 0;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		enabled = tag.getBoolean("Enabled");
		stealsFromPlayers = tag.getBoolean("StealsFromPlayers");
		playerCooldown = tag.getInt("PlayerCooldown");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("Enabled", enabled);
		tag.putBoolean("StealsFromPlayers", stealsFromPlayers);
		tag.putInt("PlayerCooldown", playerCooldown);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean stealsFromPlayers() {
		return stealsFromPlayers;
	}

	public int getPlayerCooldown() {
		return playerCooldown;
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof FishingBobberEntity) {
			MutableBoolean enabled = new MutableBoolean(), stealsFromPlayers = new MutableBoolean();
			MutableFloat playerCooldown = new MutableFloat();
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER)) {
				DisarmingFishingBobberEffect.setValues(user.getRandom(), enabled, stealsFromPlayers, playerCooldown, Collections.singleton(stack));
			} else if (!(user instanceof PlayerEntity) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER)) {
				DisarmingFishingBobberEffect.setValues(user.getRandom(), enabled, stealsFromPlayers, playerCooldown, user.getEquippedItems());
			}
			if (enabled.booleanValue()) {
				DisarmingFishingBobberComponent disarmingFishingBobberComponent = ModEntityComponents.DISARMING_FISHING_BOBBER.get(entity);
				disarmingFishingBobberComponent.enabled = true;
				disarmingFishingBobberComponent.stealsFromPlayers = stealsFromPlayers.booleanValue();
				disarmingFishingBobberComponent.playerCooldown = MathHelper.floor(playerCooldown.floatValue() * 20);
			}
		}
	}
}
