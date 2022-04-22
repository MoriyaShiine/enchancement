package moriyashiine.enchancement.mixin.leech;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public class TridentEntityMixin {
	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$leech(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.LEECH, stack) || (owner instanceof DrownedEntity && EnchancementUtil.hasEnchantment(ModEnchantments.LEECH, owner))) {
			ModEntityComponents.LEECH.maybeGet(this).ifPresent(leechComponent -> {
				leechComponent.setHasLeech(true);
				leechComponent.sync();
			});
		}
	}

	@ModifyVariable(method = "tick", at = @At("STORE"))
	private int enchancement$leech(int value) {
		if (ModEntityComponents.LEECH.get(this).getStuckEntity() != null) {
			return 0;
		}
		return value;
	}
}
