# Macro
- Minigame run the pre-setup commands
- Bukkit: `Spigot`
- Type: `Fake`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Player who joined this minigame will perform macro command for each
tutorial lines. But if a command starting with "#" then the command will be executed by
the server bukkit as a OP.
> e.g. `help`: player will run `help` command as ownself  
> e.g. `#say Hi, <player>`: Bukkit will run command as a OP

- Command in tutorial doesn't need to start with slash(`/`) 
- Support placeholder: `<player>` means joining player's name. 



# Play Video
- [Youtube](https://youtu.be/wJe4FHQYFpM)



# Config
```yaml
Macro:
  title: Macro
  min-players: 1
  max-players: 1
  waiting-time: 1
  play-time: 1
  active: true
  icon: STONE
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
  - help
  - '#say Hi, <player>'
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
    color: RESET
    food-level-change: true
    player-hurt: true
```
- 



# Warning
- This is fake minigame, not real minigame.