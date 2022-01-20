# RandomScore
- Literally random score minigame
- Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- Sneak to get random score

# Play Video
- <video url>

# Config
```yaml
RandomScore:
  title: RandomScore
  min-player-count: 2
  max-player-count: 4
  waiting-time: 10
  time-limit: 20
  active: true
  icon: DISPENSER
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
  - 'Sneak: get random score'
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
- Nothing