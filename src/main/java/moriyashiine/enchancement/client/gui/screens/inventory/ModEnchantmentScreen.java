/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.screens.inventory;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import moriyashiine.enchancement.common.world.inventory.ModEnchantmentMenu;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;

import static moriyashiine.enchancement.common.world.inventory.ModEnchantmentMenu.PAGE_SIZE;

public class ModEnchantmentScreen extends AbstractContainerScreen<ModEnchantmentMenu> {
	public static int bookshelfCount = 0;

	private static final Identifier ENCHANTING_TABLE_LOCATION = Enchancement.id("textures/gui/container/enchanting_table.png");
	private static final Identifier ENCHANTING_BOOK_LOCATION = Identifier.withDefaultNamespace("textures/entity/enchantment/enchanting_table_book.png");

	private static final Identifier UP_ARROW_LOCATION = Enchancement.id("container/enchanting_table/up_arrow");
	private static final Identifier UP_ARROW_HIGHLIGHTED_LOCATION = Enchancement.id("container/enchanting_table/up_arrow_highlighted");

	private static final Identifier DOWN_ARROW_LOCATION = Enchancement.id("container/enchanting_table/down_arrow");
	private static final Identifier DOWN_ARROW_HIGHLIGHTED_LOCATION = Enchancement.id("container/enchanting_table/down_arrow_highlighted");

	private static final Identifier CHECKMARK_LOCATION = Enchancement.id("container/enchanting_table/checkmark");
	private static final Identifier CHECKMARK_HIGHLIGHTED_LOCATION = Enchancement.id("container/enchanting_table/checkmark_highlighted");

	private static final Identifier STRENGTH_LOCATION = Enchancement.id("container/enchanting_table/strength");
	private static final Identifier STRENGTH_HIGHLIGHTED_LOCATION = Enchancement.id("container/enchanting_table/strength_highlighted");

	private static final int CHISELED_BOOKSHELF_Y = 48, BOOKSHELF_Y = 9, UP_Y = 34, DOWN_Y = UP_Y + 17, ENCHANT_Y = 72;

	private final RandomSource random = RandomSource.create();
	private BookModel bookModel;
	public float flip;
	public float oFlip;
	public float flipT;
	public float flipA;
	public float open;
	public float oOpen;
	private ItemStack last = ItemStack.EMPTY;

	private List<Component> infoTexts = null;

	private int highlightedEnchantmentIndex = -1;
	private int materialIndex = 0, materialIndexTicks = 0, chiseledTicks = 0;

	public boolean receivedPacket = false;

