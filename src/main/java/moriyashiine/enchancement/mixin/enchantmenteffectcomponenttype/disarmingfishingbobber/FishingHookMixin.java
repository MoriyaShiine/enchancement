/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DisarmedWanderingTraderComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DisarmingFishingBobberComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
	@Shadow
	public abstract @Nullable Player getPlayerOwner();

	public FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@Inject(method = "pullEntity", at = @At("HEAD"), cancellable = true)
	private void enchancment$disarmingFishingBobber(Entity entity, CallbackInfo ci) {
		if (!entity.is(ModEntityTypeTags.CANNOT_DISARM) && level() instanceof ServerLevel level && entity instanceof LivingEntity living) {
			DisarmingFishingBobberComponent disarmingFishingBobberComponent = ModEntityComponents.DISARMING_FISHING_BOBBER.get(this);
			if (disarmingFishingBobberComponent.isEnabled()) {
				ItemStack stack = ItemStack.EMPTY;
				EquipmentSlot slot = EquipmentSlot.MAINHAND;
				if (entity instanceof EnderMan enderman && enderman.getCarriedBlock() != null) {
					stack = new ItemStack(enderman.getCarriedBlock().getBlock());
				} else {
					for (InteractionHand hand : InteractionHand.values()) {
						ItemStack handStack = living.getItemInHand(hand);
						if (!handStack.isEmpty() && handStack == living.getActiveItem()) {
							stack = handStack;
							slot = hand.asEquipmentSlot();
						}
					}
					if (stack.isEmpty()) {
						stack = living.getMainHandItem();
						if (stack.isEmpty()) {
							stack = living.getOffhandItem();
							slot = EquipmentSlot.OFFHAND;
						}
					}
				}
				if (!stack.isEmpty()) {
					Player owner = getPlayerOwner();
					int disableTime = 0;
					if (entity instanceof Player player && !disarmingFishingBobberComponent.stealsFromPlayers()) {
						disableTime = disarmingFishingBobberComponent.getPlayerCooldown();
						if (entity != owner) {
							disarmingFishingBobberComponent.disableStack(player, stack, disableTime);
						}
					} else if (owner != null) {
						if (entity instanceof Mob mob) {
							if (stack.isDamageableItem()) {
								stack.setDamageValue(mob.getRandom().nextIntBetweenInclusive(stack.getDamageValue() / 2, stack.getDamageValue()));
							}
							if (!owner.isCreative()) {
								mob.setTarget(owner);
								mob.setLastHurtByMob(owner);
								if (entity instanceof ReputationEventHandler observer) {
									observer.onReputationEventFrom(ReputationEventType.VILLAGER_HURT, owner);
								}
								if (entity instanceof Piglin piglin) {
									PiglinAi.wasHurtBy(level, piglin, owner);
								}
							}
							if (entity instanceof EnderMan enderman) {
								enderman.setCarriedBlock(null);
							}
							if (entity instanceof WanderingTrader) {
								DisarmedWanderingTraderComponent disarmedWanderingTraderComponent = ModEntityComponents.DISARMED_WANDERING_TRADER.get(entity);
								if (stack.is(Items.MILK_BUCKET)) {
									disarmedWanderingTraderComponent.disarmMilk();
								} else {
									disarmedWanderingTraderComponent.disarmPotion();
								}
							} else if (entity instanceof Merchant) {
								stack = ItemStack.EMPTY;
							}
							if (entity instanceof Witch) {
								PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
								if (potionContents != null) {
									potionContents.potion().ifPresent(potion -> ModEntityComponents.DISARMED_WITCH.get(entity).disablePotion(potion));
								}
							}
						}
						if (!stack.isEmpty()) {
							ItemEntity itemEntity = new ItemEntity(level(), entity.getX(), entity.getY(0.5), entity.getZ(), stack);
							itemEntity.setDefaultPickUpDelay();
							double deltaX = owner.getX() - getX();
							double deltaY = owner.getY() - getY();
							double deltaZ = owner.getZ() - getZ();
							itemEntity.setDeltaMovement(deltaX * 0.1, deltaY * 0.1 + Math.sqrt(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)) * 0.08, deltaZ * 0.1);
							level().addFreshEntity(itemEntity);
							living.setItemSlot(slot, ItemStack.EMPTY);
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

	@ModifyReturnValue(method = "retrieve", at = @At(value = "RETURN", ordinal = 1))
	private int enchancment$disarmingFishingBobber(int original) {
		if (original > 0 && ModEntityComponents.DISARMING_FISHING_BOBBER.get(this).isEnabled()) {
			return 1;
		}
		return original;
	}
}
