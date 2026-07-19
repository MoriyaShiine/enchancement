package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedCrossbowAttackGoal.class)
public class RangedCrossbowAttackGoalMixin<T extends Monster & CrossbowAttackMob> {
	@Unique
	private Monster mob;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void enchancement$rapidCrossbowFire(Monster mob, double speedModifier, float attackRadius, CallbackInfo ci) {
		this.mob = mob;
	}

	@Inject(method = "stop", at = @At("TAIL"))
	private void enchancement$rapidCrossbowFire(CallbackInfo ci) {
		ItemStack stack = mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(mob, Items.CROSSBOW));
		if (EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			stack.releaseUsing(mob.level(), mob, mob.getUseItemRemainingTicks());
		}
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/control/LookControl;setLookAt(Lnet/minecraft/world/entity/Entity;FF)V", shift = At.Shift.AFTER), cancellable = true)
	private void enchancement$rapidCrossbowFire(CallbackInfo ci, @Local(name = "hasLineOfSight") boolean hasLineOfSight) {
		ItemStack stack = mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(mob, Items.CROSSBOW));
		if (EnchantmentHelper.has(stack, EnchancementEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			if (hasLineOfSight) {
				mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(mob, Items.CROSSBOW));
			}
			ci.cancel();
		}
	}
}
