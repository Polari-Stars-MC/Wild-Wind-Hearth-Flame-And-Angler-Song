# Project Progress

## 2026-04-08

### 本轮目标
- 处理 Background.md 中按钮、告示牌兼容问题。
- 重新对比原版 oak 衍生实现，确认当前木材套件缺口。

### 本轮完成
- 将 `ModBlockSetType` 中的自定义木材类型改为通过 `BlockSetType.register(...)` 正式注册，补齐按钮、门、活板门、压力板等依赖的方块集类型注册链路。
- 为普通告示牌与悬挂告示牌补充自定义 block entity type。
- 为普通告示牌与悬挂告示牌分别补充自定义方块实现，覆盖原版默认 `minecraft:sign` / `minecraft:hanging_sign` 的 block entity 校验路径。
- 客户端补充自定义木材类型的 `Sheets.addWoodType(...)` 注册，并注册 sign / hanging sign 的 block entity renderer。

### 对比原版 oak 后的结论
- 木材衍生体系除了 `WoodType`，还必须显式注册 `BlockSetType`，否则按钮、压力板、门等行为链不完整。
- 自定义告示牌不能直接复用原版 `minecraft:sign` 的 block entity type；否则放置时会在 `validateBlockState` 阶段因为方块不在原版合法 block 集合中而崩溃。
- 悬挂告示牌同样需要独立的自定义 block entity type，并保留 hanging sign 的文本尺寸参数。
- 客户端还需要显式注册 wood type 到 `Sheets`，并绑定对应 renderer，才能保证告示牌模型与纹理路径完整生效。

### 已验证
- 使用 IDEA MCP 编译通过。
- 已定位并修复此前崩溃根因：放置 `wwhfas:cinder_sign` 时触发 `Invalid block entity minecraft:sign`。

### 下一步建议
- 进入游戏实测 cinder / ember 的 sign 与 hanging sign：放置、破坏、上色、编辑文本、含水状态。

## 2026-04-08 继续

### 本轮目标
- 根据更新后的 Background.md，补齐按钮 datagen。
- 将告示牌 GUI 需要的贴图从根目录 textures 纳入资源包。

### 本轮完成
- 调整 [src/main/java/git/wildwind/wwhfas/datagen/provider/ModItemModelProvider.java](src/main/java/git/wildwind/wwhfas/datagen/provider/ModItemModelProvider.java) 的按钮 item model 生成逻辑，使其改为 oak 同类实现使用的 `button_inventory` 父模型和木板纹理。
- 调整 [src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockStateProvider.java](src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockStateProvider.java)，移除按钮错误的 `simpleBlockItem(...)` 生成路径，避免下次 datagen 再生成 wall/floor 用的按钮 item model。
- 同步修正当前生成产物 [src/generated/resources/assets/wwhfas/models/item/cinder_button.json](src/generated/resources/assets/wwhfas/models/item/cinder_button.json) 和 [src/generated/resources/assets/wwhfas/models/item/ember_button.json](src/generated/resources/assets/wwhfas/models/item/ember_button.json)。
- 将根目录 `textures/gui/hanging_signs` 下的两张贴图复制并重命名到运行时资源路径：
	`assets/wwhfas/textures/gui/hanging_signs/cinder.png`
	`assets/wwhfas/textures/gui/hanging_signs/ember.png`

### 对比原版 oak 后的补充结论
- 按钮物品模型不能直接指向放置态按钮方块模型；否则物品栏/手持展示会沿用 wall/floor 结构。
- oak 的正确思路是按钮物品使用 inventory 专用按钮父模型，并只注入木板纹理。
- hanging sign 的 GUI 图在资源路径上按 wood type 命名，而不是按 `*_hanging_sign` 原图文件名直接入包。

### 已验证
- 使用 IDEA MCP 对 datagen provider 编译通过。
- 目标 GUI 贴图已确认落位到 `src/main/resources/assets/wwhfas/textures/gui/hanging_signs/`。

### 补充验证与兜底
- 使用 IDEA MCP 对整个项目执行构建，构建通过。
- 对本轮涉及的 Java 文件再次执行 IDE 错误检查，未发现新增错误。
- 额外将根目录 `textures/screens` 下的 hanging sign 贴图同步纳入资源包，作为 GUI 路径之外的 screen 资源兜底：
	`assets/wwhfas/textures/screens/cinder_hanging_sign.png`
	`assets/wwhfas/textures/screens/ember_hanging_sign.png`

