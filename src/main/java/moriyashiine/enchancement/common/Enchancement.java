package moriyashiine.enchancement.common;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moriyashiine.enchancement.common.component.entity.BuryComponent;
import moriyashiine.enchancement.common.component.world.LumberjackComponent;
import moriyashiine.enchancement.common.packet.AttemptGaleJumpPacket;
import moriyashiine.enchancement.common.packet.SyncMovingForwardPacket;
import moriyashiine.enchancement.common.registry.*;
import moriyashiine.enchancement.common.util.BeheadingEntry;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
			AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
			config = AutoConfig.getConfigHolder(ModConfig.class);
		}
		ModConfig modConfig = config.getConfig();
		if (modConfig.allowedEnchantmentIdentifiers == null) {
			modConfig.allowedEnchantmentIdentifiers = modConfig.allowedEnchantments.stream().map(Identifier::tryParse).toList();
		}
		return modConfig;
	}

	private void initEvents() {
		//fire aspect
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
		//extracting
		PlayerBlockBreakEvents.BEFORE.register(new PlayerBlockBreakEvents.Before() {
			@Override
			public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
				if (!player.isSneaking()) {
					ItemStack stack = player.getMainHandStack();
					if (EnchantmentHelper.getLevel(ModEnchantments.EXTRACTING, stack) > 0 && state.isIn(ModTags.Blocks.ORES)) {
						Set<BlockPos> ores = gatherOres(new HashSet<>(), world, new BlockPos.Mutable().set(pos), state.getBlock());
						if (!ores.isEmpty() && ores.size() <= Enchancement.getConfig().maxExtractingBlocks) {
							ItemStack copy = stack.copy();
							AtomicBoolean broken = new AtomicBoolean(false);
							stack.damage(ores.size(), player, stackUser -> {
								stackUser.setStackInHand(Hand.MAIN_HAND, copy);
								broken.set(true);
							});
							if (!broken.get()) {
								BlockState replace = Registry.BLOCK.getId(state.getBlock()).getPath().contains("deepslate") ? Blocks.DEEPSLATE.getDefaultState() : Blocks.STONE.getDefaultState();
								List<ItemStack> drops = new ArrayList<>();
								ores.forEach(ore -> {
									drops.addAll(Block.getDroppedStacks(world.getBlockState(ore), (ServerWorld) world, ore, world.getBlockEntity(ore)));
									world.breakBlock(ore, false);
									world.setBlockState(ore, replace);
								});
								world.playSound(null, pos, ModSoundEvents.BLOCK_ORE_EXTRACT, SoundCategory.BLOCKS, 1, 1);
								if (!drops.isEmpty()) {
									EnchancementUtil.mergeItemEntities(drops.stream().map(drop -> new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), drop)).collect(Collectors.toList())).forEach(world::spawnEntity);
								}
								return false;
							}
						}
					}
				}
				return true;
			}

			private Set<BlockPos> gatherOres(Set<BlockPos> ores, World world, BlockPos.Mutable pos, Block original) {
				if (ores.size() < Enchancement.getConfig().maxExtractingBlocks) {
					int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								BlockState state = world.getBlockState(pos.set(originalX + x, originalY + y, originalZ + z));
								if (state.isIn(ModTags.Blocks.ORES) && !ores.contains(pos) && state.getBlock() == original) {
									ores.add(pos.toImmutable());
									gatherOres(ores, world, pos, original);
								}
							}
						}
					}
				}
				return ores;
			}
		});
		//beheading
		BeheadingEntry.initEvent();
		//lumberjack
		PlayerBlockBreakEvents.BEFORE.register(new PlayerBlockBreakEvents.Before() {
			@Override
			public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
				if (!player.isSneaking()) {
					ItemStack stack = player.getMainHandStack();
					if (EnchantmentHelper.getLevel(ModEnchantments.LUMBERJACK, stack) > 0 && state.isIn(BlockTags.LOGS)) {
						List<BlockPos> tree = gatherTree(new ArrayList<>(), world, new BlockPos.Mutable().set(pos), state.getBlock());
						if (tree.size() > 1 && tree.size() <= Enchancement.getConfig().maxLumberjackBlocks) {
							ItemStack copy = stack.copy();
							AtomicBoolean broken = new AtomicBoolean(false);
							stack.damage(tree.size(), player, stackUser -> {
								stackUser.setStackInHand(Hand.MAIN_HAND, copy);
								broken.set(true);
							});
							if (!broken.get()) {
								tree.sort(Comparator.comparingInt(Vec3i::getY).reversed());
								ModWorldComponents.LUMBERJACK.get(world).addTree(new LumberjackComponent.Tree(tree, pos));
								return false;
							}
						}
					}
				}
				return true;
			}

			private List<BlockPos> gatherTree(List<BlockPos> tree, World world, BlockPos.Mutable pos, Block original) {
				if (tree.size() < Enchancement.getConfig().maxLumberjackBlocks) {
					int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								BlockState state = world.getBlockState(pos.set(originalX + x, originalY + y, originalZ + z));
								if (state.isIn(BlockTags.LOGS) && !tree.contains(pos) && state.getBlock() == original) {
									tree.add(pos.toImmutable());
									gatherTree(tree, world, pos, original);
								}
							}
						}
					}
				}
				return tree;
			}
		});
		//bury
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!entity.getType().isIn(ModTags.EntityTypes.CANNOT_BURY) && entity instanceof LivingEntity living) {
				ItemStack stack = player.getStackInHand(hand);
				if (EnchantmentHelper.getLevel(ModEnchantments.BURY, stack) > 0) {
					BuryComponent buryComponent = ModEntityComponents.BURY.get(living);
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
