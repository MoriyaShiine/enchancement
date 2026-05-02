/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.util;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public abstract class PushComponent implements AutoSyncedComponent, CommonTickingComponent {
	protected final LivingEntity obj;
	protected boolean shouldRefresh = false;
	protected int cooldown = 0, lastCooldown = 0;

	protected int entityCooldown = 0;

	private boolean resetNextTick = false;

	protected PushComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		shouldRefresh = input.getBooleanOr("ShouldRefresh", false);
		cooldown = input.getIntOr("Cooldown", 0);
		lastCooldown = input.getIntOr("LastCooldown", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("ShouldRefresh", shouldRefresh);
		output.putInt("Cooldown", cooldown);
		output.putInt("LastCooldown", lastCooldown);
	}

	@Override
	public void tick() {
		entityCooldown = getCooldownSupplier().getCooldown(obj);
		if (hasEffect()) {
			if (shouldRefresh) {
				if (cooldown > 0) {
					cooldown--;
				}
			} else if (updateRefresh()) {
				shouldRefresh = true;
			}
		} else {
			reset();
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (resetNextTick) {
			reset();
			sync();
			resetNextTick = false;
		}
	}

	public abstract void sync();

	public abstract DataComponentType<?> getEffectType();

	protected abstract CooldownSupplier getCooldownSupplier();

	public void reset() {
		setCooldown(entityCooldown);
		shouldRefresh = false;
	}

	public void resetNextTick() {
		resetNextTick = true;
	}

	public boolean hasEffect() {
		return entityCooldown > 0;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
		lastCooldown = cooldown;
	}

	public int getLastCooldown() {
		return lastCooldown;
	}

	protected LivingEntity getControllingObj() {
		return obj.getControllingPassenger() instanceof Player player ? player : obj;
	}

	protected boolean shouldApplyDeltaMovement() {
		if (obj.getControllingPassenger() instanceof Player) {
			return obj.level().isClientSide();
		}
		return true;
	}

	protected boolean updateRefresh() {
		return obj.onGround();
	}

	protected interface CooldownSupplier {
		int getCooldown(LivingEntity entity);
	}
}
