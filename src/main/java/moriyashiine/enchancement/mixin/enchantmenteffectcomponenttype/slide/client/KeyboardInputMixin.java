/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide.client;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends ClientInput {
	@ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Input;<init>(ZZZZZZZ)V"))
	private void enchancement$slide(Args args) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(player);
			if (slideComponent.isSliding()) {
				if (slideComponent.getDelta().x() != 0) {
					args.set(0, false);
					args.set(1, false);
				}
				if (slideComponent.getDelta().z() != 0) {
					args.set(2, false);
					args.set(3, false);
				}
			}
		}
	}
}
