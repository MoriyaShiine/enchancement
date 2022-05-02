package moriyashiine.enchancement.mixin.leech.integration.impaled;

import ladysnake.impaled.common.entity.ImpaledTridentEntity;
import ladysnake.impaled.common.item.ImpaledTridentItem;
import moriyashiine.enchancement.common.component.entity.LeechComponent;
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
	private void enchancement$leech(World world, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ImpaledTridentEntity> cir) {
		LeechComponent.maybeSet(user, stack, cir.getReturnValue());
	}
}
