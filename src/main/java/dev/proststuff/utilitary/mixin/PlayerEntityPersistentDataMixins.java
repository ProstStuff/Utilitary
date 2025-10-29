package dev.proststuff.utilitary.mixin;

import dev.proststuff.utilitary.persistent.IPersistentData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Utilitary Player's Persistent Data
 * Player data will stay regardless
 */
@Mixin(ServerPlayerEntity.class)
public class PlayerEntityPersistentDataMixins implements IPersistentData {
    @Unique
    private final NbtCompound utilitary$persistentDataSet = new NbtCompound();

    @Unique
    @Override
    public NbtCompound utilitary$getPersistentDataSet() {
        return utilitary$persistentDataSet;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void utilitary$writeCustomData(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("UtilitaryPersistentData", utilitary$persistentDataSet);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void utilitary$readCustomData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("UtilitaryPersistentData")) {
            utilitary$persistentDataSet.copyFrom(nbt.getCompound("UtilitaryPersistentData"));
        }
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void utilitary$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        NbtCompound oldData = ((IPersistentData) oldPlayer).utilitary$getPersistentDataSet();
        this.utilitary$getPersistentDataSet().copyFrom(oldData);
    }
}
