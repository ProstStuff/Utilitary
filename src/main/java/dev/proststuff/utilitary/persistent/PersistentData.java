package dev.proststuff.utilitary.persistent;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

// TODO: Client sync
public class PersistentData {
    public static PersistentState.Type<PersistentDataState> PLAYER_PERSISTENT_DATA = new PersistentState.Type<>(
            PersistentDataState::new,
            PersistentDataState::new,
            null
    );

    public static PersistentDataState get(World world) {
        if (world instanceof ServerWorld serverWorld) {
            return get(serverWorld);
        }

        return null;
    }

    public static PersistentDataState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(PLAYER_PERSISTENT_DATA, "UtilitaryPersistentData");
    }

    public static void sync(ServerPlayerEntity player) {
        get(player.getServerWorld()).send(player);
    }

    public static void register() {}
}
