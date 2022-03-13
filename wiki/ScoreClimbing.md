# ScoreClimbing
- The Score will climb, but can fall down from random spot
- Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- `Chat`: check current score(max: 3)
- `Sneak`: stop Game and check score

# Play Video
- [Youtube](https://www.youtube.com/watch?v=vRAdgDn6u2Q)

# Config
```yaml
ScoreClimbing:
  title: ScoreClimbing
  min-player-count: 2
  max-player-count: 4
  waiting-time: 20
  play-time: 60
  active: true
  icon: OAK_STAIRS
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
  - 'Every 1 sec: score get to plus until random timing and after then score get to
    minus until the game end'
  - 'Chat: check current score(max: 3)'
  - 'Sneak: stop Game and check score'
  custom-data:
    chatting: true
    score-notifying: false
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