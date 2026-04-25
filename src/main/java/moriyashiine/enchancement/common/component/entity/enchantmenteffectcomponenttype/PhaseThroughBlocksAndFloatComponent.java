/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class PhaseThroughBlocksAndFloatComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Projectile obj;
	private int maxPhaseBlocks = 0;
	private int ticksInAir = 0;

	public PhaseThroughBlocksAndFloatComponent(Projectile obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		maxPhaseBlocks = input.getIntOr("MaxPhaseBlocks", 0);
		ticksInAir = input.getIntOr("TicksInAir", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("MaxPhaseBlocks", maxPhaseBlocks);
		output.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		if (shouldPhase()) {
			if (++ticksInAir >= 200 || (obj instanceof AbstractArrow arrow && arrow.isInGround())) {
				disable();
			}
		}
	}

	public void sync() {
		ModEntityComponents.PHASE_THROUGH_BLOCKS_AND_FLOAT.sync(obj);
	}

	public int getMaxPhaseBlocks() {
		return maxPhaseBlocks;
	}

	public void setMaxPhaseBlocks(int maxPhaseBlocks) {
		this.maxPhaseBlocks = maxPhaseBlocks;
	}

	public boolean shouldPhase() {
		return maxPhaseBlocks > 0;
	}

	public void disable() {
		setMaxPhaseBlocks(0);
		obj.setNoGravity(false);
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof AbstractArrow) {
			float maxPhaseBlocks = 0;
			if (EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT)) {
				maxPhaseBlocks = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT, (ServerLevel) user.level(), stack, 0);
			} else if (!(user instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT)) {
				for (ItemStack equippedStack : EnchancementUtil.getHeldItems(user)) {
					maxPhaseBlocks = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT, (ServerLevel) user.level(), equippedStack, 0);
				}
			}
			if (maxPhaseBlocks != 0) {
				PhaseThroughBlocksAndFloatComponent phaseThroughBlocksAndFloatComponent = ModEntityComponents.PHASE_THROUGH_BLOCKS_AND_FLOAT.get(entity);
				phaseThroughBlocksAndFloatComponent.setMaxPhaseBlocks(Mth.floor(maxPhaseBlocks));
				phaseThroughBlocksAndFloatComponent.sync();
				entity.setNoGravity(true);
			}
		}
	}
}
