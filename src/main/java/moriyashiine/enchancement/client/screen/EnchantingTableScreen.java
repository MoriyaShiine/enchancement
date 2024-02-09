/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.screen;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

public class EnchantingTableScreen extends HandledScreen<EnchantingTableScreenHandler> {
	public static boolean forceTransparency = false;

	private static final Identifier TEXTURE = Enchancement.id("textures/gui/container/enchanting_table.png");
	private static final Identifier BOOK_TEXTURE = new Identifier("textures/entity/enchanting_table_book.png");

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
	private int ingredientIndex = 0, ingredientIndexTicks = 0;

	public EnchantingTableScreen(EnchantingTableScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		backgroundHeight += 16;
		titleY -= 16;
	}

	@Override
	protected void init() {
		super.init();
		bookModel = new BookModel(client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int strength = 0;
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2 - 16;
		context.drawTexture(TEXTURE, posX, posY, 0, 0, backgroundWidth, backgroundHeight);
		if (client != null && client.player != null && handler.canEnchant(client.player, true)) {
			strength = EnchancementUtil.hasWeakEnchantments(handler.getSlot(0).getStack()) ? 1 : 2;
			if (!handler.getRepairIngredient().isEmpty()) {
				forceTransparency = true;
				context.setShaderColor(1, 1, 1, 0.5F);
				context.drawItem(handler.getRepairIngredient().getMatchingStacks()[ingredientIndex], (width - backgroundWidth) / 2 + 25, (height - backgroundHeight) / 2 + 51);
				context.setShaderColor(1, 1, 1, 1);
				forceTransparency = false;
			}
			if (handler.validEnchantments.size() > 4) {
				if (isInUpButtonBounds(posX, posY, mouseX, mouseY)) {
					context.drawTexture(TEXTURE, posX + 154, posY + 28, 192, 0, 16, 16);
				} else {
					context.drawTexture(TEXTURE, posX + 154, posY + 28, 176, 0, 16, 16);
				}
				if (isInDownButtonBounds(posX, posY, mouseX, mouseY)) {
					context.drawTexture(TEXTURE, posX + 154, posY + 45, 192, 16, 16, 16);
				} else {
					context.drawTexture(TEXTURE, posX + 154, posY + 45, 176, 16, 16, 16);
				}
			}
			if (isInEnchantButtonBounds(posX, posY, mouseX, mouseY)) {
				context.drawTexture(TEXTURE, posX + 154, posY + 66, 192, 32, 16, 16);
				if (infoTexts == null) {
					MutableText xpCost = Text.translatable("tooltip." + Enchancement.MOD_ID + ".experience_level_cost", handler.getCost()).formatted(Formatting.GREEN);
					MutableText lapisCost = Text.translatable("tooltip." + Enchancement.MOD_ID + ".material_cost", handler.getCost(), Text.translatable(Items.LAPIS_LAZULI.getTranslationKey())).formatted(Formatting.GREEN);
					MutableText repairCost = null;
					if (!handler.getRepairIngredient().isEmpty()) {
						Item currentItem = handler.getRepairIngredient().getMatchingStacks()[ingredientIndex].getItem();
						repairCost = Text.translatable("tooltip." + Enchancement.MOD_ID + ".material_cost", handler.getCost(), Text.translatable(currentItem.getTranslationKey())).formatted(Formatting.GREEN);
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
				context.drawTexture(TEXTURE, posX + 154, posY + 66, 176, 32, 16, 16);
				infoTexts = null;
			}
			highlightedEnchantmentIndex = -1;
			for (int i = 0; i < handler.validEnchantments.size() && i < 4; i++) {
				Enchantment enchantment;
				if (handler.validEnchantments.size() <= 4) {
					enchantment = handler.validEnchantments.get(i);
				} else {
					enchantment = handler.getEnchantmentFromViewIndex(i);
				}
				MutableText enchantmentName = Text.translatable(enchantment.getTranslationKey());
				boolean isAllowed = EnchancementUtil.limitCheck(true, handler.selectedEnchantments.size() < ModConfig.enchantmentLimit);
				for (Enchantment foundEnchantment : handler.selectedEnchantments) {
					if (!foundEnchantment.canCombine(enchantment)) {
						isAllowed = false;
						break;
					}
				}
				enchantmentName = Text.literal(textRenderer.trimToWidth(enchantmentName.getString(), 80));
				context.drawText(textRenderer, handler.selectedEnchantments.contains(enchantment) ? enchantmentName.formatted(Formatting.DARK_GREEN) : isAllowed ? enchantmentName.formatted(Formatting.BLACK) : enchantmentName.formatted(Formatting.DARK_RED, Formatting.STRIKETHROUGH), posX + 66, posY + 16 + (i * 19), 0xFFFFFF, false);
				if (isInBounds(posX, posY + 11 + (i * 19), mouseX, mouseY, 64, 67 + textRenderer.getWidth(enchantmentName), 0, 16)) {
					if (isAllowed || handler.selectedEnchantments.contains(enchantment)) {
						highlightedEnchantmentIndex = i;
					}
					if (infoTexts == null) {
						infoTexts = List.of(Text.translatable(enchantment.getTranslationKey()).formatted(Formatting.GRAY), Text.translatable(enchantment.getTranslationKey() + ".desc").formatted(Formatting.DARK_GRAY));
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
			context.drawTexture(TEXTURE, startX, startY, 176, 48, 8, 8);
			if (i <= strength) {
				context.drawTexture(TEXTURE, startX, startY, 184, 48, 8, 8);
			}
		}
		drawBook(context, (width - backgroundWidth) / 2, (height - backgroundHeight) / 2, client.getTickDelta());
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
		nextPageTurningSpeed = handler.slots.get(0).getStack().isEnchantable() ? nextPageTurningSpeed + 0.2F : nextPageTurningSpeed - 0.2F;
		nextPageTurningSpeed = MathHelper.clamp(nextPageTurningSpeed, 0, 1);
		pageRotationSpeed += (MathHelper.clamp((approximatePageAngle - nextPageAngle) * 0.4F, -0.2F, 0.2F) - pageRotationSpeed) * 0.9F;
		nextPageAngle += pageRotationSpeed;

		if (handler.getRepairIngredient().getMatchingStacks().length > 1) {
			ingredientIndexTicks++;
			if (ingredientIndexTicks % 20 == 0) {
				ingredientIndex = (ingredientIndex + 1) % handler.getRepairIngredient().getMatchingStacks().length;
			}
		} else {
			ingredientIndex = ingredientIndexTicks = 0;
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2 - 16;
		if (handler.canEnchant(client.player, client.player.isCreative()) && isInEnchantButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && !handler.selectedEnchantments.isEmpty() && handler.onButtonClick(client.player, 0)) {
			client.interactionManager.clickButton(handler.syncId, 0);
			return true;
		}
		if (handler.validEnchantments.size() > 4) {
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
		if (highlightedEnchantmentIndex >= 0 && handler.onButtonClick(client.player, highlightedEnchantmentIndex + 4)) {
			client.interactionManager.clickButton(handler.syncId, highlightedEnchantmentIndex + 4);
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (handler.validEnchantments.size() > 4) {
			int delta = (amount > 0 ? -1 : 1);
			handler.updateViewIndex(amount > 0);
			client.interactionManager.clickButton(handler.syncId, amount > 0 ? 1 : 2);
			nextPageAngle += delta;
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	private void drawBook(DrawContext context, int x, int y, float delta) {
		float deltaTurningSpeed = MathHelper.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
		float deltaPageangle = MathHelper.lerp(delta, this.pageAngle, this.nextPageAngle);
		DiffuseLighting.method_34742();
		context.getMatrices().push();
		context.getMatrices().translate(x + 23, y + 15, 100);
		context.getMatrices().scale(-40, 40, 40);
		context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(25));
		context.getMatrices().translate((1 - deltaTurningSpeed) * 0.2F, (1 - deltaTurningSpeed) * 0.1F, (1 - deltaTurningSpeed) * 0.25F);
		context.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-(1 - deltaTurningSpeed) * 90 - 90));
		context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		bookModel.setPageAngles(0, MathHelper.clamp(MathHelper.fractionalPart(deltaPageangle + 0.25F) * 1.6F - 0.3F, 0, 1), MathHelper.clamp(MathHelper.fractionalPart(deltaPageangle + 0.75F) * 1.6F - 0.3F, 0, 1), deltaTurningSpeed);
		VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(bookModel.getLayer(BOOK_TEXTURE));
		bookModel.render(context.getMatrices(), vertexConsumer, 0xF000F0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		context.draw();
		context.getMatrices().pop();
		DiffuseLighting.enableGuiDepthLighting();
	}

	private static boolean isInBounds(int posX, int posY, int mouseX, int mouseY, int startX, int endX, int startY, int endY) {
		return mouseX >= posX + startX && mouseX <= posX + endX && mouseY >= posY + startY && mouseY <= posY + endY;
	}

	private static boolean isInUpButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, 28, 44);
	}

	private static boolean isInDownButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, 45, 61);
	}

	private static boolean isInEnchantButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, 66, 82);
	}
}
