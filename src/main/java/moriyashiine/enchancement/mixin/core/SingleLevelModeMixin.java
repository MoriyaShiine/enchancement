package moriyashiine.enchancement.mixin.core;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AquaAffinityEnchantment.class, BindingCurseEnchantment.class, ChannelingEnchantment.class, DamageEnchantment.class, DepthStriderEnchantment.class, EfficiencyEnchantment.class, Enchantment.class, FireAspectEnchantment.class, FlameEnchantment.class, FrostWalkerEnchantment.class, ImpalingEnchantment.class, InfinityEnchantment.class, KnockbackEnchantment.class, LoyaltyEnchantment.class, LuckEnchantment.class, LureEnchantment.class, MendingEnchantment.class, MultishotEnchantment.class, PiercingEnchantment.class, PowerEnchantment.class, ProtectionEnchantment.class, PunchEnchantment.class, QuickChargeEnchantment.class, RespirationEnchantment.class, RiptideEnchantment.class, SilkTouchEnchantment.class, SoulSpeedEnchantment.class, SweepingEnchantment.class, ThornsEnchantment.class, UnbreakingEnchantment.class, VanishingCurseEnchantment.class})
public class SingleLevelModeMixin {
	@Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
	private void enchancement$singleLevelMode(CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().singleLevelMode && Enchancement.CACHED_MAX_LEVELS.containsKey(Enchantment.class.cast(this))) {
			cir.setReturnValue(1);
		}
	}
}
