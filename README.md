# Rules
- Manage all minigames of [MiniGameWorld](https://github.com/MiniGameWorlds/MiniGameWorld) in this project
- `Version` will never be changed
- All minigames version will maintain `LATEST`

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
- Change `name`, `version` in `plugin.yml` for each
- Register only specific minigames in `AllMiniGamesMain.java`(Main)