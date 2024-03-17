# Enchancement
Enchancement is a mod that aims to completely overhaul vanilla Minecraft's enchanting system, along with some other adjacent mechanics.

Enchancement currently adds 30 unique and fun enchantments to the game:

```
Helmet Enchantments:
* Assimilation: Automatically consumes food when you're hungry, prioritizing off hand. Food and drinks are consumed faster.
* Perception: Increases visibility in the dark and causes mobs to be seen through walls.
* Veil: Decreases monster detection range and hides your name behind walls.

Chestplate Enchantments:
* Amphibious: Increases underwater mining and movement speed. Decreases air consumption underwater and time spent on fire. Immediately extinguishes you and allows usage of Riptide for 5 seconds after leaving water.
* Strafe: Double tapping Left Control (configurable) launches you in your movement direction.
* Wardenspine: Harms and blinds attackers and greatly decreases damage taken from behind.

Leggings Enchantments:
* Adrenaline: Increases movement speed and reduces damage taken proportional to missing health.
* Dash: Pressing Left Shift (configurable) in the air launches you in your look direction.
* Slide: Holding Left Control (configurable) while grounded causes you to slide constantly. Pressing Left Control (configurable) in the air causes you to slam towards the ground, knocking back mobs and negating fall damage.

Boots Enchantments:
* Buoy: Causes you to walk on fluids, descending via sneaking. Holding jump while submerged rapidly propels you upwards. Prevents floating. Grants a speed boost for 5 seconds after leaving water.
* Bouncy: Negates fall damage. Launches you upwards upon landing while not sneaking. Sneaking while grounded will charge a super jump.
* Gale: Allows you to jump more times in the air.

Sword Enchantments:
* Berserk: Increases damage dealt proportional to missing health.
* Frostbite: Freezes attacked mobs. When killed, turns them into ice statues that can be moved or shattered, launching ice shards that freeze other mobs.

Bow Enchantments:
* Chaos: Gives arrows a random negative potion effect, or positive if sneaking.
* Delay: Arrows will pause after shooting, building up damage over time. Can be launched prematurely by punching, and additionally redirected by sneaking.
* Phasing: Causes arrows to have no gravity and phase through blocks, upon which they regain gravity. Arrows home in on nearby entities.

Crossbow Enchantments:
* Brimstone: Fires a piercing laser that ignores armor and increases damage with charge and distance. Costs health to fire.
* Scatter: Allows Amethyst Shards to be loaded which explode into a burst of projectiles.
* Torch: Allows rapid loading of Torches that ignite shot mobs. Torches that land on a block will be placed if fired while sneaking. The Crossbow can only load Torches.

Trident Enchantments:
* Leech: Regenerates health when dealing damage. Thrown Tridents will temporarily stick to mobs and deal damage.
* Warp: Teleports you to the location of the Trident if it didn't hit a mob.

Pickaxe Enchantments:
* Extracting: Extracts materials from connected ores, leaving the stone behind. Disabled while sneaking.
* Molten: Smelts mined blocks. Disabled while sneaking.

Axe Enchantments:
* Beheading: Slain mobs and players have a chance to drop their head.
* Lumberjack: Connected logs will also be destroyed. Disabled while sneaking.

Shovel Enchantments:
* Bury: Using on a mob will bury it if the ground below is soft enough.
* Scooping: Deals extra damage and increases mob loot drops.

Fishing Rod Enchantments:
* Disarm: Steals or disables a latched entity's held item.
* Grapple: Allows the Fishing Bobber to latch onto blocks. Launches you towards that block upon release. Reels in entities with more strength.
```

Of the core changes, this mod will:
* Overhaul the Enchanting Table with a new UI that removes the RNG involved in enchanting; you may simply choose the enchantments you wish to have.
* Force items to only allow one enchantment.
* Remove leveling from enchantments, meaning that the max level for all enchantments will be 1. Enchantments that are brought down to this level will act as their original max level counterpart, an example being Riptide I will function the same as Riptide III. Enchantments on gear made from materials easier to obtain will be weaker.
* Remove durability from most aspects of the game. There is an item tag to allow certain items to keep durability.
* Remove most vanilla enchantments from the game. Enchancement tries to make choosing your enchantments more fun and engaging by replacing many of the boring stat change enchantments with enchantments that grant the player unique abilities.

Enchancement also adds some quality of life changes:
* Add Coyote Bite, which allows attacking an entity for 3 ticks after it leaves your crosshair.
* Adds descriptions to the tooltip of enchanted items and colors enchantment names green.
* Allows keybinds (Vanilla and Enchancement by default, configurable) to be bound to the same key without conflicting.
* Fishing Bobbers are accurately thrown instead of having a weird offset.
* All Tridents have Loyalty built in as a native effect.
* Arrows shot from Bows will drop on the ground near the shot entity, allowing recollection.
* Channeling works during any weather, not just when thundering. Channeling also creates lightning that does not create fire or destroy items, and ignites entities for 3 seconds on melee attack.
* Crossbows can pull projectiles from your inventory instead of just from your off-hand.
* Drowned will use the data of their held Trident when throwing it.
* Enchanted chestplates will increase your air mobility.
* Bows are drawn faster and don't decrease your movement speed as much.
* Fire Aspect is usable as Flint and Steel if you are sneaking, and ignites for less time.
* Enchanted Books are free to merge with items in an Anvil.
* Luck of the Sea acts as if the item also has Lure.
* Ender Pearls no longer deal damage upon landing.
* Projectiles ignore invulnerability frames (with damage falloff) and negate velocity.
* Mobs can spawn with any random enchantment.
* Iron armor has been made stronger and Golden armor has been made slightly weaker.
* Tridents will automatically return to you if thrown in the void.
* Golden Apples can only be eaten while hungry.
* Makes a select few potions a bit weaker to account for the removal of some enchantments.
* Weapon enchantments such as Fire Aspect or Frostbite will not function unless the player's attack cooldown is >= 70%.

**Q: Can I disable any of these changes?**

A: Everything in the mod is configurable down to disabling the entire mod if there are features that you wish to disable. All of the config options are self explanatory except for enchantmentLimit (0 means no limit, greater than 0 means items cannot have more than that number of enchantments) and unbreakingChangesFlag (0 means all items are unbreakable, greater than 0 means items with x level of Unbreaking are unbreakable, and less than 0 means disabled). Items in the `enchancement:retains_durability` tag will ignore these changes and continue to have durability regardless of the setting.

**Q: Why did you remove most of the vanilla enchantments?**

A: I do not like how much of the vanilla enchanting meta relies exclusively on Protection, Sharpness, Power, and other such stat increasing enchantments. I also dislike Fortune because it creates a mindset of not mining ores until you have Fortune, which I find incredibly dumb.

**Q: I want to keep every enchantment anyway. How do I do this?**

A: In the configuration file, remove all entries from disallowedEnchantments.

**Q: I changed the config and now my gear is either too weak or too strong, can you fix this?**

A: Enchancement is heavily designed around using the default configuration file, any balance concerns created by modifying the config will not be addressed as it is outside of the scope of the mod's vision.
