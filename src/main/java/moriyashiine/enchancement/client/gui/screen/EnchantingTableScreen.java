/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.OverhaulMode;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

import static moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler.PAGE_SIZE;

public class EnchantingTableScreen extends HandledScreen<EnchantingTableScreenHandler> {
	public static boolean forceTransparency = false;
	public static int bookshelfCount = 0;

	private static final Identifier TEXTURE = Enchancement.id("textures/gui/container/enchanting_table.png");
	private static final Identifier BOOK_TEXTURE = Identifier.of("textures/entity/enchanting_table_book.png");

	private static final Identifier UP_ARROW_TEXTURE = Enchancement.id("container/enchanting_table/up_arrow");
	private static final Identifier UP_ARROW_HIGHLIGHTED_TEXTURE = Enchancement.id("container/enchanting_table/up_arrow_highlighted");

	private static final Identifier DOWN_ARROW_TEXTURE = Enchancement.id("container/enchanting_table/down_arrow");
	private static final Identifier DOWN_ARROW_HIGHLIGHTED_TEXTURE = Enchancement.id("container/enchanting_table/down_arrow_highlighted");

	private static final Identifier CHECKMARK_TEXTURE = Enchancement.id("container/enchanting_table/checkmark");
	private static final Identifier CHECKMARK_HIGHLIGHTED_TEXTURE = Enchancement.id("container/enchanting_table/checkmark_highlighted");

	private static final Identifier STRENGTH_TEXTURE = Enchancement.id("container/enchanting_table/strength");
	private static final Identifier STRENGTH_HIGHLIGHTED_TEXTURE = Enchancement.id("container/enchanting_table/strength_highlighted");

	private static final int CHISELED_BOOKSHELF_Y = 48, BOOKSHELF_Y = 9, UP_Y = 34, DOWN_Y = UP_Y + 17, ENCHANT_Y = 72;

	private BookModel bookModel;

	private List<Text> infoTexts = null;
	private ItemStack stack = ItemStack.EMPTY;
	private float pageAngle;
	private float nextPageAngle;
	private float approximatePageAngle;
	private float pageTurningSpeed;
	private float nextPageTurningSpeed;
	private float pageRotationSpeed;

	private int highlightedEnchantmentIndex = -1;
	private int materialIndex = 0, materialIndexTicks = 0, chiseledTicks = 0;

	public boolean receivedPacket = false;

	public EnchantingTableScreen(EnchantingTableScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		backgroundHeight += 16;
		titleY -= 16;
	}

