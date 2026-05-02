/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.GlideC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.GlideEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class GlideComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean gliding = false;
	private int airTicks = -1;

	private int minDuration = 0;

	public GlideComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		gliding = input.getBooleanOr("Gliding", false);
		airTicks = input.getIntOr("AirTicks", -1);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("Gliding", gliding);
		output.putInt("AirTicks", airTicks);
	}

	@Override
	public void tick() {
		minDuration = GlideEffect.getMinDuration(obj);
		if (canGlide()) {
			if (gliding) {
				EnchancementUtil.resetFallDistance(obj);
			}
			if (obj.onGround()) {
				airTicks = 0;
			} else if (airTicks >= 0 && (airTicks < minDuration || obj.jumping)) {
				airTicks++;
			}
		} else {
			gliding = false;
			airTicks = -1;
		}
	}

	@Override
	public void clientTick() {
		tick();
		LivingEntity controllingObj = obj.getControllingPassenger() instanceof Player player ? player : obj;
		if (!controllingObj.isSpectator() && SLibClientUtils.isHost(controllingObj)) {
			boolean shouldBeGliding = controllingObj.jumping && airTicks >= minDuration && airTicks <= GlideEffect.getMaxDuration(obj) && canGlide();
			if (gliding != shouldBeGliding) {
				gliding = shouldBeGliding;
				GlideC2SPayload.send(obj, gliding);
			}
		}
		if (isGliding()) {
			SLibClientUtils.addParticles(obj, ParticleTypes.SMALL_GUST, 4, ParticleAnchor.BASE);
		}
	}

	public boolean isGliding() {
		return gliding;
	}

	public void setGliding(boolean gliding) {
		this.gliding = gliding;
	}

	public boolean canGlide() {
		return minDuration > 0 && SLibUtils.isGroundedOrAirborne(obj) && !ModEntityComponents.BOOST_IN_FLUID.get(obj).blocksAirEffects();
	}
}
