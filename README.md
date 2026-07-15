# Utilitary
A utilitarian utility library.

---

## About
Utilitary is a library mainly adds utilities and configuration.

### Features
- JSON-based config using Minecraft codecs
  - Can be saved as `.json` and `.json5`
- Many utility class and tweaks to simplify development process
  - `EntityRenderState` now hold an entity's UUID under `utilitary$uuid`. Can be obtained by casting `EntityRenderState` with `UUIDRenderStateHolder` class
  - `Color` and `ColorWithAlpha` class to store and modify color values.
  - `SimpleIdentifier` works like `Identifier` but without the character limits