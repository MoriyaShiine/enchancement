package moriyashiine.enchancement.common.world.entity.projectile.arrow;

import moriyashiine.enchancement.common.init.EnchancementDamageTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityTypes;
import moriyashiine.enchancement.common.world.item.effects.entity.FreezeEnchantmentEffect;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class IceShard extends ShardEntity {
	private static final ParticleOptions PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, Items.ICE);

	public IceShard(EntityType<IceShard> type, Level level) {
		super(type, level);
	}

	public IceShard(Level level, LivingEntity mob, @Nullable ItemStack firedFromWeapon) {
		super(EnchancementEntityTypes.ICE_SHARD, mob, level, firedFromWeapon);
	}

	public IceShard(Level level, LivingEntity source, @Nullable Entity owner) {
		this(level, source, (ItemStack) null);
		setOwner(owner);
	}

	@Override
	protected ParticleOptions getParticleEffect() {
		return PARTICLE;
	}

	@Override
	protected ResourceKey<DamageType> getDamageType() {
		return EnchancementDamageTypes.ICE_SHARD;
	}

	@Override
	protected void onTargetHit(Entity entity) {
		FreezeEnchantmentEffect.setFreezeTicks(entity, 200);
	}
}
