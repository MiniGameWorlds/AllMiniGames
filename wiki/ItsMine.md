# ItsMine
- It's my item!!
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Get 1 score per 1 second if a player is having the item
- Hit another player to steal the item
- If any player reached the `finish-score`, game will be finished
- If left finish time is under the `glow-time`, a player having the item will be glowing
- When game starts, random player will be given the item



# Play Video
- [Youtube](https://youtu.be/YiJ7HUUMMQk)



# Config
```yaml
ItsMine:
  title: ItsMine
  min-player-count: 2
  max-player-count: 10
  waiting-time: 20
  time-limit: 120
  active: true
  icon: DIAMOND
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
  - Hit and steal item!
  custom-data:
    item: DIAMOND
    glow-time: 30
    finish-score: 100
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
- `item`: item (Material)
- `glow-time`: glow time (sec)
- `finish-score`: finish score



# Warning
- Nothing