package moriyashiine.enchancement.mixin.warp.integration.impaled;

import ladysnake.impaled.common.entity.ImpaledTridentEntity;
import ladysnake.impaled.common.item.ImpaledTridentItem;
import moriyashiine.enchancement.common.component.entity.WarpComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ImpaledTridentItem.class, remap = false)
public class ImpaledTridentItemMixin {
	@Inject(method = "createTrident", at = @At("RETURN"))
	private void enchancement$warp(World world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ImpaledTridentEntity> cir) {
		WarpComponent.maybeSet(user, stack, cir.getReturnValue());
	}
}
