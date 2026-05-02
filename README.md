# SoundGear

一个 Minecraft Forge 模组，为游戏添加耳机装备和语音聊天功能。

## 功能特性

- **16 种颜色耳机**：白色、橙色、品红、淡蓝、黄绿、粉红、灰色、淡灰、青色、紫色、蓝色、棕色、绿色、红色、黑色
- **语音频道系统**：按住 `L` 键进行 Push-to-Talk 语音通话，穿戴相同颜色耳机的玩家可互相听到语音
- **音符粒子效果**：语音激活时在玩家头顶显示音符粒子
- **护甲功能**：耳机可放置在头部装备栏，提供 0.5 护甲值，支持附魔

## 安装要求

- Minecraft 1.20.1
- Forge 47.4.0 或更高版本

## 合成配方

- **白色耳机**：铁锭 + 皮革（有形状配方）
- **其他颜色耳机**：白色耳机 + 对应染料（无形状配方）

## 使用方法

1. 合成或在创造模式中获取耳机
2. 装备到头部装备栏
3. 与其他穿戴相同颜色耳机的玩家组队
4. 按住 `L` 键说话

## 配置

- 最大语音传输距离：64 格（可配置）
- 采样率：16000Hz

## 开发

### 环境搭建

1. 克隆仓库
2. 使用 IntelliJ IDEA 或 Eclipse 导入 Gradle 项目
3. 运行 `./gradlew genIntellijRuns` 或 `./gradlew genEclipseRuns`

### 构建

```bash
./gradlew build
```

### 项目结构

```
SoundGear/
├── headphone_mod/                   # 🎨 Blockbench 模型源文件
│   ├── headphone.bbmodel            #   Blockbench 项目文件，用于 3D 模型编辑
│   ├── headphone.java               #   导出 Java 模型模板
│   └── headphone_texture.png        #   模型纹理贴图
│
├── src/main/
│   ├── java/com/E5trusw3nt/mymod/  # 🧩 Java 源代码
│   │   ├── SoundGearMod.java        # 🎯 模组主入口 - Forge 启动入口，注册物品/事件/网络/配置
│   │   ├── Config.java              # ⚙️ 配置管理 - 基于 ForgeConfigSpec 的可热重载配置
│   │   │
│   │   ├── item/                    # 📦 物品系统
│   │   │   ├── ModItems.java        #   物品注册中心 - 16 种颜色耳机均在此注册
│   │   │   ├── HeadphoneItem.java   #   耳机物品类 - 继承 ArmorItem，支持颜色频道匹配
│   │   │   └── ModArmorMaterials.java  #   护甲材质 - 定义耳机的耐久、防御、附魔等属性
│   │   │
│   │   ├── client/                  # 🎨 客户端渲染
│   │   │   ├── HeadphoneModel.java  #   3D 耳机模型 - 基于 Blockbench 导出的模型定义
│   │   │   └── HeadphoneArmorRenderer.java  # 自定义渲染器 - 取代默认平面盔甲纹理
│   │   │
│   │   └── voice/                   # 🔊 语音聊天系统
│   │       ├── KeyBindings.java     #   L 键 PTT 绑定 - 按下开始/松开停止
│   │       ├── VoiceChatNetwork.java  #   网络通道 - 基于 Forge SimpleChannel 的通信层
│   │       ├── VoiceChatClientHandler.java  # 客户端处理 - 麦克风录音、音频播放、静音检测
│   │       ├── VoiceChatServerHandler.java  # 服务端处理 - 按耳机颜色频道转发语音
│   │       ├── VoiceDataPacket.java #   语音数据包 - 传输 PCM 音频及发送者频道信息
│   │       └── VoiceStatusPacket.java  #   语音状态包 - 同步开始/停止/加入/离开等状态
│   │
│   └── resources/                   # 📦 资源文件
│       ├── pack.mcmeta              #   资源包元数据
│       ├── META-INF/
│       │   └── mods.toml            #   模组元数据
│       └── assets/soundgear/
│           ├── lang/                # 🌐 本地化语言文件
│           │   ├── en_us.json       #   英文语言文件
│           │   └── zh_cn.json       #   中文语言文件
│           │
│           ├── models/item/         # 🏗️ 物品模型 JSON（16 色耳机）
│           │   ├── headphones.json  #   白色耳机（基础模型）
│           │   ├── white_headphones.json
│           │   ├── ...
│           │   └── black_headphones.json
│           │
│           ├── textures/item/       # 🖼️ 物品纹理
│           │   ├── headphones.png          #   白色耳机纹理
│           │   ├── headphones_overlay.png  #   合成覆盖层纹理
│           │   └── ...（16 色耳机纹理同名 png）
│           │
│           └── textures/models/armor/  # 🛡️ 盔甲渲染纹理
│               ├── headphones_layer_1.png         #   默认耳机盔甲层
│               └── white_headphones_layer_1.png   #   白色耳机盔甲层
│
└── data/soundgear/recipes/          # 🔧 合成配方（16 色）
    ├── white_headphones.json        #   白色耳机配方
    ├── orange_headphones.json       #   橙色耳机配方
    └── ...（其余 14 色耳机配方）
```

## 更新日志

详见 [CHANGELOG.md](CHANGELOG.md)

## 许可证

本项目采用 MIT 许可证，详见 [LICENSE.txt](LICENSE.txt)。
