package com.fraynn.utilitary.platform;

import com.fraynn.utilitary.platform.services.IClientHelper;
import com.fraynn.utilitary.util.Phantom;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

@Environment(EnvType.CLIENT)
public class FabricClientHelper implements IClientHelper {
    @Override
    public Phantom<KeyMapping> registerKeyMapping(String modId, String name, int value, KeyMapping.Category category) {
        return Phantom.simple(KeyMappingHelper.registerKeyMapping(new KeyMapping(name, value, category)));
    }

    @Override
    public Phantom<KeyMapping> registerKeyMapping(String modId, String name, InputConstants.Type type, int value, KeyMapping.Category category) {
        return Phantom.simple(KeyMappingHelper.registerKeyMapping(new KeyMapping(name, type, value, category)));
    }

    @Override
    public Phantom<KeyMapping> registerKeyMapping(String modId, String name, InputConstants.Type type, int value, KeyMapping.Category category, int order) {
        return Phantom.simple(KeyMappingHelper.registerKeyMapping(new KeyMapping(name, type, value, category, order)));
    }
}
