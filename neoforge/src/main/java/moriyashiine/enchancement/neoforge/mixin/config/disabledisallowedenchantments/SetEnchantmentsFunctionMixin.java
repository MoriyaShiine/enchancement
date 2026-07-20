package moriyashiine.enchancement.neoforge.mixin.config.disabledisallowedenchantments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.neoforge.common.util.EvilAssCodecHack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(SetEnchantmentsFunction.class)
public abstract class SetEnchantmentsFunctionMixin extends LootItemConditionalFunction {
	@Shadow
	@Final
	@Mutable
	public static MapCodec<SetEnchantmentsFunction> MAP_CODEC;

	protected SetEnchantmentsFunctionMixin(List<LootItemCondition> predicates) {
		super(predicates);
	}

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void enchancement$disableDisallowedEnchantments(CallbackInfo ci) {
		MAP_CODEC = RecordCodecBuilder.mapCodec(
				i -> commonFields(i)
						.and(
								i.group(
										new EvilAssCodecHack<>(Enchantment.CODEC, NumberProviders.CODEC).optionalFieldOf("enchantments", Map.of()).forGetter(f -> f.enchantments),
										Codec.BOOL.fieldOf("add").orElse(false).forGetter(f -> f.add)
								)
						)
						.apply(i, SetEnchantmentsFunction::new)
		);
	}
}
