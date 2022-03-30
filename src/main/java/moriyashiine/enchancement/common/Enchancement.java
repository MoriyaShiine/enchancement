package moriyashiine.enchancement.common;

import com.google.gson.GsonBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moriyashiine.enchancement.common.component.entity.BuryComponent;
import moriyashiine.enchancement.common.component.world.LumberjackComponent;
import moriyashiine.enchancement.common.config.IdentifierTypeAdapter;
import moriyashiine.enchancement.common.config.ModConfig;
import moriyashiine.enchancement.common.packet.AttemptGaleJumpPacket;
import moriyashiine.enchancement.common.packet.SyncMovingForwardPacket;
import moriyashiine.enchancement.common.registry.*;
import moriyashiine.enchancement.common.util.BeheadingEntry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Enchancement implements ModInitializer {
	public static final String MOD_ID = "enchancement";

	private static ConfigHolder<ModConfig> config;

	public static final Map<Enchantment, Integer> CACHED_MAX_LEVELS = new HashMap<>();

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(SyncMovingForwardPacket.ID, SyncMovingForwardPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(AttemptGaleJumpPacket.ID, AttemptGaleJumpPacket::receive);
		ModEntityTypes.init();
		ModEnchantments.init();
		ModSoundEvents.init();
		initEvents();
	}

	public static ModConfig getConfig() {
		if (config == null) {
			AutoConfig.register(ModConfig.class, (config1, aClass) -> new GsonConfigSerializer<>(config1, aClass, new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Identifier.class, new IdentifierTypeAdapter()).create()));
			config = AutoConfig.getConfigHolder(ModConfig.class);
		}
		return config.getConfig();
	}

	private void initEvents() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			int fireIgnitionLevel = getConfig().fireAspectIgnitionLevel;
			if (fireIgnitionLevel >= 0 && player.isSneaking() && EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, player.getStackInHand(hand)) >= fireIgnitionLevel) {
				ActionResult result = Items.FLINT_AND_STEEL.useOnBlock(new ItemUsageContext(player, hand, hitResult));
				if (result != ActionResult.FAIL) {
					return result;
				}
			}
			return ActionResult.PASS;
		});
		BeheadingEntry.initEvent();
		PlayerBlockBreakEvents.BEFORE.register(new PlayerBlockBreakEvents.Before() {
			@Override
			public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
				if (!player.isSneaking()) {
					ItemStack stack = player.getMainHandStack();
					if (EnchantmentHelper.getLevel(ModEnchantments.LUMBERJACK, player.getMainHandStack()) > 0 && state.isIn(BlockTags.LOGS)) {
						List<BlockPos> tree = gatherTree(new ArrayList<>(), world, pos, state.getBlock());
						if (tree.size() > 1 && tree.size() <= 256) {
							ItemStack copy = stack.copy();
							AtomicBoolean broken = new AtomicBoolean(false);
							player.getMainHandStack().damage(tree.size(), player, stackUser -> {
								stackUser.setStackInHand(Hand.MAIN_HAND, copy);
								broken.set(true);
							});
							if (!broken.get()) {
								tree.sort(Comparator.comparingInt(Vec3i::getY).reversed());
								ModComponents.World.LUMBERJACK.get(world).addTree(new LumberjackComponent.Tree(tree, pos));
								return false;
							}
						}
					}
				}
				return true;
			}

			private List<BlockPos> gatherTree(List<BlockPos> tree, World world, BlockPos pos, Block original) {
				if (tree.size() < 256) {
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								BlockState state = world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
								if (state.isIn(BlockTags.LOGS) && !tree.contains(mutable) && state.getBlock() == original) {
									tree.add(mutable.toImmutable());
									gatherTree(tree, world, mutable, original);
								}
							}
						}
					}
				}
				return tree;
			}
		});
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!entity.getType().isIn(ModTags.EntityTypes.CANNOT_BURY) && entity instanceof LivingEntity living) {
				ItemStack stack = player.getStackInHand(hand);
				if (EnchantmentHelper.getLevel(ModEnchantments.BURY, stack) > 0) {
					BuryComponent buryComponent = ModComponents.Entity.BURY.get(living);
					if (buryComponent.getBuryPos() == null) {
						BlockPos down = entity.getBlockPos().down();
						BlockState state = world.getBlockState(down);
						if (state.isIn(ModTags.Blocks.BURIABLE) && state.isFullCube(world, down)) {
							if (!world.isClient) {
								buryComponent.setBuryPos(down);
								world.playSoundFromEntity(null, entity, ModSoundEvents.ENTITY_GENERIC_BURY, entity.getSoundCategory(), 1, 1);
								stack.damage(1, player, stackUser -> stackUser.sendToolBreakStatus(hand));
							} else {
								BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
								for (int i = 0; i < 24; i++) {
									world.addParticle(particle, entity.getParticleX(1), entity.getRandomBodyY(), entity.getParticleZ(1), 0, 0, 0);
								}
							}
							return ActionResult.success(world.isClient);
						}
					}
				}
			}
			return ActionResult.PASS;
		});
	}
}
