package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.grapplingfishingbobber;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.GrapplingFishingBobberComponent;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
	@SuppressWarnings("WrapWithConditionTargetsNonVoid")
	@WrapWithCondition(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectile(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/Projectile;"))
	private <T extends Projectile> boolean enchancement$grapplingFishingBobber(T projectile, ServerLevel serverLevel, ItemStack itemStack) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.getNullable(projectile);
		if (grapplingFishingBobber != null) {
			float strength = EnchancementUtil.getValue(EnchancementEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER, serverLevel, itemStack, 0);
			if (strength != 0) {
				grapplingFishingBobber.setStrength(strength);
				grapplingFishingBobber.sync();
			}
		}
		return true;
	}
}
