package moriyashiine.enchancement.fabric.mixin.enchantmenteffectcomponenttype.brimstone;

import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class, Monster.class})
public class BrimstoneCrossbowMixin {
	@Inject(method = "getProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getSupportedHeldProjectiles()Ljava/util/function/Predicate;"), cancellable = true)
	private void enchancement$brimstone(ItemStack heldWeapon, CallbackInfoReturnable<ItemStack> cir) {
		if (EnchantmentHelper.has(heldWeapon, EnchancementEnchantmentEffectComponentTypes.BRIMSTONE)) {
			ItemStack projectile = Items.ARROW.getDefaultInstance();
			projectile.set(DataComponents.ITEM_NAME, Component.translatable("enchantment.enchancement.brimstone"));
			cir.setReturnValue(projectile);
		}
	}
}
