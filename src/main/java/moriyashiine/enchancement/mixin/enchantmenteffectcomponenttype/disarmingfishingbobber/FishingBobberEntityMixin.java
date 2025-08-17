/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.DisarmedWanderingTraderComponent;
import moriyashiine.enchancement.common.component.entity.DisarmingFishingBobberComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
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

	@Inject(method = "pullHookedEntity", at = @At("HEAD"), cancellable = true)
	private void enchancment$disarmingFishingBobber(Entity entity, CallbackInfo ci) {
		if (!entity.getType().isIn(ModEntityTypeTags.CANNOT_DISARM) && getWorld() instanceof ServerWorld serverWorld && entity instanceof LivingEntity living) {
			DisarmingFishingBobberComponent disarmingFishingBobberComponent = ModEntityComponents.DISARMING_FISHING_BOBBER.get(this);
			if (disarmingFishingBobberComponent.isEnabled()) {
				ItemStack stack = ItemStack.EMPTY;
				EquipmentSlot stackSlot = EquipmentSlot.MAINHAND;
				if (entity instanceof EndermanEntity enderman && enderman.getCarriedBlock() != null) {
					stack = new ItemStack(enderman.getCarriedBlock().getBlock());
				} else {
					for (Hand hand : Hand.values()) {
						ItemStack handStack = living.getStackInHand(hand);
						if (!handStack.isEmpty() && handStack == living.getActiveItem()) {
							stack = handStack;
							stackSlot = LivingEntity.getSlotForHand(hand);
						}
					}
					if (stack.isEmpty()) {
						stack = living.getMainHandStack();
						if (stack.isEmpty()) {
							stack = living.getOffHandStack();
							stackSlot = EquipmentSlot.OFFHAND;
						}
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
							if (entity instanceof EndermanEntity enderman) {
								enderman.setCarriedBlock(null);
							}
							if (entity instanceof WanderingTraderEntity) {
								DisarmedWanderingTraderComponent disarmedWanderingTraderComponent = ModEntityComponents.DISARMED_WANDERING_TRADER.get(entity);
								if (stack.isOf(Items.MILK_BUCKET)) {
									disarmedWanderingTraderComponent.disarmMilk();
								} else {
									disarmedWanderingTraderComponent.disarmPotion();
								}
							} else if (entity instanceof Merchant) {
								stack = ItemStack.EMPTY;
							}
							if (entity instanceof WitchEntity) {
								PotionContentsComponent potionContents = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, null);
								if (potionContents != null) {
									potionContents.potion().ifPresent(potion -> ModEntityComponents.DISARMED_WITCH.get(entity).disablePotion(potion));
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
							living.equipStack(stackSlot, ItemStack.EMPTY);
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
