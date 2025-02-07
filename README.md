# Enchancement
Enchancement is a mod that aims to completely overhaul vanilla Minecraft's enchanting system, along with some other adjacent mechanics.

Enchancement currently adds 33 unique and fun enchantments to the game:

```
Helmet Enchantments:
* Assimilation: Automatically consumes food when you're hungry, prioritizing off hand. Food and drinks are consumed faster.
* Perception: Increases visibility in the dark and causes mobs to be seen through walls.
* Veil: Decreases monster detection range and hides your name behind walls.

Chestplate Enchantments:
* Adrenaline: Increases movement speed and reduces damage taken proportional to missing health.
* Amphibious: Increases underwater mining and movement speed. Decreases air consumption underwater and time spent on fire. Immediately extinguishes you and allows usage of Riptide for some time after leaving water.
* Strafe: Pressing Left Shift (configurable) launches you in your movement direction.
* Wardenspine: Harms and blinds attackers and greatly decreases damage taken from behind.

Leggings Enchantments:
* Dash: Pressing Left Control (configurable) in the air launches you forward.
* Gale: Allows you to jump more times in the air. Holding Space (configurable) allows you to briefly glide.
* Slide: Holding Left Control (configurable) while grounded causes you to slide constantly. Pressing Left Control (configurable) in the air causes you to slam towards the ground, knocking back mobs and negating fall damage.

Boots Enchantments:
* Buoy: Causes you to walk on fluids, descending via sneaking. Holding jump while submerged rapidly propels you upwards. Prevents floating. Grants a speed boost for some time after leaving water.
* Bouncy: Negates fall damage. Launches you upwards upon landing while not sneaking. Sneaking while grounded will charge a super jump.
* Sticky: Allows sliding down and jumping off walls. Leaves a trail of honey where you walk that slows mobs.

Sword Enchantments:
* Berserk: Increases damage dealt proportional to missing health.
* Frostbite: Freezes attacked mobs. When killed, turns them into ice statues that can be moved or shattered, launching ice shards that freeze other mobs.

Bow Enchantments:
* Chaos: Gives arrows a random negative potion effect, or positive if sneaking.
* Delay: Arrows will pause after shooting, building up damage over time. Can be launched prematurely by punching, and additionally redirected by sneaking.
* Phasing: Causes arrows to have no gravity and phase through blocks, upon which they regain gravity.

Crossbow Enchantments:
* Brimstone: Fires a piercing laser that ignores armor and increases damage with charge and distance. Costs health to fire.
* Scatter: Allows Amethyst Shards to be loaded which explode into a burst of projectiles.
* Torch: Allows rapid loading of Torches that ignite shot mobs. Torches that land on a block will be placed if fired while sneaking. The Crossbow can only load Torches.

Trident Enchantments:
* Leech: Regenerates health when dealing damage. Thrown Tridents will temporarily stick to mobs and deal damage.
* Warp: Teleports you to the location of the Trident if it didn't hit a mob.

Mace Enchantments:
* Meteor: Causes smash attacks to ignite attacked mobs and pull them down. Allows charging to launch you upwards, damaging and igniting nearby mobs.
* Thunderstruck: Direct attacks chain to nearby mobs. Allows charging to launch you forward and briefly float. Punching while floating causes you to dash forward and smash into the ground.

Mining Tool Enchantments:
* Molten: Smelts mined blocks. Disabled while sneaking.

Pickaxe Enchantments:
* Extracting: Extracts materials from connected ores, leaving the stone behind. Disabled while sneaking.

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

Everything Enchancement adds is configurable:
* Disallowed Enchantments:
  * Remove most vanilla enchantments from the game. Enchancement tries to make choosing your enchantments more fun and engaging by replacing many of the stat change enchantments with enchantments that grant the player unique abilities.
* Overhaul Enchanting Table
  * Replaces the Enchanting Table UI with a new version that removes the RNG involved in enchanting; you may simply choose the enchantments you wish to have.
  * To balance the ability to freely choose enchantments, enchanting items now has an additional material cost, such as Diamonds for Diamond equipment.
  * Bookshelves now reduce the experience, lapis, and material cost of enchanting.
* Single Level Mode
  * Remove leveling from enchantments, meaning that the max level for all enchantments will be 1. Enchantments that are brought down to this level will act as their original max level counterpart, an example being Luck of the Sea I will function the same as Luck of the Sea III. Enchantments on gear made from materials easier to obtain will be weaker.
* Enchantment Limit
  * Force items to only allow one enchantment by default.
* Disable Durability
  * Remove durability from most aspects of the game. Items in the `enchancement:retains_durability` tag will continue to have durability.
  * Anvils will not break when used, unless they are part of the tag.
* Disable Velocity Checks
  * Removes the checks to determine if a player is moving too fast, since Enchancement adds many forms of movement that may trigger that.
* Enhance Mobs
  * Hostile monsters will spawn with random enchantments.
* Free Enchanted Book Merging
  * Enchanted Books can be applied to items in an Anvil at no cost.
* Rebalance Consumables
  * Arrows shot by players will drop on the ground after hitting an entity.
  * Ender Pearls deal no damage upon use.
  * (Enchanted) Golden Apples can only be consumed when hungry.
* Rebalance Enchantments
  * Fire Aspect can be used like Flint and Steel while sneaking.
  * Ignite enchantment effects apply for less time.
  * Channeling ignites entities with melee attacks.
  * Channeling can be activated without thunder.
  * Lightning spawned from Channeling will not destroy items or cause fire.
  * Trident Spin Attack enchantment effects are weaker.
  * Trident Spin Attack enchantment effects have no water drag.
  * Maces cannot have Fire Aspect.
  * Mace enchantments can be applied with less fall distance.
  * Wind Burst can launch Wind Charges on use.
  * Luck of the Sea also reduces fishing time.
* Rebalance Equipment
  * Enchanted armor has additional damage reduction.
  * Iron armor has slightly more armor, and gains armor toughness.
  * Golden armor has slightly less armor.
  * Animal armor can be enchanted with boots enchantments.
  * Bows slow you down less when charging.
  * Crossbows pull unique ammunition from your inventory instead of only from off-hand.
  * Tridents with Loyalty linger in the world if the owner is dead instead of dropping on the ground.
  * Tridents with Loyalty that are thrown into the void will automatically return to their owner.
  * Maces have slightly faster attack speed.
  * Maces deal less damage per block fallen using a logarithmic function.
  * Golden tools have the mining speed of Diamond tools, Diamond tools have the mining speed of Netherite tools, and Netherite tools have the mining speed of Golden tools.
  * Fishing Bobbers are thrown more accurately.
* Rebalance Projectiles
  * Projectiles ignore invulnerability frames and deal less damage per hit that would have been blocked.
  * Projectiles negate velocity on hit.
  * Projectiles shot by players take target velocity into account to artificially increase their hitbox, making them easier to hit when moving fast.
  * Projectiles are shot with higher velocity.
* Rebalance Status Effects
  * Instant Health, Instant Damage, Strength, and Weakness are less potent.
  * Wither Skulls apply Wither for less time.
* Toggleable Passives
  * Enchanted chestplates increase air velocity.
  * Enchanted Tridents have Loyalty built in.
  * Enchanted mining tools have Efficiency built in.
* Weapon Enchantment Cooldown Requirement
  * Enchantments such as Fire Aspect or Frostbite require your weapon cooldown to be near full to activate.
* Coyote Bite
  * Entities can be attacked for a few ticks after they leave your crosshair,
* Enchantment Descriptions
  * Enchantments display what they do under their name.
* Colored Enchantment Names
  * Enchantments are colored green, similar to how Curses are colored red.
* Allow Duplicate Keybindings
  * All keybindings that are set to the same key will activate at the same time instead of only one applying.

## External Links
Support me on Ko-fi! I release new versions of my mods early here <3

[![Ko-fi](https://i.imgur.com/6pkJV6h.png)](https://ko-fi.com/moriyashiine)

Join my Discord!

[![Discord](https://i.imgur.com/72QzxP1.png)](https://discord.gg/Am6M8VQ)
