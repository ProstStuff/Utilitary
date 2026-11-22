# Utilitary

A Minecraft library that provide extensive configuration, utilities, tweaks, and abstractions
to help with mod multi-platform development.

This library is mainly for Fraynn's / ProstStuff's mod development.
Using it in your projects is generally not recommended since there will be many breaking/experimental changes.

---

## Features

- Extensive configuration system that use Minecraft Codec class and can be saved in multiple file formats (using Minecraft Ops)
- Multi-platform capability and utilities
- Simplified Minecraft classes (e.g. Identifier, Registry)
- EntityRenderState now contain the extracted entity UUID
- GUI and rendering classes
- Either, Tried, and Phantom utility

---

## Maven

```gradle
repositories {
    maven {
		name = "JitPack"
		url = uri('https://jitpack.io')
	}
}

dependencies {
    // In Common gradle
    compileOnly "com.github.ProstStuff:Utilitary:<version>+<minecraft_version>-common"
    // In Fabric gradle
    implementation "com.github.ProstStuff:Utilitary:<version>+<minecraft_version>-fabric"
    // In NeoForge gradle
    implementation "com.github.ProstStuff:Utilitary:<version>+<minecraft_version>-neoforge"
}
```