	@Override
	protected void init() {
		super.init();
		bookModel = new BookModel(client.getLoadedEntityModels().getModelPart(EntityModelLayers.BOOK));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int strength = 0;
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2 - 16;
		context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, posX, posY, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
		if (client != null && client.player != null && handler.canEnchant(client.player, true)) {
			strength = EnchancementUtil.hasWeakEnchantments(handler.getSlot(0).getStack()) ? 1 : 2;
			if (handler.validEnchantments.size() > PAGE_SIZE) {
				if (isInUpButtonBounds(posX, posY, mouseX, mouseY)) {
					context.drawGuiTexture(RenderLayer::getGuiTextured, UP_ARROW_HIGHLIGHTED_TEXTURE, posX + 154, posY + UP_Y, 16, 16);
				} else {
					context.drawGuiTexture(RenderLayer::getGuiTextured, UP_ARROW_TEXTURE, posX + 154, posY + UP_Y, 16, 16);
				}
				if (isInDownButtonBounds(posX, posY, mouseX, mouseY)) {
					context.drawGuiTexture(RenderLayer::getGuiTextured, DOWN_ARROW_HIGHLIGHTED_TEXTURE, posX + 154, posY + DOWN_Y, 16, 16);
				} else {
					context.drawGuiTexture(RenderLayer::getGuiTextured, DOWN_ARROW_TEXTURE, posX + 154, posY + DOWN_Y, 16, 16);
				}
			}
			if (isInEnchantButtonBounds(posX, posY, mouseX, mouseY)) {
				context.drawGuiTexture(RenderLayer::getGuiTextured, CHECKMARK_HIGHLIGHTED_TEXTURE, posX + 154, posY + ENCHANT_Y, 16, 16);
				if (infoTexts == null) {
					MutableText xpCost = Text.translatable("tooltip." + Enchancement.MOD_ID + ".experience_level_cost", handler.getCost()).formatted(Formatting.GREEN);
					MutableText lapisCost = Text.translatable("tooltip." + Enchancement.MOD_ID + ".material_cost", handler.getCost(), Text.translatable(Items.LAPIS_LAZULI.getTranslationKey())).formatted(Formatting.GREEN);
					MutableText repairCost = null;
					if (!handler.getEnchantingMaterial().isEmpty()) {
						MutableText itemName = Text.translatable(handler.getEnchantingMaterial().get(materialIndex).value().getTranslationKey());
						repairCost = Text.translatable("tooltip." + Enchancement.MOD_ID + ".material_cost", handler.getCost(), itemName).formatted(Formatting.GREEN);
					}
					if (!client.player.isCreative()) {
						if (client.player.experienceLevel < handler.getCost()) {
							xpCost.formatted(Formatting.RED);
						}
						if (handler.getSlot(1).getStack().getCount() < handler.getCost()) {
							lapisCost.formatted(Formatting.RED);
						}
						if (repairCost != null && handler.getSlot(2).getStack().getCount() < handler.getCost()) {
							repairCost.formatted(Formatting.RED);
						}
					}
					if (repairCost == null) {
						infoTexts = List.of(xpCost, lapisCost);
					} else {
						infoTexts = List.of(xpCost, lapisCost, repairCost);
					}
				}
				context.drawTooltip(textRenderer, infoTexts, mouseX, mouseY);
			} else {
				context.drawGuiTexture(RenderLayer::getGuiTextured, CHECKMARK_TEXTURE, posX + 154, posY + ENCHANT_Y, 16, 16);
				infoTexts = null;
			}
			highlightedEnchantmentIndex = -1;
			for (int i = 0; i < handler.validEnchantments.size() && i < PAGE_SIZE; i++) {
				RegistryEntry<Enchantment> enchantment;
				if (handler.validEnchantments.size() <= PAGE_SIZE) {
					enchantment = handler.validEnchantments.get(i);
				} else {
					enchantment = handler.getEnchantmentFromViewIndex(i);
				}
				MutableText enchantmentName = enchantment.value().description().copy();
				ItemStack stack = handler.getSlot(0).getStack();
				boolean isAllowed = EnchantmentHelper.isCompatible(handler.selectedEnchantments, enchantment) && !EnchancementUtil.exceedsLimit(stack, stack.getEnchantments().getSize() + handler.selectedEnchantments.size() + 1);
				enchantmentName = Text.literal(textRenderer.trimToWidth(enchantmentName.getString(), 80));
				context.drawText(textRenderer, handler.selectedEnchantments.contains(enchantment) ? enchantmentName.formatted(Formatting.DARK_GREEN) : isAllowed ? enchantmentName.formatted(Formatting.BLACK) : enchantmentName.formatted(Formatting.DARK_RED, Formatting.STRIKETHROUGH), posX + 66, posY + 16 + (i * 19), 0xFFFFFF, false);
				if (isInBounds(posX, posY + 11 + (i * 19), mouseX, mouseY, 64, 67 + textRenderer.getWidth(enchantmentName), 0, 16)) {
					if (isAllowed || handler.selectedEnchantments.contains(enchantment)) {
						highlightedEnchantmentIndex = i;
					}
					if (infoTexts == null) {
						MutableText name = enchantment.value().description().copy().formatted(Formatting.GRAY);
						MutableText description = Text.translatable(EnchancementUtil.getTranslationKey(enchantment) + ".desc").formatted(Formatting.DARK_GRAY);
						infoTexts = description.getString().isEmpty() ? List.of(name) : List.of(name, Text.literal(" - ").formatted(Formatting.GRAY).append(description));
					}
					context.drawTooltip(textRenderer, infoTexts, mouseX, mouseY);
				} else {
					infoTexts = null;
				}
			}
		}
		for (int i = 2; i > 0; i--) {
			int startX = posX + 39 + MathHelper.lerp(MathHelper.lerp(delta, pageTurningSpeed, nextPageTurningSpeed), 0, 4);
			int startY = posY + 41 - (i * 10);
			context.drawGuiTexture(RenderLayer::getGuiTextured, STRENGTH_TEXTURE, startX, startY, 8, 8);
			if (i <= strength) {
				context.drawGuiTexture(RenderLayer::getGuiTextured, STRENGTH_HIGHLIGHTED_TEXTURE, startX, startY, 8, 8);
			}
		}
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && receivedPacket && handler.chiseledEnchantments.isEmpty()) {
			if (isInChiseledWarningBounds(posX, posY, mouseX, mouseY)) {
				context.drawTooltip(textRenderer, Text.translatable("tooltip.enchancement.no_chiseled_enchantments").formatted(Formatting.RED), mouseX, mouseY);
			}
			float scale = 1 + MathHelper.sin(chiseledTicks / 8F) / 4;
			context.getMatrices().push();
			context.getMatrices().translate(posX + 104, posY + CHISELED_BOOKSHELF_Y, 0);
			context.getMatrices().scale(scale, scale, scale);
			context.getMatrices().translate(-8, -8, 0);
			context.drawItem(Items.CHISELED_BOOKSHELF.getDefaultStack(), 0, 0);
			context.getMatrices().pop();
		}
		context.drawItem(Items.BOOKSHELF.getDefaultStack(), posX + 154, posY + BOOKSHELF_Y);
		context.getMatrices().push();
		context.getMatrices().scale(0.5F, 0.5F, 0.5F);
		String bookshelfCountText = bookshelfCount + "/" + 15;
		context.drawTooltip(client.textRenderer, Text.literal(bookshelfCountText), (posX + 178) * 2, (posY + BOOKSHELF_Y + 20) * 2);
		context.getMatrices().scale(1, 1, 1);
		context.getMatrices().pop();
		drawBook(context, (width - backgroundWidth) / 2, (height - backgroundHeight) / 2, client.getRenderTickCounter().getTickProgress(true));
		if (!handler.getEnchantingMaterial().isEmpty()) {
			forceTransparency = true;
			RenderSystem.setShaderColor(1, 1, 1, 0.5F);
			context.drawItem(handler.getEnchantingMaterial().get(Math.min(materialIndex, handler.getEnchantingMaterial().size() - 1)).value().getDefaultStack(), (width - backgroundWidth) / 2 + 25, (height - backgroundHeight) / 2 + 51);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			forceTransparency = false;
		}
	}

	@Override
	protected void handledScreenTick() {
		ItemStack stack = handler.getSlot(0).getStack();
		if (!ItemStack.areEqual(stack, this.stack)) {
			this.stack = stack;
			while (nextPageAngle <= approximatePageAngle + 1 && nextPageAngle >= approximatePageAngle - 1) {
				approximatePageAngle += (client.player.getRandom().nextInt(4) - client.player.getRandom().nextInt(4));
			}
		}
		pageAngle = nextPageAngle;
		pageTurningSpeed = nextPageTurningSpeed;
		nextPageTurningSpeed = handler.slots.getFirst().getStack().isEnchantable() ? nextPageTurningSpeed + 0.2F : nextPageTurningSpeed - 0.2F;
		nextPageTurningSpeed = MathHelper.clamp(nextPageTurningSpeed, 0, 1);
		pageRotationSpeed += (MathHelper.clamp((approximatePageAngle - nextPageAngle) * 0.4F, -0.2F, 0.2F) - pageRotationSpeed) * 0.9F;
		nextPageAngle += pageRotationSpeed;

		if (handler.getEnchantingMaterial().size() > 1) {
			materialIndexTicks++;
			if (materialIndexTicks % 20 == 0) {
				materialIndex = (materialIndex + 1) % handler.getEnchantingMaterial().size();
			}
		} else {
			materialIndex = materialIndexTicks = 0;
		}
		chiseledTicks++;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2 - 16;
		if (handler.canEnchant(client.player, client.player.isCreative()) && isInEnchantButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && !handler.selectedEnchantments.isEmpty() && handler.onButtonClick(client.player, 0)) {
			client.interactionManager.clickButton(handler.syncId, 0);
			return true;
		}
		if (handler.validEnchantments.size() > PAGE_SIZE) {
			if (isInUpButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && handler.onButtonClick(client.player, 1)) {
				client.interactionManager.clickButton(handler.syncId, 1);
				client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
				nextPageAngle += 1;
				return true;
			}
			if (isInDownButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && handler.onButtonClick(client.player, 2)) {
				client.interactionManager.clickButton(handler.syncId, 2);
				client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
				nextPageAngle -= 1;
				return true;
			}
		}
		if (highlightedEnchantmentIndex >= 0 && handler.onButtonClick(client.player, highlightedEnchantmentIndex + PAGE_SIZE)) {
			client.interactionManager.clickButton(handler.syncId, highlightedEnchantmentIndex + PAGE_SIZE);
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (handler.validEnchantments.size() > PAGE_SIZE) {
			int delta = (verticalAmount > 0 ? -1 : 1);
			handler.updateViewIndex(verticalAmount > 0);
			client.interactionManager.clickButton(handler.syncId, verticalAmount > 0 ? 1 : 2);
			nextPageAngle += delta;
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	private void drawBook(DrawContext context, int x, int y, float delta) {
		float deltaTurningSpeed = MathHelper.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
		float deltaPageangle = MathHelper.lerp(delta, this.pageAngle, this.nextPageAngle);
		DiffuseLighting.enableGuiShaderLighting();
		context.getMatrices().push();
		context.getMatrices().translate(x + 23, y + 15, 100);
		context.getMatrices().scale(-40, 40, 40);
		context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(25));
		context.getMatrices().translate((1 - deltaTurningSpeed) * 0.2F, (1 - deltaTurningSpeed) * 0.1F, (1 - deltaTurningSpeed) * 0.25F);
		context.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-(1 - deltaTurningSpeed) * 90 - 90));
		context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		bookModel.setPageAngles(0, MathHelper.clamp(MathHelper.fractionalPart(deltaPageangle + 0.25F) * 1.6F - 0.3F, 0, 1), MathHelper.clamp(MathHelper.fractionalPart(deltaPageangle + 0.75F) * 1.6F - 0.3F, 0, 1), deltaTurningSpeed);
		context.draw(vertexConsumers -> {
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(bookModel.getLayer(BOOK_TEXTURE));
			bookModel.render(context.getMatrices(), vertexConsumer, 0xF000F0, OverlayTexture.DEFAULT_UV);
		});
		context.draw();
		context.getMatrices().pop();
		DiffuseLighting.enableGuiDepthLighting();
	}

	private static boolean isInBounds(int posX, int posY, int mouseX, int mouseY, int startX, int endX, int startY, int endY) {
		return mouseX >= posX + startX && mouseX <= posX + endX && mouseY >= posY + startY && mouseY <= posY + endY;
	}

	private static boolean isInUpButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, UP_Y, UP_Y + 16);
	}

	private static boolean isInDownButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, DOWN_Y, DOWN_Y + 16);
	}

	private static boolean isInEnchantButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, ENCHANT_Y, ENCHANT_Y + 16);
	}

	private static boolean isInChiseledWarningBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 88, 120, CHISELED_BOOKSHELF_Y - 16, CHISELED_BOOKSHELF_Y + 16);
	}
}
