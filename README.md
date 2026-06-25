# Cape Provider X

<img align="right" src="https://raw.githubusercontent.com/picapes/cape-provider-x/refs/heads/dev/src/main/resources/assets/icon.png" width="192" />

Adds capes to your game!  

Choose from multiple providers or configure your own.  

---

### ✨ About
Cape Provider X is a modified version of the [**Cape Provider** mod](https://github.com/litetex-oss/mcm-cape-provider), maintained by [@xsyanic](https://github.com/xsyanic).  
It contains everything from the original mod, plus additional improvements and integrations.

---

### ⚠️ Compatibility Notice
You **must remove any other cape-related mods** before using Cape Provider X.  
Having multiple cape mods installed at the same time will cause **conflicts and game crashes**.  

---

### 🔧 Changes in This Version
- Tweaks and improvements by **@xsyanic**  
- Added support for [**✨ PiCapes**](https://picapes.syanic.org)  
- Added support for [SkinMC Capes](https://skinmc.net/capes) & [Cosmetica Capes](https://cosmetica.cc/)
- General fixes and stability improvements 
- Cape visible in Offline mode servers too
  
---

### Creating a custom cape provider

<img align="right" src="https://raw.githubusercontent.com/litetex-oss/mcm-cape-provider/refs/heads/dev/assets/config-2.avif" width=360 />

The mod provides many different ways how a provider can be added.

---

### 🙌 Credits & Contributions
- **Original Mod**: [Cape Provider](https://github.com/litetex-oss/mcm-cape-provider) by [@litetex](https://github.com/litetex-oss)  
- **Modifications & Maintenance**: [@xsyanic](https://github.com/xsyanic)  
- **PiCapes Integration**: [PiCapes Project](https://picapes.github.io)  
- Community feedback, bug reports, and testing by all contributors ❤️  

---

💡 This project builds upon the original work, ensuring compatibility while adding modern cape sources and quality-of-life updates.

The simplest way to display a cape is by going into the `config/cape-provider` directory and creating a cape texture file named `cape.png`.

Additionally there are the following optional files:
* `owners.txt` - Determines which player names or UUIDs will get the cape displayed. If this file is not present then all players will display with the cape.
* `name.txt` - To override the display name of the provider

You can also add more providers by creating corresponding directories in `config/cape-provider/simple-custom`.<br/>Example: `config/cape-provider/simple-custom/my-super-cool-provider/cape.png`

#### Remote Provider in configuration

> Recommended for:
> * Users that want to add a custom remote provider

This demo showcases how to apply the capes inside [``custom-cape-demo``](https://github.com/litetex-oss/mcm-cape-provider/tree/dev/custom-cape-demo).

1. Open the config file located in ``config/cape-provider/config.json``
2. In the ``remoteCustomProviders`` section add the following entry:
    ```jsonc
    {
      "id": "cp1",
      "name": "CustomProvider1",
      // You can replace uuid with $id, $name or $idNoHyphen to customize the cape per Player
      "uriTemplate": "https://raw.githubusercontent.com/litetex-oss/mcm-cape-provider/refs/heads/dev/custom-cape-demo/uuid.png"
    }
    ```
    <details><summary>Example for SkinMC</summary>

    ```jsonc
    {
      "id": "skinmc",
      "name": "SkinMC",
      "uriTemplate": "https://skinmc.net/api/v1/skinmcCape/$id"
    }
    ```

    </details>
3. Restart the game and activate the provider

For more details have a look at [RemoteCustomProvider](https://github.com/litetex-oss/mcm-cape-provider/tree/dev/src/main/java/net/litetex/capes/provider/custom/remote/RemoteCustomProvider.java) and [RemoteCustomProviderConfig](https://github.com/litetex-oss/mcm-cape-provider/tree/dev/src/main/java/net/litetex/capes/provider/custom/remote/RemoteCustomProviderConfig.java)

NOTE: Texture resolvers can be selected using the `textureResolverId` attribute (see below for details).

#### via Mods

> Recommended for:
> * Mods

If you are a mod developer and want to e.g. display a cape for supporters or contributors of your mod, you can provide it using the mod's resources and/or metadata in ``fabric.mod.json``.
The overall behavior is similar to how [``modmenu``](https://github.com/TerraformersMC/ModMenu?tab=readme-ov-file#fabric-metadata-api) handles this.

##### Local/Simple (Recommended)

This approach requires no network communication and is the recommended way.
It works by reading metadata and resources from the `cape` directory.

Here is an example:
1. Add the following mod metadata:
    ``fabric.mod.json``
    ```json5
    {
      ...
      "custom": {
        "cape": "Contributors"
      }
    }
    ```
2. Create a `cape` directory inside `resources`
3. Add the cape texture in `cape/cape.png`
4. Add the players that should be given the cape in `cape/owners.txt` with their UUIDs or names

<details><summary>Note: There is also a more detailed variant</summary>

``fabric.mod.json``
```json5
{
  "custom": {
    "cape": {
      "name-extra": "Contributors",
      "owners": {
        // You can also used UUIDs
        "names": [
          "Notch"
        ]
      }
    }
  }
}
```

</details>

The mod uses this strategy itself. See the [`fabric.mod.json`](https://github.com/litetex-oss/mcm-cape-provider/tree/dev/src/main/resources/fabric.mod.json) or [`cape` directory](https://github.com/litetex-oss/mcm-cape-provider/tree/dev/src/main/resources/cape) for details.

##### Remote

Here's an example implementation that shows how a remote cape provider can be added:

``fabric.mod.json``
```json5
{
  ...
  "custom": {
    "cape": "https://raw.githubusercontent.com/litetex-oss/mcm-cape-provider/refs/heads/dev/custom-cape-demo/uuid.png"
  }
}
```

<details><summary>Here's a more detailed variant</summary>

``fabric.mod.json``
```json5
{
  "custom": {
    "cape": {
      // Gives everyone a christmas cape
      // You can also use variables here, like $uuid. See above for more details
      // You may have to escape the $ with \ or you can alternatively use § instead of $
      // Alternative: "uriTemplate"
      "url": "https://example.org/textures/§uuid.png",
      "changeCapeUrl": "https://...",
      "rateLimitedReqPerSec": 20 // Default is 20
    }
  }
}
```

</details>

#### Programmatic

You can also create a [programmatic cape provider](https://github.com/litetex-oss/mcm-cape-provider/tree/dev/PROGRAMMATIC_PROVIDER.md).

### Further notes

#### Maximum size

Images/Textures should not exceed 10MB. Otherwise they might be ignored.

#### Texture resolvers / Animated textures

The following resolvers are currently built-in:

| Resolver-ID | Animated | Format | Example | Notes |
| --- | --- | --- | --- | --- |
| `default` / null | ❌ | [PNG](https://de.wikipedia.org/wiki/Portable_Network_Graphics) | [uuid.png](https://raw.githubusercontent.com/litetex-oss/mcm-cape-provider/refs/heads/dev/custom-cape-demo/uuid.png) | |
| `sprite` | ✔ | Stacked [PNG](https://de.wikipedia.org/wiki/Portable_Network_Graphics) | [animated.png](https://raw.githubusercontent.com/litetex-oss/mcm-cape-provider/refs/heads/dev/custom-cape-demo/animated.png) | |
| `gif` | ✔ | [GIF](https://de.wikipedia.org/wiki/Graphics_Interchange_Format) | [animated.gif](https://raw.githubusercontent.com/litetex-oss/mcm-cape-provider/refs/heads/dev/custom-cape-demo/animated.gif) | _Usage not recommended_<br/>GIFs require more resources when compared to more modern formats like PNG. |

Please note that animated textures can be frozen or completely disabled in the settings.

<!-- modrinth_exclude.start -->

## Installation
[Installation guide for the latest release](https://github.com/litetex-oss/mcm-cape-provider/releases/latest#Installation)

### Usage in other mods

Add the following to ``build.gradle``:
```groovy
dependencies {
    implementation 'net.litetex.mcm:cape-provider:<version>'
    // Further documentation: https://wiki.fabricmc.net/documentation:fabric_loom
}
```

> [!NOTE]
> The contents are hosted on [Maven Central](https://repo.maven.apache.org/maven2/net/litetex/mcm/). You shouldn't have to change anything as this is the default maven repo.<br/>
> If this somehow shouldn't work you can also try [Modrinth Maven](https://support.modrinth.com/en/articles/8801191-modrinth-maven).

## Contributing
See the [contributing guide](./CONTRIBUTING.md) for detailed instructions on how to get started with our project.

<!-- modrinth_exclude.end -->
