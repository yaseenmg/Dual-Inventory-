# Dual Inventory — Source Code (محدَّث)

## Build warning

This source snapshot uses Minecraft SRG-mapped method and field names (for example `m_150109_()`). A standard ForgeGradle build may fail unless the mapping setup matches the source. Treat the build as unverified until it is compiled and tested in a clean Forge 1.20.1 / Java 17 environment.

## Features

- Two independently saved inventory sets, A and B.
- Switch with the default Z key (rebindable in Options → Controls).
- Saves the main inventory, armor and offhand.
- Optional Traveler's Backpack and Curios integration.
- Per-player data stored through a Forge capability.

## Source layout

```
src/main/java/com/example/dualinventory/
├── DualInventoryMod.java
├── capability/
│   ├── InventorySnapshot.java
│   ├── DualInventoryData.java
│   ├── DualInventoryProvider.java
│   ├── CapabilityHandler.java
│   └── InventorySwapService.java
├── compat/
│   ├── TravelersBackpackCompat.java
│   └── CuriosCompat.java
├── network/
│   ├── SwapInventoryPacket.java
│   └── NetworkHandler.java
├── client/
│   ├── KeyBindings.java
│   ├── ClientEvents.java
│   ├── ClientSetup.java
│   └── ClientForgeEvents.java
└── event/
    └── PlayerEvents.java
```

## Compatibility

- Minecraft Java Edition 1.20.1
- Minecraft Forge 47.x
- Java 17
- Optional: Traveler's Backpack and Curios

## Build

From the `dualinv` directory, use Java 17 and run `gradle build` if Gradle is installed. The Gradle wrapper JAR is not included in this repository snapshot, so `gradlew` cannot be used until that file is restored.

## Test before release

- [ ] Build in a clean ForgeGradle environment.
- [ ] Test repeated A ↔ B swaps for item loss or duplication.
- [ ] Test death, respawn and dimension changes.
- [ ] Test multiplayer on a dedicated server.
- [ ] Test Traveler's Backpack and Curios separately.
