/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rotationburst;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Unique
	private final List<ItemStack> lastArmorSet = new ArrayList<>();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$rotationBurst(CallbackInfo ci) {
		if (!lastArmorSet.isEmpty()) {
			int index = 0, difference = 0;
			for (ItemStack newStack : getAllArmorItems()) {
				if (!ItemStack.areEqual(lastArmorSet.get(index), newStack)) {
					difference++;
				}
				index++;
			}
			if (difference != 0) {
				ModEntityComponents.ROTATION_BURST.get(this).markDelay();
			}
		}
		lastArmorSet.clear();
		getAllArmorItems().forEach(lastArmorSet::add);
	}
}
