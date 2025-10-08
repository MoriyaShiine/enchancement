/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public record ConditionalAttributeEnchantmentEffect(AttributeEnchantmentEffect effect,
													LootCondition condition) implements EnchantmentEntityEffect {
	public static final MapCodec<ConditionalAttributeEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					AttributeEnchantmentEffect.CODEC.fieldOf("attribute").forGetter(ConditionalAttributeEnchantmentEffect::effect),
					LootCondition.CODEC.fieldOf("condition").forGetter(ConditionalAttributeEnchantmentEffect::condition))
			.apply(instance, ConditionalAttributeEnchantmentEffect::new));

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		if (user instanceof LivingEntity living) {
			LootContext ctx = createContext(world, living);
			if (condition().test(ctx)) {
				Identifier id = effect().id().withSuffixedPath("/" + context.slot().getName());
				if (!living.getAttributeInstance(effect().attribute()).hasModifier(id)) {
					effect().apply(world, level, context, user, pos, true);
					ModEntityComponents.CONDITIONAL_ATTRIBUTES.get(living).addAttribute(effect().attribute(), id, condition());
				}
			}
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}

	public static LootContext createContext(ServerWorld world, LivingEntity living) {
		return new LootContext.Builder(new LootWorldContext.Builder(world)
				.add(LootContextParameters.THIS_ENTITY, living)
				.add(LootContextParameters.ORIGIN, living.getEntityPos())
				.add(LootContextParameters.DAMAGE_SOURCE, living.getRecentDamageSource())
				.build(LootContextTypes.ENTITY)).build(Optional.empty());
	}
}
