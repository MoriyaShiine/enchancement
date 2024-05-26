/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.drowneduseheldtrident;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DrownedEntity.class)
public class DrownedEntityMixin extends ZombieEntity {
	public DrownedEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyArg(method = "shootAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V"))
	private ItemStack enchancement$drownedUseHeldTrident(ItemStack value) {
		if (ModConfig.drownedUseHeldTrident) {
			ItemStack stack = getMainHandStack();
			if (!(stack.getItem() instanceof TridentItem)) {
				stack = getOffHandStack();
			}
			return stack;
		}
		return value;
	}
}
