/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.DisarmingFishingBobberComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.Merchant;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {
	@Shadow
	public abstract @Nullable PlayerEntity getPlayerOwner();

	public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IILnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$disarmingFishingBobber(PlayerEntity thrower, World world, int luckBonus, int waitTimeReductionTicks, ItemStack stack, CallbackInfo ci) {
		DisarmingFishingBobberComponent.maybeSet(thrower, stack, this);
	}

	@Inject(method = "pullHookedEntity", at = @At("HEAD"), cancellable = true)
	private void enchancment$disarmingFishingBobber(Entity entity, CallbackInfo ci) {
		if (getWorld() instanceof ServerWorld serverWorld && entity instanceof LivingEntity living) {
			DisarmingFishingBobberComponent disarmingFishingBobberComponent = ModEntityComponents.DISARMING_FISHING_BOBBER.get(this);
			if (disarmingFishingBobberComponent.isEnabled()) {
				boolean offhand = false;
				ItemStack stack;
				if (entity instanceof EndermanEntity enderman && enderman.getCarriedBlock() != null) {
					stack = new ItemStack(enderman.getCarriedBlock().getBlock());
				} else {
					stack = living.getMainHandStack();
					if (stack.isEmpty()) {
						offhand = true;
						stack = living.getOffHandStack();
					}
				}
				if (!stack.isEmpty()) {
					PlayerEntity owner = getPlayerOwner();
					int disableTime = 0;
					if (entity instanceof PlayerEntity player && !disarmingFishingBobberComponent.stealsFromPlayers()) {
						disableTime = disarmingFishingBobberComponent.getPlayerCooldown();
						if (entity != owner) {
							disarmingFishingBobberComponent.disableStack(player, stack, disableTime);
						}
					} else if (owner != null) {
						if (entity instanceof MobEntity mob) {
							if (stack.isDamageable()) {
								stack.setDamage(mob.getRandom().nextBetween(stack.getDamage() / 2, stack.getDamage()));
							}
							if (!owner.isCreative()) {
								mob.setTarget(owner);
								mob.setAttacker(owner);
								if (entity instanceof InteractionObserver observer) {
									observer.onInteractionWith(EntityInteraction.VILLAGER_HURT, owner);
								}
								if (entity instanceof PiglinEntity piglin) {
									PiglinBrain.onAttacked(serverWorld, piglin, owner);
								}
							}
							if (entity instanceof Merchant) {
								stack = ItemStack.EMPTY;
							}
							if (entity instanceof EndermanEntity enderman) {
								enderman.setCarriedBlock(null);
							}
							if (entity instanceof WitchEntity) {
								PotionContentsComponent potionContents = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, null);
								if (potionContents != null) {
									potionContents.potion().ifPresent(potion -> ModEntityComponents.DISARMED_WITCH.get(entity).disablePotion(potion.value()));
								}
							}
						}
						if (!stack.isEmpty()) {
							ItemEntity itemEntity = new ItemEntity(getWorld(), entity.getX(), entity.getBodyY(0.5), entity.getZ(), stack);
							itemEntity.setToDefaultPickupDelay();
							double deltaX = owner.getX() - getX();
							double deltaY = owner.getY() - getY();
							double deltaZ = owner.getZ() - getZ();
							itemEntity.setVelocity(deltaX * 0.1, deltaY * 0.1 + Math.sqrt(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)) * 0.08, deltaZ * 0.1);
							getWorld().spawnEntity(itemEntity);
							living.equipStack(offhand ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND, ItemStack.EMPTY);
						}
					}
					if (owner != null) {
						disableTime = Math.max(disableTime, disarmingFishingBobberComponent.getUserCooldown());
						if (disableTime > 0) {
							disarmingFishingBobberComponent.disableStack(owner, disarmingFishingBobberComponent.getStack(), disableTime);
						}
					}
					ci.cancel();
				}
			}
		}
	}

	@ModifyReturnValue(method = "use", at = @At(value = "RETURN", ordinal = 1))
	private int enchancment$disarmingFishingBobber(int original) {
		if (original > 0 && ModEntityComponents.DISARMING_FISHING_BOBBER.get(this).isEnabled()) {
			return 1;
		}
		return original;
	}
}
