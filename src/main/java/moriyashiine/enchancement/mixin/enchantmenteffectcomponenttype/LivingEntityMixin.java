/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.EquipmentResetEvent;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Unique
	private final EntityEquipment lastEquipment = new EntityEquipment();

	@Shadow
	@Final
	protected EntityEquipment equipment;

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$equipmentReset(CallbackInfo ci) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() != EquipmentSlot.Type.HAND) {
				EquipmentResetEvent.maybeReset((LivingEntity) (Object) this, lastEquipment.get(slot), equipment.get(slot));
			}
		}
		lastEquipment.setAll(equipment);
	}
}
