# Utilitary
When there's not enough utilities to ease modding development.

## About

---
Formerly called **RECONSTRUCT WHAT!?**,
**Utilitary** is a Fabric library that comes with pre-built JSON config system and utilities to ease Minecraft modding.

---
### Features

- Configuration file that formatted to `.json`
  - Very readable format
  - Easily expandable
  - Use custom-made codec for maximum serialization
  - Automatic syncing between server and client
    - Large config is broken down into chunks to prevent overloading the packet
  - Detect config file change and update to all players
  - Config are automatically loaded, even startup!
- Common, helper, and utility classes