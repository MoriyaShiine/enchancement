/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone;

import moriyashiine.enchancement.client.packet.StopBrimstoneSoundsS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "RETURN", ordinal = 1, shift = At.Shift.BY, by = 2))
	private void enchancement$brimstone(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
		StopBrimstoneSoundsS2CPacket.stopSounds((Entity) (Object) this, stack);
	}
}
