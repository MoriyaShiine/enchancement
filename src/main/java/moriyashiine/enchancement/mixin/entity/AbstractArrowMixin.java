/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.serialization.Codec;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.ShardEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
	public AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@WrapWithCondition(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;setPickupItemStack(Lnet/minecraft/world/item/ItemStack;)V"))
	private boolean enchancement$fixModProjectileSerializationRead(AbstractArrow instance, ItemStack itemStack) {
		return !isDisallowed();
	}

	@WrapWithCondition(method = "addAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/ValueOutput;store(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V"))
	private <T> boolean enchancement$fixModProjectileSerializationWrite(ValueOutput instance, String key, Codec<T> codec, T value) {
		return codec != ItemStack.CODEC || !isDisallowed();
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	@Unique
	private boolean isDisallowed() {
		return getType() == ModEntityTypes.BRIMSTONE || (Object) this instanceof ShardEntity;
	}
}
