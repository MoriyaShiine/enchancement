/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.client.render.entity.state.EntityRenderStateAddition;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAddition {
	@Unique
	private ItemStack activeStack = ItemStack.EMPTY;
	@Unique
	private Random random = null;
	@Unique
	private boolean canCameraSee = false, isGlowing = false, hidesLabels = false;

	@Override
	public ItemStack enchancement$getActiveStack() {
		return activeStack;
	}

	@Override
	public void enchancement$setActiveStack(ItemStack stack) {
		this.activeStack = stack;
	}

	@Override
	public Random enchancement$getRandom() {
		return random;
	}

	@Override
	public void enchancement$setRandom(Random random) {
		this.random = random;
	}

	@Override
	public boolean enchancement$canCameraSee() {
		return canCameraSee;
	}

	@Override
	public void enchancement$setCanCameraSee(boolean canCameraSee) {
		this.canCameraSee = canCameraSee;
	}

	@Override
	public boolean enchancement$isGlowing() {
		return isGlowing;
	}

	@Override
	public void enchancement$setGlowing(boolean glowing) {
		this.isGlowing = glowing;
	}

	@Override
	public boolean enchancement$hidesLabels() {
		return hidesLabels;
	}

	@Override
	public void enchancement$setHidesLabels(boolean hidesLabels) {
		this.hidesLabels = hidesLabels;
	}
}
