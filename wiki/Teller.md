# Teller
- Tutorials will be shown to the player
- Bukkit: `Spigot`
- Type: `Fake`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Setup tutorials which will be shown to the player



# Play Video
- [Youtube](https://youtu.be/HkM1zigblus)



# Config
```yaml
Teller:
  title: Teller
  min-players: 1
  max-players: 1
  waiting-time: 1
  play-time: 1
  active: true
  icon: BOOK
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
  - §a§l[Tutorial]
  - This game print tutorial
  - Edit this minigame tutorial
  - ''
  custom-data:
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
    color: AQUA
    food-level-change: true
    player-hurt: true
```
- `tutorial`: messages which will be sended to the player



# Warning
- This is fake minigame, not real minigame.