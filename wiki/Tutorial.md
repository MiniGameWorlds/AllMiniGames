# Tutorial
- Tutorial minigame for your server
- Bukkit: `Spigot` 
- Type: `Custom`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- Use for server tutorial

# Play Video
- [Youtube](https://www.youtube.com/watch?v=Nstng0YEiBg)

# Config
```yaml
Tutorial:
  title: Tutorial
  min-players: 9999
  max-players: 9999
  waiting-time: 9999
  play-time: 1
  active: true
  icon: BOOK
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
  - Look around the tutorials of minigame system
  - '§lWiki: §nhttps://github.com/MiniGameWorlds/MiniGameWorld/blob/main/resources/userWiki/Home.md'
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
    food-level-change: false
    player-hurt: false
```


# Warning
- Game will **NEVER** starts
- No event will process in this minigame