# DodgeBlock
- Dodge the falling blocks!
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Dodge the falling blocks!
- You will die if hit by the falling blocks
- If someone died, others get a score



# Play Video
- [Youtube](https://www.youtube.com/watch?v=3CQUab1ysSE)



# Config
```yaml
DodgeBlock:
  title: DodgeBlock
  min-players: 2
  max-players: 10
  waiting-time: 20
  play-time: 120
  active: true
  icon: GLOWSTONE
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
  - Dodge the falling block!
  custom-data:
    blocks:
    - GLOWSTONE
    - LANTERN
    - LANTERN
    - CAKE
    spawn-rate: 1
    spawn-rate-increase: 1
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
    chat: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: GRAY
    food-level-change: false
    player-hurt: true
```
- `blocks`: Falling block list 
- `spawn-rate`: falling block spawn count per second
- `spawn-rate-increase`: `spawn-rate` increasement per second
- `pos1`: first born area position of falling blocks
- `pos2`: second born area position of falling blocks



# Warning
- Before playing, setup `pos1` and `pos2` for falling block born area. (falling blocks will spawn between the `pos1` and `pos2`)
- If you add non-block to `blocks`, the block will not be spawned