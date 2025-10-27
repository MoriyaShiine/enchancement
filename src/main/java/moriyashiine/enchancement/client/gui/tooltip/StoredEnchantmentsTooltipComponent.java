/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.gui.tooltip;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsEvent;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;

import java.util.*;

public final class StoredEnchantmentsTooltipComponent implements TooltipComponent, TooltipData {
	private static final Map<ItemStack, Identifier> TEXTURE_MAP = new LinkedHashMap<>();
	private static final Map<RegistryEntry<Enchantment>, List<Identifier>> ICON_CACHE = new Reference2ObjectOpenHashMap<>();
	private final ItemEnchantmentsComponent enchantments;
	private int width = 0, height = 0;

	public StoredEnchantmentsTooltipComponent(ItemEnchantmentsComponent enchantments) {
		this.enchantments = enchantments;
	}

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
		TEXTURE_MAP.put(Items.SHIELD.getDefaultStack(), Identifier.of("container/slot/shield"));
		TEXTURE_MAP.put(Items.SADDLE.getDefaultStack(), Enchancement.id("container/slot/saddle"));
		TEXTURE_MAP.put(Items.ELYTRA.getDefaultStack(), Enchancement.id("container/slot/elytra"));
		TEXTURE_MAP.put(Items.FLINT_AND_STEEL.getDefaultStack(), Enchancement.id("container/slot/flint_and_steel"));
		TEXTURE_MAP.put(Items.SHEARS.getDefaultStack(), Enchancement.id("container/slot/shears"));
		TEXTURE_MAP.put(Items.BRUSH.getDefaultStack(), Enchancement.id("container/slot/brush"));
		TEXTURE_MAP.put(Items.COMPASS.getDefaultStack(), Enchancement.id("container/slot/compass"));
	}

	public static void clearIconCache() {
		ICON_CACHE.clear();
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		return width;
	}

	@Override
	public int getHeight(TextRenderer textRenderer) {
		return height;
	}

	public void cacheDimensions(TextRenderer textRenderer, int x, int y, DrawContext context, @Nullable TooltipPositioner positioner) {
		width = height = 0;
		int screenWidth = context.getScaledWindowWidth();
		// No need to sort the enchantments here as the dimensions will be the same regardless
		for (RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
			Text name = Enchantment.getName(enchantment, enchantments.getLevel(enchantment));
			width = Math.max(width, (getIcons(enchantment).isEmpty() ? 0 : 18) + textRenderer.getWidth(name));
			height += 18;
			List<Text> description = EnchantmentDescriptionsEvent.getDescription(name, enchantment);
			if (description != null) {
				for (Text text : description) {
					width = Math.max(width, textRenderer.getWidth(text));
					height += 9;
				}
			}
		}
		// Dimensions are calculated once again based on how the tooltip positioner shifts the tooltip
		if (positioner != null) {
			Vector2ic pos = positioner.getPosition(screenWidth, context.getScaledWindowHeight(), x, y, width, height);
			cacheDimensions(textRenderer, pos.x(), pos.y(), context, null);
		}
	}

	@Override
	public void drawText(DrawContext context, TextRenderer textRenderer, int x, int y) {
		int screenWidth = context.getScaledWindowWidth();
		for (RegistryEntry<Enchantment> enchantment : getSortedEnchantments()) {
			boolean hasIcon = renderIcons(enchantment, x, y, context);
			Text name = Enchantment.getName(enchantment, enchantments.getLevel(enchantment));
			context.drawText(textRenderer, name, x + (hasIcon ? 18 : 0), y + 5, -1, true);
			y += 18;
			List<Text> description = EnchantmentDescriptionsEvent.getDescription(name, enchantment);
			if (description != null) {
				for (Text text : description) {
					context.drawText(textRenderer, text, x, y, -1, true);
					y += 9;
				}
			}
		}
	}

	private List<RegistryEntry<Enchantment>> getSortedEnchantments() {
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>(this.enchantments.getEnchantments());
		enchantments.sort((e1, e2) -> {
			if (e1.getKey().isPresent()) {
				if (e2.getKey().isPresent()) {
					return e1.getKey().get().getValue().compareTo(e2.getKey().get().getValue());
				}
				return 1;
			}
			return 0;
		});
		return enchantments;
	}

	private static List<Identifier> getIcons(RegistryEntry<Enchantment> registryEntry) {
		return ICON_CACHE.computeIfAbsent(registryEntry, enchantment -> {
			Set<Identifier> icons = new HashSet<>();
			for (Map.Entry<ItemStack, Identifier> entry : TEXTURE_MAP.entrySet()) {
				// Skip icon if anvil enchanting is disabled and the only way for this item to be enchanted
				if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && !entry.getKey().isEnchantable()) {
					continue;
				}
				if (entry.getKey().canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)) {
					icons.add(entry.getValue());
				}
			}
			return List.copyOf(icons);
		});
	}

	private static boolean renderIcons(RegistryEntry<Enchantment> enchantment, int x, int y, DrawContext context) {
		List<Identifier> icons = getIcons(enchantment);
		if (icons.isEmpty()) {
			return false;
		}
		int size = icons.size();
		if (size == 1) {
			context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, icons.getFirst(), x, y, 16, 16);
			return true;
		}
		double time = Util.getMeasuringTimeMs() / 1500d;
		double i = MathHelper.floorMod(time, size);
		float alpha = (float) (Math.min(MathHelper.fractionalPart(i), 0.25) / 0.25);
		context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, icons.get((int) i), x, y, 16, 16, ColorHelper.withAlpha(alpha, -1));
		if (alpha < 1) {
			Identifier icon = i - 1 < 0 ? icons.getLast() : icons.get((int) (i - 1));
			context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, icon, x, y, 16, 16, ColorHelper.withAlpha(1 - alpha, -1));
		}
		return true;
	}
}
