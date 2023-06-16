/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class EnchantingTableScreen extends HandledScreen<EnchantingTableScreenHandler> {
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

	public EnchantingTableScreen(EnchantingTableScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
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
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2;
		RenderSystem.setShaderTexture(0, TEXTURE);
		context.drawTexture(TEXTURE, posX, posY, 0, 0, backgroundWidth, backgroundHeight);
		if (client != null && client.player != null && handler.canEnchant(client.player, true)) {
			if (handler.validEnchantments.size() > 3) {
				if (isInUpButtonBounds(posX, posY, mouseX, mouseY)) {
					context.drawTexture(TEXTURE, posX + 154, posY + 12, 192, 0, 16, 16);
				} else {
					context.drawTexture(TEXTURE, posX + 154, posY + 12, 176, 0, 16, 16);
				}
				if (isInDownButtonBounds(posX, posY, mouseX, mouseY)) {
					context.drawTexture(TEXTURE, posX + 154, posY + 29, 192, 16, 16, 16);
				} else {
					context.drawTexture(TEXTURE, posX + 154, posY + 29, 176, 16, 16, 16);
				}
			}
			if (isInEnchantButtonBounds(posX, posY, mouseX, mouseY)) {
				context.drawTexture(TEXTURE, posX + 154, posY + 50, 192, 32, 16, 16);
				if (infoTexts == null) {
					infoTexts = List.of(Text.translatable("tooltip." + Enchancement.MOD_ID + ".experience_level_cost", handler.getExperienceLevelCost()).formatted(Formatting.DARK_GREEN), Text.translatable("tooltip." + Enchancement.MOD_ID + ".lapis_lazuli_cost", handler.getLapisLazuliCost()).formatted(Formatting.BLUE));
				}
				context.drawTooltip(client.textRenderer, infoTexts, mouseX, mouseY);
			} else {
				context.drawTexture(TEXTURE, posX + 154, posY + 50, 176, 32, 16, 16);
				infoTexts = null;
			}
			highlightedEnchantmentIndex = -1;
			for (int i = 0; i < handler.validEnchantments.size() && i < 3; i++) {
				Enchantment enchantment;
				if (handler.validEnchantments.size() <= 3) {
					enchantment = handler.validEnchantments.get(i);
				} else {
					enchantment = handler.getEnchantmentFromViewIndex(i);
				}
				MutableText enchantmentName = Text.translatable(enchantment.getTranslationKey());
				boolean isAllowed = true;
				for (Enchantment foundEnchantment : handler.selectedEnchantments) {
					if (!foundEnchantment.canCombine(enchantment)) {
						isAllowed = false;
						break;
					}
				}
				context.drawText(textRenderer, handler.selectedEnchantments.contains(enchantment) ? enchantmentName.formatted(Formatting.DARK_GREEN) : isAllowed ? enchantmentName.formatted(Formatting.BLACK) : enchantmentName.formatted(Formatting.DARK_RED, Formatting.STRIKETHROUGH), posX + 66, posY + 16 + (i * 19), 0xFFFFFF, false);
				if (isInBounds(posX, posY + 16 + (i * 19), mouseX, mouseY, 64, 67 + textRenderer.getWidth(enchantmentName), 0, 8)) {
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
		renderBook(context.getMatrices(), client.getTickDelta());
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
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int posX = (width - backgroundWidth) / 2;
		int posY = (height - backgroundHeight) / 2;
		if (handler.canEnchant(client.player, client.player.isCreative()) && isInEnchantButtonBounds(posX, posY, (int) mouseX, (int) mouseY) && !handler.selectedEnchantments.isEmpty() && handler.onButtonClick(client.player, 0)) {
			client.interactionManager.clickButton(handler.syncId, 0);
			return true;
		}
		if (handler.validEnchantments.size() > 3) {
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
		if (highlightedEnchantmentIndex >= 0 && handler.onButtonClick(client.player, highlightedEnchantmentIndex + 3)) {
			client.interactionManager.clickButton(handler.syncId, highlightedEnchantmentIndex + 3);
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (handler.validEnchantments.size() > 3) {
			int delta = (amount > 0 ? -1 : 1);
			handler.updateViewIndex(amount > 0);
			client.interactionManager.clickButton(handler.syncId, amount > 0 ? 1 : 2);
			nextPageAngle += delta;
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	private void renderBook(MatrixStack matrices, float delta) {
		int scaleFactor = (int) client.getWindow().getScaleFactor();
		float deltaTurningSpeed = MathHelper.lerp(delta, pageTurningSpeed, nextPageTurningSpeed);
		float leftFlipAmount = MathHelper.lerp(delta, pageAngle, nextPageAngle) + 0.25F;
		float rightFlipAmount = MathHelper.lerp(delta, pageAngle, nextPageAngle) + 0.75F;
		DiffuseLighting.disableGuiDepthLighting();
		RenderSystem.backupProjectionMatrix();
		RenderSystem.viewport((width - 320) / 2 * scaleFactor, (height - 240) / 2 * scaleFactor, 320 * scaleFactor, 240 * scaleFactor);
		Matrix4f matrix4f = new Matrix4f().translation(-0.34F, 0.23F, 0).perspective((float) Math.toRadians(90), 4 / 3F, 9, 80);
		RenderSystem.setProjectionMatrix(matrix4f, null);
		matrices.push();
		matrices.translate(0.0, 3.3F, 1984);
		matrices.scale(5, 5, 5);
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(20));
		matrices.translate((1 - deltaTurningSpeed) * 0.2F, (1 - deltaTurningSpeed) * 0.1F, (1 - deltaTurningSpeed) * 0.25F);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-(1 - deltaTurningSpeed) * 90 - 90));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		bookModel.setPageAngles(0, MathHelper.clamp((leftFlipAmount - (float) MathHelper.floor(leftFlipAmount)) * 1.6F - 0.3F, 0, 1), MathHelper.clamp((rightFlipAmount - (float) MathHelper.floor(rightFlipAmount)) * 1.6F - 0.3F, 0, 1), deltaTurningSpeed);
		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		bookModel.render(matrices, immediate.getBuffer(bookModel.getLayer(BOOK_TEXTURE)), 0xF000F0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		immediate.draw();
		matrices.pop();
		RenderSystem.viewport(0, 0, client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
		RenderSystem.restoreProjectionMatrix();
		DiffuseLighting.enableGuiDepthLighting();
	}

	private static boolean isInBounds(int posX, int posY, int mouseX, int mouseY, int startX, int endX, int startY, int endY) {
		return mouseX >= posX + startX && mouseX <= posX + endX && mouseY >= posY + startY && mouseY <= posY + endY;
	}

	private static boolean isInUpButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, 12, 28);
	}

	private static boolean isInDownButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, 29, 45);
	}

	private static boolean isInEnchantButtonBounds(int posX, int posY, int mouseX, int mouseY) {
		return isInBounds(posX, posY, mouseX, mouseY, 154, 170, 50, 66);
	}
}
