package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.applyrandommobeffect;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ApplyRandomMobEffectGenericComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public class ThrownTridentMixin {
	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$applyRandomMobEffect(Level level, LivingEntity owner, ItemStack tridentItem, CallbackInfo ci) {
		ApplyRandomMobEffectComponent.maybeSet(owner, tridentItem, 1, tridentItem, effects -> {
			ApplyRandomMobEffectGenericComponent applyRandomMobEffectGeneric = EnchancementEntityComponents.APPLY_RANDOM_MOB_EFFECT_GENERIC.get(this);
			applyRandomMobEffectGeneric.setEffects(effects);
			applyRandomMobEffectGeneric.sync();
		});
	}
}
