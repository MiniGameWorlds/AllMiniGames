# RockScissorPaper
- `Rock`, `Scissor`, `Paper` !
- Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- Enter chat: `R` or `S` or `P`

# Play Video
- [Youtube](https://www.youtube.com/watch?v=-Gf2OqfEDps)

# Config
```yaml
RockScissorPaper:
  title: RSP
  min-player-count: 2
  max-player-count: 2
  waiting-time: 15
  play-time: 30
  active: true
  icon: SHEARS
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
  - 'enter chat: R or S or P'
  - Result will be appeared at the end
  - selection can be changed
  custom-data:
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
    color: RESET
    food-level-change: true
    player-hurt: true
```

# Warning
<!-- 
- <e.g. - Avoid building with Brick_Block>
- <e.g. - Make sure PVP on>
- <e.g. - Player can die while playing> 
-->
- `min-player-count` and `max-player-count` must be 2