# FallingBlock
- Floor will sink
- Bukkit: `Spigot` 
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- If player steps on the floor, the block will sink
- But sinked block will give score to the player

# Playe Video
- [Youtube](https://www.youtube.com/watch?v=f6_GAp6FOVs)

# Config
```yaml
FallingBlock:
  title: FallingBlock
  min-players: 2
  max-players: 4
  waiting-time: 10
  play-time: 120
  active: true
  icon: SAND
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - block will be disappeared after you stepped
  - 'remove block with step: +1'
  - 'fallen: die'
  custom-data:
    pos1:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    pos2:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    removing-block: STONE
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: true
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RESET
```
- `pos1`, `pos2`: Stage block area
- `removing-block`: Sink block material


# Warning
- Player will be die if falling down below the `y` coordinate of `pos1.y`
- Stage blocks (between `pos1` and `pos2`) will be filled every game starts