## 2026-04-08 告示牌渲染排查

### 本轮目标
- 处理更新后的 Background.md 中告示牌方块渲染异常。
- 核实 hanging sign GUI 的实际资源路径。

### 本轮发现
- 运行日志明确报出 sign / hanging sign block model 缺纹理，不是 renderer 未注册：
	`wwhfas:entity/signs/cinder`
	`wwhfas:entity/signs/ember`
	`wwhfas:entity/signs/hanging/cinder`
	`wwhfas:entity/signs/hanging/ember`
- 根因是 datagen 生成的 [src/generated/resources/assets/wwhfas/models/block/cinder_sign.json](src/generated/resources/assets/wwhfas/models/block/cinder_sign.json) 等模型文件，把 `particle` 纹理错误指向了 entity 贴图路径，而这些路径不会进入 `blocks` atlas。
- 通过读取 1.21.1 的 [HangingSignEditScreen.java](jar:///D:/me/codex/HearthFlame&AnglerSong/build/moddev/artifacts/neoforge-21.1.214-sources.jar!/net/minecraft/client/gui/screens/inventory/HangingSignEditScreen.java) 可确认 hanging sign GUI 使用的资源路径是：
	`textures/gui/hanging_signs/<woodType>.png`

### 本轮完成
- 将 [src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockStateProvider.java](src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockStateProvider.java) 中 signBlock 与 hangingSignBlock 的第三个参数从 entity 贴图路径改为对应木板纹理。
- 同步修正当前生成产物：
	[src/generated/resources/assets/wwhfas/models/block/cinder_sign.json](src/generated/resources/assets/wwhfas/models/block/cinder_sign.json)
	[src/generated/resources/assets/wwhfas/models/block/ember_sign.json](src/generated/resources/assets/wwhfas/models/block/ember_sign.json)
	[src/generated/resources/assets/wwhfas/models/block/cinder_hanging_sign.json](src/generated/resources/assets/wwhfas/models/block/cinder_hanging_sign.json)
	[src/generated/resources/assets/wwhfas/models/block/ember_hanging_sign.json](src/generated/resources/assets/wwhfas/models/block/ember_hanging_sign.json)
- 确认 hanging sign GUI 的正确资源命名应为：
	`assets/wwhfas/textures/gui/hanging_signs/cinder.png`
	`assets/wwhfas/textures/gui/hanging_signs/ember.png`

### 已验证
- 使用 IDEA MCP 对 [src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockStateProvider.java](src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockStateProvider.java) 编译通过。
- IDE 错误检查未发现新增问题。

### 下一步建议
- 重新启动客户端，优先观察 sign / hanging sign 的方块渲染是否恢复。
- 如果 hanging sign GUI 仍然发黑，则需要在新一轮日志里继续确认是否是贴图内容本身而不是资源路径问题。

## 2026-04-08 告示牌命名空间修正

### 本轮发现
- 1.21.1 的 [Sheets.java](jar:///D:/me/codex/HearthFlame&AnglerSong/build/moddev/artifacts/neoforge-21.1.214-sources.jar!/net/minecraft/client/renderer/Sheets.java) 在创建 sign 和 hanging sign 材质时，会对 `woodType.name()` 直接执行 `ResourceLocation.parse(...)`。
- 1.21.1 的 [HangingSignEditScreen.java](jar:///D:/me/codex/HearthFlame&AnglerSong/build/moddev/artifacts/neoforge-21.1.214-sources.jar!/net/minecraft/client/gui/screens/inventory/HangingSignEditScreen.java) 也会对 `woodType.name()` 直接执行 `ResourceLocation.parse(...)`，再拼接 `textures/gui/hanging_signs/<path>.png`。
- 项目原先在 [src/main/java/git/wildwind/wwhfas/block/ModWoodType.java](src/main/java/git/wildwind/wwhfas/block/ModWoodType.java) 和 [src/main/java/git/wildwind/wwhfas/block/ModBlockSetType.java](src/main/java/git/wildwind/wwhfas/block/ModBlockSetType.java) 中使用了无命名空间的 `cinder` / `ember`。这会让资源解析默认落到 `minecraft`，从而解释了你截图里的黑板面和紫色缺失纹理效果，以及 hanging sign GUI 发黑。

### 本轮完成
- 将自定义 `WoodType` 名称改为 `wwhfas:cinder` 和 `wwhfas:ember`。
- 将自定义 `BlockSetType` 名称同步改为带 `wwhfas` 命名空间，保持整条木材类型链一致。

### 已验证
- 使用 IDEA MCP 对整个项目再次构建，构建通过。
- IDE 错误检查未发现新增问题。

## 2026-04-08 船注册补齐

### 本轮目标
- 根据最新的 Background.md，补齐 cinder / ember 的船与运输船注册。

### 本轮发现
- 这两个木材套件缺的不是自定义实体类型，而是 `Boat.Type` 扩展、对应物品注册，以及客户端 boat model layer 定义。
- 1.21.1 的 `BoatItem` 仍然通过 `Boat.Type` 决定放置出的船变种；船掉落物、渲染纹理路径和模型 layer 也都从 `Boat.Type` 读取。
- NeoForge 的运行时枚举扩展需要两部分同时存在：
	- 在 mods.toml 中声明 `enumExtensions`
	- 提供 `META-INF/enumextensions.json`
- `Boat.Type` 带有 `@NamedEnum(1)`，因此扩展条目的名字参数必须带 `wwhfas:` 命名空间，否则纹理路径和网络同步链会再次落到默认命名空间。

### 本轮完成
- 在 `src/main/templates/META-INF/neoforge.mods.toml` 中加入 `enumExtensions="META-INF/enumextensions.json"`。
- 新增 `src/main/resources/META-INF/enumextensions.json`，为 `Boat.Type` 注入 `WWHFAS_CINDER` 与 `WWHFAS_EMBER` 两个扩展常量。
- 新增 `src/main/java/git/wildwind/wwhfas/registry/ModBoatTypes.java`，通过 `EnumProxy` 提供木板、序列化名称、船物品、运输船物品与 stick 掉落参数。
- 调整 `src/main/java/git/wildwind/wwhfas/registry/ModItems.java`，为两套木材补充 `*_boat` 与 `*_chest_boat` 物品注册，并接入 `BoatItem`。
- 调整 `src/main/java/git/wildwind/wwhfas/client/ModClientHooks.java`，补充自定义 boat / chest boat 的 layer definition 注册，确保 vanilla `BoatRenderer` 能烘焙到这两种新木材的模型层。
- 调整 datagen：
	- `ModItemModelProvider` 补充 boat / chest boat item model
	- `ModRecipeProvider` 补充 boat / chest boat recipe
	- `ModLangProvider` 补充 boat / chest boat 中英文名称
- 同步补齐当前生成产物：
	- `src/generated/resources/assets/wwhfas/models/item/*_boat.json`
	- `src/generated/resources/assets/wwhfas/models/item/*_chest_boat.json`
	- `src/generated/resources/data/wwhfas/recipe/*_boat.json`
	- `src/generated/resources/data/wwhfas/recipe/*_chest_boat.json`
	- `src/generated/resources/assets/wwhfas/lang/en_us.json`
	- `src/generated/resources/assets/wwhfas/lang/zh_cn.json`

### 已验证
- 使用 IDEA MCP 对本轮涉及文件执行 IDE 错误检查，未发现新增错误。
- 使用 IDEA MCP 对整个项目再次构建，构建通过。

### 下一步建议
- 进入游戏验证 cinder / ember 船和运输船的四个关键路径：
	- 创造模式物品栏显示
	- 合成配方产出
	- 放置后的船体纹理与模型
	- 破坏后掉落是否回到对应木材船物品

## 2026-04-08 船模型层重复注册修正

### 本轮发现
- 客户端崩溃不是 `Boat.Type` 扩展本身有问题，而是模型层被重复注册。
- vanilla 的 `LayerDefinitions.createRoots()` 会在资源重载时直接遍历 `Boat.Type.values()`，并为每个 boat type 自动注册 `createBoatModelName(...)` 与 `createChestBoatModelName(...)`。
- 由于 cinder / ember 已经通过运行时枚举扩展进入 `Boat.Type.values()`，`ModClientHooks` 中手动补的四个 boat layer definition 会和 vanilla 自动注册的同名 key 冲突，最终触发 `Multiple entries with same key: wwhfas:chest_boat/ember#main`。

### 本轮完成
- 从 `src/main/java/git/wildwind/wwhfas/client/ModClientHooks.java` 中移除了 cinder / ember boat 与 chest boat 的手动 layer definition 注册，只保留 sign / hanging sign 的客户端注册。

### 已验证
- 仍需重新启动客户端确认运行时崩溃已消失；代码层面的最终验证已在本轮后续重新执行。

## 2026-04-09 树木 tags 与原版行为补齐

### 本轮目标
- 根据最新的 Background.md，完整检查 cinder / ember 两套树与原版木材的 tags、堆肥、剥皮和可燃性行为链。

### 本轮发现
- 当前项目已有基础木材 tags，但还缺少一批 vanilla 汇总 tag 与 common conventions tag，尤其是 `logs`、`overworld_natural_logs`、`all_signs`、`all_hanging_signs`、`c:stripped_logs`、`c:stripped_woods`、`c:fences/wooden`、`c:fence_gates/wooden`。
- NeoForge 21.1.214 本地依赖里存在 `NeoForgeDataMaps.COMPOSTABLES`，但不存在先前参考资料中的 `STRIPPABLES` data map；因此斧头剥皮不能照搬较新的 data map 写法，必须回到当前版本真实支持的实现方式。
- 仅靠木材方块属性上的 `ignitedByLava()` 不足以完整对齐 vanilla 木材行为，仍需向 `FireBlock` 注册不同方块的引燃/燃烧参数。

### 本轮完成
- 调整 [src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockTagsProvider.java](src/main/java/git/wildwind/wwhfas/datagen/provider/ModBlockTagsProvider.java)，补齐两套木材进入 vanilla `logs`、`overworld_natural_logs`、`all_signs`、`all_hanging_signs` 以及 common `c:` 标签的 datagen 逻辑。
- 调整 [src/main/java/git/wildwind/wwhfas/datagen/provider/ModItemTagsProvider.java](src/main/java/git/wildwind/wwhfas/datagen/provider/ModItemTagsProvider.java)，同步补齐 item 侧的 `logs`、`signs`、`hanging_signs` 与 common `c:` 标签复制逻辑。
- 新增 [src/main/java/git/wildwind/wwhfas/datagen/provider/ModDataMapProvider.java](src/main/java/git/wildwind/wwhfas/datagen/provider/ModDataMapProvider.java)，为树叶和树苗生成 `neoforge:compostables` data map。
- 新增 [src/main/java/git/wildwind/wwhfas/registry/ModCommonSetup.java](src/main/java/git/wildwind/wwhfas/registry/ModCommonSetup.java)，在 common setup 中补齐两套木材的 flammability 参数，并通过 `BlockToolModificationEvent` 为 log/wood 接入斧头剥皮行为。
- 调整 [src/main/java/git/wildwind/wwhfas/WildWindMod.java](src/main/java/git/wildwind/wwhfas/WildWindMod.java) 与 [src/main/java/git/wildwind/wwhfas/datagen/ModDataGen.java](src/main/java/git/wildwind/wwhfas/datagen/ModDataGen.java)，把 common setup 与 data map provider 接入主注册链。
- 同步补齐当前生成产物：
	- `src/generated/resources/data/minecraft/tags/block/logs.json`
	- `src/generated/resources/data/minecraft/tags/block/overworld_natural_logs.json`
	- `src/generated/resources/data/minecraft/tags/block/all_signs.json`
	- `src/generated/resources/data/minecraft/tags/block/all_hanging_signs.json`
	- `src/generated/resources/data/minecraft/tags/item/logs.json`
	- `src/generated/resources/data/minecraft/tags/item/signs.json`
	- `src/generated/resources/data/minecraft/tags/item/hanging_signs.json`
	- `src/generated/resources/data/c/tags/block/*`
	- `src/generated/resources/data/c/tags/item/*`
	- `src/generated/resources/data/neoforge/data_maps/item/compostables.json`

### 已验证
- 使用 IDEA MCP 对整个项目重新构建，构建通过。
- 对本地 `neoforge-21.1.214-sources.jar` 做了源码核对，确认当前版本可用的是 `COMPOSTABLES` data map，而斧头剥皮需改走运行时事件，不存在可直接使用的 `STRIPPABLES` data map。

### 下一步建议
- 进入游戏实测 cinder / ember 的四条原版行为链：
	- 斧头对 log / wood 的剥皮
	- 树叶与树苗投入堆肥桶
	- 木材、树叶、树苗被火点燃与燃烧速度
	- 标签驱动的配方、告示牌和相关兼容模组交互