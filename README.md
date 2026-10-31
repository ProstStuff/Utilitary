# Utilitary

---

A Minecraft library that provide extensive configuration, utilities, tweaks, and abstractions
to help with mod development.

This library is mainly for Fraynn's / ProstStuff's mod development, and not for public use.
The Modrinth releases are based on whether the version is used in any of the developed mod.

## Features

- Extensive configuration system that use Minecraft Codec class and can be saved in multiple file formats (using Minecraft Ops)
- Simplified Minecraft classes (e.g. Identifier, Registry)
- EntityRenderState now contain the extracted entity UUID
- GUI and rendering classes
- Either and Tried utility

## Maven

```gradle
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }

        filter {
            includeGroup "maven.modrinth"
        }
    }
    
    maven {
		name = "JitPack"
		url = uri('https://jitpack.io')
	}
}

dependencies {
    // Stable used version only
    implementation "maven.modrinth:utilitary:${utilitary_version}" 
    // All versions
    implementation "com.github.ProstStuff:Utilitary:${utilitary_version}"
}
```