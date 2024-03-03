/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.chaos;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {
	@Inject(method = "createArrow", at = @At("RETURN"))
	private void enchancement$chaos(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
		if (cir.getReturnValue() instanceof ArrowEntity arrow) {
			boolean hasChaos = shooter instanceof PlayerEntity ? EnchancementUtil.hasEnchantment(ModEnchantments.CHAOS, shooter.getActiveItem()) : EnchancementUtil.hasEnchantment(ModEnchantments.CHAOS, shooter);
			if (hasChaos) {
				int attempts = 0;
				StatusEffectCategory category = shooter.isSneaking() ? StatusEffectCategory.BENEFICIAL : StatusEffectCategory.HARMFUL;
				List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
				Set<StatusEffect> disallowed = new HashSet<>();
				for (StatusEffectInstance instance : effects) {
					disallowed.add(instance.getEffectType());
				}
				while (attempts < 128) {
					StatusEffect effect = Registries.STATUS_EFFECT.get(shooter.getRandom().nextInt(Registries.STATUS_EFFECT.size()));
					if (!disallowed.contains(effect)) {
						Optional<RegistryKey<StatusEffect>> key = Registries.STATUS_EFFECT.getKey(effect);
						if (key.isPresent() && effect != null && effect.getCategory() == category && !Registries.STATUS_EFFECT.entryOf(key.get()).isIn(ModTags.StatusEffects.CHAOS_UNCHOOSABLE)) {
							List<StatusEffectInstance> statusEffects = new ArrayList<>();
							for (StatusEffectInstance instance : effects) {
								statusEffects.add(new StatusEffectInstance(instance.getEffectType(), Math.max(instance.mapDuration(i -> i / 8), 1), instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles()));
							}
							statusEffects.add(new StatusEffectInstance(effect, effect.isInstant() ? 1 : 200));
							ModEntityComponents.CHAOS.get(arrow).setOriginalStack(((PersistentProjectileEntityAccessor) arrow).enchancement$asItemStack());
							arrow.initFromStack(PotionUtil.setCustomPotionEffects(new ItemStack(Items.TIPPED_ARROW), statusEffects));
							return;
						}
					}
					attempts++;
				}
			}
		}
	}
}
