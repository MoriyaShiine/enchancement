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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
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
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		attributes.clear();
		NbtList list = tag.getList("Attributes", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < list.size(); i++) {
			NbtCompound attributeCompound = list.getCompound(i);
			ConditionalAttribute.CODEC.parse(registryLookup.getOps(NbtOps.INSTANCE), attributeCompound).ifSuccess(attributes::add);
		}
		removeAll = tag.getBoolean("RemoveAll");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList attributes = new NbtList();
		for (ConditionalAttribute attribute : this.attributes) {
			ConditionalAttribute.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), attribute).ifSuccess(attributes::add);
		}
		tag.put("Attributes", attributes);
		tag.putBoolean("RemoveAll", removeAll);
	}

	@Override
	public void serverTick() {
		LootContext ctx = ConditionalAttributeEnchantmentEffect.createContext(obj);
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
