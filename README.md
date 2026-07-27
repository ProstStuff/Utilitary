# Utilitary

---

A Minecraft library that provide extensive configuration, utilities, and tweaks
to help with mod development.

**Features**

- **Extensive configuration system** that use Minecraft codec as their base serialization and deserialization, supports many values! Also include the following:
  - *Your namespace, Your folder.* Your config saved to a new folder under the identifier namespace it's saved as
  - Custom metadata to ensure a clear separation between not-safe for modification value 
  - Easy version migration
  - Can be saved to many other _file format_.
    - Utilitary only provide JSON (.json) and JSON5 (.json5) format by default
  - Adding comments to config value to be shown in the file (if the format supports it). Translation is supported!
  - Can be named with restricted characters using **SimpleIdentifier** (use with caution)
- **EntityRenderState** now stores the entity UUID
- **ColorRGB** and **ColorARGB** that stores color value in their (alpha,) red, green, blue integer value (0-255)
- **SimpleIdentifier** that works like Identifier but without the character limitation with Identifier (use with caution)
- *and many more!*