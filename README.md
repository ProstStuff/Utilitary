# Utilitary
A utilitarian utility library.

---

## About
Utilitary is a library mainly adds utilities and configuration.

### Features
- JSON-based config using Minecraft codecs
- Many utility class and tweaks to simplify development process
  - `EntityRenderState` now hold an entity's UUID under `utilitary$uuid`. Can be obtained by casting `EntityRenderState` with `UUIDRenderStateHolder` class
  - `Color` and `ColorWithAlpha` class to store and modify color values.
  - `SimpleIdentifier` works like `Identifier` but without the character limits

---

## Usage

### Setup
`build.grade`
```
repositories {
    ...
    maven {
        url = "https://jitpack.io"
    }
    ...
}

dependencies {
    ...
    implementation "com.github.ProstStuff:Utilitary:${utilitary_version}"
    ...
}
```

`gradle.properties`
```properties
utilitary_version=1.0.0-alpha // Check Utilitary GitHub repository for versions (release tag)
```

### Config

The config system uses Minecraft codecs to serialize and deserialize values.
The structure of the config is roughly similar to Minecraft's network payload, making it easier to understand for newer developers.

```java
public record UtilitaryConfig(boolean safeMode, boolean debugPrinting) {
    public static final SimpleIdentifier ID = SimpleIdentifier.of(Utilitary.ID, "config");
    
    public static final Codec<UtilitaryConfig> CODEC = RecordCodecBuilder.create(
            (inst) -> inst.group(
                    Codec.BOOL.fieldOf("safeMode").forGetter(UtilitaryConfig::safeMode),
                    Codec.BOOL.fieldOf("debugPrinting").forGetter(UtilitaryConfig::debugPrinting)
            ).apply(inst, UtilitaryConfig::new)
    );

    public static final ConfigType<UtilitaryConfig, VersionOnlyConfigMetadata, VersionOnlyConfigMetadataType> TYPE;

    static {
        // Setting up migrations (optional)
        NavigableMap<Integer, ConfigType.Migration> migrations = new TreeMap<>();

        migrations.put(1, (context) -> {
            JsonObject object = context.data().getAsJsonObject();
            object.add("debugPrinting", object.remove("debug"));
            return object;
        });

        TYPE = ConfigManager.register(
                new ConfigType<>(
                        Identifier.fromNamespaceAndPath(Utilitary.ID, "config_type"), // Config type ID
                        2, // Version of the config type
                        ConfigCodec.of(CODEC, null), // Config type codec (Codec, StreamCodec), both can be null
                        (_) -> new UtilitaryConfig(false, false),  // Defaults
                        migrations, // Migrations (can be left as null)
                        new VersionOnlyConfigMetadataType() // Config metadata ("config" field)
        ));
    }
}
```

```java
static {
    UtilitaryConfig config = ConfigManager.load(UtilitaryConfig.ID, UtilitaryConfig.TYPE); // Loading
    ConfigManager.save(UtilitaryConfig.ID, UtilitaryConfig.TYPE, config); // Saving
}
```

Additionally, the config file have a metadata field named as `config`
that can be extended with `ConfigMetadataType` to provide additional metadata fields.

With this, the config file can provide additional information that's not necessarily in the config data itself

```java
public class VersionOnlyConfigMetadataType implements ConfigMetadataType<VersionOnlyConfigMetadataType.Metadata> {
    public static final Codec<Metadata> CODEC = Codec.INT.comapFlatMap(VersionOnlyConfigMetadataType::read, Metadata::version);

    @Override
    public Codec<Metadata> codec() {
        return CODEC;
    }

    @Override
    public <T> VersionOnlyConfigMetadataType.@NonNull Metadata create(Context<T> context) {
        return new Metadata(context.version());
    }

    public static DataResult<Metadata> read(Integer version) {
        return DataResult.success(new Metadata(version));
    }

    public record Metadata(int version) implements ConfigMetadata {}
}
```

`config.json` from `./config/utilitary/config.json`:
```json
{
  "config": 2,
  "data": {
    "safeMode": false,
    "debugPrinting": false
  }
}
```

Currently, the config is using `.json` format, meaning commenting is not possible.
Utilitary has plan to switch to `.json5` format to allow such features or for better readability
but is not the top of Utilitary development priority.