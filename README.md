# Sentinel
A Minecraft Fabric 1.21 admin mod for players with OP to run commands and handle gamerules without needing to type commands. Inspired by the ULX admin addon in Garry's Mod.

## Features

- **Admin Panel GUI**: Press F8 (configurable keybind) to open the admin panel (requires OP level 2+)
- **Command Execution**: Execute any command directly from the GUI without typing in chat
- **Gamerule Management**: Toggle common gamerules with a simple click interface
- **Player Management**: Kick, ban, teleport, change gamemode, and manage OP status for players
- **Broadcast & Announcements**: Send titles and subtitles to players using Minecraft's native title system
- **Permission-based**: Only accessible to players with operator permissions

## Project Structure

```
src/
├── main/java/com/leokayasen/sentinel/
│   └── Sentinel.java                     # Main mod class
├── client/java/com/leokayasen/sentinel/client/
│   ├── SentinelClient.java              # Client mod initializer
│   └── gui/
│       ├── AdminPanelScreen.java        # Main admin panel GUI
│       ├── GameruleScreen.java          # Gamerule management interface
│       ├── PlayerManagementScreen.java  # Player administration tools
│       └── BroadcastScreen.java         # Broadcast & announcements system
└── main/resources/
    ├── fabric.mod.json                  # Mod metadata
    └── sentinel.mixins.json             # Mixin configuration
```

## Usage

1. Install the mod on a Fabric 1.21 server and client
2. Ensure you have OP permissions (level 2 or higher)
3. Press F8 to open the admin panel
4. Use the various screens to manage the server:
   - **Main Panel**: Execute commands directly
   - **Gamerule Screen**: Toggle gamerules like keepInventory, doFireTick, mobSpawning, etc.
   - **Player Management**: Kick, ban, teleport, change gamemode, and manage OP status
   - **Broadcast & Announcements**: Send titles and subtitles to all players or specific players

## Building

> **Note**: The project currently has Fabric Loom version compatibility issues. The code is complete and functional, but the build system needs Fabric Loom version updates to match the current Fabric ecosystem.

To build (once build issues are resolved):
```bash
./gradlew build
```

The mod JAR will be available in `build/libs/`

## Implementation Details

- **GUI System**: Uses Minecraft's native Screen and Widget system for seamless integration
- **Command Execution**: Leverages the player's network handler to send commands as if typed in chat
- **Permission Checking**: Only displays and functions for players with OP level 2+
- **Keybind System**: Registers F8 key through Fabric's keybinding API
- **Localization**: Supports language files for internationalization
