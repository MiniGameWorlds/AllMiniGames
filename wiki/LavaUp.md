# LavaUp
- Run away from the lava!
- Bukkit: `Spigot` 
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Lava will come up to the ceiling!
- Players have to run away from the lava




# Play Video
- [Youtube](https://youtu.be/ANY7zvUzv2w)



# Config
```yaml
LavaUp:
  title: LavaUp
  min-player-count: 2
  max-player-count: 10
  waiting-time: 20
  play-time: 120
  active: true
  icon: LAVA
  view: true
  scoreboard: true
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - Run away from the §c§lLAVA
  custom-data:
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
    up-speed: 3.0
    up-scale: 1
    chat: true
    score-notifying: false
    block-break: false
    block-place: false
    pvp: true
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RED
    food-level-change: false
    player-hurt: true
```
- `pos1`: First position of area which will be filled up with lava
- `pos2`: Second position of area which will be filled up with lava
- `up-speed`: lava up speed (sec) (decimals)
- `up-scale`: lava up scale



# Warning
- Map must be made of not burning blocks
- Before play, setup `pos1` and `pos2`
- **Y** of "pos1" is the floor lava height
- **Y** of "pos2" is the max lava height