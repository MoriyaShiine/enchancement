/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.entity.WindBurstHolder;
import net.minecraft.entity.projectile.WindChargeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WindChargeEntity.class)
public class WindBurstEntityMixin implements WindBurstHolder {
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
