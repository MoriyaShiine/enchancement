/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.enchantment.effect.MaceEffect;
import moriyashiine.strawberrylib.api.event.TickEntityEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class RebalanceEquipmentEvent {
	public static class AllowComponent implements DefaultItemComponentEvents.ModifyCallback {
		@Override
		public void modify(DefaultItemComponentEvents.ModifyContext context) {
			if (ModConfig.rebalanceEquipment) {
				context.modify(item -> EnchancementUtil.isGroundAnimalArmor(item.getDefaultInstance()), (builder, _) -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(1)));
			}
		}
	}

	public static class AllowEnchanting implements EnchantmentEvents.AllowEnchanting {
		@Override
		public TriState allowEnchanting(Holder<Enchantment> enchantment, ItemStack target, EnchantingContext enchantingContext) {
			if (ModConfig.rebalanceEquipment && EnchancementUtil.isGroundAnimalArmor(target) && enchantment.is(ModEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}

	public static class Interrupt implements ServerLivingEntityEvents.AfterDamage {
		@Override
		public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
			if (ModConfig.rebalanceEquipment && source.getEntity() != null && !source.is(ModDamageTypeTags.DOES_NOT_INTERRUPT) && entity instanceof Player player && isValid(player)) {
				player.getCooldowns().addCooldown(entity.getUseItem(), 20);
				entity.releaseUsingItem();
			}
		}
	}

	public static class Tick implements TickEntityEvent {
		@Override
		public void tick(Level level, Entity entity) {
			if (ModConfig.rebalanceEquipment && entity instanceof Player player && player.getTicksUsingItem() == EnchancementUtil.getTridentChargeTime(player.getUseItem()) && isValid(player)) {
				SLibUtils.playSound(entity, ModSoundEvents.GENERIC_PING);
			}
		}
	}

	private static boolean isValid(Player player) {
		return player.getUseItem().getItem() instanceof TridentItem || MaceEffect.EFFECTS.stream().anyMatch(effect -> effect.isUsing(player));
	}
}
