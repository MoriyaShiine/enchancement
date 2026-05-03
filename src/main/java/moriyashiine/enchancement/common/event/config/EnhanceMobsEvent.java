/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.DirectionBurstPayload;
import moriyashiine.enchancement.common.world.entity.TridentSpinAttackUser;
import moriyashiine.enchancement.common.world.item.effects.DirectionBurstEffect;
import moriyashiine.strawberrylib.api.event.TickEntityEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EnhanceMobsEvent implements TickEntityEvent {
	@Override
	public void tick(Level level, Entity entity) {
		if (ModConfig.enhanceMobs && !level.isClientSide() && entity instanceof Mob mob && !mob.isPassenger()) {
			if (!mob.onGround()) {
				DirectionBurstComponent directionBurstComponent = ModEntityComponents.DIRECTION_BURST.get(mob);
				if (directionBurstComponent.hasEffect() && directionBurstComponent.canUse() && SLibUtils.isSufficientlyHigh(mob, 2.5)) {
					float strength = mob.onGround() ? DirectionBurstEffect.getGroundStrength(mob) : DirectionBurstEffect.getAirStrength(mob);
					Vec3 inputDelta = switch (mob.getRandom().nextInt(4)) {
						case 3 -> new Vec3(strength, 0, 0);
						case 2 -> new Vec3(-strength, 0, 0);
						case 1 -> new Vec3(0, 0, strength);
						default -> new Vec3(0, 0, -strength);
					};
					Vec3 delta = directionBurstComponent.createDelta(inputDelta);
					DirectionBurstPayload.use(mob, delta, directionBurstComponent);
				}
			}
			if ((mob.getId() + mob.tickCount) % 40 == 0 && mob.isUsingItem()) {
				LivingEntity target = mob.getTarget();
				if (target != null && target.slib$exists()) {
					if (target.isLookingAtMe(mob, 0.1, true, false, mob.getEyeY())) {
						mob.postPiercingAttack();
					}
					if (mob.isInWaterOrRain()) {
						ItemStack weapon = mob.getWeaponItem();
						float riptideStrength = EnchantmentHelper.getTridentSpinAttackStrength(weapon, mob);
						if (riptideStrength > 0) {
							SLibUtils.playSound(mob, EnchantmentHelper.pickHighestLevel(weapon, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW).value());
							Vec3 push = new Vec3(target.getX() - mob.getX(), target.getY() - mob.getY(), target.getZ() - mob.getZ()).normalize().scale(riptideStrength);
							((TridentSpinAttackUser) mob).enchancement$startAutoSpinAttack(20, 8, weapon);
							mob.push(push.x(), push.y(), push.z());
							mob.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
							mob.getNavigation().stop();
							if (mob.onGround()) {
								mob.move(MoverType.SELF, new Vec3(0, 1.2, 0));
							}
						}
					}
				}
			}
		}
	}
}
