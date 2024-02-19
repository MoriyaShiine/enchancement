/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.wardenspine;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract float getHeadYaw();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "modifyAppliedDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Ljava/lang/Iterable;Lnet/minecraft/entity/damage/DamageSource;)I"), argsOnly = true)
	private float enchancement$wardenspine(float value, DamageSource source) {
		if (source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS) || source.isOf(DamageTypes.THORNS)) {
			return value;
		}
		if (source.getSource() != null) {
			int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.WARDENSPINE, (LivingEntity) (Object) this);
			if (level > 0 && Math.abs(MathHelper.subtractAngles(getHeadYaw(), source.getSource().getHeadYaw())) <= 75) {
				if (source.getSource() instanceof LivingEntity living) {
					getWorld().playSound(null, getBlockPos(), ModSoundEvents.ENTITY_GENERIC_WARDENSPINE, getSoundCategory(), 1, 1);
					living.damage(getDamageSources().thorns(this), 4);
					living.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, level * 150));
				}
				return Math.max(0, value * (1 - level * 0.4F));
			}
		}
		return value;
	}
}
