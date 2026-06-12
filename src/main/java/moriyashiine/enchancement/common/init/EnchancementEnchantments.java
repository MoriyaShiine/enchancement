/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.tag.EnchancementDamageTypeTags;
import moriyashiine.enchancement.common.tag.EnchancementEnchantmentTags;
import moriyashiine.enchancement.common.tag.EnchancementItemTags;
import moriyashiine.enchancement.common.tag.EnchancementMobEffectTags;
import moriyashiine.enchancement.common.world.item.effects.*;
import moriyashiine.enchancement.common.world.item.effects.entity.*;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.AttackerBehindCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.HasExtendedWaterTimeCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.InCombatCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.WetCondition;
import moriyashiine.enchancement.datagen.provider.EnchancementEnchantmentTagsProvider;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.advancements.predicates.entity.MovementPredicate;
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
import net.minecraft.world.entity.EntityTypes;
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

import java.util.ArrayList;
import java.util.List;

public class EnchancementEnchantments {
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
	public static final ResourceKey<Enchantment> E_SPEED = createKey("e_speed");
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
	// excavating tool
	public static final ResourceKey<Enchantment> BURROWING = createKey("burrowing");
	// pickaxe
	public static final ResourceKey<Enchantment> EXTRACTING = createKey("extracting");
	// axe
	public static final ResourceKey<Enchantment> BEHEADING = createKey("beheading");
	public static final ResourceKey<Enchantment> LUMBERJACK = createKey("lumberjack");
	// shovel
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
				if (!EnchancementEnchantmentTagsProvider.TREASURE_ENCHANTMENTS.contains(id)) {
					EnchancementEnchantmentTagsProvider.TREASURE_ENCHANTMENTS.add(id);
				}
			} else if (!EnchancementEnchantmentTagsProvider.NON_TREASURE_ENCHANTMENTS.contains(id)) {
				EnchancementEnchantmentTagsProvider.NON_TREASURE_ENCHANTMENTS.add(id);
			}
		}
		List<EquipmentSlotGroup> groups = new ArrayList<>();
		groups.add(group);
		if (group == EquipmentSlotGroup.ARMOR) {
			groups.add(EquipmentSlotGroup.BODY);
			groups.add(EquipmentSlotGroup.SADDLE);
		}
		Enchantment.Builder builder = Enchantment.enchantment(Enchantment.definition(supportedItems, 5, maxLevel, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(40, 10), 2, groups.toArray(new EquipmentSlotGroup[0])));
		effectsAdder.addEffects(builder);
		return builder.build(id);
	}

	public static Enchantment create(Identifier id, HolderSet<Item> supportedItems, int maxLevel, EquipmentSlotGroup group, EffectsAdder effectsAdder) {
		return create(id, false, supportedItems, maxLevel, group, effectsAdder);
	}

	public static void bootstrap(BootstrapContext<Enchantment> registry) {
		registry.register(EMPTY_KEY, EMPTY);
		// lookup
		HolderGetter<DamageType> damageTypes = registry.lookup(Registries.DAMAGE_TYPE);
		HolderGetter<Enchantment> enchantments = registry.lookup(Registries.ENCHANTMENT);
		HolderGetter<EntityType<?>> entityTypes = registry.lookup(Registries.ENTITY_TYPE);
		HolderGetter<Item> items = registry.lookup(Registries.ITEM);
		// helmet
		registry.register(ASSIMILATION, create(ASSIMILATION.identifier(),
				items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.MODIFY_CONSUMPTION_TIME,
							new MultiplyValue(LevelBasedValue.perLevel(0.875F, -0.125F)));
					builder.withEffect(
							EnchantmentEffectComponents.TICK,
							new AutomateEatingEnchantmentEffect(MinMaxBounds.Ints.atMost(14)),
							AllOfCondition.allOf(
									LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().periodicTick(20)),
									() -> InvertedLootItemCondition.invert(() -> InCombatCondition.INSTANCE).build()));
				}));
		registry.register(PERCEPTION, create(PERCEPTION.identifier(),
				items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.ENTITY_XRAY,
							new AddValue(LevelBasedValue.perLevel(8)));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.NIGHT_VISION,
							new AddValue(LevelBasedValue.perLevel(0.25F, 0.75F)));
				}));
		registry.register(VEIL, create(VEIL.identifier(),
				items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.HIDE_NAME_BEHIND_WALLS);
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE,
							new MultiplyValue(new LevelBasedValue.Fraction(LevelBasedValue.constant(1), LevelBasedValue.perLevel(2))));
					builder.withEffect(
							EnchantmentEffectComponents.ATTRIBUTES,
							new EnchantmentAttributeEffect(Enchancement.id("enchantment.veil"),
									Attributes.WAYPOINT_TRANSMIT_RANGE,
									LevelBasedValue.constant(-1),
									AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.HIDE_NON_ARMOR_ATTRIBUTE_TOOLTIPS);
				}));
		// chestplate
		registry.register(ADRENALINE, create(ADRENALINE.identifier(),
				items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> builder.withSpecialEffect(
						EnchancementEnchantmentEffectComponentTypes.RAGE,
						new RageEffect(
								new AddValue(LevelBasedValue.constant(0)),
								new AddValue(LevelBasedValue.perLevel(0.4F / 14)),
								new AddValue(LevelBasedValue.perLevel(0.6F / 14)))
				)));
		registry.register(AMPHIBIOUS, create(AMPHIBIOUS.identifier(),
				items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.EXTEND_WATER_TIME);
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.EXTENDED_WATER_SPIN_ATTACK);
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
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.HIDE_NON_ARMOR_ATTRIBUTE_TOOLTIPS);
				}));
		registry.register(STRAFE, create(STRAFE.identifier(),
				items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> builder.withSpecialEffect(
						EnchancementEnchantmentEffectComponentTypes.DIRECTION_BURST,
						new DirectionBurstEffect(
								new AddValue(LevelBasedValue.perLevel(1.5F, -0.5F)),
								new AddValue(LevelBasedValue.constant(1.2F)),
								new AddValue(LevelBasedValue.constant(0.8F))))));
		registry.register(WARDENSPINE, create(WARDENSPINE.identifier(),
				items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.WARDENSPINE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchantmentEffectComponents.DAMAGE_PROTECTION,
							new AddValue(LevelBasedValue.perLevel(8)),
							AllOfCondition.allOf(
									DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(EnchancementDamageTypeTags.BYPASSES_WARDENSPINE))),
									() -> AttackerBehindCondition.INSTANCE));
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.VICTIM,
							EnchantmentTarget.ATTACKER,
							AllOf.entityEffects(
									new DamageEntity(
											LevelBasedValue.perLevel(2),
											LevelBasedValue.perLevel(2),
											damageTypes.getOrThrow(DamageTypes.THORNS)),
									new ApplyMobEffect(
											HolderSet.direct(MobEffects.DARKNESS),
											LevelBasedValue.perLevel(4),
											LevelBasedValue.perLevel(4),
											LevelBasedValue.constant(0),
											LevelBasedValue.constant(0)),
									new PlaySoundEffect(
											List.of(
													Holder.direct(EnchancementSoundEvents.GENERIC_WARDENSPINE)),
											ConstantFloat.of(1),
											ConstantFloat.of(1))),
							AllOfCondition.allOf(
									DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(EnchancementDamageTypeTags.BYPASSES_WARDENSPINE))),
									() -> AttackerBehindCondition.INSTANCE));
				}));
		// leggings
		registry.register(DASH, create(DASH.identifier(),
				items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> builder.withSpecialEffect(
						EnchancementEnchantmentEffectComponentTypes.ROTATION_BURST,
						new RotationBurstEffect(
								new AddValue(LevelBasedValue.perLevel(1.2F, -0.2F)),
								new AddValue(LevelBasedValue.perLevel(0.75F, 0.2F)),
								new AddValue(LevelBasedValue.constant(3)),
								new AddValue(LevelBasedValue.constant(1.1F)))
				)));
		registry.register(GALE, create(GALE.identifier(),
				items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.AIR_JUMP,
							new AirJumpEffect(
									new AddValue(LevelBasedValue.perLevel(1)),
									new AddValue(LevelBasedValue.constant(1.45F)),
									new AddValue(LevelBasedValue.constant(0.5F)),
									new AddValue(LevelBasedValue.constant(0.5F))));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.GLIDE,
							new GlideEffect(
									new AddValue(LevelBasedValue.constant(0.6F)),
									new AddValue(LevelBasedValue.perLevel(2, 1))));
				}));
		registry.register(SLIDE, create(SLIDE.identifier(),
				items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.SLAM,
							new AddValue(LevelBasedValue.constant(0.5F)));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.SLIDE,
							new AddValue(LevelBasedValue.perLevel(0.21F, 0.065F)));
				}));
		// boots
		registry.register(BOUNCY, create(BOUNCY.identifier(),
				items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.BOUNCY_EXCLUSIVE_SET));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.BOUNCE);
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.CHARGE_JUMP,
							new ChargeJumpEffect(
									new AddValue(LevelBasedValue.constant(30)),
									new AddValue(LevelBasedValue.constant(0.5F)),
									new AddValue(LevelBasedValue.perLevel(0.6F, 0.4F))));
				}));
		registry.register(BUOY, create(BUOY.identifier(),
				items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.EXTEND_WATER_TIME);
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.BOOST_IN_FLUID,
							new AddValue(LevelBasedValue.perLevel(0.5F)));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.FLUID_WALKING);
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.PREVENT_SWIMMING);
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.MODIFY_SUBMERGED_MOVEMENT_SPEED,
							new ModifySubmergedMovementSpeedEffect(
									new AddValue(LevelBasedValue.perLevel(0.2F)),
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
									DamageSourcePredicate.Builder.damageType()
											.tag(TagPredicate.is(DamageTypeTags.BURN_FROM_STEPPING))
											.tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))));
				}));
		registry.register(E_SPEED, create(E_SPEED.identifier(),
				items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> builder.withSpecialEffect(
						EnchancementEnchantmentEffectComponentTypes.E_METER,
						new EMeterEffect(
								new AddValue(LevelBasedValue.perLevel(0.7F, 0.3F)),
								new AddValue(LevelBasedValue.perLevel(0.275F, 0.125F)))
				)));
		registry.register(STICKY, create(STICKY.identifier(),
				items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				EquipmentSlotGroup.ARMOR,
				builder -> {
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.HONEY_TRAIL,
							new AddValue(LevelBasedValue.perLevel(1.5F)));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.WALL_JUMP,
							new AddValue(LevelBasedValue.perLevel(0.65F, 0.15F)));
				}));
		// sword
		registry.register(BERSERK, create(BERSERK.identifier(),
				items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withSpecialEffect(
						EnchancementEnchantmentEffectComponentTypes.RAGE,
						new RageEffect(
								new AddValue(LevelBasedValue.perLevel(0.175F)),
								new AddValue(LevelBasedValue.constant(0)),
								new AddValue(LevelBasedValue.constant(0)))
				)));
		registry.register(FROSTBITE, create(FROSTBITE.identifier(),
				items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.FROSTBITE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.VICTIM,
							new FreezeEnchantmentEffect(LevelBasedValue.perLevel(3)),
							DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true)));
				}));
		// bow
		registry.register(CHAOS, create(CHAOS.identifier(),
				items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						EnchancementEnchantmentEffectComponentTypes.APPLY_RANDOM_MOB_EFFECT,
						new ApplyRandomMobEffectEffect(
								new AddValue(LevelBasedValue.perLevel(4)),
								EnchancementMobEffectTags.CHAOS_UNCHOOSABLE)
				)));
		registry.register(DELAY, create(DELAY.identifier(),
				items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						EnchancementEnchantmentEffectComponentTypes.DELAYED_LAUNCH,
						new DelayedLaunchEffect(
								new AddValue(LevelBasedValue.constant(10)),
								new AddValue(LevelBasedValue.constant(3)),
								new AddValue(LevelBasedValue.perLevel(0.5F, 0.25F)),
								true)
				)));
		registry.register(PHASING, create(PHASING.identifier(),
				items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						EnchancementEnchantmentEffectComponentTypes.PHASE,
						new PhaseEffect(
								new AddValue(LevelBasedValue.perLevel(2, 1)),
								true)
				)));
		// crossbow
		registry.register(BRIMSTONE, create(BRIMSTONE.identifier(),
				items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.BRIMSTONE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING);
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.BRIMSTONE,
							new BrimstoneEffect(
									new AddValue(LevelBasedValue.perLevel(0.5F))));
				}));
		registry.register(SCATTER, create(SCATTER.identifier(),
				items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING);
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION);
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE,
							new AllowLoadingProjectileEffect(
									Enchancement.id("crossbow_amethyst"),
									EnchancementSoundEvents.CROSSBOW_SCATTER,
									Items.AMETHYST_SHARD,
									false));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.RANGED_SHOOT_COOLDOWN,
							new AddValue(LevelBasedValue.constant(1)));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.SCATTER_SHOT,
							new ScatterShotEffect(
									new AddValue(LevelBasedValue.perLevel(6)),
									new AddValue(LevelBasedValue.perLevel(8)),
									Items.AMETHYST_SHARD));
				}));
		registry.register(TORCH, create(TORCH.identifier(),
				items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE,
							new AllowLoadingProjectileEffect(
									Enchancement.id("crossbow_torch"),
									SoundEvents.CROSSBOW_SHOOT,
									Items.TORCH,
									true));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE);
				}));
		// trident
		registry.register(LEECH, create(LEECH.identifier(),
				items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.LEECHING_TRIDENT,
							new LeechingTridentEffect(
									new AddValue(LevelBasedValue.constant(1)),
									new AddValue(LevelBasedValue.constant(1)),
									new AddValue(LevelBasedValue.perLevel(2))));
					builder.withEffect(
							EnchantmentEffectComponents.POST_ATTACK,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.ATTACKER,
							new HealEnchantmentEffect(LevelBasedValue.perLevel(0.5F)),
							DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true)));
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
									ConstantFloat.of(0)),
							DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true)));
				}));
		registry.register(WARP, create(WARP.identifier(),
				items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						EnchancementEnchantmentEffectComponentTypes.TELEPORT_ON_HIT,
						new TeleportOnHitEffect(
								true,
								false)
				)));
		// mace
		registry.register(METEOR, create(METEOR.identifier(), true,
				items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.MACE_EXCLUSIVE_SET));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.ERUPTION,
							new EruptionEffect(
									new AddValue(LevelBasedValue.constant(1.35F)),
									new AddValue(LevelBasedValue.perLevel(4))));
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
													ConstantFloat.of(1)),
											LevelBasedValue.constant(48))),
							LootItemEntityPropertyCondition.hasProperties(
									LootContext.EntityTarget.DIRECT_ATTACKER,
									EntityPredicate.Builder.entity()
											.flags(EntityFlagsPredicate.Builder.flags().setIsFlying(false))
											.moving(MovementPredicate.fallDistance(MinMaxBounds.Doubles.atLeast(1.5)))));
				}));
		registry.register(THUNDERSTRUCK, create(THUNDERSTRUCK.identifier(), true,
				items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.MACE_EXCLUSIVE_SET));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.CHAIN_LIGHTNING,
							new AddValue(LevelBasedValue.perLevel(0.35F)));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.LIGHTNING_DASH,
							new LightningDashEffect(
									new AddValue(LevelBasedValue.constant(3)),
									new AddValue(LevelBasedValue.perLevel(0.8F, 0.3F)),
									new AddValue(LevelBasedValue.perLevel(1)),
									new AddValue(LevelBasedValue.perLevel(0.8F, 0.3F))));
				}));
		// excavating tool
		registry.register(BURROWING, create(BURROWING.identifier(),
				items.getOrThrow(EnchancementItemTags.EXCAVATING_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.ARMOR_DENTING,
							new AddValue(LevelBasedValue.perLevel(1 / 8F)));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.WIDE_MINING,
							new AddValue(LevelBasedValue.constant(0.5F)));
				}));
		// pickaxe
		registry.register(EXTRACTING, create(EXTRACTING.identifier(),
				items.getOrThrow(ItemTags.PICKAXES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.exclusiveWith(enchantments.getOrThrow(EnchancementEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.MINE_ORE_VEINS,
							new AddValue(LevelBasedValue.perLevel(0.5F)));
				}));
		// axe
		registry.register(BEHEADING, create(BEHEADING.identifier(),
				items.getOrThrow(ItemTags.AXES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.HEAD_DROPS,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.VICTIM,
							new AddValue(LevelBasedValue.perLevel(0.5F)),
							LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypes.PLAYER))));
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.HEADSHOT,
							new AddValue(LevelBasedValue.perLevel(1.5F)));
				}));
		registry.register(LUMBERJACK, create(LUMBERJACK.identifier(),
				items.getOrThrow(ItemTags.AXES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.DISABLE_BLOCKING,
							new AddValue(LevelBasedValue.perLevel(2.5F)));
					builder.withSpecialEffect(
							EnchancementEnchantmentEffectComponentTypes.FELL_TREES,
							new AddValue(LevelBasedValue.perLevel(0.25F))
					);
				}));
		// shovel
		registry.register(SCOOPING, create(SCOOPING.identifier(),
				items.getOrThrow(ItemTags.SHOVELS),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.BURY_ENTITY,
							new AddValue(LevelBasedValue.perLevel(16, -4)));
					builder.withEffect(
							EnchantmentEffectComponents.DAMAGE,
							new AddValue(LevelBasedValue.perLevel(1)));
					builder.withEffect(
							EnchantmentEffectComponents.EQUIPMENT_DROPS,
							EnchantmentTarget.ATTACKER,
							EnchantmentTarget.VICTIM,
							new AddValue(LevelBasedValue.perLevel(0.02F)),
							LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypes.PLAYER))));
				}));
		// hoe
		registry.register(APEX, create(APEX.identifier(),
				items.getOrThrow(ItemTags.HOES),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> {
					builder.withEffect(
							EnchancementEnchantmentEffectComponentTypes.CRITICAL_TIPPER,
							new CriticalTipperEffect(
									new AddValue(LevelBasedValue.constant(0.5F)),
									EnchancementParticleTypes.CRITICAL_TIPPER));
					builder.withEffect(
							EnchantmentEffectComponents.DAMAGE,
							new AddValue(LevelBasedValue.perLevel(0.5F)));
				}));
		// fishing rod
		registry.register(DISARM, create(DISARM.identifier(),
				items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						EnchancementEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER,
						new DisarmingFishingBobberEffect(
								false,
								new AddValue(LevelBasedValue.perLevel(2.5F)),
								new AddValue(LevelBasedValue.perLevel(12, -2)))
				)));
		registry.register(GRAPPLE, create(GRAPPLE.identifier(),
				items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
				2,
				EquipmentSlotGroup.MAINHAND,
				builder -> builder.withEffect(
						EnchancementEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER,
						new AddValue(LevelBasedValue.perLevel(1))
				)));
	}

	public interface EffectsAdder {
		void addEffects(Enchantment.Builder builder);
	}
}
