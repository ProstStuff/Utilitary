package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.platform.services.IClientHelper;
import com.fraynn.utilitary.util.Phantom;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeoForgeClientHelper implements IClientHelper {
    private static final Map<String, List<LazyPhantom<KeyMapping>>> QUEUE = new HashMap<>();

    public void registerFor(String modId, IEventBus bus) {
        bus.addListener(RegisterKeyMappingsEvent.class, event -> {
            List<LazyPhantom<KeyMapping>> mappings = QUEUE.get(modId);

            if (mappings == null) return;

            mappings.forEach(keyMapping -> event.register(keyMapping.get()));
            QUEUE.remove(modId);
        });
    }

    @Override
    public Phantom<KeyMapping> registerKeyMapping(String modId, String name, int value, KeyMapping.Category category) {
        LazyPhantom<KeyMapping> lazy = new LazyPhantom<>(Lazy.of(() -> new KeyMapping(name, value, category)));
        QUEUE.computeIfAbsent(modId, (_) -> new ArrayList<>()).add(lazy);
        return lazy;
    }

    @Override
    public Phantom<KeyMapping> registerKeyMapping(String modId, String name, InputConstants.Type type, int value, KeyMapping.Category category) {
        LazyPhantom<KeyMapping> lazy = new LazyPhantom<>(Lazy.of(() -> new KeyMapping(name, type, value, category)));
        QUEUE.computeIfAbsent(modId, (_) -> new ArrayList<>()).add(lazy);
        return lazy;
    }

    @Override
    public Phantom<KeyMapping> registerKeyMapping(String modId, String name, InputConstants.Type type, int value, KeyMapping.Category category, int order) {
        LazyPhantom<KeyMapping> lazy = new LazyPhantom<>(Lazy.of(() -> new KeyMapping(name, type, value, category, order)));
        QUEUE.computeIfAbsent(modId, (_) -> new ArrayList<>()).add(lazy);
        return lazy;
    }

    public record LazyPhantom<T>(Lazy<T> lazy) implements Phantom<T> {
        @Override
        public T get() {
            return lazy.get();
        }
    }
}
