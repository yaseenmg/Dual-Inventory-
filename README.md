# Dual Inventory

Dual Inventory is a Minecraft Forge mod that lets each player keep two independently saved inventory and equipment sets, then switch between them with a keybind.

## Features

- Two persistent inventory sets, A and B
- Server-side inventory switching initiated by the client
- Per-player data saved through Forge capabilities
- Optional integrations with Traveler's Backpack and Curios
- An on-screen indicator for the active inventory set

## Compatibility

| Component | Version |
|---|---|
| Minecraft: Java Edition | 1.20.1 |
| Mod loader | Minecraft Forge 47.4.10 |
| Java | 17 |
| Mod ID | `dualinventory` |
| Mod version | 1.0.0 |

Traveler's Backpack and Curios are optional. The mod is intended to work without either integration installed.

## Controls

The default inventory-switch key is **Z**. Review or change it under **Options → Controls → Key Binds**.

## Build

Install a Java 17 JDK, then run these commands from the repository root:

```bash
./gradlew build
```

On Windows:

```bat
gradlew.bat build
```

The Gradle wrapper is included. A successful build does not by itself confirm the in-game behavior of inventory switching or optional integrations.

## Source layout

- `src/main/java/com/example/dualinventory/` — mod, inventory capabilities, swap service, client controls, events, networking, and integrations
- `src/main/resources/` — Forge metadata, access transformer, language text, and pack metadata
- `gradle/` — Gradle wrapper configuration

## Verification

The repository contains the source and build configuration. Test single-player and dedicated-server switching, death and respawn, dimension changes, repeated swaps, and optional mod integrations before treating a build as release-ready.

## License

The project is distributed under the MIT License. See [LICENSE](LICENSE).
