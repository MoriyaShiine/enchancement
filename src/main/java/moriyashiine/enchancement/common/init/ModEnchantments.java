/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModMobEffectTags;
import moriyashiine.enchancement.common.world.item.effects.*;
import moriyashiine.enchancement.common.world.item.effects.entity.*;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.AttackerBehindCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.HasExtendedWaterTimeCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.InCombatCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.WetCondition;
import moriyashiine.enchancement.data.provider.ModEnchantmentTagsProvider;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.MovementPredicate;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.List;

public class ModEnchantments {
	// placeholder
	public static final ResourceKey<Enchantment> EMPTY_KEY = createKey("empty");
	public static final Enchantment EMPTY = Enchantment.enchantment(Enchantment.definition(HolderSet.empty(), 1, 1, Enchantment.constantCost(0), Enchantment.constantCost(0), 0)).build(EMPTY_KEY.identifier());
	// helmet
	public static final ResourceKey<Enchantment> ASSIMILATION = createKey("assimilation");
	public static final ResourceKey<Enchantment> PERCEPTION = createKey("perception");
	public static final ResourceKey<Enchantment> VEIL = createKey("veil");
	// chestplate
	public static final ResourceKey<Enchantment> ADRENALINE = createKey("adrenaline");
	public static final ResourceKey<Enchantment> AMPHIBIOUS = createKey("amphibious");
	public static final ResourceKey<Enchantment> STRAFE = createKey("strafe");
	public static final ResourceKey<Enchantment> WARDENSPINE = createKey("wardenspine");
	// leggings
	public static final ResourceKey<Enchantment> DASH = createKey("dash");
	public static final ResourceKey<Enchantment> GALE = createKey("gale");
	public static final ResourceKey<Enchantment> SLIDE = createKey("slide");
	// boots
	public static final ResourceKey<Enchantment> BOUNCY = createKey("bouncy");
	public static final ResourceKey<Enchantment> BUOY = createKey("buoy");
	public static final ResourceKey<Enchantment> STICKY = createKey("sticky");
	// sword
	public static final ResourceKey<Enchantment> BERSERK = createKey("berserk");
	public static final ResourceKey<Enchantment> FROSTBITE = createKey("frostbite");
	// bow
	public static final ResourceKey<Enchantment> CHAOS = createKey("chaos");
	public static final ResourceKey<Enchantment> DELAY = createKey("delay");
	public static final ResourceKey<Enchantment> PHASING = createKey("phasing");
	// crossbow
	public static final ResourceKey<Enchantment> BRIMSTONE = createKey("brimstone");
	public static final ResourceKey<Enchantment> SCATTER = createKey("scatter");
	public static final ResourceKey<Enchantment> TORCH = createKey("torch");
	// trident
	public static final ResourceKey<Enchantment> LEECH = createKey("leech");
	public static final ResourceKey<Enchantment> WARP = createKey("warp");
	// mace
	public static final ResourceKey<Enchantment> METEOR = createKey("meteor");
	public static final ResourceKey<Enchantment> THUNDERSTRUCK = createKey("thunderstruck");
	// mining tool
	public static final ResourceKey<Enchantment> MOLTEN = createKey("molten");
	// pickaxe
	public static final ResourceKey<Enchantment> EXTRACTING = createKey("extracting");
	// axe
	public static final ResourceKey<Enchantment> BEHEADING = createKey("beheading");
	public static final ResourceKey<Enchantment> LUMBERJACK = createKey("lumberjack");
	// shovel
	public static final ResourceKey<Enchantment> BURY = createKey("bury");
	public static final ResourceKey<Enchantment> SCOOPING = createKey("scooping");
	// hoe
	public static final ResourceKey<Enchantment> APEX = createKey("apex");
	// fishing rod
	public static final ResourceKey<Enchantment> DISARM = createKey("disarm");
	public static final ResourceKey<Enchantment> GRAPPLE = createKey("grapple");

	private static ResourceKey<Enchantment> createKey(String id) {
		return ResourceKey.create(Registries.ENCHANTMENT, Enchancement.id(id));
	}

