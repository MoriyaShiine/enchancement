/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.accessor;

import net.minecraft.block.HoneyBlock;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HoneyBlock.class)
public interface HoneyBlockAccessor {
	@Invoker("addCollisionEffects")
	void enchancement$addCollisionEffects(World world, Entity entity);

	@Invoker("updateSlidingVelocity")
	void enchancement$updateSlidingVelocity(Entity entity);
}
