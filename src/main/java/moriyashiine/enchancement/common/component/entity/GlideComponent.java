/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.GlideEffect;
import moriyashiine.enchancement.common.payload.GlideC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class GlideComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean gliding = false;
	private int airTicks = 0;

	private int minDuration = 0;

	public GlideComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		gliding = readView.getBoolean("Gliding", false);
		airTicks = readView.getInt("AirTicks", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("Gliding", gliding);
		writeView.putInt("AirTicks", airTicks);
	}

	@Override
	public void tick() {
		minDuration = GlideEffect.getMinDuration(obj);
		if (canGlide()) {
			if (gliding) {
				EnchancementUtil.resetFallDistance(obj);
			}
			if (obj.isOnGround()) {
				airTicks = 0;
			} else if (airTicks < minDuration || obj.jumping) {
				airTicks++;
			}
		} else {
			gliding = false;
			airTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		boolean shouldBeGliding = obj.jumping && airTicks >= minDuration && airTicks <= GlideEffect.getMaxDuration(obj) && canGlide();
		if (gliding != shouldBeGliding && SLibClientUtils.isHost(obj)) {
			gliding = shouldBeGliding;
			GlideC2SPayload.send(gliding);
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
		return minDuration > 0 && SLibUtils.isGroundedOrAirborne(obj);
	}
}
