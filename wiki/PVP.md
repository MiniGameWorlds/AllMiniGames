# PVP
- Player vs Player
- Type: `SoloBattle`
- Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- Fight each other with given tools and forced health scale

# Play Video
- [Youtube](https://www.youtube.com/watch?v=PMmOHS4mbLc)

# Config
```yaml
PVP:
  title: PVP
  min-player-count: 2
  max-player-count: 5
  waiting-time: 30
  play-time: 180
  active: true
  icon: STONE_SWORD
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - 'kill: +1'
  custom-data:
    health: 30
    items:
    - ==: org.bukkit.inventory.ItemStack
      v: 2730
      type: STONE_SWORD
    - ==: org.bukkit.inventory.ItemStack
      v: 2730
      type: BOW
    - ==: org.bukkit.inventory.ItemStack
      v: 2730
      type: ARROW
      amount: 32
    - ==: org.bukkit.inventory.ItemStack
      v: 2730
      type: COOKED_PORKCHOP
      amount: 10
    - ==: org.bukkit.inventory.ItemStack
      v: 2730
      type: GOLDEN_APPLE
    - ==: org.bukkit.inventory.ItemStack
      v: 2730
      type: WOODEN_AXE
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
    color: RED
```
- `health`: player's forced health scale (2 = 1 heart)
- `items`: took kits (can edit)

# Warning
- nothing