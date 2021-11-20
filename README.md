# Rules
- Manage worldbiomusic's all minigames of [MiniGameWorld](https://github.com/MiniGameWorlds/MiniGameWorld) in this project
- `Version` will never be changed
- All minigames api version will maintain `LATEST`
- Minigames not released in [Discussion](https://github.com/MiniGameWorlds/MiniGameWorld/discussions/categories/minigames) are not stable

## Base plugin.yml format
```yaml
name: AllMiniGames
main: com.worldbiomusic.allgames.AllMiniGamesMain
version: 1.0
author: worldbiomusic
api-version: 1.14
depend: [MiniGameWorld]
```

## When Release
- Change `name`, `version` in `plugin.yml` for each minigames
- Register only released minigames in `AllMiniGamesMain.java`