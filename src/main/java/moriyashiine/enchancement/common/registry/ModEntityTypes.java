package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntityTypes {
	public static final EntityType<IceShardEntity> ICE_SHARD = FabricEntityTypeBuilder.<IceShardEntity>create(SpawnGroup.MISC, IceShardEntity::new).dimensions(EntityType.ARROW.getDimensions()).build();

	public static void init() {
		Registry.register(Registry.ENTITY_TYPE, new Identifier(Enchancement.MOD_ID, "ice_shard"), ICE_SHARD);
	}
}
