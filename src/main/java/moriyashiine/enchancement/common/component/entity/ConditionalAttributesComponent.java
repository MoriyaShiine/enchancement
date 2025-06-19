/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.enchantment.effect.entity.ConditionalAttributeEnchantmentEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
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
	public void readData(ReadView readView) {
		attributes.clear();
		attributes.addAll(readView.read("Attributes", ConditionalAttribute.CODEC.listOf()).orElse(List.of()));
		removeAll = readView.getBoolean("RemoveAll", false);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.put("Attributes", ConditionalAttribute.CODEC.listOf(), List.copyOf(attributes));
		writeView.putBoolean("RemoveAll", removeAll);
	}

	@Override
	public void serverTick() {
		LootContext ctx = ConditionalAttributeEnchantmentEffect.createContext((ServerWorld) obj.getWorld(), obj);
		attributes.removeIf(attribute -> {
			if (!removeAll && attribute.condition().test(ctx)) {
				return false;
			}
			obj.getAttributeInstance(attribute.attribute()).removeModifier(attribute.id());
			return true;
		});
		removeAll = false;
	}

	public void addAttribute(RegistryEntry<EntityAttribute> attribute, Identifier id, LootCondition condition) {
		attributes.add(new ConditionalAttribute(attribute, id, condition));
	}

	public void markRemoved() {
		removeAll = true;
	}

	public record ConditionalAttribute(RegistryEntry<EntityAttribute> attribute, Identifier id,
									   LootCondition condition) {
		static final Codec<ConditionalAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						EntityAttribute.CODEC.fieldOf("attribute").forGetter(ConditionalAttribute::attribute),
						Identifier.CODEC.fieldOf("id").forGetter(ConditionalAttribute::id),
						LootCondition.CODEC.fieldOf("condition").forGetter(ConditionalAttribute::condition))
				.apply(instance, ConditionalAttribute::new));
	}
}
