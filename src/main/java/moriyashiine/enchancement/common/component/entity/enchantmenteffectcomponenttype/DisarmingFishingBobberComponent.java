/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.DisarmingFishingBobberEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

import java.util.Collections;

public class DisarmingFishingBobberComponent implements CardinalComponent {
	private ItemStack stack = ItemStack.EMPTY;
	private boolean enabled = false, stealsFromPlayers = false;
	private int playerCooldown = 0, userCooldown = 0;

	@Override
	public void readData(ValueInput input) {
		stack = input.read("Stack", ItemStack.CODEC).orElse(ItemStack.EMPTY);
		enabled = input.getBooleanOr("Enabled", false);
		stealsFromPlayers = input.getBooleanOr("StealsFromPlayers", false);
		playerCooldown = input.getIntOr("PlayerCooldown", 0);
		userCooldown = input.getIntOr("UserCooldown", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		if (!stack.isEmpty()) {
			output.store("Stack", ItemStack.CODEC, stack);
		}
		output.putBoolean("Enabled", enabled);
		output.putBoolean("StealsFromPlayers", stealsFromPlayers);
		output.putInt("PlayerCooldown", playerCooldown);
		output.putInt("UserCooldown", userCooldown);
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

	public void disableStack(Player player, ItemStack stack, int duration) {
		DisarmedPlayerComponent disarmedPlayerComponent = ModEntityComponents.DISARMED_PLAYER.get(player);
		disarmedPlayerComponent.getDisarmedStacks().add(stack);
		disarmedPlayerComponent.sync();
		player.getCooldowns().addCooldown(stack, duration);
		player.releaseUsingItem();
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof FishingHook) {
			MutableBoolean enabled = new MutableBoolean(), stealsFromPlayers = new MutableBoolean();
			MutableFloat playerCooldown = new MutableFloat(), userCooldown = new MutableFloat();
			if (EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER)) {
				DisarmingFishingBobberEffect.setValues(user.getRandom(), enabled, stealsFromPlayers, playerCooldown, userCooldown, Collections.singleton(stack));
			} else if (!(user instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER)) {
				DisarmingFishingBobberEffect.setValues(user.getRandom(), enabled, stealsFromPlayers, playerCooldown, userCooldown, EnchancementUtil.getHeldItems(user));
			}
			if (enabled.booleanValue()) {
				DisarmingFishingBobberComponent disarmingFishingBobberComponent = ModEntityComponents.DISARMING_FISHING_BOBBER.get(entity);
				disarmingFishingBobberComponent.stack = stack;
				disarmingFishingBobberComponent.enabled = true;
				disarmingFishingBobberComponent.stealsFromPlayers = stealsFromPlayers.booleanValue();
				disarmingFishingBobberComponent.playerCooldown = Mth.floor(playerCooldown.floatValue() * 20);
				disarmingFishingBobberComponent.userCooldown = Mth.floor(userCooldown.floatValue() * 20);
			}
		}
	}
}
