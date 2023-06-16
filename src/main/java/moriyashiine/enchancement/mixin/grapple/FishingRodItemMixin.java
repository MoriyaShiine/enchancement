/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.grapple;

import moriyashiine.enchancement.common.entity.projectile.GrappleFishingBobberEntity;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
	@Unique
	private ItemStack cachedStack = null;

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$grapple(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack stack) {
		cachedStack = stack;
	}

	@ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private Entity enchancement$grapple(Entity value) {
		boolean hasGrapple = EnchancementUtil.hasEnchantment(ModEnchantments.GRAPPLE, cachedStack);
		cachedStack = null;
		if (hasGrapple && value instanceof FishingBobberEntity fishingBobber) {
			return new GrappleFishingBobberEntity(fishingBobber.getPlayerOwner(), fishingBobber.getWorld(), fishingBobber.luckOfTheSeaLevel, fishingBobber.lureLevel);
		}
		return value;
	}
}
