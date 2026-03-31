/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.enchantment.HeadDrop;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.commons.lang3.mutable.MutableFloat;

public class HeadDropsEvent implements ServerEntityCombatEvents.AfterKilledOtherEntity {
	@Override
	public void afterKilledOtherEntity(ServerLevel level, Entity entity, LivingEntity killedEntity, DamageSource damageSource) {
		if (entity instanceof LivingEntity attacker) {
			float dropModifier = getDropChance(level, attacker, new DamageSource(attacker.damageSources().generic().typeHolder(), attacker, attacker));
			if (dropModifier > 0) {
				for (EntityType<?> entityType : HeadDrop.DROP_MAP.keySet()) {
					if (killedEntity.getType() == entityType) {
						HeadDrop entry = HeadDrop.DROP_MAP.get(entityType);
						if (killedEntity.getRandom().nextFloat() * dropModifier < entry.chance()) {
							ItemStack stack = new ItemStack(entry.drop());
							if (stack.getItem() == Items.PLAYER_HEAD && killedEntity instanceof Player player) {
								stack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(player.getGameProfile()));
							}
							Containers.dropItemStack(level, killedEntity.getX(), killedEntity.getY(), killedEntity.getZ(), stack);
						}
					}
				}
			}
		}
	}

	private static float getDropChance(ServerLevel level, LivingEntity attacker, DamageSource damageSource) {
		MutableFloat mutableFloat = new MutableFloat(0);
		RandomSource random = attacker.getRandom();
		EnchantmentHelper.runIterationOnEquipment(attacker, (enchantment, enchantmentLevel, _) -> {
			LootContext lootContext = Enchantment.damageContext(level, enchantmentLevel, attacker, damageSource);
			enchantment.value().getEffects(ModEnchantmentEffectComponentTypes.HEAD_DROPS).forEach(effect -> {
				if (effect.enchanted() == EnchantmentTarget.VICTIM && effect.affected() == EnchantmentTarget.VICTIM && effect.matches(lootContext)) {
					mutableFloat.setValue(effect.effect().process(enchantmentLevel, random, mutableFloat.floatValue()));
				}
			});
		});
		if (damageSource.getEntity() instanceof LivingEntity living) {
			EnchantmentHelper.runIterationOnEquipment(living, (enchantment, enchantmentLevel, _) -> {
				LootContext lootContext = Enchantment.damageContext(level, enchantmentLevel, attacker, damageSource);
				enchantment.value().getEffects(ModEnchantmentEffectComponentTypes.HEAD_DROPS).forEach(effect -> {
					if (effect.enchanted() == EnchantmentTarget.ATTACKER && effect.affected() == EnchantmentTarget.VICTIM && effect.matches(lootContext)) {
						mutableFloat.setValue(effect.effect().process(enchantmentLevel, random, mutableFloat.floatValue()));
					}
				});
			});
		}
		return mutableFloat.floatValue();
	}
}
