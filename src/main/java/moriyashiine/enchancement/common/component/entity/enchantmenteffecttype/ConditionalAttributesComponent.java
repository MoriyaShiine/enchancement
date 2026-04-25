/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffecttype;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.world.item.effects.entity.ConditionalAttributeEnchantmentEffect;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class ConditionalAttributesComponent implements ServerTickingComponent {
	private final LivingEntity obj;
	private final List<ConditionalAttribute> attributes = new ArrayList<>();
	private boolean removeAll = false;

	public ConditionalAttributesComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		attributes.clear();
		attributes.addAll(input.read("Attributes", ConditionalAttribute.CODEC.listOf()).orElse(List.of()));
		removeAll = input.getBooleanOr("RemoveAll", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("Attributes", ConditionalAttribute.CODEC.listOf(), attributes);
		output.putBoolean("RemoveAll", removeAll);
	}

	@Override
	public void serverTick() {
		LootContext context = ConditionalAttributeEnchantmentEffect.createContext((ServerLevel) obj.level(), obj);
		attributes.removeIf(attribute -> {
			if (!removeAll && attribute.condition().test(context)) {
				return false;
			}
			obj.getAttribute(attribute.attribute()).removeModifier(attribute.id());
			return true;
		});
		removeAll = false;
	}

	public void addAttribute(Holder<Attribute> attribute, Identifier id, LootItemCondition condition) {
		attributes.add(new ConditionalAttribute(attribute, id, condition));
	}

	public void markRemoved() {
		removeAll = true;
	}

	public record ConditionalAttribute(Holder<Attribute> attribute, Identifier id, LootItemCondition condition) {
		static final Codec<ConditionalAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Attribute.CODEC.fieldOf("attribute").forGetter(ConditionalAttribute::attribute),
						Identifier.CODEC.fieldOf("id").forGetter(ConditionalAttribute::id),
						LootItemCondition.DIRECT_CODEC.fieldOf("condition").forGetter(ConditionalAttribute::condition))
				.apply(instance, ConditionalAttribute::new));
	}
}
