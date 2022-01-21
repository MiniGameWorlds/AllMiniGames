# FitTool
- Break blocks with fit tools
- Available Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- Type: `Solo`
- Version: `LATEST`
- Available Minecraft Versions: `1.14+`

# How to play
- Break blocks with given tools
- Get `1` score per block

# Config
```yaml
FitTool:
  title: FitTool
  min-player-count: 1
  max-player-count: 1
  waiting-time: 10
  time-limit: 30
  active: true
  icon: STONE_PICKAXE
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - Break blocks with fit tools
  - 'Breaking block: +1'
  custom-data:
    blocks:
    - COBWEB
    - OAK_WOOD
    - COBBLESTONE
    - DIRT
    pos1:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    pos2:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    inventory-save: true
    minigame-respawn: true
```
- `blocks`: random block list (upper case of block name)
- Random blocks will be filled between `pos1` and `pos2`


# Warning
- Setup `custom-data.pos1` and `custom-data.pos2` before playing