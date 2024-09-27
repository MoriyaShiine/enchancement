/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.BeheadingEntry;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

public class HeadDropsEvent implements ServerEntityCombatEvents.AfterKilledOtherEntity {
	@Override
	public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
		if (entity instanceof LivingEntity attacker) {
			float dropModifier = getDropChance(world, attacker, new DamageSource(attacker.getDamageSources().generic().getTypeRegistryEntry(), attacker, attacker));
			if (dropModifier > 0) {
				for (EntityType<?> entityType : BeheadingEntry.DROP_MAP.keySet()) {
					if (killedEntity.getType() == entityType) {
						BeheadingEntry entry = BeheadingEntry.DROP_MAP.get(entityType);
						if (killedEntity.getRandom().nextFloat() * dropModifier < entry.chance()) {
							ItemStack stack = new ItemStack(entry.drop());
							if (stack.getItem() == Items.PLAYER_HEAD && killedEntity instanceof PlayerEntity player) {
								stack.set(DataComponentTypes.PROFILE, new ProfileComponent(player.getGameProfile()));
							}
							ItemScatterer.spawn(world, killedEntity.getX(), killedEntity.getY(), killedEntity.getZ(), stack);
						}
					}
				}
			}
		}
	}

	private static float getDropChance(ServerWorld world, LivingEntity attacker, DamageSource damageSource) {
		MutableFloat mutableFloat = new MutableFloat(0);
		Random random = attacker.getRandom();
		EnchantmentHelper.forEachEnchantment(attacker, (enchantment, level, context) -> {
			LootContext lootContext = Enchantment.createEnchantedDamageLootContext(world, level, attacker, damageSource);
			enchantment.value().getEffect(ModEnchantmentEffectComponentTypes.HEAD_DROPS).forEach(effect -> {
				if (effect.enchanted() == EnchantmentEffectTarget.VICTIM && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lootContext)) {
					mutableFloat.setValue(effect.effect().apply(level, random, mutableFloat.floatValue()));
				}
			});
		});
		if (damageSource.getAttacker() instanceof LivingEntity livingEntity) {
			EnchantmentHelper.forEachEnchantment(livingEntity, (enchantment, level, context) -> {
				LootContext lootContext = Enchantment.createEnchantedDamageLootContext(world, level, attacker, damageSource);
				enchantment.value().getEffect(ModEnchantmentEffectComponentTypes.HEAD_DROPS).forEach(effect -> {
					if (effect.enchanted() == EnchantmentEffectTarget.ATTACKER && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lootContext)) {
						mutableFloat.setValue(effect.effect().apply(level, random, mutableFloat.floatValue()));
					}
				});
			});
		}
		return mutableFloat.floatValue();
	}
}
