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
	private ItemStack stack = ItemStack.EMPTY;
	private boolean enabled = false, stealsFromPlayers = false;
	private int playerCooldown = 0, userCooldown = 0;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		stack = ItemStack.fromNbtOrEmpty(registryLookup, tag.getCompound("Stack"));
		enabled = tag.getBoolean("Enabled");
		stealsFromPlayers = tag.getBoolean("StealsFromPlayers");
		playerCooldown = tag.getInt("PlayerCooldown");
		userCooldown = tag.getInt("UserCooldown");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (!stack.isEmpty()) {
			tag.put("Stack", stack.toNbt(registryLookup));
		}
		tag.putBoolean("Enabled", enabled);
		tag.putBoolean("StealsFromPlayers", stealsFromPlayers);
		tag.putInt("PlayerCooldown", playerCooldown);
		tag.putInt("UserCooldown", userCooldown);
	}

	public ItemStack getStack() {
		return stack;
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

	public int getUserCooldown() {
		return userCooldown;
	}

	public void disableStack(PlayerEntity player, ItemStack stack, int duration) {
		DisarmedPlayerComponent disarmedPlayerComponent = ModEntityComponents.DISARMED_PLAYER.get(player);
		disarmedPlayerComponent.getDisarmedStacks().add(stack);
		disarmedPlayerComponent.sync();
		player.getItemCooldownManager().set(stack, duration);
		player.stopUsingItem();
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof FishingBobberEntity) {
			MutableBoolean enabled = new MutableBoolean(), stealsFromPlayers = new MutableBoolean();
			MutableFloat playerCooldown = new MutableFloat(), userCooldown = new MutableFloat();
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER)) {
				DisarmingFishingBobberEffect.setValues(user.getRandom(), enabled, stealsFromPlayers, playerCooldown, userCooldown, Collections.singleton(stack));
			} else if (!(user instanceof PlayerEntity) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER)) {
				DisarmingFishingBobberEffect.setValues(user.getRandom(), enabled, stealsFromPlayers, playerCooldown, userCooldown, user.getEquippedItems());
			}
			if (enabled.booleanValue()) {
				DisarmingFishingBobberComponent disarmingFishingBobberComponent = ModEntityComponents.DISARMING_FISHING_BOBBER.get(entity);
				disarmingFishingBobberComponent.stack = stack;
				disarmingFishingBobberComponent.enabled = true;
				disarmingFishingBobberComponent.stealsFromPlayers = stealsFromPlayers.booleanValue();
				disarmingFishingBobberComponent.playerCooldown = MathHelper.floor(playerCooldown.floatValue() * 20);
				disarmingFishingBobberComponent.userCooldown = MathHelper.floor(userCooldown.floatValue() * 20);
			}
		}
	}
}
