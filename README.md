# Utilitary

---

A Minecraft library that provide extensive configuration, utilities, tweaks, and abstractions
to help with mod development.

Mainly for Fraynn's mod development that require long development time (to help in reducing it of course)

## **Features**

### Extensive Configuration System
Use Minecraft codec as their base serialization, but able to be saved as many different files.

- *Your namespace, Your folder.* Your config saved to a new folder under the identifier namespace it's saved as
  - Custom metadata to ensure a clear separation between not-safe for modification value
  - Easy version fixer
  - Can be saved to many other _file format_.
    - Utilitary only provide JSON (.json) and JSON5 (.json5) format by default
  - Adding comments to config value to be shown in the file (if the format supports it). Translation is supported!
  - Can be named with restricted characters using **SimpleIdentifier** (use with caution)

- **EntityRenderState** now stores the entity UUID
- **ColorRGB** and **ColorARGB** that stores color value in their (alpha, red, green, blue) integer value (0-255)
- **SimpleIdentifier** that works like Identifier but without the character limitation with Identifier (use with caution)
- **Gui classes and utils**
- *and many more!*