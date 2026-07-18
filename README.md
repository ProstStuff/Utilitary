# Utilitary
A utilitarian utility library.

---

## About
Utilitary is a library that mainly add utilities and configuration to Minecraft.

### Features
- A config system that use Minecraft codecs
  - Extensible config that can be saved to many different file format
- Many utility class and tweaks to simplify development process
  - `EntityRenderState` now hold an entity's UUID under `utilitary$uuid`. Can be obtained by casting `EntityRenderState` with `UUIDRenderStateHolder` class
  - `Color` and `ColorWithAlpha` class to store and modify color values.
  - `SimpleIdentifier` works like `Identifier` but without the character limits