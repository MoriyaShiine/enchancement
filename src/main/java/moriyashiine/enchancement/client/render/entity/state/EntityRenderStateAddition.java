/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity.state;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;

public interface EntityRenderStateAddition {
	ItemStack enchancement$getActiveStack();

	void enchancement$setActiveStack(ItemStack stack);

	Random enchancement$getRandom();

	void enchancement$setRandom(Random random);

	boolean enchancement$canCameraSee();

	void enchancement$setCanCameraSee(boolean canCameraSee);

	boolean enchancement$isGlowing();

	void enchancement$setGlowing(boolean glowing);

	boolean enchancement$hidesLabels();

	void enchancement$setHidesLabels(boolean hidesLabels);
}
