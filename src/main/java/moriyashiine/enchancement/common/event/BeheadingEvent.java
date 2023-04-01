/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.BeheadingEntry;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;

public class BeheadingEvent implements ServerEntityCombatEvents.AfterKilledOtherEntity {
	@Override
	public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
		if (entity instanceof LivingEntity attacker && EnchancementUtil.hasEnchantment(ModEnchantments.BEHEADING, attacker)) {
			int looting = EnchantmentHelper.getLooting(attacker);
			for (EntityType<?> entityType : BeheadingEntry.DROP_MAP.keySet()) {
				if (killedEntity.getType() == entityType) {
					BeheadingEntry entry = BeheadingEntry.DROP_MAP.get(entityType);
					if (killedEntity.getRandom().nextFloat() < entry.chance() + (looting * 0.15F)) {
						ItemStack stack = new ItemStack(entry.drop());
						if (stack.getItem() == Items.PLAYER_HEAD && killedEntity.getType() == EntityType.PLAYER) {
							stack.getOrCreateNbt().putString("SkullOwner", killedEntity.getName().getString());
						}
						ItemScatterer.spawn(world, killedEntity.getX(), killedEntity.getY(), killedEntity.getZ(), stack);
					}
				}
			}
		}
	}
}
