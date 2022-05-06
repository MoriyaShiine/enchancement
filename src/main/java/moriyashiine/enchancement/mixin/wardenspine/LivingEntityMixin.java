/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.wardenspine;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract float getHeadYaw();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "applyEnchantmentsToDamage", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
	private void enchancement$wardenspine(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
		if (source instanceof EntityDamageSource entityDamageSource && entityDamageSource.isThorns()) {
			return;
		}
		if (source.getSource() instanceof LivingEntity living && Math.abs(MathHelper.subtractAngles(getHeadYaw(), living.getHeadYaw())) <= 75 && EnchancementUtil.hasEnchantment(ModEnchantments.WARDENSPINE, this)) {
			living.damage(DamageSource.thorns(this), 4);
			cir.setReturnValue(cir.getReturnValueF() / 2);
		}
	}
}
