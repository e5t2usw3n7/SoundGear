# SoundGear 模组更新日志

本文件记录 SoundGear 模组的所有重要变更。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [1.3.2] - 2026-05-04

### 修复

- **修复耳机模型不随人物转头旋转**：在 `HeadphoneArmorRenderer.getGenericArmorModel()` 中，返回耳机模型前从原始模型复制头部旋转数据 (`head.xRot/yRot/zRot`)，使耳机 3D 模型能正确跟随玩家视野方向旋转

---

## [1.3.1] - 2026-05-04

### 变更

- **统一所有耳机使用 3D 模型**：删除 `WhiteHeadphoneModel`，所有 16 种颜色的耳机统一使用 `HeadphoneModel`（基于 `headphone_mod/headphone.bbmodel` 的高精度 Blockbench 建模），穿戴时以立体 3D 耳机模型展示
- **`HeadphoneModel.java` 重构**：将模型重写为继承 `HumanoidModel<LivingEntity>`，所有耳机几何体作为 `head` 部件的子部件，确保佩戴位置合理且随头部自然旋转，其他身体部件均为空占位
- **`HeadphoneArmorRenderer.java`**：所有颜色耳机均使用 `HeadphoneModel`，不再区分白色耳机与其他颜色

### 移除

- 移除 `WhiteHeadphoneModel.java` 类及其相关引用

---

## [1.3.0] - 2026-05-03

### 新增

- **白色耳机专属 3D 盔甲模型**：为白色耳机添加了独立的 Blockbench 高精度建模 (`headphone.bbmodel`)，穿戴后以立体 3D 模型展示，而非其他颜色的 2D 扁平纹理
- **白色耳机专用护甲材质**：新增 `WHITE_HEADPHONES` 护甲材质，自动路由到独立的 `white_headphones_layer_1.png` 纹理

### 变更

- **HeadphoneModel.java**：重写 `renderToBuffer()`，在渲染标准人体部件后额外渲染自定义 `headphone` 模型部件，并同步头部旋转，使 3D 模型可以正确显示
- **HeadphoneItem.java**：新增接受 `ArmorMaterial` 参数的重载构造函数，白色耳机使用 `WHITE_HEADPHONES` 材质
- **ModItems.java**：白色耳机注册使用独立的 `WHITE_HEADPHONES` 材质
- **清理旧文件**：删除了不再使用的 `headphones.json`、`headphones.png`、`headphones_overlay.png`（属于 1.2.0 移除的旧单一耳机物品的残留）

### 修复

- **修复 3D 模型不可见问题**：之前的 `HeadphoneModel.renderToBuffer()` 继承自 `HumanoidModel`，不渲染自定义 `headphone` 部件，导致 Blockbench 导出模型始终不可见

---

## [1.2.0] - 2026-05-02

### 设计理念变更

- **耳机从单一物品变为 16 种颜色独立物品**：彻底重构了物品体系，每种颜色的耳机（白色、橙色、品红、淡蓝、黄绿、黄绿、粉红、灰色、淡灰、青色、紫色、蓝色、棕色、绿色、红色、黑色）都是独立注册的物品
- **移除 DyeableLeatherItem 接口**：不再使用皮革染色机制，改为工作台合成——白色耳机 + 对应染料 = 对应颜色耳机
- **合成配方设计**：白色耳机使用铁锭+皮革合成（有形状配方），其他 15 种颜色通过白色耳机+染料的无形状配方合成
- **每种颜色拥有独立贴图**：16 种颜色各有一张独立的物品纹理文件，每种颜色都有对应的物品模型
- **语音频道不变**：颜色仍决定语音频道，同颜色耳机的玩家可以互相听到语音

### 为什么做这个改动？

我想了一想，之前的单一耳机+DyeableLeatherItem 方案存在以下问题：

1. 颜色数据依赖 NBT 存储，染色逻辑复杂且容易出 bug
2. 无法在创造模式中直观地选择特定颜色

新方案虽然增加了物品数量，但每个物品都有独立的贴图、模型和名称，玩家一眼就能分辨颜色，工作台合成也更符合原版 Minecraft 的设计直觉。

### 新增
- 16 种颜色耳机物品（`white_headphones` ~ `black_headphones`）
- 16 张独立物品纹理（`white_headphones.png` ~ `black_headphones.png`）
- 16 个物品模型 JSON 文件
- 白色耳机的有形状合成配方（铁锭 + 皮革）
- 15 种染色配方（白色耳机 + 染料）
- 中英文语言文件更新（16 种颜色的物品名称）

### 移除
- `DyeableLeatherItem` 接口及相关的 NBT 染色逻辑
- 旧的单一 `headphones` 物品
- `headphones_overlay.png` 覆盖层纹理（不再需要）

### 变更
- `ModItems.java`：从单一注册改为 16 种颜色批量注册
- `HeadphoneItem.java`：简化类结构，移除染色相关代码
- `SoundGearMod.java`：创造模式标签页改为添加所有 16 种耳机

---

## [1.1.0] - 2026-05-02

### 新增
- **语音聊天系统**：按住 `L` 键进行 Push-to-Talk 语音通话
- **颜色频道匹配**：穿戴相同颜色耳机的玩家才能互相听到语音
- **染色功能**：支持用 16 种染料对耳机进行染色，右键切换颜色/频道
- **音符粒子效果**：语音激活时在玩家头顶显示音符粒子
- **语音配置**：最大传输距离（64格）、采样率（16000Hz）可配置
- **网络包系统**：VoiceDataPacket（音频数据）、VoiceStatusPacket（说话状态）
- **服务端转发**：自动转发语音数据给同频道玩家
- **玩家加入/离开通知**：同频道玩家上下线时收到音符粒子提示
- **语言文件更新**：中英文翻译更新（按键绑定、物品名称等）

### 变更
- 模组名称从 `ExampleMod` 更名为 `SoundGear`
- 模组 ID 从 `mymod` 更名为 `soundgear`
- 耳机物品添加至创造模式「战斗」标签页

## [1.0.0] - 2026-04-28

### 新增

- **耳机头盔物品**：可放置在头部装备栏，提供 0.5 护甲值
- **ModArmorMaterials**：自定义护甲材质 `HEADPHONES`
- **附魔支持**：可像普通头盔一样接受附魔
- **初始纹理**：16x16 像素耳机物品纹理

---

> **提示**：可以在终端执行 `git log --oneline` 查看基于 commit 的详细变更记录。