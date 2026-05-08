/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.PhaseEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Collections;

public class PhaseComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final AbstractArrow obj;
	private int maxPhaseBlocks = 0;
	private boolean bypassShields = false;

	private int ticksInAir = 0;

	public PhaseComponent(AbstractArrow obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		maxPhaseBlocks = input.getIntOr("MaxPhaseBlocks", 0);
		bypassShields = input.getBooleanOr("BypassShields", false);
		ticksInAir = input.getIntOr("TicksInAir", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putInt("MaxPhaseBlocks", maxPhaseBlocks);
		output.putBoolean("BypassShields", bypassShields);
		output.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		if (shouldPhase()) {
			if (++ticksInAir >= 200 || obj.isInGround()) {
				disable();
			}
		}
	}

	public void sync() {
		ModEntityComponents.PHASE.sync(obj);
	}

	public int getMaxPhaseBlocks() {
		return maxPhaseBlocks;
	}

	public void setMaxPhaseBlocks(int maxPhaseBlocks) {
		this.maxPhaseBlocks = maxPhaseBlocks;
	}

	public boolean bypassShields() {
		return bypassShields;
	}

	public void setBypassShields(boolean bypassShields) {
		this.bypassShields = bypassShields;
	}

	public boolean shouldPhase() {
		return maxPhaseBlocks > 0;
	}

	public void disable() {
		setMaxPhaseBlocks(0);
		setBypassShields(false);
		obj.setNoGravity(false);
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof AbstractArrow) {
			MutableFloat maxPhaseBlocks = new MutableFloat();
			MutableBoolean bypassShields = new MutableBoolean();
			if (EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.PHASE)) {
				PhaseEffect.setValues(user.getRandom(), maxPhaseBlocks, bypassShields, Collections.singleton(stack));
			} else if (!(user instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.PHASE)) {
				PhaseEffect.setValues(user.getRandom(), maxPhaseBlocks, bypassShields, EnchancementUtil.getHeldItems(user));
			}
			if (maxPhaseBlocks.floatValue() != 0) {
				PhaseComponent phaseComponent = ModEntityComponents.PHASE.get(entity);
				phaseComponent.setMaxPhaseBlocks(maxPhaseBlocks.intValue());
				phaseComponent.setBypassShields(bypassShields.booleanValue());
				phaseComponent.sync();
				entity.setNoGravity(true);
			}
		}
	}
}
