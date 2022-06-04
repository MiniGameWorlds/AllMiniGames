# Hungry Fishing
- Catch a food by fishing!
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Fish and fill your hunger with items!
- If you fail to catch, you will be hungry
- If you fulfill all your hunger, will get `left play time` score



# Play Video
- [Youtube](https://www.youtube.com/watch?v=ZKx_IU-eEcE)



# Config
```yaml
HungryFishing:
  title: HungryFishing
  min-players: 2
  max-players: 10
  waiting-time: 20
  play-time: 180
  active: true
  icon: FISHING_ROD
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
  - Fish and fill your hunger with items!
  - If you fail to catch, you will be hungry
  custom-data:
    catch-items:
      COOKIE: 40
      COOKED_PORKCHOP: 10
      MELON_SLICE: 30
      CARROT: 20
    hunger: 1
    fail-hunger: 2
    chat: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: false
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: AQUA
    food-level-change: true
    player-hurt: false
```
- `catch-items`: itme list when a player catch a fish
- `hunger`: player's starting hunger
- `fail-hunger`: player's hunger decrease amount when fail to catch a fish



# Warning
- Sum of `catch-items` chance percent must be 100%.
- Make sure that game map have water so players can catch a fish.