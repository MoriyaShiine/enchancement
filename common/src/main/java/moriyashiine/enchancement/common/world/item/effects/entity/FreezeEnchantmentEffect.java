package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record FreezeEnchantmentEffect(LevelBasedValue duration) implements EnchantmentEntityEffect {
	public static final MapCodec<FreezeEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			LevelBasedValue.CODEC.fieldOf("duration").forGetter(FreezeEnchantmentEffect::duration)
	).apply(instance, FreezeEnchantmentEffect::new));

	@Override
	public MapCodec<FreezeEnchantmentEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		setFreezeTicks(entity, Mth.floor(duration().calculate(enchantmentLevel) * 20));
	}

	public static void setFreezeTicks(Entity entity, int freezeTicks) {
		if (entity.isAlive() && !entity.is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
			EnchancementEntityComponents.FROZEN.maybeGet(entity).ifPresent(frozen -> {
				if (frozen.getFreezeTicks() < freezeTicks) {
					frozen.setFreezeTicks(freezeTicks);
					frozen.sync();
				}
			});
		}
	}
}
