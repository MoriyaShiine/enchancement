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
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract float getHeadYaw();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float enchancement$wardenspine(float value, DamageSource source) {
		if (!world.isClient && !(source instanceof EntityDamageSource entityDamageSource && entityDamageSource.isThorns())) {
			if (source.getSource() instanceof LivingEntity living && Math.abs(MathHelper.subtractAngles(getHeadYaw(), living.getHeadYaw())) <= 75) {
				if (EnchancementUtil.hasEnchantment(ModEnchantments.WARDENSPINE, LivingEntity.class.cast(this))) {
					living.damage(DamageSource.thorns(this), 4);
					return value / 2;
				}
			}
		}
		return value;
	}
}
