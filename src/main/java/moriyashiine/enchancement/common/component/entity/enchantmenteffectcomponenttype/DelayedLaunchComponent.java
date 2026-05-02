/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.DelayedLaunchEffect;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Collections;

public class DelayedLaunchComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final AbstractArrow obj;
	private ItemStack weapon = null;
	private int maxDuration = 0, peakDuration = 0;
	private float maxMultiplier = 0;
	private boolean allowRedirect = false;

	private Vec3 storedDeltaMovement = null;
	private float forcedXRot = 0, forcedYRot = 0;
	private float cachedPow = 0, cachedUncertainty = 0;
	private int ticksFloating = 0;

	public DelayedLaunchComponent(AbstractArrow obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		weapon = input.read("Weapon", ItemStack.CODEC).orElse(null);
		maxDuration = input.getIntOr("MaxDuration", 0);
		peakDuration = input.getIntOr("PeakDuration", 0);
		maxMultiplier = input.getFloatOr("MaxMultiplier", 0);
		allowRedirect = input.getBooleanOr("AllowRedirect", false);

		storedDeltaMovement = input.read("StoredDeltaMovement", Vec3.CODEC).orElse(null);
		forcedXRot = input.getFloatOr("ForcedXRot", 0);
		forcedYRot = input.getFloatOr("ForcedYRot", 0);
		cachedPow = input.getFloatOr("CachedPow", 0);
		cachedUncertainty = input.getFloatOr("CachedUncertainty", 0);
		ticksFloating = input.getIntOr("TicksFloating", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.storeNullable("Weapon", ItemStack.CODEC, weapon);
		output.putInt("MaxDuration", maxDuration);
		output.putInt("PeakDuration", peakDuration);
		output.putFloat("MaxMultiplier", maxMultiplier);
		output.putBoolean("AllowRedirect", allowRedirect);

		output.storeNullable("StoredDeltaMovement", Vec3.CODEC, storedDeltaMovement);
		output.putFloat("ForcedXRot", forcedXRot);
		output.putFloat("ForcedYRot", forcedYRot);
		output.putFloat("CachedPow", cachedPow);
		output.putFloat("CachedUncertainty", cachedUncertainty);
		output.putInt("TicksFloating", ticksFloating);
	}

	@Override
	public void tick() {
		if (isEnabled()) {
			obj.setDeltaMovement(Vec3.ZERO);
			obj.setXRot(forcedXRot);
			obj.setYRot(forcedYRot);
			ticksFloating++;
		}
	}

	@Override
	public void serverTick() {
		if (isEnabled()) {
			if (storedDeltaMovement == null) {
				storedDeltaMovement = obj.getDeltaMovement();
				forcedXRot = obj.getXRot();
				forcedYRot = obj.getYRot();
				sync();
				obj.needsSync = true;
			}
			LivingEntity mobTarget = null;
			if (ModConfig.enhanceMobs && obj.getOwner() instanceof Mob mob && mob.getTarget() instanceof LivingEntity target) {
				mobTarget = target;
				if (!mob.swinging && target.distanceTo(mob) < 6) {
					mob.swing(InteractionHand.MAIN_HAND);
				}
			}
			boolean punching = obj.getOwner() instanceof LivingEntity living && living.swinging && (living.isHolding(stack -> ItemStack.matchesIgnoringComponents(stack, weapon, DataComponentType::ignoreSwapAnimation)));
			if (ticksFloating > maxDuration || punching) {
				if (allowRedirect && obj.getOwner() instanceof LivingEntity living) {
					if ((punching && living.isShiftKeyDown()) || mobTarget != null) {
						Vec3 pos;
						if (mobTarget != null) {
							pos = mobTarget.getEyePosition();
						} else {
							HitResult result = ProjectileUtil.getHitResultOnViewVector(living, entity -> !entity.isSpectator() && entity.isPickable(), 64);
							if (result instanceof EntityHitResult entityHitResult) {
								pos = entityHitResult.getEntity().getEyePosition();
							} else {
								pos = result.getLocation();
							}
						}
						obj.shoot(pos.x() - obj.getX(), pos.y() - obj.getY(), pos.z() - obj.getZ(), cachedPow, cachedUncertainty);
						storedDeltaMovement = obj.getDeltaMovement();
					}
				}
				obj.setBaseDamage(obj.baseDamage * Mth.lerp(Math.min(1, (float) ticksFloating / peakDuration), 1, 1 + maxMultiplier));
				obj.setDeltaMovement(storedDeltaMovement);
				obj.needsSync = true;
				maxDuration = peakDuration = 0;
				maxMultiplier = 0;
				allowRedirect = false;
				sync();
			}
		}
		tick();
	}

	public void sync() {
		ModEntityComponents.DELAYED_LAUNCH.sync(obj);
	}

	public boolean isEnabled() {
		return maxDuration > 0;
	}

	public boolean wasEverEnabled() {
		return storedDeltaMovement != null;
	}

	public boolean shouldChangeParticles() {
		return peakDuration > 0 && ticksFloating >= peakDuration;
	}

	public static void maybeSet(LivingEntity shooter, ItemStack weapon, Entity projectile, float pow, float uncertainty) {
		if (projectile instanceof AbstractArrow arrow) {
			MutableFloat maxDuration = new MutableFloat(), peakDuration = new MutableFloat(), maxMultiplier = new MutableFloat();
			MutableBoolean allowRedirect = new MutableBoolean();
			if (EnchantmentHelper.has(weapon, ModEnchantmentEffectComponentTypes.DELAYED_LAUNCH)) {
				DelayedLaunchEffect.setValues(shooter.getRandom(), maxDuration, peakDuration, maxMultiplier, allowRedirect, Collections.singleton(weapon));
			} else if (!(shooter instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(shooter, ModEnchantmentEffectComponentTypes.DELAYED_LAUNCH)) {
				DelayedLaunchEffect.setValues(shooter.getRandom(), maxDuration, peakDuration, maxMultiplier, allowRedirect, EnchancementUtil.getHeldItems(shooter));
			}
			if (maxDuration.floatValue() != 0) {
				DelayedLaunchComponent delayedLaunchComponent = ModEntityComponents.DELAYED_LAUNCH.get(projectile);
				delayedLaunchComponent.weapon = weapon;
				delayedLaunchComponent.maxDuration = Mth.floor(maxDuration.floatValue() * 20);
				delayedLaunchComponent.peakDuration = Mth.floor(peakDuration.floatValue() * 20);
				delayedLaunchComponent.maxMultiplier = maxMultiplier.floatValue();
				delayedLaunchComponent.allowRedirect = allowRedirect.booleanValue();

				delayedLaunchComponent.cachedPow = pow;
				delayedLaunchComponent.cachedUncertainty = uncertainty;

				delayedLaunchComponent.sync();
				arrow.setCritArrow(true);
			}
		}
	}
}
