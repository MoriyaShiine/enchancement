/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.disarm;

import moriyashiine.enchancement.common.component.entity.DisarmedPlayerComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Merchant;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {
	@Shadow
	public abstract @Nullable PlayerEntity getPlayerOwner();

	public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "pullHookedEntity", at = @At("HEAD"), cancellable = true)
	private void enchancment$disarm(Entity entity, CallbackInfo ci) {
		if (!getWorld().isClient && entity instanceof LivingEntity living && ModEntityComponents.DISARM.get(this).hasDisarm()) {
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
				if (entity instanceof PlayerEntity player) {
					if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
						DisarmedPlayerComponent disarmedPlayerComponent = ModEntityComponents.DISARMED_PLAYER.get(player);
						disarmedPlayerComponent.getDisarmedItems().add(stack.getItem());
						disarmedPlayerComponent.sync();
						player.getItemCooldownManager().set(stack.getItem(), 60);
						player.stopUsingItem();
					}
				} else {
					PlayerEntity owner = getPlayerOwner();
					if (owner != null) {
						if (stack.isDamageable()) {
							stack.setDamage(MathHelper.nextInt(living.getRandom(), stack.getDamage(), (int) (stack.getMaxDamage() - (stack.getMaxDamage() * 0.05F))));
						}
						if (entity instanceof MobEntity mob) {
							if (!owner.isCreative()) {
								mob.setTarget(owner);
								mob.setAttacker(owner);
								if (entity instanceof InteractionObserver observer) {
									observer.onInteractionWith(EntityInteraction.VILLAGER_HURT, owner);
								}
								if (entity instanceof PiglinEntity piglin) {
									PiglinBrainAccessor.enchancement$onAttacked(piglin, owner);
								}
							}
							if (entity instanceof Merchant) {
								stack = ItemStack.EMPTY;
							}
							if (entity instanceof EndermanEntity enderman) {
								enderman.setCarriedBlock(null);
							}
							if (entity instanceof WitchEntity) {
								Potion potion = PotionUtil.getPotion(stack);
								if (potion != Potions.EMPTY) {
									ModEntityComponents.DISARMED_WITCH.get(entity).disablePotion(potion);
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
				}
				ci.cancel();
			}
		}
	}

	@Inject(method = "use", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void enchancment$disarm(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValueI() > 0 && ModEntityComponents.DISARM.get(this).hasDisarm()) {
			cir.setReturnValue(1);
		}
	}
}
