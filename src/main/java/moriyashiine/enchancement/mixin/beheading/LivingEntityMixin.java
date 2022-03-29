package moriyashiine.enchancement.mixin.beheading;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.BeheadingEntry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "dropLoot", at = @At("HEAD"))
	private void enchancement$beheading(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if (source.getSource() instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(ModEnchantments.BEHEADING, living) > 0) {
			int looting = EnchantmentHelper.getLooting(living);
			for (EntityType<?> entityType : BeheadingEntry.DROP_MAP.keySet()) {
				if (getType() == entityType) {
					BeheadingEntry entry = BeheadingEntry.DROP_MAP.get(entityType);
					if (random.nextFloat() < entry.chance + (looting * 0.15F)) {
						ItemStack stack = new ItemStack(entry.drop);
						if (stack.getItem() == Items.PLAYER_HEAD && getType() == EntityType.PLAYER) {
							stack.getOrCreateNbt().putString("SkullOwner", getName().getString());
						}
						ItemScatterer.spawn(world, getX(), getY(), getZ(), stack);
					}
				}
			}
		}
	}
}
