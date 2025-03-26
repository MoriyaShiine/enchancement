/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide.client;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
	@ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/PlayerInput;<init>(ZZZZZZZ)V"))
	private void enchancement$slide(Args args) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.get(player);
			if (slideComponent.isSliding()) {
				if (slideComponent.getVelocity().x() != 0) {
					args.set(0, false);
					args.set(1, false);
				}
				if (slideComponent.getVelocity().z() != 0) {
					args.set(2, false);
					args.set(3, false);
				}
			}
		}
	}
}
