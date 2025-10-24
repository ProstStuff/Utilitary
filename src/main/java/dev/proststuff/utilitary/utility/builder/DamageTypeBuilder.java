package dev.proststuff.utilitary.utility.builder;

import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DeathMessageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Simple DamageType builder class
 */
public class DamageTypeBuilder {
    private final RegistryKey<DamageType> registryKey;
    private DamageScaling damageScaling = DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER;
    private float exhaustion = 0.0f;
    private DamageEffects damageEffects = DamageEffects.HURT;
    private DeathMessageType deathMessageType = DeathMessageType.DEFAULT;

    public DamageTypeBuilder(RegistryKey<DamageType> registryKey) {
        this.registryKey = registryKey;
    }

    public DamageTypeBuilder(String namespace, String name) {
        this(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(namespace, name)));
    }

    public DamageTypeBuilder setDamageScaling(DamageScaling damageScaling) {
        this.damageScaling = damageScaling;
        return this;
    }

    public DamageTypeBuilder setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
        return this;
    }

    public DamageTypeBuilder setDamageEffects(DamageEffects damageEffects) {
        this.damageEffects = damageEffects;
        return this;
    }

    public DamageTypeBuilder setDeathMessageType(DeathMessageType deathMessageType) {
        this.deathMessageType = deathMessageType;
        return this;
    }

    public DamageType build() {
        Identifier id = registryKey.getValue();
        return new DamageType(String.format("%s.%s", id.getNamespace(), id.getPath()), damageScaling, exhaustion, damageEffects, deathMessageType);
    }
}