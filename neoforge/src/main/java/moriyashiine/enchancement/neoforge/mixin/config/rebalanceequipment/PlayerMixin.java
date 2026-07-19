package moriyashiine.enchancement.neoforge.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(value = Player.class, priority = 2000)
public abstract class PlayerMixin extends LivingEntity {
	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@WrapOperation(method = "getProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getAllSupportedProjectiles(Lnet/minecraft/world/item/ItemStack;)Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$rebalanceEquipment(ProjectileWeaponItem instance, ItemStack stack, Operation<Predicate<ItemStack>> original, @Local(name = "supportedProjectiles") Predicate<ItemStack> supportedProjectiles) {
		Predicate<ItemStack> supported = original.call(instance, stack);
		if (EnchancementConfig.rebalanceEquipment) {
			supported = supported.or(supportedProjectiles);
		}
		return supported;
	}

	@WrapOperation(method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
	private double enchancement$rebalanceEquipment(Player instance, Holder<Attribute> registryEntry, Operation<Double> original) {
		double value = original.call(instance, registryEntry);
		if (EnchancementConfig.rebalanceEquipment) {
			return Math.max(0.5F, value);
		}
		return value;
	}
}
