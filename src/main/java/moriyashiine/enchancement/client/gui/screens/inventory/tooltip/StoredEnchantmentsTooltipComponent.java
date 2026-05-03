/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.screens.inventory.tooltip;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsClientEvent;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.joml.Vector2ic;
import org.jspecify.annotations.Nullable;

import java.util.*;

public final class StoredEnchantmentsTooltipComponent implements ClientTooltipComponent, TooltipComponent {
	private static final Map<Item, Identifier> TEXTURE_MAP = new LinkedHashMap<>();
	private static final Map<Holder<Enchantment>, List<Identifier>> ICON_CACHE = new Reference2ObjectOpenHashMap<>();
	private final ItemEnchantments enchantments;
	private int width = 0, height = 0;

	public StoredEnchantmentsTooltipComponent(ItemEnchantments enchantments) {
		this.enchantments = enchantments;
	}

	static {
		addIcon(Items.IRON_HELMET, Identifier.parse("container/slot/helmet"));
		addIcon(Items.IRON_CHESTPLATE, Identifier.parse("container/slot/chestplate"));
		addIcon(Items.IRON_LEGGINGS, Identifier.parse("container/slot/leggings"));
		addIcon(Items.IRON_BOOTS, Identifier.parse("container/slot/boots"));
		addIcon(Items.IRON_HORSE_ARMOR, Enchancement.id("container/slot/animal"));
		addIcon(Items.IRON_NAUTILUS_ARMOR, Identifier.parse("container/slot/nautilus_armor"));
		addIcon(Items.SHIELD, Identifier.parse("container/slot/shield"));
		addIcon(Items.IRON_SWORD, Identifier.parse("container/slot/sword"));
		addIcon(Items.IRON_SPEAR, Identifier.parse("container/slot/spear"));
		addIcon(Items.BOW, Enchancement.id("container/slot/bow"));
		addIcon(Items.CROSSBOW, Enchancement.id("container/slot/crossbow"));
		addIcon(Items.TRIDENT, Enchancement.id("container/slot/trident"));
		addIcon(Items.MACE, Enchancement.id("container/slot/mace"));
		addIcon(Items.IRON_PICKAXE, Identifier.parse("container/slot/pickaxe"));
		addIcon(Items.IRON_AXE, Identifier.parse("container/slot/axe"));
		addIcon(Items.IRON_SHOVEL, Identifier.parse("container/slot/shovel"));
		addIcon(Items.IRON_HOE, Identifier.parse("container/slot/hoe"));
		addIcon(Items.FISHING_ROD, Enchancement.id("container/slot/fishing_rod"));
		addIcon(Items.SADDLE, Enchancement.id("container/slot/saddle"));
		addIcon(Items.ELYTRA, Enchancement.id("container/slot/elytra"));
		addIcon(Items.FLINT_AND_STEEL, Enchancement.id("container/slot/flint_and_steel"));
		addIcon(Items.SHEARS, Enchancement.id("container/slot/shears"));
		addIcon(Items.BRUSH, Enchancement.id("container/slot/brush"));
		addIcon(Items.COMPASS, Enchancement.id("container/slot/compass"));
	}

	public static void addIcon(Item item, Identifier texture) {
		TEXTURE_MAP.put(item, texture);
	}

	public static void clearIconCache() {
		ICON_CACHE.clear();
	}

	@Override
	public int getWidth(Font font) {
		return width;
	}

	@Override
	public int getHeight(Font font) {
		return height;
	}

	public void cacheDimensions(Font font, int x, int y, GuiGraphicsExtractor graphics, @Nullable ClientTooltipPositioner positioner) {
		width = height = 0;
		int screenWidth = graphics.guiWidth();
		// No need to sort the enchantments here as the dimensions will be the same regardless
		for (Holder<Enchantment> enchantment : enchantments.keySet()) {
			Component name = Enchantment.getFullname(enchantment, enchantments.getLevel(enchantment));
			width = Math.max(width, (getIcons(enchantment).isEmpty() ? 0 : 18) + font.width(name));
			height += 18;
			List<Component> description = EnchantmentDescriptionsClientEvent.getDescription(name, enchantment);
			if (description != null) {
				for (Component text : description) {
					width = Math.max(width, font.width(text));
					height += 9;
				}
			}
		}
		// Dimensions are calculated once again based on how the tooltip positioner shifts the tooltip
		if (positioner != null) {
			Vector2ic pos = positioner.positionTooltip(screenWidth, graphics.guiHeight(), x, y, width, height);
			cacheDimensions(font, pos.x(), pos.y(), graphics, null);
		}
	}

	@Override
	public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
		for (Holder<Enchantment> enchantment : getSortedEnchantments()) {
			boolean hasIcon = renderIcons(enchantment, x, y, graphics);
			Component name = Enchantment.getFullname(enchantment, enchantments.getLevel(enchantment));
			graphics.text(font, name, x + (hasIcon ? 18 : 0), y + 5, -1, true);
			y += 18;
			List<Component> description = EnchantmentDescriptionsClientEvent.getDescription(name, enchantment);
			if (description != null) {
				for (Component component : description) {
					graphics.text(font, component, x, y, -1, true);
					y += 9;
				}
			}
		}
	}

	private List<Holder<Enchantment>> getSortedEnchantments() {
		List<Holder<Enchantment>> enchantments = new ArrayList<>(this.enchantments.keySet());
		enchantments.sort((e1, e2) -> {
			if (e1.unwrapKey().isPresent()) {
				if (e2.unwrapKey().isPresent()) {
					return e1.unwrapKey().get().identifier().compareTo(e2.unwrapKey().get().identifier());
				}
				return 1;
			}
			return 0;
		});
		return enchantments;
	}

	private static List<Identifier> getIcons(Holder<Enchantment> enchantment) {
		return ICON_CACHE.computeIfAbsent(enchantment, holder -> {
			Set<Identifier> icons = new HashSet<>();
			for (Map.Entry<Item, Identifier> entry : TEXTURE_MAP.entrySet()) {
				if (entry.getKey().getDefaultInstance().canBeEnchantedWith(holder, EnchantingContext.ACCEPTABLE)) {
					icons.add(entry.getValue());
				}
			}
			return List.copyOf(icons);
		});
	}

	private static boolean renderIcons(Holder<Enchantment> enchantment, int x, int y, GuiGraphicsExtractor graphics) {
		List<Identifier> icons = getIcons(enchantment);
		if (icons.isEmpty()) {
			return false;
		}
		int size = icons.size();
		if (size == 1) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icons.getFirst(), x, y, 16, 16);
			return true;
		}
		double time = Util.getMillis() / 1500d;
		double i = Mth.positiveModulo(time, size);
		float alpha = (float) (Math.min(Mth.frac(i), 0.25) / 0.25);
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icons.get((int) i), x, y, 16, 16, ARGB.color(alpha, -1));
		if (alpha < 1) {
			Identifier icon = i - 1 < 0 ? icons.getLast() : icons.get((int) (i - 1));
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, x, y, 16, 16, ARGB.color(1 - alpha, -1));
		}
		return true;
	}
}