	public ModEnchantmentScreen(ModEnchantmentMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 182);
		titleLabelY -= 16;
		inventoryLabelY -= 16;
	}

	@Override
	protected void init() {
		super.init();
		bookModel = new BookModel(minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);
		extractTooltip(graphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int strength = 0;
		int left = (width - imageWidth) / 2;
		int top = (height - imageHeight) / 2 - 16;
		graphics.blit(RenderPipelines.GUI_TEXTURED, ENCHANTING_TABLE_LOCATION, left, top, 0, 0, imageWidth, imageHeight, 256, 256);
		extractBookshelfAmount(graphics, left, top);
		if (minecraft.player != null && menu.canEnchant(minecraft.player, true)) {
			strength = EnchancementUtil.hasWeakEnchantments(menu.getSlot(0).getItem()) ? 1 : 2;
			extractMain(graphics, mouseX, mouseY, left, top);
		}
		extractStrengthOrbs(graphics, a, left, top, strength);
		extractChiseledModeWarning(graphics, mouseX, mouseY, left, top);
		extractBook(graphics, left, top + 16);
		extractEnchantingMaterial(graphics, left, top);
	}

	@Override
	public void containerTick() {
		super.containerTick();
		minecraft.player.experienceDisplayStartTick = minecraft.player.tickCount;
		tickBook();

		if (menu.getEnchantingMaterial().size() > 1) {
			materialIndexTicks++;
			if (materialIndexTicks % 20 == 0) {
				materialIndex = (materialIndex + 1) % menu.getEnchantingMaterial().size();
			}
		} else {
			materialIndex = materialIndexTicks = 0;
		}
		chiseledTicks++;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int left = (width - imageWidth) / 2;
		int top = (height - imageHeight) / 2 - 16;
		if (menu.canEnchant(minecraft.player, minecraft.player.isCreative()) && isInEnchantButtonBounds(left, top, (int) event.x(), (int) event.y()) && !menu.selectedEnchantments.isEmpty() && menu.clickMenuButton(minecraft.player, 0)) {
			minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
			return true;
		}
		if (menu.validEnchantments.size() > PAGE_SIZE) {
			if (isInUpButtonBounds(left, top, (int) event.x(), (int) event.y()) && menu.clickMenuButton(minecraft.player, 1)) {
				minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 1);
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
				flip += 1;
				return true;
			}
			if (isInDownButtonBounds(left, top, (int) event.x(), (int) event.y()) && menu.clickMenuButton(minecraft.player, 2)) {
				minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 2);
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
				flip -= 1;
				return true;
			}
		}
		if (highlightedEnchantmentIndex >= 0 && menu.clickMenuButton(minecraft.player, highlightedEnchantmentIndex + PAGE_SIZE)) {
			minecraft.gameMode.handleInventoryButtonClick(menu.containerId, highlightedEnchantmentIndex + PAGE_SIZE);
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
			return true;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (menu.validEnchantments.size() > PAGE_SIZE) {
			int wheel = (scrollY > 0 ? -1 : 1);
			menu.updateViewIndex(scrollY > 0);
			minecraft.gameMode.handleInventoryButtonClick(menu.containerId, scrollY > 0 ? 1 : 2);
			flip += wheel;
			return true;
		}
		return super.mouseScrolled(x, y, scrollX, scrollY);
	}

	private void tickBook() {
		ItemStack current = menu.getSlot(0).getItem();
		if (!ItemStack.matches(current, last)) {
			last = current;
			do {
				flipT = flipT + (random.nextInt(4) - random.nextInt(4));
			} while (flip <= flipT + 1 && flip >= flipT - 1);
		}
		oFlip = flip;
		oOpen = open;
		if (ModEnchantmentMenu.isEnchantable(menu.getSlot(0).getItem())) {
			open += 0.2F;
		} else {
			open -= 0.2F;
		}
		open = Mth.clamp(open, 0, 1);
		float diff = Mth.clamp((flipT - flip) * 0.4F, -0.2F, 0.2F);
		flipA = flipA + (diff - flipA) * 0.9F;
		flip = flip + flipA;
	}

	private void extractBookshelfAmount(GuiGraphicsExtractor graphics, int left, int top) {
		graphics.item(Items.BOOKSHELF.getDefaultInstance(), left + 154, top + BOOKSHELF_Y);
		graphics.pose().pushMatrix();
		graphics.pose().scale(0.5F, 0.5F);
		String bookshelfCountText = bookshelfCount + "/" + 15;
		EnchancementClient.drawTooltipsImmediately = true;
		graphics.setTooltipForNextFrame(minecraft.font, Component.literal(bookshelfCountText), (left + 178) * 2, (top + BOOKSHELF_Y + 20) * 2);
		graphics.pose().scale(1, 1);
		graphics.pose().popMatrix();
	}

	private void extractMain(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int left, int top) {
		if (menu.validEnchantments.size() > PAGE_SIZE) {
			if (isInUpButtonBounds(left, top, mouseX, mouseY)) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UP_ARROW_HIGHLIGHTED_LOCATION, left + 154, top + UP_Y, 16, 16);
			} else {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UP_ARROW_LOCATION, left + 154, top + UP_Y, 16, 16);
			}
			if (isInDownButtonBounds(left, top, mouseX, mouseY)) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DOWN_ARROW_HIGHLIGHTED_LOCATION, left + 154, top + DOWN_Y, 16, 16);
			} else {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DOWN_ARROW_LOCATION, left + 154, top + DOWN_Y, 16, 16);
			}
		}
		if (isInEnchantButtonBounds(left, top, mouseX, mouseY)) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, CHECKMARK_HIGHLIGHTED_LOCATION, left + 154, top + ENCHANT_Y, 16, 16);
			if (infoTexts == null) {
				MutableComponent expCost = Component.translatable("tooltip." + Enchancement.MOD_ID + ".experience_level_cost", menu.getCost()).withStyle(ChatFormatting.GREEN);
				MutableComponent lapisCost = Component.translatable("tooltip." + Enchancement.MOD_ID + ".material_cost", menu.getCost(), Component.translatable(Items.LAPIS_LAZULI.getDescriptionId())).withStyle(ChatFormatting.GREEN);
				MutableComponent repairCost = null;
				if (!menu.getEnchantingMaterial().isEmpty()) {
					MutableComponent itemName = Component.translatable(menu.getEnchantingMaterial().get(materialIndex).value().getDescriptionId());
					repairCost = Component.translatable("tooltip." + Enchancement.MOD_ID + ".material_cost", menu.getCost(), itemName).withStyle(ChatFormatting.GREEN);
				}
				if (!minecraft.player.isCreative()) {
					if (minecraft.player.experienceLevel < menu.getCost()) {
						expCost.withStyle(ChatFormatting.RED);
					}
					if (menu.getSlot(1).getItem().getCount() < menu.getCost()) {
						lapisCost.withStyle(ChatFormatting.RED);
					}
					if (repairCost != null && menu.getSlot(2).getItem().getCount() < menu.getCost()) {
						repairCost.withStyle(ChatFormatting.RED);
					}
				}
				if (repairCost == null) {
					infoTexts = List.of(expCost, lapisCost);
				} else {
					infoTexts = List.of(expCost, lapisCost, repairCost);
				}
			}
			graphics.setComponentTooltipForNextFrame(font, infoTexts, mouseX, mouseY);
		} else {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, CHECKMARK_LOCATION, left + 154, top + ENCHANT_Y, 16, 16);
			infoTexts = null;
		}
		highlightedEnchantmentIndex = -1;
		for (int i = 0; i < menu.validEnchantments.size() && i < PAGE_SIZE; i++) {
			Holder<Enchantment> enchantment;
			if (menu.validEnchantments.size() <= PAGE_SIZE) {
				enchantment = menu.validEnchantments.get(i);
			} else {
				enchantment = menu.getEnchantmentFromViewIndex(i);
			}
			MutableComponent enchantmentName = enchantment.value().description().copy();
			ItemStack toEnchant = menu.getSlot(0).getItem();
			boolean isAllowed = EnchantmentHelper.isEnchantmentCompatible(menu.selectedEnchantments, enchantment) && !EnchancementUtil.exceedsLimit(toEnchant, toEnchant.getEnchantments().size() + menu.selectedEnchantments.size() + 1);
			enchantmentName = Component.literal(font.plainSubstrByWidth(enchantmentName.getString(), 80));
			graphics.text(font, menu.selectedEnchantments.contains(enchantment) ? enchantmentName.withStyle(ChatFormatting.DARK_GREEN) : isAllowed ? enchantmentName.withStyle(ChatFormatting.BLACK) : enchantmentName.withStyle(ChatFormatting.DARK_RED, ChatFormatting.STRIKETHROUGH), left + 66, top + 16 + (i * 19), 0xFFFFFFFF, false);
			if (isInBounds(left, top + 11 + (i * 19), mouseX, mouseY, 64, 67 + font.width(enchantmentName), 0, 16)) {
				if (isAllowed || menu.selectedEnchantments.contains(enchantment)) {
					highlightedEnchantmentIndex = i;
				}
				if (infoTexts == null) {
					MutableComponent tooltipName = enchantment.value().description().copy().withStyle(ChatFormatting.GRAY);
					MutableComponent tooltipDescription = Component.translatable(EnchancementUtil.getTranslationKey(enchantment) + ".desc").withStyle(ChatFormatting.DARK_GRAY);
					if (tooltipDescription.getString().isEmpty()) {
						infoTexts = List.of(tooltipName);
					} else {
						infoTexts = new ArrayList<>();
						infoTexts.add(tooltipName);
						infoTexts.addAll(SLibClientUtils.wrapText(Component.literal(" - ").withStyle(ChatFormatting.GRAY).append(tooltipDescription)));
					}
				}
				graphics.setComponentTooltipForNextFrame(font, infoTexts, mouseX, mouseY);
			} else {
				infoTexts = null;
			}
		}
	}

	private void extractStrengthOrbs(GuiGraphicsExtractor graphics, float a, int left, int top, int strength) {
		for (int i = 2; i > 0; i--) {
			int startX = left + 39 + Mth.lerpInt(Mth.lerp(a, oOpen, open), 0, 4);
			int startY = top + 41 - (i * 10);
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, STRENGTH_LOCATION, startX, startY, 8, 8);
			if (i <= strength) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, STRENGTH_HIGHLIGHTED_LOCATION, startX, startY, 8, 8);
			}
		}
	}

	private void extractChiseledModeWarning(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int left, int top) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && receivedPacket && menu.chiseledEnchantments.isEmpty()) {
			if (isInChiseledWarningBounds(left, top, mouseX, mouseY)) {
				graphics.setTooltipForNextFrame(font, Component.translatable("tooltip.enchancement.no_chiseled_enchantments").withStyle(ChatFormatting.RED), mouseX, mouseY);
			}
			float scale = 1 + Mth.sin(chiseledTicks / 8F) / 4;
			graphics.pose().pushMatrix();
			graphics.pose().translate(left + 104, top + CHISELED_BOOKSHELF_Y);
			graphics.pose().scale(scale, scale);
			graphics.pose().translate(-8, -8);
			graphics.item(Items.CHISELED_BOOKSHELF.getDefaultInstance(), 0, 0);
			graphics.pose().popMatrix();
		}
	}

	private void extractBook(GuiGraphicsExtractor graphics, int left, int top) {
		float a = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
		float open = Mth.lerp(a, oOpen, this.open);
		float flip = Mth.lerp(a, oFlip, this.flip);
		int x0 = left + 4;
		int y0 = top - 2;
		int x1 = x0 + 38;
		int y1 = y0 + 31;
		graphics.book(bookModel, ENCHANTING_BOOK_LOCATION, 40, open, flip, x0, y0, x1, y1);
	}

	private void extractEnchantingMaterial(GuiGraphicsExtractor context, int left, int top) {
		if (!menu.getEnchantingMaterial().isEmpty()) {
			context.item(menu.getEnchantingMaterial().get(Math.min(materialIndex, menu.getEnchantingMaterial().size() - 1)).value().getDefaultInstance(), (width - imageWidth) / 2 + 25, (height - imageHeight) / 2 + 51);
			// rendering the slot texture again after the item mimics transparency
			context.blit(RenderPipelines.GUI_TEXTURED, ENCHANTING_TABLE_LOCATION, left + 25, top + 67, 25, 67, 16, 16, 16, 16, 256, 256, 0x7FFFFFFF);
		}
	}

	private static boolean isInBounds(int left, int top, int mouseX, int mouseY, int startX, int endX, int startY, int endY) {
		return mouseX >= left + startX && mouseX <= left + endX && mouseY >= top + startY && mouseY <= top + endY;
	}

	private static boolean isInUpButtonBounds(int left, int top, int mouseX, int mouseY) {
		return isInBounds(left, top, mouseX, mouseY, 154, 170, UP_Y, UP_Y + 16);
	}

	private static boolean isInDownButtonBounds(int left, int top, int mouseX, int mouseY) {
		return isInBounds(left, top, mouseX, mouseY, 154, 170, DOWN_Y, DOWN_Y + 16);
	}

	private static boolean isInEnchantButtonBounds(int left, int top, int mouseX, int mouseY) {
		return isInBounds(left, top, mouseX, mouseY, 154, 170, ENCHANT_Y, ENCHANT_Y + 16);
	}

	private static boolean isInChiseledWarningBounds(int left, int top, int mouseX, int mouseY) {
		return isInBounds(left, top, mouseX, mouseY, 88, 120, CHISELED_BOOKSHELF_Y - 16, CHISELED_BOOKSHELF_Y + 16);
	}
}
