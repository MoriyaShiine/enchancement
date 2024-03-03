/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.chaos;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileEntityAccessor {
	@Invoker("asItemStack")
	ItemStack enchancement$asItemStack();

	@Accessor("inGround")
	boolean enchancement$inGround();

	@Accessor("inGroundTime")
	int enchancement$inGroundTime();
}
