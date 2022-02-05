# TimingPVP
- PVP with random item & timing
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- All players' armor and weapons will be changed regularly with random
- Die: -1

# Play Video
- [Youtube](https://youtu.be/FoKiBWDDuKY)

# Config
```yaml
TimingPVP:
  title: TimingPVP
  min-player-count: 2
  max-player-count: 10
  waiting-time: 15
  time-limit: 90
  active: true
  icon: CHORUS_FRUIT
  view: true
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - All players' armor and weapons will be changed regularly
  - All players' armor and weapons  will be changed regularly
  - 'Die: ยงc-1'
  custom-data:
    timingDelay: 10
    randomItems:
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: WOODEN_SWORD
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: STONE_SWORD
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: IRON_SWORD
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: GOLDEN_SWORD
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: DIAMOND_SWORD
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: WOODEN_AXE
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: STONE_AXE
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: IRON_AXE
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: GOLDEN_AXE
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: DIAMOND_AXE
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: BOW
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: CROSSBOW
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: TRIDENT
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: true
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RESET
    food-level-change: true
    player-hurt: true
```
- `timingDelay`: player item reset delay
- `randomItems`: random item list (except for armor, off hand)

# Warning
- Nothing