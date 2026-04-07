### List of changes made by @xsyanic to this new branch forked from mcm-cape-provider for fresh start
> fp = Fresh Patches

## 07-04-2026 (on 5.1.0-fp1)

1. `\src\main\java\net\litetex\capes\handler\RealPlayerValidator.java`
- Hardcodedly commented parts of code to allow Offline mode support

2. `\src\main\java\net\litetex\capes\provider\PiCapesCapeProvider.java`
- Added PiCapes Cape provider

3. `\src\main\java\net\litetex\capes\provider\SkinmcCapeProvider.java`
- Added SkinMC Cape provider

4. `\src\main\resources\META-INF\services\net.litetex.capes.provider.CapeProvider`
- Registred both PiCapes & SkinMC Cape Providers

5. `\src\main\java\net\litetex\capes\config\Config.java`
- Set PiCapes to Default capes provider

6. `src\main\resources\fabric.mod.json`, `src\main\resources\assets\icon.png`, `gradle.properties` & `README.md`
- Changed branding to fit to **Cape Provider X** again.

---