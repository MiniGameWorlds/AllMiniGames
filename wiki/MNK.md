# MNK
- Minigame like Tic-Tac-Toe
- Bukkit: `Spigot` 
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- You win if you place `k` blocks in a row with your own color blocks on the `m` x `n` board
- Turn game two players place own block on the board

# Play Video
- [Youtube](https://www.youtube.com/watch?v=HHfuNiRLALI)

# Config
```yaml
MNK:
  title: MNK
  min-players: 2
  max-players: 2
  waiting-time: 10
  play-time: 600
  active: true
  icon: CRAFTING_TABLE
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
    board-pos1:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    board-pos2:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    board-block: OAK_WOOD
    white-play-block: WHITE_WOOL
    black-play-block: BLACK_WOOL
    turn-time: 30
    fly: false
    length: 5
    finish-delay: 10
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
### custom-data section
- `board-pos1`: Board first position
- `board-pos2`: Board second position
- `board-block`: Board base block(Material)
- `white-play-block`: White play block (Material)
- `black-play-block`: Black play block (Material)
- `turn-time`: Turn time (second)
- `fly`: Players fly (true / false)
- `length`: block length for win (`k`)
- `finish_delay`: delay after finish (second)

# Warning
- Board must bigger than `custom-data.length`(`k`)

