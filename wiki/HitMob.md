# HitMob
- Hit mobs until you die!
- Bukkit: `Spigot` 
- Type: `Solo`
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- Get score for damage to mob
- If you die, game will be finish


# Play Video
- [Youtube](https://www.youtube.com/watch?v=nBfuKsp5HaE)

# Config
```yaml
HitMob:
  title: HitMob
  min-players: 1
  max-players: 1
  waiting-time: 5
  play-time: 60
  active: true
  icon: WOODEN_SWORD
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
  - ''
  - ''
  custom-data:
    tools:
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: WOODEN_SWORD
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: BOW
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: ARROW
      amount: 10
    mob: SKELETON
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RED
    food-level-change: true
    player-hurt: true
```
- `custom-data.tools`: Can change player tools
- `custom-data.mob`: Mob type which will spawn when the game starts

# Warning
- Mob doesn't get damage, do not set to creeper or other mobs which has exceptions