# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project snapshot

Wild Wind: Hearth Flame & Angler Song is a NeoForge mod for Minecraft 1.21.1. The codebase targets Java 21 and uses NeoForge 21.1.230 with GeckoLib 4.8.4.

The repository is split between handwritten game code under `src/main/java`, runtime resources under `src/main/resources`, and generated assets/data under `src/generated/resources`.

## Common commands

Use the Gradle wrapper from the repo root.

- Build the mod: `./gradlew build`
- Run the full verification lifecycle: `./gradlew check`
- Run tests: `./gradlew test`
- Run a single test class: `./gradlew test --tests fully.qualified.TestClass`
- Run a single test method: `./gradlew test --tests fully.qualified.TestClass.testMethod`
- Launch the client dev run: `./gradlew runClient`
- Launch the dedicated server dev run: `./gradlew runServer`
- Launch the data generator: `./gradlew runData`
- Run NeoForge GameTests: `./gradlew gameTestServer`
- Clean build outputs: `./gradlew clean`

There is no separate lint plugin configured in `build.gradle`; `check` is the standard repo-wide verification entry point.

## Architecture overview

### Entry points and lifecycle

- `org.polaris2023.wild_wind.WildWindMod` is the main mod entry point. It registers registries, common setup, datagen hooks, configs, and loot-table modifications.
- `registry/ModRegistries` is the central bootstrap layer. It wires all deferred registries and also owns the custom `MUDCRAB_VARIANTS` registry.
- `registry/ModCommonSetup` contains common lifecycle behavior such as flammability registration, axe stripping overrides, right-click interactions, and opening the arrow fletching menu.
- `client/ModClientHooks` contains all client-only wiring: renderers, screens, tooltip components, wood-type registration, and client-side input behavior.
- `network/WildWindNetwork` registers payload handlers. Keep packet registration close to the payload type it serves.

### Content registration model

- The mod uses deferred registration heavily. Blocks, items, menus, entities, recipes, effects, sounds, particles, data components, attributes, and related registries are all centralized in `registry/`.
- `registry/ModBlocks` and `registry/ModItems` build the content sets for the three wood families (`cinder`, `ember`, `azalea`) plus the non-wood terrain blocks and utility items.
- The codebase uses paired block/item bundles such as `WoodSet` and `WoodItems` to keep related content aligned.
- `ModTerrainBlocks` provides the non-tree blocks like reeds, cattails, scorched dirt, scorched grass, scorched twig, and tiny cactus.

### Gameplay systems

- The arrow fletching system is a custom crafting flow built from three layers: `menu/ArrowFletchingMenu`, `recipe/ArrowFletchingRecipe`, and `client/screen/ArrowFletchingScreen`.
- `ArrowFletchingMenu` is the server-side container logic; `ArrowFletchingRecipe` defines matching, assembly, and network serialization; the screen renders the UI.
- `item/OmniClawItem` and `item/component/OmniClawTools` implement the multi-tool item state, with client tooltip support and a server payload for slot selection.
- `interaction/BlockPropertyBookFactory` drives the block-property book interaction that is triggered from common right-click handling.

### Data generation and resources

- `datagen/ModDataGen` is the single entry point for all data providers.
- Client-facing generated content includes blockstates, models, language files, and item models.
- Server-facing generated content includes loot tables, recipes, tags, biome/worldgen data, and data maps.
- `src/main/templates/META-INF/neoforge.mods.toml` is expanded during datagen/build metadata generation.
- `src/generated/resources` is part of the main resources source set. Regenerate outputs instead of hand-editing generated files when the source providers change.

### Mixins and runtime hooks

- Mixins live under `mixin/` and are declared in `src/main/resources/wild_wind.mixins.json`.
- Runtime behavior is also extended through NeoForge events, especially in `WildWindMod`, `ModCommonSetup`, `ModClientHooks`, and `WildWindNetwork`.
- Keep client-only code isolated to the client package and avoid leaking those classes into common bootstrapping paths.

## Repository-specific notes

- Mod id: `wild_wind`
- Main package: `org.polaris2023.wild_wind`
- Generated metadata and resources depend on the Gradle properties in `gradle.properties`.
- `README.md` only contains licensing information, so the Gradle files and source tree are the primary source of project truth.