	public static Enchantment create(Identifier id, boolean treasure, HolderSet<Item> supportedItems, int maxLevel, EquipmentSlotGroup group, EffectsAdder effectsAdder) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			if (treasure) {
				if (!ModEnchantmentTagsProvider.TREASURE_ENCHANTMENTS.contains(id)) {
					ModEnchantmentTagsProvider.TREASURE_ENCHANTMENTS.add(id);
				}
			} else if (!ModEnchantmentTagsProvider.NON_TREASURE_ENCHANTMENTS.contains(id)) {
				ModEnchantmentTagsProvider.NON_TREASURE_ENCHANTMENTS.add(id);
			}
		}
		Enchantment.Builder builder = Enchantment.enchantment(Enchantment.definition(supportedItems, 5, maxLevel, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(40, 10), 2, group));
		effectsAdder.addEffects(builder);
		return builder.build(id);
	}

	public static Enchantment create(Identifier id, HolderSet<Item> supportedItems, int maxLevel, EquipmentSlotGroup group, EffectsAdder effectsAdder) {
		return create(id, false, supportedItems, maxLevel, group, effectsAdder);
	}

	public static void bootstrap(BootstrapContext<Enchantment> registry) {
		registry.register(EMPTY_KEY, EMPTY);
		// lookup
		HolderGetter<DamageType> damageTypeLookup = registry.lookup(Registries.DAMAGE_TYPE);
		HolderGetter<Enchantment> enchantmentLookup = registry.lookup(Registries.ENCHANTMENT);
		HolderGetter<EntityType<?>> entityTypeLookup = registry.lookup(Registries.ENTITY_TYPE);
		HolderGetter<Item> itemLookup = registry.lookup(Registries.ITEM);
		// helmet
		registry.register(ASSIMILATION, create(ASSIMILATION.identifier(),
				itemLookup.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.MODIFY_CONSUMPTION_TIME,
							new MultiplyValue(LevelBasedValue.perLevel(0.875F, -0.125F)));
					builder.withEffect(
							EnchantmentEffectComponents.TICK,
							new AutomateEatingEnchantmentEffect(MinMaxBounds.Ints.atMost(14)),
							AllOfCondition.allOf(
									LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().periodicTick(20)),
									() -> InvertedLootItemCondition.invert(() -> InCombatCondition.INSTANCE).build()
							));
				}));
		registry.register(PERCEPTION, create(PERCEPTION.identifier(),
				itemLookup.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.ENTITY_XRAY,
							new AddValue(LevelBasedValue.perLevel(8)));
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.NIGHT_VISION,
							new AddValue(LevelBasedValue.perLevel(0.25F, 0.75F)));
				}));
		registry.register(VEIL, create(VEIL.identifier(),
				itemLookup.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.HIDE_NAME_BEHIND_WALLS);
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE,
							new MultiplyValue(new LevelBasedValue.Fraction(LevelBasedValue.constant(1), LevelBasedValue.perLevel(2))));
					builder.withEffect(
							EnchantmentEffectComponents.ATTRIBUTES,
							new EnchantmentAttributeEffect(Enchancement.id("enchantment.veil"),
									Attributes.WAYPOINT_TRANSMIT_RANGE,
									LevelBasedValue.constant(-1),
									AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
				}));
		// chestplate
		registry.register(ADRENALINE, create(ADRENALINE.identifier(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> builder.withSpecialEffect(
						ModEnchantmentEffectComponentTypes.RAGE,
						new RageEffect(
								new AddValue(LevelBasedValue.constant(0)),
								new AddValue(LevelBasedValue.perLevel(0.4F / 14)),
								new AddValue(LevelBasedValue.perLevel(0.02857142857F))
						)
				)));
		registry.register(AMPHIBIOUS, create(AMPHIBIOUS.identifier(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME);
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.EXTENDED_WATER_SPIN_ATTACK);
					builder.withEffect(
							EnchantmentEffectComponents.TICK,
							ExtinguishEnchantmentEffect.INSTANCE,
							() -> HasExtendedWaterTimeCondition.INSTANCE);
					builder.withEffect(
							EnchantmentEffectComponents.TICK,
							new SetExtendedWaterTimeEffect(
									LevelBasedValue.perLevel(6, 4)),
							() -> WetCondition.INSTANCE);
					builder.withEffect(
							EnchantmentEffectComponents.ATTRIBUTES,
							new EnchantmentAttributeEffect(Enchancement.id("enchantment.amphibious"),
									Attributes.BURNING_TIME,
									LevelBasedValue.perLevel(-0.25F),
									AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
					builder.withEffect(
							EnchantmentEffectComponents.ATTRIBUTES,
							new EnchantmentAttributeEffect(Enchancement.id("enchantment.amphibious"),
									Attributes.OXYGEN_BONUS,
									LevelBasedValue.perLevel(1.5F),
									AttributeModifier.Operation.ADD_VALUE));
					builder.withEffect(
							EnchantmentEffectComponents.ATTRIBUTES,
							new EnchantmentAttributeEffect(Enchancement.id("enchantment.amphibious"),
									Attributes.SUBMERGED_MINING_SPEED,
									LevelBasedValue.perLevel(2),
									AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
					builder.withEffect(
							EnchantmentEffectComponents.ATTRIBUTES,
							new EnchantmentAttributeEffect(Enchancement.id("enchantment.amphibious"),
									Attributes.WATER_MOVEMENT_EFFICIENCY,
									LevelBasedValue.perLevel(0.5F),
									AttributeModifier.Operation.ADD_VALUE));
				}));
		registry.register(STRAFE, create(STRAFE.identifier(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> builder.withSpecialEffect(
						ModEnchantmentEffectComponentTypes.DIRECTION_BURST,
						new DirectionBurstEffect(
								new AddValue(LevelBasedValue.perLevel(1.25F, -0.5F)),
								new AddValue(LevelBasedValue.constant(1.1F)),
								new AddValue(LevelBasedValue.constant(0.8F))))));
		registry.register(WARDENSPINE, create(WARDENSPINE.identifier(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.WARDENSPINE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchantmentEffectComponents.DAMAGE_PROTECTION,
							new AddValue(LevelBasedValue.perLevel(8)),
							AllOfCondition.allOf(
									DamageSourceCondition.hasDamageSource(net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(ModDamageTypeTags.BYPASSES_WARDENSPINE))),
									() -> AttackerBehindCondition.INSTANCE
							));
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.VICTIM,
							EnchantmentTarget.ATTACKER,
							AllOf.entityEffects(
									new DamageEntity(
											LevelBasedValue.perLevel(2),
											LevelBasedValue.perLevel(2),
											damageTypeLookup.getOrThrow(DamageTypes.THORNS)),
									new ApplyMobEffect(
											HolderSet.direct(MobEffects.DARKNESS),
											LevelBasedValue.perLevel(4),
											LevelBasedValue.perLevel(4),
											LevelBasedValue.constant(0),
											LevelBasedValue.constant(0)
									),
									new PlaySoundEffect(
											List.of(
													Holder.direct(ModSoundEvents.ENTITY_GENERIC_WARDENSPINE)
											),
											ConstantFloat.of(1),
											ConstantFloat.of(1)
									)
							),
							AllOfCondition.allOf(
									DamageSourceCondition.hasDamageSource(net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(ModDamageTypeTags.BYPASSES_WARDENSPINE))),
									() -> AttackerBehindCondition.INSTANCE
							));
				}));
		// leggings
		registry.register(DASH, create(DASH.identifier(),
				itemLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> builder.withSpecialEffect(
						ModEnchantmentEffectComponentTypes.ROTATION_BURST,
						new RotationBurstEffect(
								new AddValue(LevelBasedValue.perLevel(1.2F, -0.2F)),
								new AddValue(LevelBasedValue.perLevel(0.85F, 0.15F)),
								new AddValue(LevelBasedValue.constant(3)),
								new AddValue(LevelBasedValue.constant(1.1F))))));
		registry.register(GALE, create(GALE.identifier(),
				itemLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.AIR_JUMP,
							new AirJumpEffect(
									new AddValue(LevelBasedValue.perLevel(1)),
									new AddValue(LevelBasedValue.constant(1.45F)),
									new AddValue(LevelBasedValue.constant(0.5F)),
									new AddValue(LevelBasedValue.constant(0.5F))));
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.GLIDE,
							new GlideEffect(
									new AddValue(LevelBasedValue.constant(0.6F)),
									new AddValue(LevelBasedValue.perLevel(2, 1))
							));
				}));
		registry.register(SLIDE, create(SLIDE.identifier(),
				itemLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.SLAM,
							new AddValue(LevelBasedValue.constant(0.5F)));
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.SLIDE,
							new AddValue(LevelBasedValue.perLevel(0.21F, 0.065F)));
				}));
		// boots
		registry.register(BOUNCY, create(BOUNCY.identifier(),
				itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.BOUNCY_EXCLUSIVE_SET));
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.BOUNCE);
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.CHARGE_JUMP,
							new ChargeJumpEffect(
									new AddValue(LevelBasedValue.constant(30)),
									new AddValue(LevelBasedValue.constant(0.5F)),
									new AddValue(LevelBasedValue.perLevel(0.6F, 0.4F))));
				}));
		registry.register(BUOY, create(BUOY.identifier(),
				itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME);
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.BOOST_IN_FLUID,
							new AddValue(LevelBasedValue.perLevel(0.7F, 0.3F)));
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.FLUID_WALKING);
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.PREVENT_SWIMMING);
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.MODIFY_SUBMERGED_MOVEMENT_SPEED,
							new ModifySubmergedMovementSpeedEffect(
									new AddValue(LevelBasedValue.perLevel(0.175F)),
									SubmersionGate.WATER_ONLY));
					builder.withEffect(
							EnchantmentEffectComponents.TICK,
							new SetExtendedWaterTimeEffect(
									LevelBasedValue.perLevel(6, 4)),
							() -> WetCondition.INSTANCE);
					builder.withEffect(
							EnchantmentEffectComponents.TICK,
							new ConditionalAttributeEnchantmentEffect(
									new EnchantmentAttributeEffect(Enchancement.id("enchantment.buoy"),
											Attributes.STEP_HEIGHT,
											LevelBasedValue.constant(1),
											AttributeModifier.Operation.ADD_VALUE),
									HasExtendedWaterTimeCondition.INSTANCE));
					builder.withEffect(
							EnchantmentEffectComponents.TICK,
							new ConditionalAttributeEnchantmentEffect(
									new EnchantmentAttributeEffect(Enchancement.id("enchantment.buoy"),
											Attributes.SAFE_FALL_DISTANCE,
											LevelBasedValue.perLevel(2),
											AttributeModifier.Operation.ADD_VALUE),
									HasExtendedWaterTimeCondition.INSTANCE));
					builder.withEffect(
							EnchantmentEffectComponents.DAMAGE_IMMUNITY,
							DamageImmunity.INSTANCE,
							DamageSourceCondition.hasDamageSource(
									net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType()
											.tag(TagPredicate.is(DamageTypeTags.BURN_FROM_STEPPING))
											.tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
							)
					);
				}));
		registry.register(STICKY, create(STICKY.identifier(),
				itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.HONEY_TRAIL,
							new AddValue(LevelBasedValue.perLevel(1.5F))
					);
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.WALL_JUMP,
							new AddValue(LevelBasedValue.perLevel(0.65F, 0.15F)));
				}));
		// sword
		registry.register(BERSERK, create(BERSERK.identifier(),
				itemLookup.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withSpecialEffect(
						ModEnchantmentEffectComponentTypes.RAGE,
						new RageEffect(
								new AddValue(LevelBasedValue.perLevel(0.175F)),
								new AddValue(LevelBasedValue.constant(0)),
								new AddValue(LevelBasedValue.constant(0))
						)
				)));
		registry.register(FROSTBITE, create(FROSTBITE.identifier(),
				itemLookup.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.FROSTBITE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.VICTIM,
							new FreezeEnchantmentEffect(LevelBasedValue.perLevel(6)),
							DamageSourceCondition.hasDamageSource(net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType().isDirect(true)));
				}));
		// bow
		registry.register(CHAOS, create(CHAOS.identifier(),
				itemLookup.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.APPLY_RANDOM_MOB_EFFECT,
						new ApplyRandomMobEffectEffect(
								new AddValue(LevelBasedValue.perLevel(4)),
								ModMobEffectTags.CHAOS_UNCHOOSABLE
						)
				)));
		registry.register(DELAY, create(DELAY.identifier(),
				itemLookup.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.DELAYED_LAUNCH,
						new DelayedLaunchEffect(
								new AddValue(LevelBasedValue.constant(10)),
								new AddValue(LevelBasedValue.constant(3)),
								new AddValue(LevelBasedValue.perLevel(0.5F, 0.25F)),
								true
						)
				)));
		registry.register(PHASING, create(PHASING.identifier(),
				itemLookup.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT,
						new AddValue(LevelBasedValue.perLevel(2, 1))
				)));
		// crossbow
		registry.register(BRIMSTONE, create(BRIMSTONE.identifier(),
				itemLookup.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.BRIMSTONE_EXCLUSIVE_SET));
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING);
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.BRIMSTONE,
							new BrimstoneEffect(
									new AddValue(LevelBasedValue.perLevel(0.5F))
							));
				}));
		registry.register(SCATTER, create(SCATTER.identifier(),
				itemLookup.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET));
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING);
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION
					);
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE,
							new AllowLoadingProjectileEffect(
									Enchancement.id("crossbow_amethyst"),
									ModSoundEvents.ITEM_CROSSBOW_SCATTER,
									Items.AMETHYST_SHARD,
									false
							)
					);
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.RANGED_SHOOT_COOLDOWN,
							new AddValue(LevelBasedValue.constant(1))
					);
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.SCATTER_SHOT,
							new ScatterShotEffect(
									new AddValue(LevelBasedValue.perLevel(6)),
									new AddValue(LevelBasedValue.perLevel(8)),
									Items.AMETHYST_SHARD
							)
					);
				}));
		registry.register(TORCH, create(TORCH.identifier(),
				itemLookup.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET));
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE,
							new AllowLoadingProjectileEffect(
									Enchancement.id("crossbow_torch"),
									SoundEvents.CROSSBOW_SHOOT,
									Items.TORCH,
									true
							)
					);
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE);
				}));
		// trident
		registry.register(LEECH, create(LEECH.identifier(),
				itemLookup.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT,
							new LeechingTridentEffect(
									new AddValue(LevelBasedValue.constant(1)),
									new AddValue(LevelBasedValue.constant(1)),
									new AddValue(LevelBasedValue.perLevel(2))
							));
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.ATTACKER,
							new HealEnchantmentEffect(LevelBasedValue.perLevel(0.5F)),
							DamageSourceCondition.hasDamageSource(net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType().isDirect(true)));
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.VICTIM,
							new SpawnParticlesEffect(
									ParticleTypes.DAMAGE_INDICATOR,
									SpawnParticlesEffect.offsetFromEntityPosition(0.5F),
									SpawnParticlesEffect.offsetFromEntityPosition(0.5F),
									SpawnParticlesEffect.movementScaled(0),
									SpawnParticlesEffect.movementScaled(0),
									ConstantFloat.of(0)
							),
							DamageSourceCondition.hasDamageSource(net.minecraft.advancements.criterion.DamageSourcePredicate.Builder.damageType().isDirect(true)));
				}));
		registry.register(WARP, create(WARP.identifier(),
				itemLookup.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.TELEPORT_ON_HIT,
						new TeleportOnHitEffect(
								true,
								false)
				)));
		// mace
		registry.register(METEOR, create(METEOR.identifier(), true,
				itemLookup.getOrThrow(ItemTags.MACE_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.MACE_EXCLUSIVE_SET));
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.ERUPTION,
							new EruptionEffect(
									new AddValue(LevelBasedValue.constant(1.35F)),
									new AddValue(LevelBasedValue.perLevel(4))
							)
					);
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.VICTIM,
							AllOf.entityEffects(
									BuryEffect.INSTANCE,
									new Ignite(LevelBasedValue.perLevel(6)),
									new SmashEffect(LevelBasedValue.perLevel(2.5F)),
									new SpawnParticlesWithCountEnchantmentEffect(
											new SpawnParticlesEffect(
													ParticleTypes.LAVA,
													SpawnParticlesEffect.offsetFromEntityPosition(0.5F),
													SpawnParticlesEffect.offsetFromEntityPosition(0.5F),
													SpawnParticlesEffect.fixedVelocity(UniformFloat.of(-1, 1)),
													SpawnParticlesEffect.movementScaled(1),
													ConstantFloat.of(1)
											),
											LevelBasedValue.constant(48)
									)
							),
							LootItemEntityPropertyCondition.hasProperties(
									LootContext.EntityTarget.DIRECT_ATTACKER,
									net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
											.flags(net.minecraft.advancements.criterion.EntityFlagsPredicate.Builder.flags().setIsFlying(false))
											.moving(MovementPredicate.fallDistance(MinMaxBounds.Doubles.atLeast(1.5)))
							));
				}));
		registry.register(THUNDERSTRUCK, create(THUNDERSTRUCK.identifier(), true,
				itemLookup.getOrThrow(ItemTags.MACE_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.MACE_EXCLUSIVE_SET));
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING,
							new AddValue(LevelBasedValue.perLevel(0.35F))
					);
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.LIGHTNING_DASH,
							new LightningDashEffect(
									new AddValue(LevelBasedValue.constant(3)),
									new AddValue(LevelBasedValue.perLevel(0.8F, 0.3F)),
									new AddValue(LevelBasedValue.perLevel(1)),
									new AddValue(LevelBasedValue.perLevel(0.8F, 0.3F))
							));
				}));
		// mining tool
		registry.register(MOLTEN, create(MOLTEN.identifier(),
				itemLookup.getOrThrow(ItemTags.MINING_ENCHANTABLE),
				1,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET));
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.SMELT_MINED_BLOCKS);
				}));
		// pickaxe
		registry.register(EXTRACTING, create(EXTRACTING.identifier(),
				itemLookup.getOrThrow(ItemTags.PICKAXES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantmentLookup.getOrThrow(ModEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET));
					builder.withSpecialEffect(
							ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS,
							new AddValue(LevelBasedValue.perLevel(0.5F)));
				}));
		// axe
		registry.register(BEHEADING, create(BEHEADING.identifier(),
				itemLookup.getOrThrow(ItemTags.AXES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.HEAD_DROPS,
						EnchantmentTarget.ATTACKER,
						EnchantmentTarget.VICTIM,
						new AddValue(LevelBasedValue.perLevel(0.5F)),
						LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypeLookup, EntityType.PLAYER)))
				)));
		registry.register(LUMBERJACK, create(LUMBERJACK.identifier(),
				itemLookup.getOrThrow(ItemTags.AXES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withSpecialEffect(
						ModEnchantmentEffectComponentTypes.FELL_TREES,
						new AddValue(LevelBasedValue.perLevel(0.25F)))));
		// shovel
		registry.register(BURY, create(BURY.identifier(),
				itemLookup.getOrThrow(ItemTags.SHOVELS),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.BURY_ENTITY,
						new AddValue(LevelBasedValue.perLevel(16, -8)))));
		registry.register(SCOOPING, create(SCOOPING.identifier(),
				itemLookup.getOrThrow(ItemTags.SHOVELS),
				4,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							EnchantmentEffectComponents.DAMAGE,
							new AddValue(LevelBasedValue.perLevel(0.5F)));
					builder.withEffect(
							EnchantmentEffectComponents.EQUIPMENT_DROPS,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.VICTIM,
							new AddValue(LevelBasedValue.perLevel(0.01F)),
							LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, net.minecraft.advancements.criterion.EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypeLookup, EntityType.PLAYER))));
				}));
		// hoe
		registry.register(APEX, create(APEX.identifier(),
				itemLookup.getOrThrow(ItemTags.HOES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER,
							new CriticalTipperEffect(
									new AddValue(LevelBasedValue.constant(0.5F)),
									ModParticleTypes.CRITICAL_TIPPER
							)
					);
					builder.withEffect(
							EnchantmentEffectComponents.DAMAGE,
							new AddValue(LevelBasedValue.perLevel(0.25F)));
				}));
		// fishing rod
		registry.register(DISARM, create(DISARM.identifier(),
				itemLookup.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER,
						new DisarmingFishingBobberEffect(
								false,
								new AddValue(LevelBasedValue.perLevel(2.5F)),
								new AddValue(LevelBasedValue.perLevel(12, -2))
						))));
		registry.register(GRAPPLE, create(GRAPPLE.identifier(),
				itemLookup.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						ModEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER,
						new AddValue(LevelBasedValue.perLevel(1)))));
	}

	public interface EffectsAdder {
		void addEffects(Enchantment.Builder builder);
	}
}
