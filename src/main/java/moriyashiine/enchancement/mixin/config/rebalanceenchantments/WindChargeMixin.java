/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.world.entity.WindBurstHolder;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WindCharge.class)
public class WindChargeMixin implements WindBurstHolder {
	@Unique
	private boolean fromWindBurst = false;

	@Override
	public boolean enchancement$fromWindBurst() {
		return fromWindBurst;
	}

	@Override
	public void enchancement$setFromWindBurst(boolean fromWindBurst) {
		this.fromWindBurst = fromWindBurst;
	}
}
