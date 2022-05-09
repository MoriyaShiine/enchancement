/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.disarm;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PiglinBrain.class)
public interface PiglinBrainAccessor {
	@Invoker("onAttacked")
	static void enchancement$onAttacked(PiglinEntity piglin, LivingEntity attacker) {
		throw new UnsupportedOperationException();
	}
}
