/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ChaosArrowComponent implements Component {
	private ItemStack originalStack = ItemStack.EMPTY;

	@Override
	public void readFromNbt(NbtCompound tag) {
		originalStack = ItemStack.fromNbt(tag.getCompound("OriginalStack"));
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.put("OriginalStack", originalStack.writeNbt(new NbtCompound()));
	}

	public ItemStack getOriginalStack() {
		return originalStack;
	}

	public void setOriginalStack(ItemStack originalStack) {
		this.originalStack = originalStack;
	}

	public static void applyChaos(LivingEntity shooter, ItemStack stack, Consumer<List<StatusEffectInstance>> consumer) {
		boolean hasChaos = shooter instanceof PlayerEntity ? EnchancementUtil.hasEnchantment(ModEnchantments.CHAOS, shooter.getActiveItem()) : EnchancementUtil.hasEnchantment(ModEnchantments.CHAOS, shooter);
		if (hasChaos) {
			int attempts = 0;
			StatusEffectCategory category = shooter.isSneaking() ? StatusEffectCategory.BENEFICIAL : StatusEffectCategory.HARMFUL;
			List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
			Set<StatusEffect> disallowed = new HashSet<>();
			if (stack.isOf(Items.SPECTRAL_ARROW)) {
				disallowed.add(StatusEffects.GLOWING);
			}
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
						consumer.accept(statusEffects);
						return;
					}
				}
				attempts++;
			}
		}
	}
}
