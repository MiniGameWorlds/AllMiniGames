# Clock
- Jump over the clock hand!
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Clock hand will be more faster
- Clock hand can change direction randomly



# Play Video
- [Youtube](https://youtu.be/Tzq334h6XdE)



# Config
```yaml
Clock:
  title: Clock
  min-player-count: 2
  max-player-count: 10
  waiting-time: 20
  play-time: 120
  active: true
  icon: CLOCK
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
  tutorial: null
  custom-data:
    center:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    hand-length: 5.0
    hand-speed: 0.0
    hand-speed-increment: 0.01
    clockwise: true
    random-direction-mode: false
    particle: FLAME
    chat: true
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
    food-level-change: false
    player-hurt: false
```
- `center`: clock center (Location)
- `hand-length`: clock hand length per block (decimal) 
- `hand-speed`: round per second (decimal) (e.g. 0.5 = 0.5 round per second)
- `hand-speed-increment`: `clock-speed` increment per second (decimal)
- `clockwise`: true = clockwise, false = anticlockwise (true / false)
- `random-direction-mode`: true = hand direction changes randomly (true / false)
- `particle`: particle ([Particle](https://minecraft.fandom.com/wiki/Particles) must be **upper case**)


# Warning
- Turn on `Particles` in video settings
- Pillar in the center can prevent that challengers only move around the clock center