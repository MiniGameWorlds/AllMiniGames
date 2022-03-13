# Spleef
- Dig other's bottom block!
- Bukkit: `Spigot` 
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- You can get score by digging blocks
- Make other players fall from the floor

# Play Video
- [Youtube](https://www.youtube.com/watch?v=Hs8T4LUuCSw)

# Config
```yaml
Spleef:
  title: Spleef
  min-player-count: 2
  max-player-count: 20
  waiting-time: 20
  play-time: 300
  active: true
  icon: STONE_SHOVEL
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - 'Break block: §a+1'
  - 'fallen: §cdie'
  custom-data:
    block: SNOW_BLOCK
    tool: STONE_SHOVEL
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
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: WHITE
    food-level-change: true
    player-hurt: true
```
- `custom-data.pos1`: Spleef floor first position (Location) 
- `custom-data.pos2`: Spleef floor second position (Location)
- `custom-data.block`: Spleef floor block material (Material)
- `custom-data.tool`: Tool which will give to players when game starts (Material)

# Warning
- Player will be processed fallen from the floor with `y` of `custom-data.pos2`
- You have to setup `custom-data.pos1` and `custom-data.pos2` before play