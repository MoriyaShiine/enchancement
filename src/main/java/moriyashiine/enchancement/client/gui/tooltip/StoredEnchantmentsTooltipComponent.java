/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.gui.tooltip;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.*;

public record StoredEnchantmentsTooltipComponent(
		ItemEnchantmentsComponent enchantments) implements TooltipComponent, TooltipData {
	private static final Map<ItemStack, Identifier> TEXTURE_MAP = new LinkedHashMap<>();

	static {
		TEXTURE_MAP.put(Items.IRON_HELMET.getDefaultStack(), Identifier.of("container/slot/helmet"));
		TEXTURE_MAP.put(Items.IRON_CHESTPLATE.getDefaultStack(), Identifier.of("container/slot/chestplate"));
		TEXTURE_MAP.put(Items.IRON_LEGGINGS.getDefaultStack(), Identifier.of("container/slot/leggings"));
		TEXTURE_MAP.put(Items.IRON_BOOTS.getDefaultStack(), Identifier.of("container/slot/boots"));
		TEXTURE_MAP.put(Items.IRON_HORSE_ARMOR.getDefaultStack(), Enchancement.id("container/slot/animal"));
		TEXTURE_MAP.put(Items.SHIELD.getDefaultStack(), Identifier.of("container/slot/shield"));
		TEXTURE_MAP.put(Items.IRON_SWORD.getDefaultStack(), Identifier.of("container/slot/sword"));
		TEXTURE_MAP.put(Items.BOW.getDefaultStack(), Enchancement.id("container/slot/bow"));
		TEXTURE_MAP.put(Items.CROSSBOW.getDefaultStack(), Enchancement.id("container/slot/crossbow"));
		TEXTURE_MAP.put(Items.TRIDENT.getDefaultStack(), Enchancement.id("container/slot/trident"));
		TEXTURE_MAP.put(Items.MACE.getDefaultStack(), Enchancement.id("container/slot/mace"));
		TEXTURE_MAP.put(Items.IRON_PICKAXE.getDefaultStack(), Identifier.of("container/slot/pickaxe"));
		TEXTURE_MAP.put(Items.IRON_AXE.getDefaultStack(), Identifier.of("container/slot/axe"));
		TEXTURE_MAP.put(Items.IRON_SHOVEL.getDefaultStack(), Identifier.of("container/slot/shovel"));
		TEXTURE_MAP.put(Items.IRON_HOE.getDefaultStack(), Identifier.of("container/slot/hoe"));
		TEXTURE_MAP.put(Items.FISHING_ROD.getDefaultStack(), Enchancement.id("container/slot/fishing_rod"));
	}

	@Override
	public int getHeight(TextRenderer textRenderer) {
		if (getSlots() == 0) {
			return 0;
		}
		return 20;
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		return 18 * getSlots();
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>(enchantments().getEnchantments());
		enchantments.sort((e1, e2) -> {
			if (e1.getKey().isPresent()) {
				if (e2.getKey().isPresent()) {
					return e1.getKey().get().getValue().compareTo(e2.getKey().get().getValue());
				}
				return 1;
			}
			return 0;
		});
		int[] posX = {x};
		renderSlots(enchantments, identifier -> {
			context.drawGuiTexture(RenderLayer::getGuiTextured, identifier, posX[0], y, 16, 16);
			posX[0] += 18;
		});
	}

	private int renderSlots(Collection<RegistryEntry<Enchantment>> enchantments, Renderer renderer) {
		int slots = 0;
		Set<Identifier> renderedTextures = new HashSet<>();
		for (RegistryEntry<Enchantment> enchantment : enchantments) {
			Optional<TagKey<Item>> tagKey = enchantment.value().getApplicableItems().getTagKey();
			if (tagKey.isPresent()) {
				for (Map.Entry<ItemStack, Identifier> entry : TEXTURE_MAP.entrySet()) {
					if (entry.getKey().canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE) && renderedTextures.add(entry.getValue())) {
						slots++;
						renderer.render(entry.getValue());
					}
				}
			}
		}
		return slots;
	}

	private int getSlots() {
		return renderSlots(enchantments().getEnchantments(), identifier -> {
		});
	}

	interface Renderer {
		void render(Identifier identifier);
	}
}
