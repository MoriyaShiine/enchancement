/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.component.entity.LeechingTridentComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public record LeechingTridentEffect(EnchantmentValueEffect damage, EnchantmentValueEffect healAmount,
									EnchantmentValueEffect duration) {
	public static final Codec<LeechingTridentEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("damage").forGetter(LeechingTridentEffect::damage),
					EnchantmentValueEffect.CODEC.fieldOf("heal_amount").forGetter(LeechingTridentEffect::healAmount),
					EnchantmentValueEffect.CODEC.fieldOf("duration").forGetter(LeechingTridentEffect::duration))
			.apply(instance, LeechingTridentEffect::new));

	public static void setValues(Random random, MutableFloat damage, MutableFloat healAmount, MutableFloat duration, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				List<EnchantmentEffectEntry<LeechingTridentEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT);
				if (effects != null) {
					effects.forEach(effect -> {
						damage.setValue(effect.effect().damage().apply(level, random, damage.floatValue()));
						healAmount.setValue(effect.effect().healAmount().apply(level, random, healAmount.floatValue()));
						duration.setValue(effect.effect().duration().apply(level, random, duration.floatValue()));
					});
				}
			});
		}
	}

	public static void renderLeechTrident(TridentEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Model model, Identifier texture, int light, Runnable runnable, CallbackInfo ci) {
		LeechingTridentComponent leechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(entity);
		LivingEntity stuckEntity = leechingTridentComponent.getStuckEntity();
		if (stuckEntity != null) {
			float renderTicks = (leechingTridentComponent.getLeechingTicks() + tickDelta) / 20F;
			float offsetX = MathHelper.sin(renderTicks), offsetZ = MathHelper.cos(renderTicks);
			matrices.push();
			matrices.translate(offsetX, 0, offsetZ);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) -MathHelper.wrapDegrees((MathHelper.atan2(stuckEntity.getZ() - entity.getZ() + offsetZ, stuckEntity.getX() - entity.getX() + offsetX) * 57.2957763671875) - 90) + 90));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(60));
			matrices.translate(0, -(leechingTridentComponent.getStabTicks() / 20F), 0);
			model.render(matrices, ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(texture), false, entity.isEnchanted()), light, OverlayTexture.DEFAULT_UV);
			matrices.pop();
			runnable.run();
			ci.cancel();
		}
	}
}
