# Dual Inventory

**Two independently saved player inventory sets for Minecraft Forge 1.20.1.** Switch between Inventory A and Inventory B with a configurable keybind.

> This repository is being prepared as the source-code home for Dual Inventory. See the build notes below before compiling.

## Features

- **Two inventory sets (A/B):** keep separate item and equipment loadouts.
- **Instant switching:** send a swap request to the server using the client keybind.
- **Per-player saved data:** inventory sets are stored through Forge capabilities.
- **Optional integrations:** supports Traveler's Backpack and Curios when those mods are installed.
- **Client HUD indicator:** shows which inventory set is active.

## Compatibility

| Component | Target |
|---|---|
| Minecraft | Java Edition 1.20.1 |
| Mod loader | Minecraft Forge |
| Java | Java 17 |
| Mod ID | `dualinventory` |
| Mod version | `1.0.0` |
| License declared by the project | MIT |

### Optional mod integrations

- **Traveler's Backpack** — allows the worn backpack to participate in inventory switching.
- **Curios API** — allows Curios equipment slots to participate in switching.

Both integrations are optional. Dual Inventory is intended to run without either mod.

## Controls

The current source registers **Z** as the inventory-switch key. Check Minecraft's **Options → Controls → Key Binds** to review or change the binding.

## Project layout

```
dualinv/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── gradlew
└── src/main/
    ├── java/com/example/dualinventory/
    │   ├── capability/   # Saved inventory data and swap service
    │   ├── client/       # Keybind, client events, and HUD indicator
    │   ├── compat/       # Optional mod integrations
    │   ├── event/        # Player lifecycle events
    │   └── network/      # Client-to-server swap packet
    └── resources/
        ├── META-INF/mods.toml
        └── assets/dualinventory/lang/
```

## Build notes

The Gradle project targets Forge 1.20.1 and Java 17. However, the source snapshot's own development notes warn that some code uses Minecraft SRG-mapped names and that a standard ForgeGradle build may not compile without adjustments. Treat a successful build as **not yet verified** until you build and test it in a clean environment.

From the `dualinv` project directory, with a Java 17 JDK installed, try:

```bash
./gradlew build
```

On Windows:

```bat
gradlew.bat build
```

If the build fails, retain the complete Gradle error output when reporting the issue. Do not assume the resulting JAR is tested merely because the Gradle task starts.

## Testing checklist

- [ ] Build with Java 17 and Forge 1.20.1.
- [ ] Test switching A ↔ B in single-player.
- [ ] Verify items do not duplicate or disappear after repeated swaps.
- [ ] Test death, respawn, and dimension changes.
- [ ] Test on a dedicated server with a second player.
- [ ] Test optional Traveler's Backpack and Curios integrations separately.

## Known testing note

The development README documents a previous first-switch duplication bug and a Curios synchronization change that still needed final in-game confirmation. These behaviors should be explicitly retested before treating the mod as release-ready.

## License

The mod metadata declares **MIT**. A complete repository should include a top-level `LICENSE` file with the full MIT license text and the correct copyright holder before redistribution.

## Contributions and bug reports

When reporting a bug, include:
- Minecraft and Forge versions
- Java version
- Whether the issue occurs in single-player or multiplayer
- Other inventory/equipment mods installed
- The complete relevant log or crash report
- Steps to reproduce the issue


## Source tree status

The Java sources, Forge metadata, Gradle build files, language resources, and project notes are in the `dualinv/` directory. The Gradle wrapper JAR (`dualinv/gradle/wrapper/gradle-wrapper.jar`) is not yet present in this GitHub copy, so the checked-in `gradlew` launcher is not available here; use an installed Gradle command or add the wrapper JAR from the original source archive before using the wrapper.
