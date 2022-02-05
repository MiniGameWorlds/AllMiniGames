# OnePunch
- Just punch
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- Hit other player: +1
- Hit by other player: die

# Play Video
- [Youtube](https://www.youtube.com/watch?v=iKNFxQwEWAw)

# Config
```yaml
OnePunch:
  title: OnePunch
  min-player-count: 2
  max-player-count: 10
  waiting-time: 10
  time-limit: 60
  active: true
  icon: GRASS
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
  - 'Hit other player: +1'
  - 'Hit by other player: die'
  custom-data:
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: false
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RESET
    food-level-change: true
    player-hurt: false
```

# Warning
<!-- 
- <e.g. - Avoid building with Brick_Block>
- <e.g. - Make sure PVP on>
- <e.g. - Player can die while playing> 
-->
- Nothing