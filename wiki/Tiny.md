# Tiny
- Hit a tiny `bat` with snowball
- Bukkit: `Spigot` 
- Type: `Solo`
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- Gets score when hit a bat with your snowball

# Play Video
- [Youtube](https://www.youtube.com/watch?v=wA10tNRT0cU)

# Config
```yaml
Tiny:
  title: Tiny
  min-players: 1
  max-players: 1
  waiting-time: 5
  play-time: 60
  active: true
  icon: OAK_BUTTON
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - 'Hit bat: ยงa+1'
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
    color: YELLOW
    food-level-change: true
    player-hurt: true
```

# Warning
- Make sure that bat can not escape from your minigame place (even removed game finised)