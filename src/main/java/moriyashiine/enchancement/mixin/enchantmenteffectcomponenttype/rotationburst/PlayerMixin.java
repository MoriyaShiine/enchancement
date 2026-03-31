/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rotationburst;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
	@Unique
	private final List<ItemStack> lastArmorSet = new ArrayList<>();

	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$rotationBurst(CallbackInfo ci) {
		if (!lastArmorSet.isEmpty()) {
			int index = 0, difference = 0;
			for (ItemStack newStack : EnchancementUtil.getArmorItems(this)) {
				if (!ItemStack.matches(lastArmorSet.get(index), newStack)) {
					difference++;
				}
				index++;
			}
			if (difference != 0) {
				ModEntityComponents.ROTATION_BURST.get(this).markDelay();
			}
		}
		lastArmorSet.clear();
		lastArmorSet.addAll(EnchancementUtil.getArmorItems(this));
	}
}
