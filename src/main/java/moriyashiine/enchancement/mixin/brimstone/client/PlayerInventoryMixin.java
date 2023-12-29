/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone.client;

import moriyashiine.enchancement.client.packet.StopBrimstoneSoundsS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
	@Shadow
	@Final
	public DefaultedList<ItemStack> main;

	@Shadow
	@Final
	public PlayerEntity player;

	@Shadow
	public int selectedSlot;

	@Inject(method = "scrollInHotbar", at = @At("HEAD"))
	private void enchancement$brimstone(double scrollAmount, CallbackInfo ci) {
		StopBrimstoneSoundsS2CPacket.maybeStopSounds(player, main.get(selectedSlot));
	}
}
