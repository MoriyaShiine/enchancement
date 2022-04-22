package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {
	public static final Enchantment EMPTY = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR);

	//helmet
	public static final Enchantment ASSIMILATION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	public static final Enchantment BUFFET = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	public static final Enchantment PERCEPTION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	//chestplate
	public static final Enchantment AMPHIBIOUS = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST);
	public static final Enchantment WARDENSPINE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST);
	//leggings
	public static final Enchantment DASH = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);
	public static final Enchantment IMPACT = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);
	public static final Enchantment SLIDE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);
	//boots
	public static final Enchantment ACCELERATION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
	public static final Enchantment BOUNCY = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
	public static final Enchantment GALE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
	//sword
	public static final Enchantment BERSERK = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
	public static final Enchantment FROSTBITE = new FrostbiteEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
	//bow
	public static final Enchantment CHAOS = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment DELAY = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment PHASING = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	//crossbow
	public static final Enchantment TORCH = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.CROSSBOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	//trident
	public static final Enchantment LEECH = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.TRIDENT, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment WARP = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.TRIDENT, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	//pickaxe
	public static final Enchantment EXTRACTING = new PickaxeEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	public static final Enchantment MOLTEN = new MoltenEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	//axe
	public static final Enchantment BEHEADING = new AxeEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	public static final Enchantment LUMBERJACK = new AxeEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	//shovel
	public static final Enchantment BURY = new ShovelEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	public static final Enchantment SCOOPING = new ShovelEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	//fishing rod
	public static final Enchantment DISARM = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment GRAPPLE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);

	public static void init() {
		//helmet
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "assimilation"), ASSIMILATION);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "buffet"), BUFFET);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "perception"), PERCEPTION);
		//chestplate
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "amphibious"), AMPHIBIOUS);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "wardenspine"), WARDENSPINE);
		//leggings
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "dash"), DASH);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "impact"), IMPACT);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "slide"), SLIDE);
		//boots
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "acceleration"), ACCELERATION);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "bouncy"), BOUNCY);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "gale"), GALE);
		//sword
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "berserk"), BERSERK);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "frostbite"), FROSTBITE);
		//bow
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "chaos"), CHAOS);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "delay"), DELAY);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "phasing"), PHASING);
		//crossbow
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "torch"), TORCH);
		//trident
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "leech"), LEECH);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "warp"), WARP);
		//pickaxe
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "extracting"), EXTRACTING);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "molten"), MOLTEN);
		//axe
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "beheading"), BEHEADING);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "lumberjack"), LUMBERJACK);
		//shovel
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "bury"), BURY);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "scooping"), SCOOPING);
		//fishing rod
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "disarm"), DISARM);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "grapple"), GRAPPLE);
	}
}
