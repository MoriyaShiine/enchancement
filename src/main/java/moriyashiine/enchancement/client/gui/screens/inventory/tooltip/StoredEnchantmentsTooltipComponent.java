/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.screens.inventory.tooltip;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsEvent;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.joml.Vector2ic;
import org.jspecify.annotations.Nullable;

import java.util.*;

public final class StoredEnchantmentsTooltipComponent implements ClientTooltipComponent, TooltipComponent {
	private static final Map<ItemStack, Identifier> TEXTURE_MAP = new LinkedHashMap<>();
	private static final Map<Holder<Enchantment>, List<Identifier>> ICON_CACHE = new Reference2ObjectOpenHashMap<>();
	private final ItemEnchantments enchantments;
	private int width = 0, height = 0;

	public StoredEnchantmentsTooltipComponent(ItemEnchantments enchantments) {
		this.enchantments = enchantments;
	}

	static {
		TEXTURE_MAP.put(Items.IRON_HELMET.getDefaultInstance(), Identifier.parse("container/slot/helmet"));
		TEXTURE_MAP.put(Items.IRON_CHESTPLATE.getDefaultInstance(), Identifier.parse("container/slot/chestplate"));
		TEXTURE_MAP.put(Items.IRON_LEGGINGS.getDefaultInstance(), Identifier.parse("container/slot/leggings"));
		TEXTURE_MAP.put(Items.IRON_BOOTS.getDefaultInstance(), Identifier.parse("container/slot/boots"));
		TEXTURE_MAP.put(Items.IRON_HORSE_ARMOR.getDefaultInstance(), Enchancement.id("container/slot/animal"));
		TEXTURE_MAP.put(Items.IRON_NAUTILUS_ARMOR.getDefaultInstance(), Identifier.parse("container/slot/nautilus_armor"));
		TEXTURE_MAP.put(Items.SHIELD.getDefaultInstance(), Identifier.parse("container/slot/shield"));
		TEXTURE_MAP.put(Items.IRON_SWORD.getDefaultInstance(), Identifier.parse("container/slot/sword"));
		TEXTURE_MAP.put(Items.IRON_SPEAR.getDefaultInstance(), Identifier.parse("container/slot/spear"));
		TEXTURE_MAP.put(Items.BOW.getDefaultInstance(), Enchancement.id("container/slot/bow"));
		TEXTURE_MAP.put(Items.CROSSBOW.getDefaultInstance(), Enchancement.id("container/slot/crossbow"));
		TEXTURE_MAP.put(Items.TRIDENT.getDefaultInstance(), Enchancement.id("container/slot/trident"));
		TEXTURE_MAP.put(Items.MACE.getDefaultInstance(), Enchancement.id("container/slot/mace"));
		TEXTURE_MAP.put(Items.IRON_PICKAXE.getDefaultInstance(), Identifier.parse("container/slot/pickaxe"));
		TEXTURE_MAP.put(Items.IRON_AXE.getDefaultInstance(), Identifier.parse("container/slot/axe"));
		TEXTURE_MAP.put(Items.IRON_SHOVEL.getDefaultInstance(), Identifier.parse("container/slot/shovel"));
		TEXTURE_MAP.put(Items.IRON_HOE.getDefaultInstance(), Identifier.parse("container/slot/hoe"));
		TEXTURE_MAP.put(Items.FISHING_ROD.getDefaultInstance(), Enchancement.id("container/slot/fishing_rod"));
		TEXTURE_MAP.put(Items.SHIELD.getDefaultInstance(), Identifier.parse("container/slot/shield"));
		TEXTURE_MAP.put(Items.SADDLE.getDefaultInstance(), Enchancement.id("container/slot/saddle"));
		TEXTURE_MAP.put(Items.ELYTRA.getDefaultInstance(), Enchancement.id("container/slot/elytra"));
		TEXTURE_MAP.put(Items.FLINT_AND_STEEL.getDefaultInstance(), Enchancement.id("container/slot/flint_and_steel"));
		TEXTURE_MAP.put(Items.SHEARS.getDefaultInstance(), Enchancement.id("container/slot/shears"));
		TEXTURE_MAP.put(Items.BRUSH.getDefaultInstance(), Enchancement.id("container/slot/brush"));
		TEXTURE_MAP.put(Items.COMPASS.getDefaultInstance(), Enchancement.id("container/slot/compass"));
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
			List<Component> description = EnchantmentDescriptionsEvent.getDescription(name, enchantment);
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
			List<Component> description = EnchantmentDescriptionsEvent.getDescription(name, enchantment);
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
			for (Map.Entry<ItemStack, Identifier> entry : TEXTURE_MAP.entrySet()) {
				if (entry.getKey().canBeEnchantedWith(holder, EnchantingContext.ACCEPTABLE)) {
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
